package onsiteservice;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.HeaderUtils;
import utils.NetworkUtils;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;

import config.Urls;

public class ArrivedRequest {
	public static HttpHandler<String> arrive(Context context,
			final Handler handler, Map<String, String> map, String serviceID,
			String verificationCode) {
		final Message resultMsg = Message.obtain();
		RequestParams params = HeaderUtils.setHeader(map);
		params.addBodyParameter("serviceID", serviceID);
		params.addBodyParameter("verificationCode", verificationCode);
		HttpHandler<String> httpHandler = NetworkUtils.post(
				Urls.VERIFICATE_URL, params, new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub

						if (msg.what == 0) {
							resultMsg.what = 0;
							try {
								String type = new JSONObject((String) msg.obj)
										.getString("success");
								if (type.equals("0"))
									Log.i("abcd", msg.obj + "");
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
		return httpHandler;
	}
}
