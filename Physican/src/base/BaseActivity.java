package base;

import java.io.File;
import java.util.List;
import java.util.Map;

import login.CheckVersionRequest;
import utils.AppManager;
import utils.NetworkUtils;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import bean.Version;
import bean.VersionDetails;

import com.alibaba.fastjson.JSON;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.winning.pregnancy.util.AlertDialog;

/**
 * Created by asus on 2015/11/25.
 */
public abstract class BaseActivity extends FragmentActivity {
	private Context context;
	private String path = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
			+ File.separator + "yunDoctor.apk";
	private Map<String, String> firstmap;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		context = this;
		setCurrentView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
		AppManager.getAppManager().addActivity(this);
		setViewData();
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();

		sdkHelper.pushActivity(this);
		Map<String, String> map = SharedPreferencesUtils
				.getSharedPreferencesForAppConfig(context);
		firstmap = SharedPreferencesUtils.getSharedPreferencesFirstRun(context);
		String firstRun = firstmap.get("FIRSTRUN");
		CheckVersionRequest.checkVersion(this, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Version version = JSON.parseObject(msg.obj + "",
							Version.class);
					Log.i("msg", msg.obj + "");
					SharedPreferencesUtils.setSharedPreferencesFirstRun(
							context, "NO");

					List<VersionDetails> list = version.getData();
					if (list.size() >= 1) {
						final String url = version.getData().get(0)
								.getTagetUrl();
						if (version.getSuccess() == 1000) {
							Toast.makeText(context, "当前程序版本过低，即将自动升级", 0)
									.show();
							NetworkUtils.download(url, path, new Handler() {
								public void handleMessage(Message msg) {
								};
							}, context);
						} else {

							AlertDialog dialog = new AlertDialog(context);
							String st = "更新提示";
							String msg1 = "发现新版本，建议您升级到最新版本使用全新功能，获得更好的用户体验！";
							// dialog = new AlertDialog(this);
							dialog.builder();
							dialog.setTitle(st);
							dialog.setMsg(msg1);
							dialog.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											NetworkUtils.download(url, path,
													new Handler() {
														public void handleMessage(
																Message msg) {
														};
													}, context);

										}
									});
							dialog.setNegativeButton("取消",
									new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

										}
									});

							dialog.setCanceledOnTouchOutside(false);

							dialog.show();
							dialog.setOnKeyListener(new OnKeyListener() {

								@Override
								public boolean onKey(DialogInterface dialog,
										int keyCode, KeyEvent event) {
									// TODO Auto-generated method stub
									if (keyCode == KeyEvent.KEYCODE_BACK
											&& event.getRepeatCount() == 0) {
										dialog.dismiss();
										// finish();

									}
									return false;
								}
							});

						}

					}

					break;

				default:
					

					break;
				}
				// Log.i("wait", msg.obj + "");

			}

		}, map, "NO");

	}

	protected abstract void setCurrentView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 给view填充数据
	 */
	protected abstract void setViewData();

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();

		sdkHelper.popActivity(this);
		super.onStop();
	}

}
