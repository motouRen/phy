package com.arvin.physican;

import inquirymessage.InquiryMessageFragment;
import inquirymessage.InquriyMessageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import onsiteservice.OnsiteServiceFragment;
import personcenter.DoctorInfoRequest;
import personcenter.PersonCenterFragment;
import utils.AppManager;
import utils.FragmentChangeHelper;
import utils.FragmentTabUtils;
import utils.NetworkUtils;
import utils.PixelUtils;
import utils.SharedPreferencesUtils;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseActivity;
import bean.Doctor;
import bean.DoctorData;
import bean.Done;
import bean.DoneDetail;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.util.AlertDialog;

import config.Urls;

public class MainActivity extends BaseActivity {

	public static final String TAG = "MainActivity";
	private long exitTime;
	public static MainActivity instance;
	private RadioGroup radiogroup_main;
	AlertDialog dialog;
	private Toast toast;

	private RelativeLayout container_radiogroup;
	private TextView textView_redPoint_onsiteService;
	private Context mContext;
	private TextView textView_redPoint_inquiryMessage;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	private HttpHandler<String> httpHandler;

	private BitmapUtils bitmapUtils;
	private OSSService ossService = MyApplication.getInstance().ossService;
	private OSSBucket bucket = MyApplication.getInstance().ossService
			.getOssBucket(MyApplication.getInstance().BUCKETNAME);
	private static OnsiteServiceFragment onsite;
	private static InquiryMessageFragment inquriy;
	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				if (onsite != null) {
					onsite.setOnsiteCount();
				}
			} else {
				if (inquriy != null) {
					inquriy.reloadData(0);
				}
			}

		};
	};

	private PersonCenterFragment person;
	private int newOnsite;
	private FragmentTabUtils fragmentTabUtils;

	public void changeFragment(FragmentChangeHelper helper) {

		if (helper != null) {
			FragmentTransaction tran = getSupportFragmentManager()
					.beginTransaction();

			Fragment targetFragment = helper.getTargetFragment();
			if (targetFragment != null) {

				String targetFragmentTag = helper.getTargetFragmentTag();
				if (targetFragmentTag != null) {
					tran.addToBackStack(targetFragmentTag);
				}

				// 检查是否要删除回退栈里对应的Fragment
				String[] removeFragmentTag = helper.getRemoveFragmentTag();

				if (removeFragmentTag != null) {
					for (String s : removeFragmentTag) {
						tran.remove(getSupportFragmentManager()
								.findFragmentByTag(s));
					}
				}

				// 检查是否清空回退栈
				if (helper.isClearAllBackStack()) {

					getSupportFragmentManager().popBackStack(null,
							FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}

				tran.replace(R.id.fragment_container, targetFragment);

				tran.commit();

			} else { // 如果没有目标Fragment，代表需要返回到栈中的指定Fragment
				String backToFragmentTag = helper.getBackToFragmentTag();

				if (backToFragmentTag != null) {
					getSupportFragmentManager().popBackStackImmediate(
							backToFragmentTag,
							FragmentManager.POP_BACK_STACK_INCLUSIVE);
				}
			}
		}
	}

	public void updateMsg() {

		runOnUiThread(new Runnable() {
			public void run() {
				Map<String, Integer> map = SharedPreferencesUtils
						.getSharedPreferencesTotalUnReadCount(mContext);
				int count = map.get("TOTALUNREADCOUNT");
				textView_redPoint_inquiryMessage.setText(count + "");
				// getSupportFragmentManager().findFragmentById(R.id.f);
				if (count > 0) {
					textView_redPoint_inquiryMessage
							.setVisibility(View.VISIBLE);
					// textView_redPoint_inquiryMessage.setVisibility(visibility);
				} else {
					textView_redPoint_inquiryMessage.setVisibility(View.GONE);
				}

			}
		});

	}

	public void showToast() {
		String st = "错误提示";
		String msg1 = "似乎已断开与互联网的连接。";
		dialog = new AlertDialog(mContext);
		dialog.builder();
		dialog.setTitle(st);
		dialog.setMsg(msg1);
		dialog.setPositiveButton("好", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		dialog.setCanceledOnTouchOutside(false);

		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					dialog.dismiss();
					// finish();

				}
				return false;
			}
		});

	}

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_main);
		mContext = this;
		instance = this;
		Intent intent = getIntent();
		if (intent != null) {
			newOnsite = intent.getIntExtra("newOnsite", 0);

		}
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);

		container_radiogroup = (RelativeLayout) findViewById(R.id.container_radiogroup);
		textView_redPoint_onsiteService = (TextView) findViewById(R.id.textView_redPoint_onsiteService);
		textView_redPoint_inquiryMessage = (TextView) findViewById(R.id.textView_redPoint_inquiryMessage);
		float x_inquiryMessage = PixelUtils.dip2px(mContext,
				PixelUtils.getWidthInDp(mContext) / 12 + 20);
		float x_onsiteService = PixelUtils.dip2px(mContext, 13);
		textView_redPoint_inquiryMessage.setX(x_inquiryMessage);
		textView_redPoint_onsiteService.setX(x_onsiteService);
		radiogroup_main = (RadioGroup) findViewById(R.id.radiogroup_main);
		if (newOnsite == 1) {
			fragmentTabUtils.onCheckedChanged(radiogroup_main,
					R.id.radio_onsiteservice);
		}

	}

	@Override
	protected void setViewData() {
		// TODO Auto-generated method stub
		Map<String, Integer> map = SharedPreferencesUtils
				.getSharedPreferencesTotalUnReadCount(mContext);
		int count = map.get("TOTALUNREADCOUNT");
		textView_redPoint_inquiryMessage.setText(count + "");
		if (count > 0) {
			textView_redPoint_inquiryMessage.setVisibility(View.VISIBLE);
			// textView_redPoint_inquiryMessage.setVisibility(visibility);
		} else {
			textView_redPoint_inquiryMessage.setVisibility(View.GONE);
		}

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().emLogin();

		inquriy = new InquiryMessageFragment();
		onsite = new OnsiteServiceFragment();
		person = new PersonCenterFragment();
		fragments.add(inquriy);
		fragments.add(onsite);
		fragments.add(person);

		fragmentTabUtils = new FragmentTabUtils(getSupportFragmentManager(),
				fragments, R.id.fragment_container, radiogroup_main);

		Map<String, String> map = SharedPreferencesUtils
				.getSharedPreferencesForAppConfig(mContext);
		String userID = map.get("userID");

		DoctorInfoRequest.obtainDoctorInfo(this, new Handler() {
			private DoctorData doctorData;

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				switch (msg.what) {
				case 0:
					Doctor doctor = JSON
							.parseObject(msg.obj + "", Doctor.class);

					doctorData = doctor.getData().get(0);

					String receiveInquiryNotify = doctorData
							.getReceiveInquiryNotify();
					String path = doctorData.getHeadPhoto();
					Log.i("image", path + "");

					SharedPreferences sharedPreferences = mContext
							.getSharedPreferences("APPCONFIG",
									Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putString("receiveInquiryNotify",
							receiveInquiryNotify);

					editor.commit();

					break;

				default:
					Log.i(TAG, Thread.currentThread().getName() + "---->");

					break;
				}

			}
		}, map);

		setOnsiteCount();
	}

	public void setOnsiteCount() {
		Map<String, String> map = SharedPreferencesUtils
				.getSharedPreferencesForAppConfig(mContext);
		String userID = map.get("userID");
		httpHandler = InquriyMessageRequest.message(mContext, new Handler() {
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
					// Log.i("123", )
					Log.i(TAG, Thread.currentThread().getName() + "---->");
					showToast();

					break;
				}
			}

		}, map, Urls.FACEBYDOCTORUNSERVICED_URL, userID, 0 + "", 30 + "");

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (container_radiogroup != null) {
			if (container_radiogroup.isShown()) {
				showConflictDialog();
			} else {
				super.onBackPressed();
			}
		}

		else {
			super.onBackPressed();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPause");
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	public static class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int reload = intent.getIntExtra("reload", 0);
			Log.i("reload", reload + "");
			handler.sendEmptyMessage(reload);
		}

	}

	private void showConflictDialog() {

		String st = "退出程序";
		String msg = "是否退出孕医生?";
		dialog = new AlertDialog(this);
		dialog.builder();
		dialog.setTitle(st);
		dialog.setMsg(msg);

		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MyApplication.getInstance().logout(null);
				finish();
				AppManager.getAppManager().AppExit(MyApplication.getInstance());

			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		dialog.setCanceledOnTouchOutside(false);

		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					dialog.dismiss();
					// finish();

				}
				return false;
			}
		});
	}
}
