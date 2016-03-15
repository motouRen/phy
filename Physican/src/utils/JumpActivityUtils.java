package utils;

import onsiteservice.ArrivedActivity;
import onsiteservice.BeginOnsite;
import onsiteservice.ClosedActivity;
import onsiteservice.DoneActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class JumpActivityUtils {
	public static synchronized void jumpActivity(Context mContext, int state,
			int position, String json) {

		// 跳转到二级页面

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (state) {
		case 1:
			intent.setClass(mContext, BeginOnsite.class);
			break;
		case 3:
			intent.setClass(mContext, ClosedActivity.class);
			break;
		case 4:
			intent.setClass(mContext, DoneActivity.class);
			break;
		default:
			intent.setClass(mContext, ArrivedActivity.class);
			break;
		}
		//
		bundle.putString("json", json);
		bundle.putInt("position", position);
		intent.putExtras(bundle);
		mContext.startActivity(intent);
		// mContext.startActivity(intent);
	}

}
