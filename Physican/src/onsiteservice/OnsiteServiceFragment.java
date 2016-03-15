package onsiteservice;

import inquirymessage.InquriyMessageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.FragmentChangeHelper;
import utils.JumpActivityUtils;
import utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import base.BaseFragment;
import bean.Done;
import bean.DoneDetail;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.lidroid.xutils.http.HttpHandler;

import config.Urls;
import customview.MyTitleBar;
import customview.MyTitleBar.topBarClickListener;

public class OnsiteServiceFragment extends BaseFragment {
	public static final String TAG = "OnsiteServiceFragment";
	private TextView[] arrTabs = new TextView[3];
	private ImageView[] arrGreenLines = new ImageView[3];
	private MainActivity activity;
	private int pageNo = 1;
	private int pageSize = 30;
	private ListView showDetails_onsite_listView;
	private OnsiteAdapter adapter;
	private Map<String, String> map;
	private String userID;
	private String dataString;
	private ArrayList<String> list = new ArrayList<String>();
	private List<DoneDetail> totalList = new ArrayList<DoneDetail>();
	private int clickPosition = 0;
	private FragmentChangeHelper helper;
	private HttpHandler<String> httpHandler;
	private MyFirstPageReceiver receiver_first;
	private MySencondPageReceiver receiver_sencond;
	private TextView textView_redPoint_onsiteService;
	protected int state;
	private int reloadPageNum = 0;
	private MyTitleBar myTitleBar;
	private HttpHandler<String> httpHandler1;

	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;
			// textView_redPoint_onsiteService=(TextView)
			// activity.findViewById(R.id.textView_redPoint_onsiteService);
		}
		super.onAttach(activity);
	}

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View rootView = inflater.inflate(R.layout.fragment_onsiteservice,
				container, false);
		textView_redPoint_onsiteService = (TextView) activity
				.findViewById(R.id.textView_redPoint_onsiteService);
		arrTabs[0] = (TextView) rootView
				.findViewById(R.id.waitingOnsite_onsiteService);
		arrTabs[1] = (TextView) rootView
				.findViewById(R.id.waitingColse_onsiteService);
		arrTabs[2] = (TextView) rootView
				.findViewById(R.id.finished_onsiteService);
		arrGreenLines[0] = (ImageView) rootView
				.findViewById(R.id.greenLine_visiting_onsite);
		arrGreenLines[1] = (ImageView) rootView
				.findViewById(R.id.greenLine_colse_onsite);
		arrGreenLines[2] = (ImageView) rootView
				.findViewById(R.id.greenLine_finished_onsite);

		arrTabs[0].setEnabled(false);
		showDetails_onsite_listView = (ListView) rootView
				.findViewById(R.id.showDetails_onsite_listView);
		showDetails_onsite_listView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						String json = list.get(clickPosition);
						Done done = JSON.parseObject(json, Done.class);
						List<DoneDetail> data = done.getData();
						state = data.get(arg2).getState();
						JumpActivityUtils.jumpActivity(activity, state, arg2,
								json);

					}
				});
		for (int j = 0; j < arrTabs.length; j++) {
			arrTabs[j].setEnabled(true);
			arrTabs[j].setTag(j);
			arrTabs[j].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 用于更新listview内容
					list.clear();
					totalList.clear();
					adapter.reloadAdapter(totalList, true);
					pageNo = 0;
					if (httpHandler != null) {
						httpHandler.cancel();

					}
					int checkedId = (Integer) v.getTag();
					switch (checkedId) {
					case 0:
						changeSelectedItem(0);
						initFristPage(0);
						reloadPageNum = 0;
						break;
					case 1:
						changeSelectedItem(1);
						initSecondPage(0);
						reloadPageNum = 1;
						break;
					default:
						changeSelectedItem(2);
						initThirdPage(0);
						reloadPageNum = 3;
						break;
					}

				}

			});
		}
		myTitleBar = (MyTitleBar) rootView
				.findViewById(R.id.onsiteservice_mytitlebar);
		myTitleBar.setOnTopBarClickListener(new topBarClickListener() {

			@Override
			public void rightClick() {
				// TODO Auto-generated method stub

			}

			@Override
			public void leftClick() {
				// TODO Auto-generated method stub
				list.clear();
				totalList.clear();
				adapter.reloadAdapter(totalList, true);
				pageNo = 0;
				if (httpHandler != null) {
					httpHandler.cancel();

				}
				if (reloadPageNum == 0) {
					changeSelectedItem(0);
					initFristPage(0);
					// 用于更新listview内容

				} else if (reloadPageNum == 1) {
					changeSelectedItem(1);
					initSecondPage(0);

				} else {
					changeSelectedItem(2);
					initThirdPage(0);
				}

			}
		});
		return rootView;
	}

	@Override
	protected void initView() {
		showDetails_onsite_listView.setAdapter(adapter);
		// TODO Auto-generated method stub

	}

	@Override
	protected void loadData() {
		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(activity);
		userID = map.get("userID");
		adapter = new OnsiteAdapter(totalList, activity);
		initFristPage(pageNo);
		receiver_first = new MyFirstPageReceiver();
		receiver_sencond = new MySencondPageReceiver();
		IntentFilter filter_first = new IntentFilter();
		filter_first.addAction("reload_firstpage");
		filter_first.setPriority(998);
		activity.registerReceiver(receiver_first, filter_first);

		IntentFilter filter_sencond = new IntentFilter("reload_sencondpage");
		filter_first.setPriority(997);
		activity.registerReceiver(receiver_sencond, filter_sencond);

	}

	private void changeSelectedItem(int i) {
		// TODO Auto-generated method stub

		for (int j = 0; j < arrGreenLines.length; j++) {
			if (i == j) {

				arrGreenLines[j].setImageResource(R.drawable.green_underline);
				arrTabs[j].setEnabled(false);
			} else {

				arrGreenLines[j].setImageResource(R.drawable.grey_underline);
				arrTabs[j].setEnabled(true);
			}

		}

	}

	public void initFristPage(int pageNum) {
		httpHandler = InquriyMessageRequest.message(activity, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				dataString = msg.obj + "";
				list.clear();
				totalList.clear();
				adapter.reloadAdapter(totalList, true);
				Log.i("msg", dataString);
				super.handleMessage(msg);

				switch (msg.what) {
				case 0:
					Done done = JSON.parseObject(msg.obj.toString(), Done.class);
					List<DoneDetail> data = done.getData();

					if (data.size() != 0) {
						textView_redPoint_onsiteService
								.setVisibility(View.VISIBLE);

					} else {
						textView_redPoint_onsiteService
								.setVisibility(View.GONE);
					}
					if (data.size() > 30) {
						pageNo++;
						adapter.reloadAdapter(data, false);
						initFristPage(pageNo);
						clickPosition = pageNo;
						list.add(msg.obj + "");

					} else {
						if (adapter != null) {
							adapter.reloadAdapter(data, false);
							list.add(msg.obj + "");

						}

						break;
					}

					break;

				default:
					totalList.clear();
					adapter.reloadAdapter(totalList, true);
					activity.showToast();
					// Toast.makeText(activity, "数据异常，请稍后重试", 0).show();
					break;
				}
			}

		}, map, Urls.FACEBYDOCTORUNSERVICED_URL, userID, pageNum + "", pageSize
				+ "");

	}

	private void initSecondPage(int pageNum) {
		httpHandler1 = InquriyMessageRequest.message(activity, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				super.handleMessage(msg);

				switch (msg.what) {
				case 0:
					Done done = JSON.parseObject(msg.obj.toString(), Done.class);
					List<DoneDetail> data = done.getData();

					if (data.size() != 0) {
						textView_redPoint_onsiteService
								.setVisibility(View.VISIBLE);

					} else {
						textView_redPoint_onsiteService
								.setVisibility(View.GONE);
					}

					break;

				default:
					totalList.clear();
					adapter.reloadAdapter(totalList, true);
					activity.showToast();
					break;
				}
			}

		}, map, Urls.FACEBYDOCTORUNSERVICED_URL, userID, 0 + "", 30 + "");

		httpHandler = InquriyMessageRequest.message(activity, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				list.clear();
				totalList.clear();
				adapter.reloadAdapter(totalList, true);
				switch (msg.what) {
				case 0:
					Done done = JSON.parseObject(msg.obj.toString(), Done.class);
					List<DoneDetail> data = done.getData();

					if (data.size() > 30) {
						pageNo++;
						clickPosition = pageNo;
						adapter.reloadAdapter(data, false);
						initFristPage(pageNo);

					} else {
						if (data.size() == 0) {

						} else {
							adapter.reloadAdapter(data, false);
							list.add(msg.obj + "");
						}

						break;
					}

					break;

				default:
					activity.showToast();
					break;
				}
			}

		}, map, Urls.FACEBYDOCTORSERVICED_URL, userID, pageNum + "", pageSize
				+ "");

	}

	private void initThirdPage(int pageNum) {
		httpHandler = InquriyMessageRequest.message(activity, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				Log.i("bbbbbb", msg.obj + "");
				super.handleMessage(msg);
				list.clear();
				totalList.clear();
				adapter.reloadAdapter(totalList, true);
				switch (msg.what) {
				case 0:
					Done done = JSON.parseObject(msg.obj.toString(), Done.class);
					List<DoneDetail> data = done.getData();

					if (data.size() > 30) {
						pageNo++;
						clickPosition = pageNo;
						adapter.reloadAdapter(data, false);
						initFristPage(pageNo);
						list.add(msg.obj + "");

					} else {
						adapter.reloadAdapter(data, false);
						list.add(msg.obj + "");
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

		}, map, Urls.FACEBYDOCTORCLOSED_URL, userID, pageNum + "", pageSize
				+ "");

	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
		clickPosition = 0;

	}

	public void onDetach() {
		super.onDetach();
		try {
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

	class MyFirstPageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			list.clear();
			adapter.reloadAdapter(totalList, true);
			changeSelectedItem(0);
			initFristPage(0);

		}
	}

	class MySencondPageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			list.clear();
			adapter.reloadAdapter(totalList, true);
			changeSelectedItem(1);
			initSecondPage(0);

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (httpHandler != null) {
			httpHandler.cancel();

		}
		if (httpHandler1 != null) {
			httpHandler1.cancel();

		}
		activity.getSupportFragmentManager().popBackStack();

		activity.unregisterReceiver(receiver_first);
		activity.unregisterReceiver(receiver_sencond);
	}

	public void setOnsiteCount() {
		if (activity != null && adapter != null) {
			loadData();
		}

	}

}
