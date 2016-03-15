package login;

import java.util.Map;

import utils.AppManager;
import utils.PhoneInfoUtils;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.arvin.physican.MainActivity;
import com.arvin.physican.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class SplashActivity extends FragmentActivity {
	private Context mContext = this;
	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();
	private String userID;
	private String appVersion;
	private String deviceID;
	private String deviceName;
	private String firmware;
	private String appName;
	private Toast toast;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x111:
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();
				break;
			case 0x222:
				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				finish();
				break;
			case 0x333:
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));

				finish();
				break;

			}

		}
	};

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		// context = this;
		toast = Toast.makeText(mContext, "请检查您的网络设置", 0);
		setCurrentView();
		initData();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (toast != null) {
			toast.cancel();
			// toast.

		}
	}

	protected void setCurrentView() {
		setContentView(R.layout.activity_splash);
	}

	protected void initData() {
		deviceID = PhoneInfoUtils.getMyUUID(mContext);
		deviceName = PhoneInfoUtils.getMyPhoneModel();
		firmware = PhoneInfoUtils.getMyPhoneVersion();
		appName = PhoneInfoUtils.getAppPackageName(mContext);
		try {
			appVersion = PhoneInfoUtils.getAppVersion(mContext);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mLocationClient = new LocationClient(mContext);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Battery_Saving);
		option.setCoorType("bd09ll");
		option.setScanSpan(20000);
		option.setIsNeedAddress(true);
		option.setNeedDeviceDirect(false);
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.start();

	}

	public class MyLocationListener implements BDLocationListener {

		private String province;
		private String city;

		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			province = arg0.getProvince();
			city = arg0.getCity();
			Map<String, String> map = SharedPreferencesUtils
					.getSharedPreferencesForAppConfig(mContext);
			userID = map.get("userID");
			String phoneNumber = map.get("phoneNumber");
			String password = map.get("password");
			String easeMobUserName = map.get("easeMobUserName");
			String receiveInquiryNotify = map.get("receiveInquiryNotify");
			String assistantID = map.get("assistantID");
			String headPhoto = map.get("headPhoto");
			String getLatitude = arg0.getLatitude() + "";
			String getLongitude = arg0.getLongitude() + "";
			SharedPreferencesUtils.setSharedPreferencesForAppConfig(
					getApplicationContext(), userID, deviceID, deviceName,
					firmware, appName, appVersion, province, city, phoneNumber,
					password, receiveInquiryNotify, easeMobUserName,
					assistantID, headPhoto, getLongitude, getLatitude);
			mLocationClient.stop();
			Message message = Message.obtain();

			// 有网络
			if (AppManager.isNetworkAvailable(getApplicationContext())) {

				// 第一次使用-->跳转登陆,

				if (TextUtils.isEmpty(userID) || userID.equals("0")) {
					message.what = 0x111;
					mHandler.sendMessageDelayed(message, 2 * 1000);
				}
				// 不是第一次登陆-->读取本地保存的密码,上传服务器比对,成功进入主界面
				else {
					message.what = 0x222;
					mHandler.sendMessageDelayed(message, 2 * 1000);
				}
			}

			// 无网络
			// 跳转登陆,提示没有网络
			else {
				message.what = 0x333;
				mHandler.sendMessageDelayed(message, 2 * 1000);
			}

			// 以上操作均在sendmessage的delay中进行
			// 测试-->直接跳转登陆界面
		}
	}

}
