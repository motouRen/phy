package login;

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
import com.winning.pregnancyandroid.BaseActivity;

public class ExitLoginActivity extends BaseActivity {

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

		String st = "退出程序";
		String msg = "是否退出孕医生?";
		dialog = new AlertDialog(oThis);
		dialog.builder();
		dialog.setTitle(st);
		dialog.setMsg(msg);

		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppManager.getAppManager().AppExit(MyApplication.getInstance());

			}
		});
		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
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
					finish();

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
