/**
 * 
 */
package com.winning.pregnancyandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import login.CheckVersionRequest;
import utils.NetworkUtils;
import utils.SharedPreferencesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import bean.Version;
import bean.VersionDetails;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.R;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.util.AnimUtils;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.widget.CustomProgressDialog;

/**
 * @author ZJJ
 */
public abstract class BaseActivity extends FragmentActivity {
	protected MyApplication application;
	protected BaseActivity oThis = this;
	protected int screenheight;
	protected int screenwidth;
	protected Display display;
	protected CustomProgressDialog proDialog;// 加载框

	// 分页加载
	protected int start = 0;
	protected int limit = 15;
	protected static final int pageLoadMore = 15;

	public static ArrayList<Activity> activityList = new ArrayList<Activity>();
	protected List<Map<String, String>> listmain = new ArrayList<Map<String, String>>();
	protected List<Map<String, String>> listsub = new ArrayList<Map<String, String>>();
	protected ListView commListView;

	protected final List<String> checked = new ArrayList<String>();// 缓存多选框选中项
	protected ImageView leftMenu, rightIvMenu;
	protected TextView menuLeft;
	protected TextView menuRight;
	protected TextView menuTitle;
	protected LinearLayout menuLeftLine;
	protected InputMethodManager manager;
	protected AlertDialog outdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		application = (MyApplication) getApplication();
		activityList.add(this);
		
		setView();
		initView();
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		WindowManager windowManager = getWindowManager();
		display = windowManager.getDefaultDisplay();
		screenwidth = (int) (display.getWidth());
		screenheight = (int) (display.getHeight());
		setListener();
		super.onStart();
	}

	/**
	 * 设置布局文件
	 */
	public abstract void setView();

	/**
	 * 初始化布局文件中的控件
	 */
	public abstract void initView();

	/**
	 * 设置控件的监听
	 */
	public abstract void setListener();

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityList.remove(this);
	}

	/**
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// 返回键退回
			oThis.finish();
			AnimUtils.inRightOutleft(oThis);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected static boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
		try {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
			return (netWorkInfo != null && netWorkInfo.isAvailable());// 检测网络是否可用
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	protected void openProDialog(String str) {
		if (StringUtil.isEmpty(str)) {
			str = "数据加载中。。。";
		}
		proDialog = CustomProgressDialog.createDialog(oThis);
		proDialog.setMessage(str);
		proDialog.show();
		proDialog.setCanceledOnTouchOutside(false);
	}

	protected void closeProDialog() {
		if (proDialog != null && proDialog.isShowing()) {
			proDialog.dismiss();
		}
	}

	public void exitApp() {
		if (outdialog != null && outdialog.isShowing()) {
			outdialog.dismiss();
		}
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		final View view = layoutInflater.inflate(R.layout.exit_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		outdialog = builder.create();
		outdialog.setView(view);
		outdialog.setCanceledOnTouchOutside(false);
		outdialog.show();
		outdialog.getWindow().setContentView(R.layout.exit_dialog);

		Window mWindow = outdialog.getWindow();
		WindowManager.LayoutParams lp = mWindow.getAttributes();
		lp.width = (int) (screenwidth * 4 / 5); // 设置宽度
		mWindow.setAttributes(lp);

		Button left = (Button) outdialog.findViewById(R.id.left);
		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				outdialog.dismiss();
				// updateManager.closeNotification();
				// new Notifier(oThis).closeNotification();
				if (activityList.size() > 0) {
					for (Activity activity : activityList) {
						activity.finish();
					}
					android.os.Process.killProcess(android.os.Process.myPid());
				}
			}
		});

		Button right = (Button) outdialog.findViewById(R.id.right);
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				outdialog.dismiss();
			}
		});
	}
}
