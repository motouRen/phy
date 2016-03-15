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
package com.easemob.chatuidemo;

import inquirymessage.InquiryHistoryActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import utils.MySQLiteOpenHelper;
import utils.SharedPreferencesUtils;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.R;
import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.applib.model.HXNotifier;
import com.easemob.applib.model.HXNotifier.HXNotificationInfoProvider;
import com.easemob.applib.model.HXSDKModel;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.MainActivity;
import com.easemob.chatuidemo.activity.VideoCallActivity;
import com.easemob.chatuidemo.activity.VoiceCallActivity;
import com.easemob.chatuidemo.domain.RobotUser;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.receiver.CallReceiver;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.dao.EMMessageLocalDao;
import com.winning.pregnancy.dao.impl.EMMessageLocalImpl;
import com.winning.pregnancy.model.EMMessageLocal;
import com.winning.pregnancy.util.MessageUtils;
import com.winning.pregnancy.util.MyTimeUtil;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancyandroid.InquiryActivity;
import com.winning.pregnancyandroid.LoginOutActivity1;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * 
 * @author easemob
 * 
 */
public class DemoHXSDKHelper extends HXSDKHelper {

	private static final String TAG = "DemoHXSDKHelper";

	/**
	 * EMEventListener
	 */
	protected EMEventListener eventListener = null;

	/**
	 * contact list in cache
	 */
	private Map<String, User> contactList;

	/**
	 * robot list in cache
	 */
	private Map<String, RobotUser> robotList;
	private CallReceiver callReceiver;

	/**
	 * 用来记录foreground Activity
	 */
	private List<Activity> activityList = new ArrayList<Activity>();
	private int reload;

	public void pushActivity(Activity activity) {
		if (!activityList.contains(activity)) {
			activityList.add(0, activity);
		}
	}

	public void popActivity(Activity activity) {
		activityList.remove(activity);
	}

	private EMMessageLocalDao emMessageLocalDao;

	@Override
	protected void initHXOptions() {
		super.initHXOptions();

		// you can also get EMChatOptions to set related SDK options
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		options.allowChatroomOwnerLeave(getModel()
				.isChatroomOwnerLeaveAllowed());
		emMessageLocalDao = new EMMessageLocalImpl(appContext);
	}

	@Override
	protected void initListener() {
		super.initListener();
		IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance()
				.getIncomingCallBroadcastAction());
		if (callReceiver == null) {
			callReceiver = new CallReceiver();
		}

