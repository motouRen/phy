package utils;

import java.util.Map;

import com.lidroid.xutils.http.RequestParams;

public class HeaderUtils {
	public static RequestParams setHeader(Map<String, String> map) {
		String userID = map.get("userID");
		String deviceID = map.get("deviceID");
		String deviceName = map.get("deviceName");
		String firmware = map.get("firmware");
		String appName = map.get("appName");
		String appVersion = map.get("appVersion");
		String province = map.get("province");
		String city = map.get("city");
		RequestParams params = new RequestParams();
		params.addHeader("userID", userID);
		params.addHeader("deviceID", deviceID);
		params.addHeader("deviceName", deviceName);
		params.addHeader("firmware", firmware);
		params.addHeader("appName", appName);
		params.addHeader("appVersion", appVersion);
		params.addHeader("province", province);
		params.addHeader("city", city);
		return params;

	}
}