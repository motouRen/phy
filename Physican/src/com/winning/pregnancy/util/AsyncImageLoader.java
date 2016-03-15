package com.winning.pregnancy.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

/**
 * 图片异步加载类
 */
public class AsyncImageLoader {
	// 最大线程数
	private static final int MAX_THREAD_NUM = 10;
	private Map<String, SoftReference<Bitmap>> imageCaches = null;
	private FileUtil fileUtil;
	// 线程池
	private ExecutorService threadPools = null;

	public AsyncImageLoader(Context context) {
		imageCaches = new HashMap<String, SoftReference<Bitmap>>();
		fileUtil = new FileUtil(context);
	}

	public Bitmap loadImage(final ImageView imageView, final String imageName,
			final String strId, final String url,
			final ImageDownloadCallBack imageDownloadCallBack) {
		final String filename = imageName;
		final String filepath = fileUtil.getAbsolutePath() + "/" + filename;

		// 先从软引用中找
		if (imageCaches.containsKey(imageName)) {
			SoftReference<Bitmap> reference = imageCaches.get(imageName);
			Bitmap bitmap = reference.get();

			// 软引用中的 Bitmap 对象可能随时被回收
			// 如果软引用中的 Bitmap 已被回收，则从文件中找
			if (bitmap != null) {
				Log.i("aaaa", "cache exists " + filename);

				return bitmap;
			}
		}

		// 从文件中找
		if (fileUtil.isBitmapExists(filename)) {
			Log.i("aaaa", "file exists " + filename);
			Bitmap bitmap = BitmapFactory.decodeFile(filepath);

			// 重新加入到内存软引用中
			imageCaches.put(imageName, new SoftReference<Bitmap>(bitmap));

			return bitmap;
		}

		// 软引用和文件中都没有再从网络下载
		if (imageName != null && !imageName.equals("")) {
			if (threadPools == null) {
				threadPools = Executors.newFixedThreadPool(MAX_THREAD_NUM);
			}

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 111 && imageDownloadCallBack != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						imageDownloadCallBack.onImageDownloaded(imageView,
								bitmap);
					}
				}
			};

			Thread thread = new Thread() {
				@Override
				public void run() {
					Log.i("aaaa", Thread.currentThread().getName()
							+ " is running");
					// 获取图片
					List<NameValuePair> ls1 = new ArrayList<NameValuePair>();
					ls1.add(new BasicNameValuePair("id", strId));
					Bitmap bitmap = HTTPGetTool.getTool().queryByteForPost(url,
							ls1);

					// 图片下载成功重新缓存并执行回调刷新界面
					if (bitmap != null) {
						// 加入到软引用中
						imageCaches.put(imageName, new SoftReference<Bitmap>(
								bitmap));
						// 缓存到文件系统
						fileUtil.saveBitmap(filepath, bitmap);

						Message msg = new Message();
						msg.what = 111;
						msg.obj = bitmap;
						handler.sendMessage(msg);
					}
				}
			};

			threadPools.execute(thread);
		}

		return null;
	}

	public void loadImageByname(final ImageView imageView,
			final String imageName, final String url,
			final ImageDownloadCallBack imageDownloadCallBack) {
		final String filename = imageName;
		final String filepath = fileUtil.getAbsolutePath() + "/" + filename;

		// 软引用和文件中都没有再从网络下载
		if (imageName != null && !imageName.equals("")) {
			if (threadPools == null) {
				threadPools = Executors.newFixedThreadPool(MAX_THREAD_NUM);
			}

			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.what == 111 && imageDownloadCallBack != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						imageDownloadCallBack.onImageDownloaded(imageView,
								bitmap);
					}
				}
			};

			// 先从软引用中找
			if (imageCaches.containsKey(imageName)) {
				SoftReference<Bitmap> reference = imageCaches.get(imageName);
				Bitmap bitmap = reference.get();

				// 软引用中的 Bitmap 对象可能随时被回收
				// 如果软引用中的 Bitmap 已被回收，则从文件中找
				if (bitmap != null) {
					Log.i("aaaa", "cache exists " + filename);
					Message msg = handler.obtainMessage();
					msg.what = 111;
					msg.obj = bitmap;
					handler.sendMessage(msg);
				}
			} else

			// 从文件中找
			if (fileUtil.isBitmapExists(filename)) {
				Log.i("aaaa", "file exists " + filename);
				Bitmap bitmap = BitmapFactory.decodeFile(filepath);

				// 重新加入到内存软引用中
				imageCaches.put(imageName, new SoftReference<Bitmap>(bitmap));
				Message msg = handler.obtainMessage();
				msg.what = 111;
				msg.obj = bitmap;
				handler.sendMessage(msg);

			} else {
				Thread thread = new Thread() {
					@Override
					public void run() {
						Log.i("aaaa", Thread.currentThread().getName()
								+ " is running");
						// 获取图片
						List<NameValuePair> ls1 = new ArrayList<NameValuePair>();
						ls1.add(new BasicNameValuePair("filename", filename));
						Bitmap bitmap = HTTPGetTool.getTool().queryByteForPost(
								url, ls1);

						// 图片下载成功重新缓存并执行回调刷新界面
						if (bitmap != null) {
							// 加入到软引用中
							imageCaches.put(imageName,
									new SoftReference<Bitmap>(bitmap));
							// 缓存到文件系统
							fileUtil.saveBitmap(filepath, bitmap);

							Message msg = handler.obtainMessage();
							msg.what = 111;
							msg.obj = bitmap;
							handler.sendMessage(msg);
						}
					}
				};

				threadPools.execute(thread);
			}

		}

	}

	/**
	 * @param imageView
	 * @param imageName
	 * @param url
	 * @param imageDownloadCallBack
	 * @return
	 */
	public Bitmap loadImageS(final String imageName, final String url) {
		Bitmap bitmap = null;

		// 软引用和文件中都没有再从网络下载
		if (imageName != null && !imageName.equals("")) {
			// 获取图片
			List<NameValuePair> ls1 = new ArrayList<NameValuePair>();
			ls1.add(new BasicNameValuePair("filename", imageName));
			bitmap = HTTPGetTool.getTool().queryByteForPost(url, ls1);

			// 图片下载成功重新缓存并执行回调刷新界面
			if (bitmap != null) {
				// 加入到软引用中
				imageCaches.put(imageName, new SoftReference<Bitmap>(bitmap));
				// 缓存到文件系统
				fileUtil.saveBitmapByName(imageName, bitmap);
			}
		}

		return bitmap;
	}

	public void shutDownThreadPool() {
		if (threadPools != null) {
			threadPools.shutdown();
			threadPools = null;
		}
	}

	/**
	 * 图片下载完成回调接口
	 */
	public interface ImageDownloadCallBack {
		void onImageDownloaded(ImageView imageView, Bitmap bitmap);
	}

}
