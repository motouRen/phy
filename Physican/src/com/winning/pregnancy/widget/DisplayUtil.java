package com.winning.pregnancy.widget;

import android.content.Context;

public class DisplayUtil {

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
		 
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float px2dipfloat(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  (pxValue / scale + 0.5f);
	}

	public static float dip2pxfloat(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  (dpValue * scale + 0.5f);
	}
		 
	
}
