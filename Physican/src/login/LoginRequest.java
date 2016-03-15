package login;

import org.json.JSONException;
import org.json.JSONObject;

import utils.NetworkUtils;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.lidroid.xutils.http.RequestParams;

import config.Urls;

public class LoginRequest {
	public static void login(Context context, final Handler handler,
			String userID, String deviceID, String deviceName, String firmware,
			String appName, String appVersion, String province, String city,
			String phoneNumber, String password) {

		if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(password))
			return;
		final Message resultMsg = Message.obtain();
		RequestParams params = new RequestParams();
		params.addHeader("userID", userID);
		params.addHeader("deviceID", deviceID);
		params.addHeader("deviceName", deviceName);
		params.addHeader("firmware", firmware);
		params.addHeader("appName", appName);
		params.addHeader("appVersion", appVersion);
		params.addHeader("province", province);
		params.addHeader("city", city);
		params.addBodyParameter("userName", phoneNumber);
		params.addBodyParameter("password", password);

		NetworkUtils.post(Urls.LOGIN_URL, params, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					resultMsg.what = 0;
					try {
						String type = new JSONObject((String) msg.obj)
								.getString("success");
						if (type.equals("0"))
							;
						else
							resultMsg.what = 1;
						resultMsg.obj = msg.obj;
						;
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					resultMsg.what = 1;
					resultMsg.obj = msg.obj;
				}
				handler.sendMessage(resultMsg);
			}
		}, context);
	}
}
