package utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferencesUtils {
	public static void setSharedPreferencesForAppConfig(Context context,
			String userID, String deviceID, String deviceName, String firmware,
			String appName, String appVersion, String province, String city,
			String phoneNumber, String password, String receiveInquiryNotify,
			String easeMobUserName, String assistantID, String headPhoto,
			String longitude, String latitude) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"APPCONFIG", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("userID", userID);
		editor.putString("deviceID", deviceID);
		editor.putString("deviceName", deviceName);
		editor.putString("firmware", firmware);
		editor.putString("appName", appName);
		editor.putString("appVersion", appVersion);
		editor.putString("province", province);
		editor.putString("city", city);
		editor.putString("phoneNumber", phoneNumber);
		editor.putString("password", password);
		editor.putString("receiveInquiryNotify", receiveInquiryNotify);
		editor.putString("easeMobUserName", easeMobUserName);
		editor.putString("assistantID", assistantID);
		editor.putString("headPhoto", headPhoto);
		editor.putString("longitude", longitude);
		editor.putString("latitude", latitude);
		editor.commit();
	}

	public static Map<String, String> getSharedPreferencesForAppConfig(
			Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"APPCONFIG", Context.MODE_PRIVATE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("userID", sharedPreferences.getString("userID", ""));
		map.put("deviceID", sharedPreferences.getString("deviceID", ""));
		map.put("deviceName", sharedPreferences.getString("deviceName", ""));
		map.put("firmware", sharedPreferences.getString("firmware", ""));
		map.put("appName", sharedPreferences.getString("appName", ""));
		map.put("appVersion", sharedPreferences.getString("appVersion", ""));
		map.put("province", sharedPreferences.getString("province", ""));
		map.put("city", sharedPreferences.getString("city", ""));
		map.put("phoneNumber", sharedPreferences.getString("phoneNumber", ""));
		map.put("password", sharedPreferences.getString("password", ""));
		map.put("receiveInquiryNotify",
				sharedPreferences.getString("receiveInquiryNotify", ""));
		map.put("easeMobUserName",
				sharedPreferences.getString("easeMobUserName", ""));
		map.put("assistantID", sharedPreferences.getString("assistantID", ""));
		map.put("headPhoto", sharedPreferences.getString("headPhoto", ""));

		map.put("longitude", sharedPreferences.getString("longitude", ""));
		map.put("latitude", sharedPreferences.getString("latitude", ""));
		return map;
	}

	public static void resetSharedPreferencesForAppConfig(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"APPCONFIG", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("userID", 0 + "");
		editor.putString("password", "");
		// editor.putString("phoneNumber", "");
		editor.commit();
	}

	public static void setSharedPreferencesTotalUnReadCount(Context context,
			int count, int isShowing) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"TOTALUNREADCOUNT", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("TOTALUNREADCOUNT", count);
		editor.putInt("isShowing", isShowing);
		editor.commit();
	}

	public static Map<String, Integer> getSharedPreferencesTotalUnReadCount(
			Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"TOTALUNREADCOUNT", Context.MODE_PRIVATE);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("TOTALUNREADCOUNT",
				sharedPreferences.getInt("TOTALUNREADCOUNT", 0));
		map.put("isShowing", sharedPreferences.getInt("isShowing", 0));
		return map;
	}

	public static void setSharedPreferencesFirstRun(Context context,
			String firstRun) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"FIRSTRUN", Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("FIRSTRUN", firstRun);
		// editor.putInt("isShowing", isShowing);
		editor.commit();
	}

	public static Map<String, String> getSharedPreferencesFirstRun(
			Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"FIRSTRUN", Context.MODE_PRIVATE);
		Map<String, String> map = new HashMap<String, String>();
		map.put("FIRSTRUN", sharedPreferences.getString("FIRSTRUN", "YES"));
		// map.put("isShowing", sharedPreferences.getInt("isShowing", 0));
		return map;
	}

}