		// 注册通话广播接收者
		appContext.registerReceiver(callReceiver, callFilter);
		// 注册消息事件监听
		initEventListener();
	}

	/**
	 * 全局事件监听 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理 activityList.size()
	 * <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
	 */
	protected void initEventListener() {
		eventListener = new EMEventListener() {
			private BroadcastReceiver broadCastReceiver = null;

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = null;
				if (event.getData() instanceof EMMessage) {
					message = (EMMessage) event.getData();
					EMLog.d(TAG, "receive the event : " + event.getEvent()
							+ ",id : " + message.getMsgId());
				}
				EMLog.d(TAG,
						"receive the event : " + JSON.toJSONString(message));

				switch (event.getEvent()) {
				case EventNewMessage:
					// 应用在后台，不需要刷新UI,通知栏提示新消息

					if (activityList.size() <= 0) {
						com.alibaba.fastjson.JSONObject jsonObject = JSON
								.parseObject(JSON.toJSONString(message));
						EMMessageLocal emMessageLocal = new EMMessageLocal();
						emMessageLocal.setAcked(jsonObject.getString("acked"));
						emMessageLocal.setChatType(jsonObject
								.getString("chatType"));
						emMessageLocal.setDelivered(jsonObject
								.getString("delivered"));
						emMessageLocal
								.setDirect(jsonObject.getString("direct"));
						emMessageLocal.setError(jsonObject.getString("error"));
						emMessageLocal
								.setFromUser(jsonObject.getString("from"));
						emMessageLocal.setIsAcked(jsonObject
								.getString("isAcked"));
						emMessageLocal.setIsDelivered(jsonObject
								.getString("isDelivered"));
						emMessageLocal.setIsread("1");
						emMessageLocal.setIsshow("1");
						emMessageLocal.setListened(jsonObject
								.getString("listened"));
						emMessageLocal.setMessage(jsonObject.getJSONObject(
								"body").getString("message"));
						emMessageLocal.setMsgId(jsonObject.getString("msgId"));
						emMessageLocal
								.setMsgTime(MyTimeUtil.convert2String(Long
										.parseLong(jsonObject
												.getString("msgTime")),
										MyTimeUtil.yyyy_MM_dd_HH_mm_ss));
						emMessageLocal
								.setStatus(jsonObject.getString("status"));
						emMessageLocal.setToUser(jsonObject.getString("to"));
						emMessageLocal.setType(jsonObject.getString("type"));
						emMessageLocal.setUserName(jsonObject
								.getString("userName"));
						String ext = message.getStringAttribute("inquiry", "");
						if (StringUtil.isNotEmpty(ext)) {
							reload = 0;
							com.alibaba.fastjson.JSONObject json = JSON
									.parseObject(ext);
							String state = json.getString("state");
							if (StringUtil.isNotEmpty(state)) {
								if (state.equals("2")) {
									HXSDKHelper.getInstance().getNotifier()
											.viberateAndPlayTone(message);

								} else {

									emMessageLocal.setMessageID(json
											.getString("messageID"));
									emMessageLocal.setServiceID(json
											.getString("serviceID"));
									emMessageLocal.setDoctorID(String
											.valueOf(MyApplication
													.getInstance().getUser()
													.getId()));
									Map<String, Integer> map = SharedPreferencesUtils
											.getSharedPreferencesTotalUnReadCount(appContext);
									int showing = map.get("isShowing");
									int count1 = map.get("TOTALUNREADCOUNT");
									Log.i("isShowing", showing + "");

									if (showing == 0) {
										if (com.arvin.physican.MainActivity.instance != null) {

											com.arvin.physican.MainActivity.instance
													.updateMsg();
										}
										SharedPreferencesUtils
												.setSharedPreferencesTotalUnReadCount(
														appContext, ++count1,
														showing);
										String sql = "select * from tb_doctors where serviceid=?";
										MySQLiteOpenHelper helper = new MySQLiteOpenHelper(
												appContext);
										List<Map<String, String>> mapList = helper
												.selectList(
														sql,
														new String[] { json
																.getString("serviceID") });
										if (mapList.size() <= 0) {
											String insertsql = "insert into tb_doctors(serviceid , count) values(? , ?)";
											boolean flag = helper
													.execData(
															insertsql,
															new String[] {
																	json.getString("serviceID"),
																	1 + "" });
											// Toast.makeText(mContext, count +
											// "个位",
											// 0).show();

										} else {
											Log.i("mapList", mapList.get(0)
													.toString());
											// Toast.makeText(mContext, count +
											// "个",
											// 0).show();
											int count = Integer
													.parseInt(mapList.get(0)
															.get("count"));
											String updatesql = "update tb_doctors set count=?  where serviceid=?";
											++count;

											helper.execData(
													updatesql,
													new String[] {
															count + "",
															json.getString("serviceID") });
										}

									}

								}
								emMessageLocalDao.insert(emMessageLocal);
								HXSDKHelper.getInstance().getNotifier()
										.onNewMsg(message);
							}

						} else {
							reload = 1;
							HXSDKHelper.getInstance().getNotifier()
									.onNewMsg(message);
						}
					} else {
						String ext = message.getStringAttribute("inquiry", "");
						if (StringUtil.isNotEmpty(ext)) {
							reload = 0;
							com.alibaba.fastjson.JSONObject json = JSON
									.parseObject(ext);
							String sql = "select * from tb_doctors where serviceid=?";
							MySQLiteOpenHelper helper = new MySQLiteOpenHelper(
									appContext);
							List<Map<String, String>> mapList = helper
									.selectList(sql, new String[] { json
											.getString("serviceID") });
							Map<String, Integer> map = SharedPreferencesUtils
									.getSharedPreferencesTotalUnReadCount(appContext);
							int count = map.get("TOTALUNREADCOUNT");
							int showing = map.get("isShowing");
							String state = json.getString("state");
							if (StringUtil.isNotEmpty(state)) {
								if (state.equals("2")) {
									HXSDKHelper.getInstance().getNotifier()
											.viberateAndPlayTone(message);

								} else {

									Log.i("isShowing", showing + "前");
									if (showing == 0) {
										if (com.arvin.physican.MainActivity.instance != null) {

											SharedPreferencesUtils
													.setSharedPreferencesTotalUnReadCount(
															appContext,
															++count, showing);
											com.arvin.physican.MainActivity.instance
													.updateMsg();
										}
										if (mapList.size() <= 0) {
											String insertsql = "insert into tb_doctors(serviceid , count) values(? , ?)";
											boolean flag = helper
													.execData(
															insertsql,
															new String[] {
																	json.getString("serviceID"),
																	1 + "" });
											// Toast.makeText(mContext, count +
											// "个位",
											// 0).show();

										} else {
											Log.i("mapList", mapList.get(0)
													.toString());
											// Toast.makeText(mContext, count +
											// "个",
											// 0).show();
											int count1 = Integer
													.parseInt(mapList.get(0)
															.get("count"));
											String updatesql = "update tb_doctors set count=?  where serviceid=?";
											++count1;
											helper.execData(
													updatesql,
													new String[] {
															count1 + "",
															json.getString("serviceID") });
										}

									}

								}
								HXSDKHelper.getInstance().getNotifier()
										.viberateAndPlayTone(message);
							}

						} else {
							reload = 1;
							HXSDKHelper.getInstance().getNotifier()
									.onNewMsg(message);
						}

					}
					Intent intent = new Intent();
					intent.setAction("com.arvin.physican.MYBROADCASTRECEIVER"); // 设置Action
					intent.putExtra("reload", reload); // 添加附加信息,用于判断刷新那一个页面
					appContext.sendBroadcast(intent);
					break;
				case EventOfflineMessage:
					if (activityList.size() <= 0) {
						EMLog.d(TAG, "received offline messages");
						List<EMMessage> messages = (List<EMMessage>) event
								.getData();
						HXSDKHelper.getInstance().getNotifier()
								.onNewMesg(messages);
					}
					break;
				// below is just giving a example to show a cmd toast, the app
				// should not follow this
				// so be careful of this
				case EventNewCMDMessage: {

					EMLog.d(TAG, "收到透传消息");
					// 获取消息body
					CmdMessageBody cmdMsgBody = (CmdMessageBody) message
							.getBody();
					final String action = cmdMsgBody.action;// 获取自定义action

					// 获取扩展属性 此处省略
					// message.getStringAttribute("");
					EMLog.d(TAG, String.format("透传消息：action:%s,message:%s",
							action, message.toString()));
					final String str = appContext
							.getString(R.string.receive_the_passthrough);

					final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
					IntentFilter cmdFilter = new IntentFilter(
							CMD_TOAST_BROADCAST);
					if (broadCastReceiver == null) {
						broadCastReceiver = new BroadcastReceiver() {

							@Override
							public void onReceive(Context context, Intent intent) {
								// TODO Auto-generated method stub
								MessageUtils.showMsgDialog(context,
										intent.getStringExtra("cmd_value"));
							}
						};

						// 注册广播接收者
						appContext.registerReceiver(broadCastReceiver,
								cmdFilter);
					}

					Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
					broadcastIntent.putExtra("cmd_value", str + action);
					appContext.sendBroadcast(broadcastIntent, null);

					break;
				}
				case EventDeliveryAck:
					message.setDelivered(true);
					break;
				case EventReadAck:
					message.setAcked(true);
					break;
				// add other events in case you are interested in
				default:
					break;
				}
			}
		};

		EMChatManager.getInstance().registerEventListener(eventListener);

		EMChatManager.getInstance().addChatRoomChangeListener(
				new EMChatRoomChangeListener() {
					private final static String ROOM_CHANGE_BROADCAST = "easemob.demo.chatroom.changeevent.toast";
					private final IntentFilter filter = new IntentFilter(
							ROOM_CHANGE_BROADCAST);
					private boolean registered = false;

					private void showToast(String value) {
						if (!registered) {
							// 注册广播接收者
							appContext.registerReceiver(
									new BroadcastReceiver() {

										@Override
										public void onReceive(Context context,
												Intent intent) {
											Toast.makeText(
													appContext,
													intent.getStringExtra("value"),
													Toast.LENGTH_SHORT).show();
										}

									}, filter);

							registered = true;
						}

						Intent broadcastIntent = new Intent(
								ROOM_CHANGE_BROADCAST);
						broadcastIntent.putExtra("value", value);
						appContext.sendBroadcast(broadcastIntent, null);
					}

					@Override
					public void onChatRoomDestroyed(String roomId,
							String roomName) {
						showToast(" room : " + roomId + " with room name : "
								+ roomName + " was destroyed");
						Log.i("info", "onChatRoomDestroyed=" + roomName);
					}

					@Override
					public void onMemberJoined(String roomId, String participant) {
						showToast("member : " + participant
								+ " join the room : " + roomId);
						Log.i("info", "onmemberjoined=" + participant);

					}

					@Override
					public void onMemberExited(String roomId, String roomName,
							String participant) {
						showToast("member : " + participant
								+ " leave the room : " + roomId
								+ " room name : " + roomName);
						Log.i("info", "onMemberExited=" + participant);

					}

					@Override
					public void onMemberKicked(String roomId, String roomName,
							String participant) {
						showToast("member : " + participant
								+ " was kicked from the room : " + roomId
								+ " room name : " + roomName);
						Log.i("info", "onMemberKicked=" + participant);

					}

				});
	}

	/**
	 * 自定义通知栏提示内容
	 * 
	 * @return
	 */
	@Override
	protected HXNotificationInfoProvider getNotificationListener() {
		// 可以覆盖默认的设置
		return new HXNotificationInfoProvider() {

			@Override
			public String getTitle(EMMessage message) {
				// 修改标题,这里使用默认
				return null;
			}

			@Override
			public int getSmallIcon(EMMessage message) {
				// 设置小图标，这里为默认
				return 0;
			}

			@Override
			public String getDisplayedText(EMMessage message) {
				// 设置状态栏的消息提示，可以根据message的类型做相应提示
				String ticker = CommonUtils.getMessageDigest(message,
						appContext);
				if (message.getType() == Type.TXT) {
					ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
				}
				Map<String, RobotUser> robotMap = ((DemoHXSDKHelper) HXSDKHelper
						.getInstance()).getRobotList();
				if (robotMap != null && robotMap.containsKey(message.getFrom())) {
					String nick = robotMap.get(message.getFrom()).getNick();
					if (!TextUtils.isEmpty(nick)) {
						return nick + ": " + ticker;
					} else {
						return message.getFrom() + ": " + ticker;
					}
				} else {
					return message.getFrom() + ": " + ticker;
				}
			}

			@Override
			public String getLatestText(EMMessage message, int fromUsersNum,
					int messageNum) {
				return null;
				// return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
			}

			@Override
			public Intent getLaunchIntent(EMMessage message) {
				// 设置点击通知栏跳转事件
				Intent intent = new Intent(appContext, InquiryActivity.class);
				// 有电话时优先跳转到通话页面
				if (isVideoCalling) {
					intent = new Intent(appContext, VideoCallActivity.class);
				} else if (isVoiceCalling) {
					intent = new Intent(appContext, VoiceCallActivity.class);
				} else {
					ChatType chatType = message.getChatType();
					if (chatType == ChatType.Chat) { // 单聊信息
						com.alibaba.fastjson.JSONObject jsonObject = JSON
								.parseObject(JSON.toJSONString(message));
						intent.putExtra("userId", message.getFrom());
						intent.putExtra("userName",
								jsonObject.getString("userName"));
						Log.i("jsonObject", jsonObject + "");

						String ext = message.getStringAttribute("inquiry", "");
						if (StringUtil.isNotEmpty(ext)) {

							com.alibaba.fastjson.JSONObject json = JSON
									.parseObject(ext);
							String state = json.getString("state");
							if (StringUtil.isNotEmpty(state)) {
								if (state.equals("2")) {
									intent = new Intent(appContext,
											InquiryHistoryActivity.class);
									intent.putExtra("userId", message.getFrom());

									intent.putExtra("userName",
											jsonObject.getString("userName"));
									intent.putExtra("serviceID", Integer
											.parseInt(json
													.getString("serviceID")));

									intent.putExtra("assistantID", 0);
									intent.putExtra(
											"gravidaID",
											Integer.parseInt(MyApplication
													.getInstance().getUser()
													.getId()));

									intent.putExtra(
											"doctorID",
											Integer.parseInt(MyApplication
													.getInstance().getUser()
													.getId()));

								}

								else {
									intent.putExtra("serviceID", Integer
											.parseInt(json
													.getString("serviceID")));

									intent.putExtra("assistantID", 0);

									intent.putExtra(
											"gravidaID",
											Integer.parseInt(MyApplication
													.getInstance().getUser()
													.getId()));

									intent.putExtra(
											"doctorID",
											Integer.parseInt(MyApplication
													.getInstance().getUser()
													.getId()));

								}
							}
							Log.i("json", json + "");

						} else {
							intent = new Intent(appContext,
									com.arvin.physican.MainActivity.class);
							intent.putExtra("newOnsite", 1);

						}

					} else { // 群聊信息
								// message.getTo()为群聊id
						intent.putExtra("groupId", message.getTo());
						if (chatType == ChatType.GroupChat) {
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_GROUP);
						} else {
							intent.putExtra("chatType",
									ChatActivity.CHATTYPE_CHATROOM);
						}

					}
				}
				return intent;
			}
		};
	}

	@Override
	protected void onConnectionConflict() {
		Intent intent = new Intent(appContext, LoginOutActivity1.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("conflict", true);
		appContext.startActivity(intent);
		// showConflictDialog();
	}

	@Override
	protected void onCurrentAccountRemoved() {
		Intent intent = new Intent(appContext, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(Constant.ACCOUNT_REMOVED, true);
		appContext.startActivity(intent);
	}

	@Override
	protected HXSDKModel createModel() {
		return new DemoHXSDKModel(appContext);
	}

	@Override
	public HXNotifier createNotifier() {
		return new HXNotifier() {
			public synchronized void onNewMsg(final EMMessage message) {
				if (EMChatManager.getInstance().isSlientMessage(message)) {
					return;
				}

				String chatUsename = null;
				List<String> notNotifyIds = null;
				// 获取设置的不提示新消息的用户或者群组ids
				if (message.getChatType() == ChatType.Chat) {
					chatUsename = message.getFrom();
					notNotifyIds = ((DemoHXSDKModel) hxModel)
							.getDisabledGroups();
				} else {
					chatUsename = message.getTo();
					notNotifyIds = ((DemoHXSDKModel) hxModel).getDisabledIds();
				}

				if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
					// 判断app是否在后台
					if (!EasyUtils.isAppRunningForeground(appContext)) {
						EMLog.d(TAG, "app is running in backgroud");
						sendNotification(message, false);
					} else {
						sendNotification(message, false);

					}

					viberateAndPlayTone(message);
				}
			}
		};
	}

	/**
	 * get demo HX SDK Model
	 */
	public DemoHXSDKModel getModel() {
		return (DemoHXSDKModel) hxModel;
	}

	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		if (getHXId() != null && contactList == null) {
			contactList = ((DemoHXSDKModel) getModel()).getContactList();
		}

		return contactList;
	}

	public Map<String, RobotUser> getRobotList() {
		if (getHXId() != null && robotList == null) {
			robotList = ((DemoHXSDKModel) getModel()).getRobotList();
		}
		return robotList;
	}

	public boolean isRobotMenuMessage(EMMessage message) {

		try {
			JSONObject jsonObj = message
					.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
			if (jsonObj.has("choice")) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public String getRobotMenuMessageDigest(EMMessage message) {
		String title = "";
		try {
			JSONObject jsonObj = message
					.getJSONObjectAttribute(Constant.MESSAGE_ATTR_ROBOT_MSGTYPE);
			if (jsonObj.has("choice")) {
				JSONObject jsonChoice = jsonObj.getJSONObject("choice");
				title = jsonChoice.getString("title");
			}
		} catch (Exception e) {
		}
		return title;
	}

	public void setRobotList(Map<String, RobotUser> robotList) {
		this.robotList = robotList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		this.contactList = contactList;
	}

	@Override
	public void logout(final EMCallBack callback) {
		endCall();
		super.logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				setContactList(null);
				setRobotList(null);
				getModel().closeDB();
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onError(int code, String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgress(int progress, String status) {
				// TODO Auto-generated method stub
				if (callback != null) {
					callback.onProgress(progress, status);
				}
			}

		});
	}

	void endCall() {
		try {
			EMChatManager.getInstance().endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showConflictDialog() {
		Looper.prepare();
		MyApplication.getInstance().logout(null);
		String st = appContext.getResources().getString(
				R.string.Logoff_notification);
		String msg = appContext.getResources().getString(
				R.string.connect_conflict);
		MessageUtils.showMsgDialogClick(appContext, st, msg,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						logout();
					}
				});
		Looper.loop();
	}

	void logout() {
		MyApplication.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				new Thread(new Runnable() {
					public void run() {
						// 重新显示登陆页面
						Intent intent = new Intent(appContext,
								LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						appContext.startActivity(intent);
					}
				}).start();
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
