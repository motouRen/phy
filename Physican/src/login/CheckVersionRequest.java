package login;

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

public class CheckVersionRequest {
	public static void checkVersion(Context context, final Handler handler,
			Map<String, String> map, String firstRun) {
		final Message resultMsg = Message.obtain();
		RequestParams params = HeaderUtils.setHeader(map);
		params.addBodyParameter("firstRun", firstRun);
		HttpHandler<String> httpHandler = NetworkUtils.postNoAnimation(
				Urls.CHECK_VERSION_URL, params, new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Log.i("sss", msg.obj.toString());
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

	}
	
	
	
	public static void checkVersion1(Context context, final Handler handler,
			Map<String, String> map, String firstRun) {
		final Message resultMsg = Message.obtain();
		RequestParams params = HeaderUtils.setHeader(map);
		params.addBodyParameter("firstRun", firstRun);
		HttpHandler<String> httpHandler = NetworkUtils.post(
				Urls.CHECK_VERSION_URL, params, new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Log.i("sss", msg.obj.toString());
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

	}
}
