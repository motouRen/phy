package inquirymessage;

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

public class InquiryRequest {
	public static HttpHandler<String> message(Context context,
			final Handler handler, Map<String, String> map, String urlString,
			String assistantID, String doctorID, String pageNo, String pageSize) {
		final Message resultMsg = Message.obtain();
		RequestParams params = HeaderUtils.setHeader(map);
		params.addBodyParameter("doctorID", doctorID);
		params.addBodyParameter("pageNo", pageNo);
		params.addBodyParameter("assistantID", assistantID);
		params.addBodyParameter("pageSize", pageSize);
		// doctorID=34&pageNo=1&assistantID=1&pageSize=20

		HttpHandler<String> httpHandler = NetworkUtils
				.post("https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByDoctorWithAssistantclosed",
						params, new Handler() {
							@Override
							public void handleMessage(Message msg) {
								// TODO Auto-generated method stub
								Log.i("sss", msg.obj.toString());
								if (msg.what == 0) {
									resultMsg.what = 0;
									try {
										String type = new JSONObject(
												(String) msg.obj)
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
