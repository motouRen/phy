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
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.arvin.physican.R;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chatuidemo.utils.ImageCache;
import com.easemob.chatuidemo.widget.photoview.PhotoView;
import com.easemob.util.EMLog;
import com.easemob.util.ImageUtils;
import com.easemob.util.PathUtil;
import com.winning.pregnancy.util.ImageLoader;

/**
 * 下载显示大图
 * 
 */
public class ShowBigImage extends BaseActivity {
	private static final String TAG = "ShowBigImage";
	private ProgressDialog pd;
	private PhotoView image;
	private int default_res = R.drawable.default_image;
	private String localFilePath;
	private Bitmap bitmap;
	private boolean isDownloaded;
	private ProgressBar loadLocalPb;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_big_image);
		super.onCreate(savedInstanceState);

		image = (PhotoView) findViewById(R.id.image);
		loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);
		default_res = getIntent().getIntExtra("default_image",
				R.drawable.default_avatar);
		Uri uri = getIntent().getParcelableExtra("uri");
		String remotepath = getIntent().getExtras().getString("remotepath");
		String secret = getIntent().getExtras().getString("secret");
		ImageLoader.getInstances().displayImage(remotepath, image,
				new ImageLoader.OnImageLoaderListener() {
					@Override
					public void onProgressImageLoader(ImageView imageView,
							int currentSize, int totalSize) {
						// holder.tv.setText(currentSize * 100 / totalSize +
						// "%");
					}

					@Override
					public void onFinishedImageLoader(ImageView imageView,
							Bitmap bitmap) {
						// holder.tv.setVisibility(View.GONE);
						// holder.pb.setVisibility(View.GONE);

					}
				});
		EMLog.d(TAG, "show big image uri:" + uri + " remotepath:" + remotepath);

	}

	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * 
	 * @param remoteUrl
	 * @return
	 */
	public String getLocalFilePath(String remoteUrl) {
		String localPath;
		if (remoteUrl.contains("/")) {
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		} else {
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath()
					+ "/" + remoteUrl;
		}
		return localPath;
	}

	/**
	 * 下载图片
	 * 
	 * @param remoteFilePath
	 */
	private void downloadImage(final String remoteFilePath,
			final Map<String, String> headers) {
		String str1 = getResources().getString(R.string.Download_the_pictures);
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage(str1);
		pd.show();
		localFilePath = getLocalFilePath(remoteFilePath);
		final EMCallBack callback = new EMCallBack() {
			public void onSuccess() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						DisplayMetrics metrics = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(
								metrics);
						int screenWidth = metrics.widthPixels;
						int screenHeight = metrics.heightPixels;

						bitmap = ImageUtils.decodeScaleImage(localFilePath,
								screenWidth, screenHeight);
						if (bitmap == null) {
							image.setImageResource(default_res);
						} else {
							image.setImageBitmap(bitmap);
							ImageCache.getInstance().put(localFilePath, bitmap);
							isDownloaded = true;
						}
						if (pd != null) {
							pd.dismiss();
						}
					}
				});
			}

			public void onError(int error, String msg) {
				EMLog.e(TAG, "offline file transfer error:" + msg);
				File file = new File(localFilePath);
				if (file.exists() && file.isFile()) {
					file.delete();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						pd.dismiss();
						image.setImageResource(default_res);
					}
				});
			}

			public void onProgress(final int progress, String status) {
				EMLog.d(TAG, "Progress: " + progress);
				final String str2 = getResources().getString(
						R.string.Download_the_pictures_new);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						pd.setMessage(str2 + progress + "%");
					}
				});
			}
		};

		EMChatManager.getInstance().downloadFile(remoteFilePath, localFilePath,
				headers, callback);

	}

	@Override
	public void onBackPressed() {
		if (isDownloaded)
			setResult(RESULT_OK);
		finish();
	}
}
