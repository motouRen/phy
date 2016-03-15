package utils;

import java.util.UUID;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

public class PhoneInfoUtils {
	public static String getMyUUID(Context context) {

		final TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;

		tmDevice = "" + tm.getDeviceId();

		tmSerial = "" + tm.getSimSerialNumber();

		androidId = ""
				+ android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

		String uniqueId = deviceUuid.toString();

		return uniqueId;

	}

	public static String getMyPhoneModel() {

		return android.os.Build.MODEL;
	}

	public static String getMyPhoneVersion() {

		return android.os.Build.VERSION.RELEASE;
	}

	public static String getAppPackageName(Context context) {

		return context.getPackageName();
	}

	public static String getAppVersion(Context context)
			throws NameNotFoundException {
		PackageManager packageManager = context.getPackageManager();

		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;

		return version;
	}
}
