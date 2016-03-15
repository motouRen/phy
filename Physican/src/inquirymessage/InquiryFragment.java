package inquirymessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import base.BaseFragment;
import bean.InquiryByDoctor;
import bean.InquiryByDoctorDetail;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.lidroid.xutils.http.HttpHandler;

import config.Urls;

public class InquiryFragment extends BaseFragment {
	public static final String TAG = "InquiryFragment";
	private MainActivity activity;
	private RelativeLayout container_radiogroup;
	private TextView[] arrTabs = new TextView[2];
	private ImageView[] arrGreenLines = new ImageView[2];
	private int pageNo = 1;
	private int pageSize = 20;
	private Map<String, String> map;
	private String userID;
	private String assistantID;
	private List<InquiryByDoctorDetail> totalList = new ArrayList<InquiryByDoctorDetail>();
	private ListView showDetails_inquiry_listView;
	private InquiryListViewAdapter adapter;
	private HttpHandler<String> httpHandler;
	private boolean flag = true;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;
			container_radiogroup = (RelativeLayout) activity
					.findViewById(R.id.container_radiogroup);
			container_radiogroup.setVisibility(View.GONE);
		}
		super.onAttach(activity);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub

		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(activity);
		userID = map.get("userID");
		assistantID = map.get("assistantID");
		adapter = new InquiryListViewAdapter(totalList, activity);
		initFristPage(pageNo);

	}

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_inquiry, container,
				false);
		ImageView imageView_inquiry_back_1 = (ImageView) view
				.findViewById(R.id.imageView_inquiry_back_1);
		showDetails_inquiry_listView = (ListView) view
				.findViewById(R.id.showDetails_inquiry_listView);
		TextView textView_topbar_left = (TextView) view
				.findViewById(R.id.textView_topbar_left);
		imageView_inquiry_back_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 将当前的fragment进行弹栈
				activity.getSupportFragmentManager().popBackStack();

			}
		});
		textView_topbar_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 将当前的fragment进行弹栈
				activity.getSupportFragmentManager().popBackStack();

			}
		});

		// doctorID， pageNo，pageSize
		arrTabs[0] = (TextView) view.findViewById(R.id.myreply_inquiry);
		arrTabs[1] = (TextView) view.findViewById(R.id.myhelperreply_inquiry);

		arrGreenLines[0] = (ImageView) view.findViewById(R.id.pinkLine_myReply);
		arrGreenLines[1] = (ImageView) view
				.findViewById(R.id.pinkLine_myHelperReply);
		for (int j = 0; j < arrTabs.length; j++) {
			arrTabs[j].setEnabled(true);
			arrTabs[j].setTag(j);
			arrTabs[j].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 用于更新listview内容
					if (httpHandler != null) {
						httpHandler.cancel();

					}
					pageNo = 1;
					adapter.reloadAdapter(totalList, true);
					int checkedId = (Integer) v.getTag();

					switch (checkedId) {
					case 0:
						// showDetails_inquiry_listView.setItemsCanFocus(true);
						flag = true;
						changeSelectedItem(0);

						initFristPage(pageNo);
						break;
					case 1:
						// showDetails_inquiry_listView.setItemsCanFocus(false);
						flag = false;
						changeSelectedItem(1);
						initSecondPage(pageNo);
						break;
					}

				}

			});
		}
		arrTabs[0].setEnabled(false);
		showDetails_inquiry_listView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// Toast.makeText(activity, "222", 0).show();

						InquiryByDoctorDetail detail = totalList.get(arg2);
						detail.getGravidaMobile();
						Intent intent = new Intent();

						intent.putExtra("userId", detail.getGravidaMobile());

						intent.putExtra("userName", detail.getDoctorName());
						intent.putExtra("serviceID", detail.getId());

						intent.putExtra("assistantID",
								Integer.parseInt(assistantID));
						intent.putExtra("gravidaID", detail.getGravidaID());

						intent.putExtra("doctorID", detail.getDoctorID());
						intent.setClass(getActivity(),
								InquiryHistoryActivity.class);
						activity.startActivity(intent);

					}
				});
		return view;
	}

	@Override
	protected void initView() {
		showDetails_inquiry_listView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		container_radiogroup.setVisibility(View.VISIBLE);
		if (httpHandler != null) {
			httpHandler.cancel();

		}

	}

	private void changeSelectedItem(int i) {
		// TODO Auto-generated method stub

		for (int j = 0; j < arrGreenLines.length; j++) {
			if (i == j) {
				// arrGreenLines[j].setHeight(2);
				arrGreenLines[j].setImageResource(R.drawable.pink_underline);
				arrTabs[j].setEnabled(false);
			} else {

				arrGreenLines[j].setImageResource(R.drawable.grey_underline);
				arrTabs[j].setEnabled(true);
			}

		}

	}

	private void initFristPage(int pageNum) {
		// TODO Auto-generated method stub
		httpHandler = InquriyMessageRequest.message(activity, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				super.handleMessage(msg);

				switch (msg.what) {
				case 0:
					InquiryByDoctor inquiryByDoctor = JSON.parseObject(
							msg.obj.toString(), InquiryByDoctor.class);
					List<InquiryByDoctorDetail> data = inquiryByDoctor
							.getData();

					if (data.size() > 30) {
						pageNo++;
						adapter.reloadAdapter(data, true);
						// reloadData(pageNo);
						initFristPage(pageNo);

					} else {
						if (adapter != null) {
							adapter.reloadAdapter(data, true);

						}
						break;
					}
					break;

				default:
					totalList.clear();
					adapter.reloadAdapter(totalList, true);
					activity.showToast();

					break;
				}
			}

		}, map, Urls.INQUIRYBYDOCTORCLOSE_URL, userID, pageNum + "", pageSize
				+ "");

	}

	private void initSecondPage(int pageNum) {

		httpHandler = InquiryRequest.message(
				activity,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {

						super.handleMessage(msg);
						switch (msg.what) {
						case 0:
							InquiryByDoctor inquiryByDoctor = JSON.parseObject(
									msg.obj.toString(), InquiryByDoctor.class);
							List<InquiryByDoctorDetail> data = inquiryByDoctor
									.getData();
							String s = msg.obj.toString();
							Log.i("wait", s);
							if (data.size() > 20) {
								pageNo++;
								// totalList.addAll(data);
								adapter.reloadAdapter(data, true);

								initSecondPage(pageNo);

							} else {
								if (adapter != null) {
									adapter.reloadAdapter(data, true);

								}
								break;
							}
							break;

						default:
							totalList.clear();
							adapter.reloadAdapter(totalList, true);
							activity.showToast();
							break;
						}
					}

				}, map, Urls.INQUIRYBYDOCTORWITHASSISTANTCLOSED_URL,
				assistantID,
				userID, pageNum + "", pageSize + "");
		// String assistantID, String doctorID, String pageNo, String pageSize

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

}
