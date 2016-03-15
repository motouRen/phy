package com.winning.pregnancy.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.winning.pregnancy.common.MyApplication;

public class HTTPGetTool {
	private static HTTPGetTool tool = null;
	private String cookie = "";
	private static final int TIMEOUT = 30 * 1000;
	private static final int REQUEST_TIMEOUT = 30 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 30 * 1000; // 设置等待数据超时时间10秒钟
	private static final int DEFAULT_HOST_CONNECTIONS = 20;
	private static final int DEFAULT_MAX_CONNECTIONS = 200;
	private static final String CHARSET = HTTP.UTF_8;
	private static HttpClient httpclient;
	private Context mContext;
	private boolean sdkInited = false;

	private HTTPGetTool() {

	}

	public synchronized boolean onInit(Context context) {
		if (sdkInited) {
			return true;
		}
		mContext = context;
		sdkInited = true;
		return true;
	}

	public static synchronized HttpClient getHttpClient() {
		try {
			if (null == httpclient) {

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sslSocketFactory = new MySSLSocketFactory(
						trustStore);
				sslSocketFactory
						.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				HttpParams params = new BasicHttpParams();
				// 设置一些基本参数
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);
				HttpProtocolParams
						.setUserAgent(
								params,
								"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
										+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
				// 超时设置
				/* 从连接池中取连接的超时时间 */
				ConnManagerParams.setTimeout(params, TIMEOUT);
				ConnManagerParams.setMaxConnectionsPerRoute(params,
						new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
				// set max total connections
				ConnManagerParams.setMaxTotalConnections(params,
						DEFAULT_MAX_CONNECTIONS);
				/* 连接超时 */
				HttpConnectionParams.setConnectionTimeout(params,
						REQUEST_TIMEOUT);
				/* 请求超时 */
				HttpConnectionParams.setSoTimeout(params, SO_TIMEOUT);

				HttpConnectionParams.setStaleCheckingEnabled(params, false);

				// 设置我们的HttpClient支持HTTP和HTTPS两种模式
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sslSocketFactory, 443));

				// 使用线程安全的连接管理来创建HttpClient
				ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
						params, schReg);
				httpclient = new DefaultHttpClient(conMgr, params);
			}
			return httpclient;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static HTTPGetTool getTool() {
		if (tool == null)
			tool = new HTTPGetTool();
		return tool;
	}

	public JSONObject post(String url, List<NameValuePair> parameters) {
		if (url == null)
			return null;
		HttpPost post = new HttpPost(url);
		try {
			post.setHeader("Cookie", cookie);
			Map<String, String> params = MyApplication.getInstance()
					.getHeaderparams();
			if (null != params) {
				Set<String> paramsKey = params.keySet();
				for (String key : paramsKey) {
					post.setHeader(key, params.get(key));
				}
			}
			if (parameters != null)
				post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			HttpResponse response = getHttpClient().execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				String strResult = EntityUtils.toString(response.getEntity(),
						HTTP.UTF_8);
				Log.e("web", strResult);
				if (strResult.equals("no session")) {
					return null;
				}
				JSONObject json = JSONObject.parseObject(strResult);
				if (json.getIntValue("success") == 1000) {
					redirectToRecevier(json.getString("data"));
				}
				return json;
			} else {
				if (post.isAborted()) {
					post.abort();
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (post.isAborted()) {
				post.abort();
			}
			return null;
		} finally {
			if (post.isAborted()) {
				post.abort();
			}
		}

	}

	public void redirectToRecevier(String msg) {
		MessageUtils.redirectToRecevier(mContext, msg);
	}

	// 得到服务的传来的byte数组
	public Bitmap queryByteForPost(String url, List<NameValuePair> parameters) {
		try {
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
			HttpResponse response = getHttpClient().execute(post);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {

				InputStream in = response.getEntity().getContent();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[256];
				int c = 0;
				while ((c = in.read(buffer)) != -1) {
					out.write(buffer, 0, c);
				}
				out.flush();
				buffer = out.toByteArray();
				out.close();
				in.close();
				Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0,
						buffer.length);
				System.out.println(bitmap);
				return bitmap;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param url
	 * @param paras
	 *            参数
	 * @param fileParas
	 *            文件参数
	 * @return
	 */
	public StringBuilder sendFile(String url, Map<String, Object> paras,
			Map<String, File> fileParas) {

		BufferedReader br = null;
		StringBuilder sBuilder = new StringBuilder();
		// 由于是使用线程操作http，所以设置Thread safe属性，不然当start多个httpworktask线程时必然报错，这点需要注意
		HttpPost post = new HttpPost(url);
		// 和File数据
		MultipartEntity entity = new MultipartEntity();
		try {
			// 添加参数
			if (paras != null && !paras.isEmpty()) {
				for (Map.Entry<String, Object> item : paras.entrySet()) {
					entity.addPart(item.getKey(), new StringBody(item
							.getValue().toString(), Charset.forName("UTF-8")));
				}
			}

			// 添加文件
			if (fileParas != null && !fileParas.isEmpty()) {
				for (Map.Entry<String, File> item : fileParas.entrySet()) {
					if (item.getValue().exists()) {
						entity.addPart(item.getKey(),
								new FileBody(item.getValue()));
					} else {
					}

				}
			}
			post.setEntity(entity);

			HttpResponse response = getHttpClient().execute(post);

			int statecode = response.getStatusLine().getStatusCode();

			if (statecode == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null) {
					InputStream is = responseEntity.getContent();
					br = new BufferedReader(new InputStreamReader(is));
					String tempStr;
					while ((tempStr = br.readLine()) != null) {
						sBuilder.append(tempStr);
					}
					br.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// return e.getMessage() + "";
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// post.abort();
		// http返回的数据
		// String resData = sBuilder.toString();
		return sBuilder;

	}

	public InputSource getVerXMLFromServer(String url) {
		if (url == null)
			return null;
		HttpGet get = new HttpGet(url);
		try {
			InputSource inStream = new InputSource();
			HttpResponse response = getHttpClient().execute(get);
			int result = response.getStatusLine().getStatusCode();
			if (result == 200) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String str = EntityUtils.toString(entity, "GBK");

					inStream.setCharacterStream(new StringReader(str));
				}
				if (get.isAborted()) {
					get.abort();
				}
				return inStream;
			} else {
				if (get.isAborted()) {
					get.abort();
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (get.isAborted()) {
				get.abort();
			}
			return null;
		}

	}
}
