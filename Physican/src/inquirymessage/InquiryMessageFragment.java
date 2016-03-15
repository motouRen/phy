package inquirymessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.AppManager;
import utils.FragmentChangeHelper;
import utils.MySQLiteOpenHelper;
import utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import base.BaseFragment;
import bean.InquiryMessage;
import bean.InquiryMessageData;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.lidroid.xutils.http.HttpHandler;
import com.winning.pregnancyandroid.InquiryActivity;

import config.Urls;
import customview.MyTitleBar;
import customview.MyTitleBar.topBarClickListener;

public class InquiryMessageFragment extends BaseFragment {
	private static InquiryMessageFragment fragment;
	public static final String TAG = "InquiryMessage";
	private MyTitleBar myTitleBar;
	private AppManager appManager = AppManager.getAppManager();
	private MainActivity activity;
	private int pageNo = 1;
	private int pageSize = 30;
	private InquiryMessage inquiryMessage;
	private List<InquiryMessageData> totalList = new ArrayList<InquiryMessageData>();
	private ListView listView_inquiryMessage;
	private InquiryMessageListViewAdapter adapter;
	private HttpHandler<String> httpHandler;
	private String easeMobUserName;
	private Map<String, String> map;
	private MySQLiteOpenHelper helper;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;
			helper = new MySQLiteOpenHelper(activity);

		}
	}

	@Override
	protected void loadData() {

		adapter = new InquiryMessageListViewAdapter(totalList, activity);
		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(activity);
		easeMobUserName = map.get("easeMobUserName");
		reloadData(pageNo);

	}

	public void reloadData(int pageNum) {
		// TODO Auto-generated method stub

		httpHandler = InquriyMessageRequest.message(
				activity,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);

						switch (msg.what) {
						case 0:
							inquiryMessage = JSON.parseObject(
									msg.obj.toString(), InquiryMessage.class);
							List<InquiryMessageData> data = inquiryMessage
									.getData();

							Log.i("123w", msg.obj.toString());

							for (int i = 0; i < data.size(); i++) {
								int id = data.get(i).getId();
								// int count = countMap.get(id);

							}

							if (data.size() > 30) {
								pageNo++;
								adapter.reloadAdapter(data, true);
								reloadData(pageNo);

							} else {
								if (adapter != null) {
									adapter.reloadAdapter(data, true);

								}
								break;
							}

							break;

						default:
							// Toast.makeText(getActivity(), text, duration)
							totalList.clear();
							adapter.reloadAdapter(totalList, true);
							activity.showToast();

							break;
						}
					}
				}, SharedPreferencesUtils
						.getSharedPreferencesForAppConfig(activity),
				Urls.MESSAGE_URL, map.get("userID") + "", pageNum + "",
				pageSize + "");
	}

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_inquerymessage,
				container, false);
		myTitleBar = (MyTitleBar) view
				.findViewById(R.id.inquerymessage_mytitlebar);
		listView_inquiryMessage = (ListView) view
				.findViewById(R.id.listView_inquiryMessage);
		listView_inquiryMessage
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						InquiryMessageData inquiryMessageData = totalList
								.get(arg2);
						final int id = inquiryMessageData.getId();
						final String gravidamobile = inquiryMessageData
								.getGravidamobile();
						// countMap.put(Integer.parseInt(id), 0);
						final String userName = inquiryMessageData
								.getDoctorname();
						int serviceID = id;
						final int assistantID = inquiryMessageData
								.getAssistantid();
						final int doctorID = inquiryMessageData.getDoctorid();
						final int gravidaID = inquiryMessageData.getGravidaid();
						// int userName=inquiryMessageData.getDoctorname();
						Intent intent = new Intent();

						intent.putExtra("userId", gravidamobile);

						intent.putExtra("userName", userName);
						intent.putExtra("serviceID", id);

						intent.putExtra("assistantID", assistantID);
						intent.putExtra("gravidaID", gravidaID);

						intent.putExtra("doctorID", doctorID);
						intent.setClass(getActivity(), InquiryActivity.class);
						activity.startActivity(intent);

					}
				});
		myTitleBar.setOnTopBarClickListener(new topBarClickListener() {

			@Override
			public void rightClick() {
				// TODO Auto-generated method stub
				InquiryFragment fragment = new InquiryFragment();
				FragmentChangeHelper helper = new FragmentChangeHelper(fragment);
				helper.addToBackStack(InquiryFragment.TAG);
				activity.changeFragment(helper);

			}

			@Override
			public void leftClick() {
				// 重新加载数据
				if (httpHandler != null) {
					httpHandler.cancel();

				}
				totalList.clear();
				reloadData(pageNo);

			}
		});
		return view;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		listView_inquiryMessage.setAdapter(adapter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		if (httpHandler != null) {
			httpHandler.cancel();

			Log.i(TAG, "onDestroy1");
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");

	}

	public void onDetach() {
		Log.i(TAG, "onDetach");

		super.onDetach();
		try {
			Log.i(TAG, "onDetach1");

			totalList.clear();
			java.lang.reflect.Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
