/**
 * 
 */
package com.winning.pregnancy.util;

import android.app.Activity;
import android.content.Context;

import com.arvin.physican.R;

/**
 * 项目名称：EcareAndroid 类名称：AnimUtils 类描述： 创建人：ZJJ 创建时间：2015-2-15 下午1:40:44 修改人：ZJJ
 * 修改时间：2015-2-15 下午1:40:44 修改备注：
 * 
 * @version
 */
public class AnimUtils {
	/**
	 * 
	 * 
	 * @Title: inRightOutleft
	 * 
	 * @Description: TODO(设置切换动画，从右边进入，左边退出)
	 * 
	 * @param @param context 设定文件
	 * 
	 * @return void 返回类型
	 * 
	 * @throws
	 */
	public static void inRightOutleft(Context context) {
		((Activity) context).overridePendingTransition(R.anim.in_from_right,
				R.anim.out_to_left);
	}

	public static void inLeftOutRight(Context context) {
		// 设置切换动画，从左边进入，右边退出
		((Activity) context).overridePendingTransition(R.anim.in_from_left,
				R.anim.out_to_right);
	}

	public static void inDownOutUp(Context context) {
		// 设置切换动画，从下边进入，上边退出
		((Activity) context).overridePendingTransition(R.anim.in_from_down,
				R.anim.out_to_up);
	}

	public static void slideInRightOutleft(Context context) {
		((Activity) context).overridePendingTransition(R.anim.slide_right,
				R.anim.slide_left);
	}

	public static void slideInLeftOutRight(Context context) {
		// 设置切换动画，从左边进入，右边退出
		((Activity) context).overridePendingTransition(R.anim.slide_left,
				R.anim.slide_right);
	}

}
