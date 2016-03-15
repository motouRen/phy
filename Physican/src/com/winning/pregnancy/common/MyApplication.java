package com.winning.pregnancy.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import bean.Doctor;
import bean.DoctorData;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.OSSServiceProvider;
import com.alibaba.sdk.android.oss.model.AccessControlList;
import com.alibaba.sdk.android.oss.model.AuthenticationType;
import com.alibaba.sdk.android.oss.model.ClientConfiguration;
import com.alibaba.sdk.android.oss.model.TokenGenerator;
import com.alibaba.sdk.android.oss.util.OSSToolKit;
import com.arvin.physican.R;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.winning.cqs.pregnantbabycare_cqs_v1.utils.DeviceHelp;
import com.winning.pregnancy.dao.EMMessageLocalDao;
import com.winning.pregnancy.dao.impl.EMMessageLocalImpl;
import com.winning.pregnancy.util.CacheConfig;
import com.winning.pregnancy.util.HTTPGetTool;
import com.winning.pregnancy.util.ImageLoader;
import com.winning.pregnancy.util.PreferencesUtils;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.util.ThreadPoolUtils;

public class MyApplication extends Application {

	// 环信////////////////

	public static Context applicationContext;

	private static MyApplication instance;

	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";

	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();

	private String url;

	static final String accessKey = "O8F5WdUUmupA6xRw"; // 测试代码没有考虑AK/SK的安全性
	static final String screctKey = "8soYx7q9KJ81rmyjEEOPEJfW78pYSK";

	public static OSSService ossService = OSSServiceProvider.getService();

	private Map<String, String> Headerparams;

	private Date currentDay;

	public static final String BUCKETNAME = "yoyub";

	public final static String OSSPATH = "https://yoyub.oss-cn-hangzhou.aliyuncs.com/inquiry/";

	public final static String OSSHEADPATH = "https://yoyub.oss-cn-hangzhou.aliyuncs.com/headPhoto/";

	private Doctor doctor;
	public static int screenWidth;
	public static int screenHeight;
	public static int screenDPI;
	private DoctorData user;

	@Override
	public void onCreate() {
		// 腾讯 日志
		// CrashReport.initCrashReport(this, "900015212", false);
		// // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		// SDKInitializer.initialize(this);
		super.onCreate();
		applicationContext = this;
		SDKInitializer.initialize(getApplicationContext());
		WindowManager wm = (WindowManager) this
				.getSystemService(WINDOW_SERVICE);
		DisplayMetrics ds = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(ds);
		screenWidth = ds.widthPixels;
		screenHeight = ds.heightPixels;
		screenDPI = ds.densityDpi;
		instance = this;
		/**
		 * this function will initialize the HuanXin SDK
		 * 
		 * @return boolean true if caller can continue to call HuanXin related
		 *         APIs after calling onInit, otherwise false.
		 * 
		 *         环信初始化SDK帮助函数
		 *         返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 * 
		 *         for example: 例子：
		 * 
		 *         public class DemoHXSDKHelper extends HXSDKHelper
		 * 
		 *         HXHelper = new DemoHXSDKHelper();
		 *         if(HXHelper.onInit(context)){ // do HuanXin related work }
		 */
		hxSDKHelper.onInit(applicationContext);
		initOss();
		Headerparams = new HashMap<String, String>();
		getDeviceInfo();
		ImageLoader.init(getApplicationContext(), new CacheConfig()
				.setDefRequiredSize(600) // 设置默认的加载图片尺寸（表示宽高任一不超过该值，默认是70px）
				.setDefaultResId(R.drawable.ddf) // 设置显示的默认图片（默认是0，即空白图片）
				.setBitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片位图模式（默认是Bitmap.CacheConfig.ARGB_8888）
				.setMemoryCachelimit(Runtime.getRuntime().maxMemory() / 3) // 设置图片内存缓存大小（默认是Runtime.getRuntime().maxMemory()
																			// /
																			// 4）
				// .setFileCachePath(Environment.getExternalStorageDirectory().toString()
				// + "/mycache") // 设置文件缓存保存目录
				);
		emMessageLocalDao = new EMMessageLocalImpl(applicationContext);
	}

