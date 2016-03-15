package login;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.AppManager;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseActivity;
import bean.Doctor;
import bean.DoctorData;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.util.AlertDialog;
import com.winning.pregnancy.util.PreferencesUtils;
import com.winning.pregnancy.util.StringUtil;

/**
 * Created by asus on 2015/11/25.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private long exitTime;
	private EditText editText_login_phoneNumber;
	private EditText editText_login_password;
	private TextView textView_login;
	private String phone;
	private String password;
	private Context mContext = this;

	private String userID;
	private String appVersion;
	private String deviceID;
	private String deviceName;
	private String firmware;
	private String appName;
	private String province;
	private String city;
	AlertDialog dialog;
	private Toast toast;
	private Toast toast2;
	private Toast toast3;
	private String receiveInquiryNotify;

	@Override
	protected void initData() {
		Map<String, String> map = SharedPreferencesUtils
				.getSharedPreferencesForAppConfig(mContext);
		toast = Toast.makeText(mContext, "请检查您的网络设置", 0);
		toast2 = Toast.makeText(mContext, "账号不能为空", 0);
		toast3 = Toast.makeText(mContext, "密码不能为空", 0);
		userID = map.get("userID");
		appVersion = map.get("appVersion");
		deviceID = map.get("deviceID");
		deviceName = map.get("deviceName");
		firmware = map.get("firmware");
		appName = map.get("appName");
		province = map.get("province");
		city = map.get("city");
		phone = map.get("phoneNumber");
		if (StringUtil.isNotEmpty(phone)) {

			editText_login_phoneNumber.setText(phone);
		}
		textView_login.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		// if ((System.currentTimeMillis() - exitTime) > 2000) {
		// // Toast toast = new Toast(mContext);
		//
		// Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
		// exitTime = System.currentTimeMillis();
		// } else {
		// finish();
		// AppManager.getAppManager().AppExit(MyApplication.getInstance());
		// }
		showConflictDialog();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		phone = editText_login_phoneNumber.getText() + "";
		password = editText_login_password.getText() + "";
		if (TextUtils.isEmpty(phone)) {
			if (toast2 != null) {
				toast2.show();
			} else {
				toast2 = Toast.makeText(mContext, "账号不能为空", 0);
				toast2.show();
			}

			if (TextUtils.isEmpty(password)) {
				if (toast3 != null) {
					toast3.show();
				} else {
					toast3 = Toast.makeText(mContext, "密码不能为空", 0);
					toast3.show();
				}
				return;
			}
			return;
		}
		// 接收登录请求
		if (AppManager.isNetworkAvailable(getApplicationContext())) {
			Log.i("password", password);
			// 第一次使用-->跳转登陆,
			LoginRequest.login(
					LoginActivity.this,
					new Handler() {

						@Override
						public void handleMessage(Message msg) {
							if (msg.what == 0) {
								String jsonString = msg.obj.toString();

								Log.i("jsonString", jsonString);
								Doctor doctor = JSON.parseObject(jsonString,
										Doctor.class);
								DoctorData data = doctor.getData().get(0);
								// 登录成功，修改app配置
								// Log.i("image", jsonString);
								userID = data.getId();
								receiveInquiryNotify = data
										.getReceiveInquiryNotify();
								String assistantID = data.getAssistantID();
								// Log.i("image", assistantID);
								String headPhoto = data.getHeadPhoto();

								String lastLocationJson = data
										.getLastLocationJson();
								try {
									JSONObject jsonObject = new JSONObject(
											lastLocationJson);
									String longitude = jsonObject
											.optString("longitude");
									String latitude = jsonObject
											.optString("latitude");
									String easeMobUserName = data
											.getEaseMobUserName();
									PreferencesUtils.putString(mContext,
											"user", jsonString);
									PreferencesUtils.putString(mContext,
											"dlzh", easeMobUserName);
									MyApplication.getInstance().emLogin();
									SharedPreferencesUtils
											.setSharedPreferencesForAppConfig(
													getApplicationContext(),
													userID, deviceID,
													deviceName, firmware,
													appName, appVersion,
													province, city, phone,
													password,
													receiveInquiryNotify,
													easeMobUserName,
													assistantID, headPhoto,
													longitude, latitude);
									AppManager.getAppManager()
											.finishAllActivity();
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								Doctor doctor = JSON.parseObject(
										msg.obj.toString(), Doctor.class);
								String error = doctor.getErr();
								Toast.makeText(getApplicationContext(), error,
										Toast.LENGTH_SHORT).show();
							}
						}
					}, 0 + "", deviceID, deviceName, firmware, appName,
					appVersion,
					province, city, phone, password);

		} else {
			showToast();
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (toast != null) {
			toast.cancel();

		}
		if (toast2 != null) {
			toast2.cancel();

		}
		if (toast3 != null) {
			toast3.cancel();

		}

	}

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_login);
		editText_login_phoneNumber = (EditText) findViewById(R.id.editText_login_phoneNumber);
		editText_login_password = (EditText) findViewById(R.id.editText_login_password);
		textView_login = (TextView) findViewById(R.id.textView_login);

	}

	@Override
	protected void setViewData() {

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
}
