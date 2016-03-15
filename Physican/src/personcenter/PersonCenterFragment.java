package personcenter;

import java.util.Map;

import login.LoginActivity;
import utils.AppManager;
import utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import base.BaseFragment;
import bean.Doctor;
import bean.DoctorData;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.lidroid.xutils.http.HttpHandler;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.util.AlertDialog;
import com.winning.pregnancy.util.PreferencesUtils;
import com.zxing.android.CaptureActivity;

/*
 * 个人中心界面
 * */
public class PersonCenterFragment extends BaseFragment {
	public static final String TAG = "PersonCenterFragment";
	private MainActivity activity;
	private RelativeLayout container_radiogroup;
	private Button button_exitLogin;
	private String receiveInquiryNotify;
	private Map<String, String> map;
	private ToggleButton toggleButton_personCenter;
	private String userID;
	protected HttpHandler<String> httpHandler;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity) activity;

		}
		super.onAttach(activity);
	}

	@Override
	protected void loadData() {
		// TODO Auto-generated method stub

		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(activity);
		receiveInquiryNotify = map.get("receiveInquiryNotify");
	}

	@Override
	protected View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_personcenter, container,
				false);

		RelativeLayout sao1sao_personcenter = (RelativeLayout) view
				.findViewById(R.id.sao1sao_personcenter);
		RelativeLayout myinfo_personcenter = (RelativeLayout) view
				.findViewById(R.id.myinfo_personcenter);
		button_exitLogin = (Button) view.findViewById(R.id.button_exitLogin);
		toggleButton_personCenter = (ToggleButton) view
				.findViewById(R.id.toggleButton_personCenter);
		toggleButton_personCenter.setChecked(receiveInquiryNotify.equals("1"));
		toggleButton_personCenter
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							final boolean isChecked) {
						httpHandler = ReceiveRequest.Receive(activity,
								new Handler() {

									@Override
									public void handleMessage(Message msg) {
										// TODO Auto-generated method stub
										super.handleMessage(msg);
										switch (msg.what) {
										case 0:
											String jsonString = msg.obj
													.toString();
											Doctor doctor = JSON.parseObject(
													jsonString, Doctor.class);
											DoctorData data = doctor.getData()
													.get(0);
											receiveInquiryNotify = data
													.getReceiveInquiryNotify();
											SharedPreferences sharedPreferences = activity
													.getSharedPreferences(
															"APPCONFIG",
															Context.MODE_PRIVATE);
											Editor editor = sharedPreferences
													.edit();
											editor.putString(
													"receiveInquiryNotify",
													receiveInquiryNotify);
											editor.commit();
											break;

										default:
											activity.showToast();

											break;
										}
									}
								}, map);
					}
				});

		myinfo_personcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 用于展示我的资料详情页面
				Intent intent = new Intent();
				intent.setClass(activity, MineActivity.class);
				activity.startActivity(intent);

			}
		});
		sao1sao_personcenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Boolean isOpen = checkCameraHardWare(activity);
				if (isOpen) {
					Intent intent = new Intent();
					intent.setClass(activity, CaptureActivity.class);
					activity.startActivity(intent);
				} else {
					Toast.makeText(activity, "手机无相机设备", 0).show();
				}

			}
		});
		button_exitLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出对话框用于判断是否退出登录，退出登陆后，修改参数，跳转到登陆页面
				AlertDialog dialog = new AlertDialog(activity);
				String st = "退出登录";
				String msg = "是否退出当前账号?";
				// dialog = new AlertDialog(this);
				dialog.builder();
				dialog.setTitle(st);
				dialog.setMsg(msg);
				dialog.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						SharedPreferencesUtils
								.resetSharedPreferencesForAppConfig(activity);
						PreferencesUtils.putString(activity, "user", "");
						PreferencesUtils.putString(activity, "dlmm", "");
						MyApplication.getInstance().logout(null);
						AppManager.getAppManager().finishActivity();
						startActivity(new Intent(activity, LoginActivity.class));

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

				/*
				 * AlertDialog.Builder builder = new
				 * AlertDialog.Builder(activity); builder.setMessage("确信退出登陆吗？")
				 * .setNegativeButton("是", new DialogInterface.OnClickListener()
				 * {
				 * 
				 * @Override public void onClick(DialogInterface dialog, int
				 * which) { // TODO Auto-generated method stub
				 * SharedPreferencesUtils
				 * .resetSharedPreferencesForAppConfig(activity);
				 * PreferencesUtils.putString(activity, "user", "");
				 * PreferencesUtils.putString(activity, "dlmm", "");
				 * MyApplication.getInstance() .logout(null);
				 * AppManager.getAppManager() .finishActivity();
				 * startActivity(new Intent(activity, LoginActivity.class));
				 * 
				 * } }).setPositiveButton("否", null); builder.create().show();
				 */
			}
		});
		return view;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (httpHandler != null) {
			httpHandler.cancel();

		}
	}

	public void onDetach() {
		super.onDetach();
		try {
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

	private boolean checkCameraHardWare(Context context) {
		PackageManager packageManager = context.getPackageManager();
		if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		}
		return false;
	}

}
