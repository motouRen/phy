package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.util.Log;

public class DateUtils {
	public static String getCurrentDate() {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return sdf.format(calendar.getTime());
	}

	public static String yearsBetween(String smDate, String bDate) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			calendar.setTime(sdf.parse(smDate));
			long time1 = calendar.getTimeInMillis();
			calendar.setTime(sdf.parse(bDate));
			long time2 = calendar.getTimeInMillis();

			long between_years = ((time1 - time2) / (1000 * 3600 * 24)) / 365;
			Log.i("aaaaa", time1 - time2 + "");
			return between_years + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0 + "";
	}
}
