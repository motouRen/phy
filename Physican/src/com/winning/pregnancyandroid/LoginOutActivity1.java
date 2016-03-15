package com.winning.pregnancyandroid;

import login.LoginActivity;
import utils.AppManager;
import utils.SharedPreferencesUtils;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.arvin.physican.R;
import com.easemob.EMCallBack;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.util.AlertDialog;
import com.winning.pregnancy.util.PreferencesUtils;

public class LoginOutActivity1 extends BaseActivity {

	AlertDialog dialog;

	@Override
	public void setView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.translucent);

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		showConflictDialog();
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		PreferencesUtils.putString(oThis, "user", "");
		MyApplication.getInstance().logout(null);
		String st = getResources().getString(R.string.Logoff_notification);
		String msg = getResources().getString(R.string.connect_conflict);
		dialog = new AlertDialog(oThis);
		dialog.builder();
		dialog.setTitle(st);
		dialog.setMsg(msg);

		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logout();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getRepeatCount() == 0) {
					dialog.dismiss();
					SharedPreferencesUtils
							.resetSharedPreferencesForAppConfig(oThis);
					PreferencesUtils.putString(oThis, "user", "");
					PreferencesUtils.putString(oThis, "dlmm", "");
					MyApplication.getInstance().logout(null);
					finish();
					AppManager.getAppManager().finishAllActivity();
					startActivity(new Intent(oThis, LoginActivity.class));

				}
				return false;
			}
		});
	}

	void logout() {
		MyApplication.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				oThis.runOnUiThread(new Runnable() {
					public void run() {
						SharedPreferencesUtils
								.resetSharedPreferencesForAppConfig(oThis);
						PreferencesUtils.putString(oThis, "user", "");
						PreferencesUtils.putString(oThis, "dlmm", "");
						MyApplication.getInstance().logout(null);
						finish();
						AppManager.getAppManager().finishAllActivity();
						startActivity(new Intent(oThis, LoginActivity.class));

					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {

			}
		});
	}

}