	private void initOss() {

		// 初始化设置
		ossService.setApplicationContext(MyApplication.getInstance());
		ossService.setGlobalDefaultHostId("oss-cn-hangzhou.aliyuncs.com"); // 设置region
																			// host
																			// 即
																			// endpoint
		ossService.setGlobalDefaultACL(AccessControlList.PUBLIC_READ); // 默认为private
		ossService.setAuthenticationType(AuthenticationType.ORIGIN_AKSK); // 设置加签类型为原始AK/SK加签
		ossService.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
					@Override
					public String generateToken(String httpMethod, String md5,
							String type, String date, String ossHeaders,
							String resource) {

						String content = httpMethod + "\n" + md5 + "\n" + type
								+ "\n" + date + "\n" + ossHeaders + resource;

						return OSSToolKit.generateToken(accessKey, screctKey,
								content);
					}
				});
		ossService
				.setCustomStandardTimeWithEpochSec(System.currentTimeMillis() / 1000);

		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectTimeout(15 * 1000); // 设置全局网络连接超时时间，默认30s
		conf.setSocketTimeout(15 * 1000); // 设置全局socket超时时间，默认30s
		conf.setMaxConnections(50); // 设置全局最大并发网络链接数, 默认50个
		ossService.setClientConfiguration(conf);
		// bucket = ossService.getOssBucket("yoyub");
		HTTPGetTool.getTool().onInit(applicationContext);
	}

	private void getDeviceInfo() {
		// TODO Auto-generated method stub
		DeviceHelp dvHelper = new DeviceHelp(getApplicationContext());
		String deviceName = dvHelper.getDeviceName();
		String deviceID = dvHelper.getUUID();
		String appVersion = dvHelper.getPackageVersion();
		String firmware = dvHelper.getSysVersion();
		String appName = dvHelper.getPackageName();
		if (isLogin()) {
			Headerparams.put("userID", String.valueOf(getUser().getId()));
		} else {
			Headerparams.put("userID", "");
		}
		Headerparams.put("deviceID", deviceID);
		Headerparams.put("deviceToken", "");
		Headerparams.put("deviceName", deviceName);
		Headerparams.put("firmware", firmware);
		Headerparams.put("appName", appName);
		Headerparams.put("appVersion", appVersion);
		Headerparams.put("logintude", "");
		Headerparams.put("latitude", "");
		Headerparams.put("province", "");
		Headerparams.put("city", "");

	}

	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 * 
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 * 
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void emLogin() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			// Toast.makeText(this, R.string.network_isnot_available,
			// Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtil.isEmpty(PreferencesUtils.getString(applicationContext,
				"user", ""))) {

			return;
		}

		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(
				PreferencesUtils.getString(applicationContext, "dlzh"),
				"winningsoft", new EMCallBack() {

					@Override
					public void onSuccess() {
						// 登陆成功，保存用户名密码
						MyApplication.getInstance().setUserName(
								PreferencesUtils.getString(applicationContext,
										"dlzh"));
						MyApplication.getInstance().setPassword("winningsoft");

						Log.i("dlzh", PreferencesUtils.getString(
								applicationContext, "dlzh"));

						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							// 处理好友和群组
							initializeContacts();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面

							ThreadPoolUtils.execute(new Runnable() {
								public void run() {
									MyApplication.getInstance().logout(null);
								}
							});
							return;
						}
						// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						boolean updatenick = EMChatManager.getInstance()
								.updateCurrentUserNick(
										MyApplication.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity",
									"update current user nick fail");
						}
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
						ThreadPoolUtils.execute(new Runnable() {
							public void run() {
							}
						});
					}
				});
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends
				.setUsername(com.easemob.chatuidemo.Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(com.easemob.chatuidemo.Constant.NEW_FRIENDS_USERNAME,
				newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(com.easemob.chatuidemo.Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(com.easemob.chatuidemo.Constant.GROUP_USERNAME, groupUser);

		// 添加"Robot"
		User robotUser = new User();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(com.easemob.chatuidemo.Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(com.easemob.chatuidemo.Constant.CHAT_ROBOT, robotUser);

		// 存入内存
		MyApplication.getInstance().setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(applicationContext);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}

	protected void logout() {
		MyApplication.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				emLogin();
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {

			}
		});
	}

	private EMMessageLocalDao emMessageLocalDao;

	public int getUnreadMsgCountTotal() {
		List<Map<String, String>> tempList = new ArrayList<Map<String, String>>();
		String sql = "select * from EMMessageLocal where isshow=1 and isread=1 and doctorID="
				+ MyApplication.getInstance().getUser().getId();
		tempList = emMessageLocalDao.query2MapList(sql, new String[] {});

		return tempList.size();
	}

	public Map<String, String> getHeaderparams() {
		if (isLogin()) {
			Headerparams.put("userID", String.valueOf(getUser().getId()));
		} else {
			Headerparams.put("userID", "");
		}
		return Headerparams;
	}

	public void setHeaderparams(Map<String, String> headerparams) {
		Headerparams = headerparams;
	}

	public boolean isLogin() {
		boolean flag = false;
		if (StringUtil.isNotEmpty(PreferencesUtils.getString(
				applicationContext, "user", ""))) {
			flag = true;
		}
		return flag;
	}

	public DoctorData getUser() {
		if (StringUtil.isNotEmpty(PreferencesUtils.getString(
				applicationContext, "user", ""))) {
			doctor = JSON.parseObject(
					PreferencesUtils.getString(applicationContext, "user", ""),
					Doctor.class);
			user = doctor.getData().get(0);

			// DoctorData data = doctor.getData().get(0);
			// 登录成功，修改app配置

		} else {
			user = new DoctorData();
		}

		return user;
	}

	public void setUser(DoctorData user) {
		this.user = user;
	}

	public Date getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(Date currentDay) {
		this.currentDay = currentDay;
	}

}
