package utils;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.arvin.physican.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NetworkUtils {
	private static BitmapUtils bitmapUtils;

	// private static httpHandler;

	public static BitmapUtils loadBitmapToExternalCache(Context mContext) {

		if (bitmapUtils == null) {
			String diskCachePath = mContext.getExternalCacheDir()
					.getAbsolutePath();
			bitmapUtils = new BitmapUtils(mContext, diskCachePath)
					.configThreadPoolSize(5)
					.configDefaultBitmapMaxSize(300, 300)
					.configDiskCacheEnabled(true)
					.configDefaultBitmapConfig(Config.ALPHA_8)
					.configDefaultLoadingImage(R.drawable.headphoto)
					.configDefaultLoadFailedImage(R.drawable.loading_image);
			// bitmapUtils.d

			Log.i("diskCachePath", diskCachePath);
		}
		return bitmapUtils;
	}

	public static HttpHandler<String> post(final String url,
			RequestParams params, final Handler handler, final Context context) {

		HttpUtils http = new HttpUtils();
		http.configTimeout(5000).configRequestRetryCount(0);
		// http.configResponseTextCharset("网络请求超时，请检查您的网络设置");
		HttpHandler<String> httpHandler = http.send(HttpMethod.POST, url,
				params, new RequestCallBack<String>() {
					private AlertDialog dialog;

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						AlertDialog.Builder builder = new AlertDialog.Builder(
								context);
						LayoutInflater inflater = (LayoutInflater) context
								.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(R.layout.rotatedialog,
								null);
						ImageView imageView = (ImageView) view
								.findViewById(R.id.dialog_imageview);

						imageView.setAnimation(AnimationUtils.loadAnimation(
								context, R.anim.rotate_dialog));
						builder.setView(view);
						dialog = builder.create();

						dialog.show();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Message msg = handler.obtainMessage();
						String result = responseInfo.result;

						if (handler.obtainMessage(msg.what, msg.obj) != null) {
							Message _msg = new Message();
							_msg.what = 0;
							_msg.obj = result;// 或者_msg.setData(message.getData());
							msg = _msg;
							handler.sendMessage(_msg);
							dialog.dismiss();
							return;

						}
						msg.what = 0;
						msg.obj = result;
						Log.i("abcd", result);
						dialog.dismiss();
						handler.sendMessage(msg);

					}

					@Override
					public void onFailure(HttpException e, String s) {

						Message msg = handler.obtainMessage();
						if (handler.obtainMessage(msg.what, msg.obj) != null) {
							Message _msg = new Message();
							_msg.what = 1;
							_msg.obj = "请检查您的网络设置";// 或者_msg.setData(message.getData());
							msg = _msg;
							handler.sendMessage(_msg);
							if (dialog != null) {
								dialog.dismiss();
							}
							return;
						}
						msg.what = 1;
						msg.obj = "请检查您的网络设置";
						if (dialog != null) {
							dialog.dismiss();
						}

						handler.sendMessage(msg);

					}
				});
		return httpHandler;
	}

	public static HttpHandler<String> postNoAnimation(final String url,
			RequestParams params, final Handler handler, final Context context) {

		HttpUtils http = new HttpUtils();
		http.configTimeout(5000);
		// http.configResponseTextCharset("网络请求超时，请检查您的网络设置");
		HttpHandler<String> httpHandler = http.send(HttpMethod.POST, url,
				params, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Message msg = handler.obtainMessage();
						String result = responseInfo.result;

						if (handler.obtainMessage(msg.what, msg.obj) != null) {
							Message _msg = new Message();
							_msg.what = 0;
							_msg.obj = result;// 或者_msg.setData(message.getData());
							msg = _msg;
							handler.sendMessage(_msg);

							return;

						}
						msg.what = 0;
						msg.obj = result;
						Log.i("abcd", result);

						handler.sendMessage(msg);

					}

					@Override
					public void onFailure(HttpException e, String s) {

						Message msg = handler.obtainMessage();
						if (handler.obtainMessage(msg.what, msg.obj) != null) {
							Message _msg = new Message();
							_msg.what = 1;
							_msg.obj = "请检查您的网络设置";// 或者_msg.setData(message.getData());
							msg = _msg;
							handler.sendMessage(_msg);

							return;
						}
						msg.what = 1;
						msg.obj = "请检查您的网络设置";

						handler.sendMessage(msg);

					}
				});
		return httpHandler;
	}

	public static HttpHandler<File> download(final String url, String path,
			final Handler handler, final Context context) {

		HttpUtils http = new HttpUtils();

		HttpHandler<File> httpHandler = http.download(url, path,
				new RequestCallBack<File>() {
					private ProgressDialog dialog;

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();

						dialog = new ProgressDialog(context);
						// 设置进度条风格，风格为圆形，旋转的
						dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						// 设置ProgressDialog 标题
						dialog.setTitle("提示信息");
						// 设置ProgressDialog 提示信息
						dialog.setMessage("app更新中，请稍后...");
						// 设置ProgressDialog 标题图标
						dialog.setIcon(android.R.drawable.ic_dialog_alert);
						// 设置ProgressDialog的最大进度
						dialog.setMax(100);
						dialog.setProgress(0);
						dialog.setCancelable(false);
						dialog.show();

					}

					@Override
					public void onSuccess(ResponseInfo<File> arg0) {
						// TODO Auto-generated method stub

						if (dialog != null && dialog.isShowing()) {
							dialog.setMessage("更新app完毕");
							dialog.dismiss();
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setDataAndType(Uri.fromFile(arg0.result),
									"application/vnd.android.package-archive");
							context.startActivity(intent);
						}

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						// TODO Auto-generated method stub
						super.onLoading(total, current, isUploading);
						dialog.setProgress((int) (current * 100 / total));

					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(context,
								"更新失败，请检查网络设置" + arg0.getMessage(), 0).show();
						if (dialog != null && dialog.isShowing()) {

							dialog.dismiss();
						}

					}
				});
		return httpHandler;
	}

}
