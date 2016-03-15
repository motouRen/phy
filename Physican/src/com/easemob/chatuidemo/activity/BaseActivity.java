/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.easemob.chatuidemo.activity;

import java.io.File;
import java.util.List;
import java.util.Map;

import login.CheckVersionRequest;
import utils.NetworkUtils;
import utils.SharedPreferencesUtils;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import bean.Version;
import bean.VersionDetails;

import com.alibaba.fastjson.JSON;
import com.easemob.applib.controller.HXSDKHelper;
import com.winning.pregnancy.util.AnimUtils;

public class BaseActivity extends FragmentActivity {

	protected BaseActivity oThis = this;
	private Map<String, String> firstmap;
	private String path = Environment.getExternalStoragePublicDirectory(
			Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
			+ File.separator + "yunDoctor.apk";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// onresume时，取消notification显示
		HXSDKHelper.getInstance().getNotifier().reset();
		Map<String, String> map = SharedPreferencesUtils
				.getSharedPreferencesForAppConfig(oThis);
		firstmap = SharedPreferencesUtils.getSharedPreferencesFirstRun(oThis);
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
					SharedPreferencesUtils.setSharedPreferencesFirstRun(oThis,
							"NO");

					List<VersionDetails> list = version.getData();
					if (list.size() >= 1) {
						final String url = version.getData().get(0)
								.getTagetUrl();
						if (version.getSuccess() == 1000) {
							Toast.makeText(oThis, "当前程序版本过低，即将自动升级", 0).show();
							NetworkUtils.download(url, path, new Handler() {
								public void handleMessage(Message msg) {
								};
							}, oThis);
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									oThis);

							// TextView textView_yes = (TextView)
							// view.findViewById(R.id.textView_yes);

							builder.setTitle("更新提示")
									.setPositiveButton("否", null)
									.setNegativeButton(
											"是",
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													NetworkUtils.download(url,
															path,
															new Handler() {
																public void handleMessage(
																		Message msg) {
																};
															}, oThis);

												}
											})
									.setMessage(
											"发现新版本，建议您升级到最新版本使用全新功能，获得更好的用户体验！");
							builder.create().show();

						}

					}

					break;

				default:
					break;
				}
				// Log.i("wait", msg.obj + "");

			}

		}, map, "NO");
		// umeng
		// MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		// MobclickAgent.onPause(this);
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		onBackPressed();
		AnimUtils.inLeftOutRight(oThis);
		// finish();
	}
}
