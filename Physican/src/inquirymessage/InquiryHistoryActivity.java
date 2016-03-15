package inquirymessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import utils.MySQLiteOpenHelper;
import utils.SharedPreferencesUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.oss.OSSService;
import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.alibaba.sdk.android.oss.storage.OSSBucket;
import com.alibaba.sdk.android.oss.storage.OSSData;
import com.arvin.physican.R;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMError;
import com.easemob.EMNotifierEvent;
import com.easemob.EMValueCallBack;
import com.easemob.applib.model.GroupRemoveListener;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatRoom;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMConversation.EMConversationType;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.AlertDialog;
import com.easemob.chatuidemo.activity.BaiduMapActivity;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ChatRoomDetailsActivity;
import com.easemob.chatuidemo.activity.ForwardMessageActivity;
import com.easemob.chatuidemo.activity.GroupDetailsActivity;
import com.easemob.chatuidemo.activity.ImageGridActivity;
import com.easemob.chatuidemo.activity.ShowBigImage;
import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.winning.pregnancy.common.MyApplication;
import com.winning.pregnancy.dao.EMMessageLocalDao;
import com.winning.pregnancy.dao.impl.EMMessageLocalImpl;
import com.winning.pregnancy.model.Content;
import com.winning.pregnancy.model.ContentString;
import com.winning.pregnancy.model.DoctorInfo;
import com.winning.pregnancy.model.InquiryMessage;
import com.winning.pregnancy.util.HTTPGetTool;
import com.winning.pregnancy.util.ImageLoader;
import com.winning.pregnancy.util.MessageUtils;
import com.winning.pregnancy.util.MyTimeUtil;
import com.winning.pregnancy.util.PictureUtil;
import com.winning.pregnancy.util.StringUtil;
import com.winning.pregnancy.util.ThreadPoolUtils;
import com.winning.pregnancy.util.URLUtils;
import com.winning.pregnancy.util.UUIDGenerator;
import com.winning.pregnancy.widget.PasteEditText;
import com.winning.runner.HandingBusinessInf;
import com.winning.runner.InquiryStateHandler;

import config.Urls;

public class InquiryHistoryActivity extends BaseActivity implements
		OnClickListener {
	private static final String TAG = "InquiryActivity";
	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_FILE = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int REQUEST_CODE_SELECT_VIDEO = 23;
	public static final int REQUEST_CODE_SELECT_FILE = 24;
	public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;

	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;

	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final int CHATTYPE_CHATROOM = 3;

	private static final int WHAT_DID_LOAD_SUCC = 1;
	private static final int WHAT_DID_LOAD_FAIL = 2;
	private static final int WHAT_DID_MSGLOADALL_SUCC = 3;
	private static final int WHAT_DID_MSGLOADALL_FAIL = 4;
	private static final int WHAT_DID_MSGLOAD_SUCC = 5;
	private static final int WHAT_DID_MSGLOAD_FAIL = 6;

	public static final String COPY_IMAGE = "EASEMOBIMG";
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	private ListView listView;
	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSend;
	private Button buttonSend2;
	// private ViewPager expressionViewpager;
	private LinearLayout btnContainer;
	private View more;
	private int position;
	private ClipboardManager clipboard;
	private InputMethodManager manager;
	private List<String> reslist;
	private Drawable[] micImages;
	private int chatType;
	private EMConversation conversation;
	public static InquiryHistoryActivity activityInstance = null;
	// 给谁发送消息
	private String toChatUsername;
	private VoiceRecorder voiceRecorder;
	private MyMessqgeAdapter adapter;
	private File cameraFile;
	static int resendPos;
	// String myFilePath;

	private GroupListener groupListener;

	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 50;
	private boolean haveMoreData = true;
	private Button btnMore;
	public String playMsgId;
	private Content con;
	private int serviceID;
	private int assistantID;
	private int gravidaID;
	private int doctorID;
	private int pageNo = 1;
	private int pageSize = 99999;
	private List<InquiryMessage> listmain = new ArrayList<InquiryMessage>();
	private LayoutInflater inflater;

	private LinearLayout bar_bottom;
	private LinearLayout bar_bottom2;
	private TextView bar_bottom2_text;
	private Button savebutton;

	private OSSService ossService = MyApplication.getInstance().ossService;
	private OSSBucket bucket = MyApplication.getInstance().ossService
			.getOssBucket(MyApplication.getInstance().BUCKETNAME);

	private SwipeRefreshLayout swipeRefreshLayout;

	Bitmap upbitmap;

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};
	public EMGroup group;
	public EMChatRoom room;
	public boolean isRobot;

	private EMMessageLocalDao emMessageLocalDao;

	private String contentID;
	private String state = "0";

	private DoctorInfo doctorInfo;
	private MySQLiteOpenHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_easeinquiry);
		activityInstance = this;
		emMessageLocalDao = new EMMessageLocalImpl(oThis);
		helper = new MySQLiteOpenHelper(oThis);
		initView();
		setUpView();
		// if (com.winning.pregnancyandroid.MainActivity.instance != null) {
		// com.winning.pregnancyandroid.MainActivity.instance.updateMsg();
		// }

	}

	/**
	 * initView
	 */
	protected void initView() {
		recordingContainer = findViewById(R.id.recording_container);
		micImage = (ImageView) findViewById(R.id.mic_image);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		listView = (ListView) findViewById(R.id.list);
		mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
		buttonSend = findViewById(R.id.btn_send);
		buttonSend2 = (Button) findViewById(R.id.btn_send);
		loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
		btnMore = (Button) findViewById(R.id.btn_more);
		more = findViewById(R.id.more);
		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
		btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
		bar_bottom = (LinearLayout) findViewById(R.id.bar_bottom);
		bar_bottom2 = (LinearLayout) findViewById(R.id.bar_bottom2);
		bar_bottom2_text = (TextView) findViewById(R.id.bar_bottom2_text);
		savebutton = (Button) findViewById(R.id.savebutton);
		LinearLayout rl_bottom = (LinearLayout) findViewById(R.id.rl_bottom);
		rl_bottom.setVisibility(View.GONE);
		mEditTextContent.setVisibility(View.GONE);
		edittext_layout.setVisibility(View.GONE);
		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11),
				getResources().getDrawable(R.drawable.record_animate_12),
				getResources().getDrawable(R.drawable.record_animate_13),
				getResources().getDrawable(R.drawable.record_animate_14), };

		// 表情list
		reslist = getExpressionRes(35);
		// 初始化表情viewpager
		List<View> views = new ArrayList<View>();
		View gv1 = getGridChildView(1);
		View gv2 = getGridChildView(2);
		views.add(gv1);
		views.add(gv2);
		edittext_layout.requestFocus();
		voiceRecorder = new VoiceRecorder(micImageHandler);
		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_active);
				} else {
					edittext_layout
							.setBackgroundResource(R.drawable.input_bar_bg_normal);
				}

			}
		});
		mEditTextContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edittext_layout
						.setBackgroundResource(R.drawable.input_bar_bg_active);
				more.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
			}
		});
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					btnMore.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
				} else {
					btnMore.setVisibility(View.VISIBLE);
					buttonSend.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.chat_swipe_layout);

		swipeRefreshLayout.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						if (listView.getFirstVisiblePosition() == 0
								&& !isloading && haveMoreData) {
							List<EMMessage> messages;
							try {
								if (chatType == CHATTYPE_SINGLE) {
									// messages =
									// conversation.loadMoreMsgFromDB(adapter.getItem(0).getMsgId(),
									// pagesize);
								} else {
									// messages =
									// conversation.loadMoreGroupMsgFromDB(adapter.getItem(0).getMsgId(),
									// pagesize);
								}
							} catch (Exception e1) {
								swipeRefreshLayout.setRefreshing(false);
								return;
							}

							// if (messages.size() > 0)
							// {
							// adapter.notifyDataSetChanged();
							// // adapter.refreshSeekTo(messages.size() - 1);
							// if (messages.size() != pagesize)
							// {
							// haveMoreData = false;
							// }
							// }
							// else
							// {
							// haveMoreData = false;
							// }

							isloading = false;

						} else {
							Toast.makeText(
									InquiryHistoryActivity.this,
									getResources().getString(
											R.string.no_more_messages),
									Toast.LENGTH_SHORT).show();
						}
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1000);
			}
		});

		inflater = LayoutInflater.from(getApplicationContext());

	}

	private void setUpView() {
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
		// 判断单聊还是群聊
		chatType = getIntent().getIntExtra("chatType", CHATTYPE_SINGLE);

		if (chatType == CHATTYPE_SINGLE) { // 单聊
			toChatUsername = getIntent().getStringExtra("userId");
			String userName = getIntent().getStringExtra("userName");
			// Map<String, RobotUser> robotMap = ((DemoHXSDKHelper)
			// HXSDKHelper.getInstance()).getRobotList();
			// if (robotMap != null && robotMap.containsKey(toChatUsername))
			// {
			// isRobot = true;
			// String nick = robotMap.get(toChatUsername).getNick();
			// if (!TextUtils.isEmpty(nick))
			// {
			// ((TextView) findViewById(R.id.name)).setText(userName);
			// }
			// else
			// {
			// ((TextView) findViewById(R.id.name)).setText(userName);
			// }
			// }
			// else
			// {
			// }
		}

		// for chatroom type, we only init conversation and create view adapter
		// on success
		if (chatType != CHATTYPE_CHATROOM) {
			onConversationInit();

			onListViewCreation();

			// show forward message if the message is not null
			// String forward_msg_id =
			// getIntent().getStringExtra("forward_msg_id");
			// if (forward_msg_id != null)
			// {
			// // 显示发送要转发的消息
			// forwardMessage(forward_msg_id);
			// }
		}
		assistantID = getIntent().getIntExtra("assistantID", 0);
		serviceID = getIntent().getIntExtra("serviceID", 0);
		gravidaID = getIntent().getIntExtra("doctorID", 0);
		doctorID = getIntent().getIntExtra("doctorID", 0);

		new InquiryStateHandler(oThis).handingBusiness(
				new HandingBusinessInf() {

					@Override
					public void onHandingSuccess(String str) {
						// TODO Auto-generated method stub
						doctorInfo = JSON.parseObject(str, DoctorInfo.class);
						ThreadPoolUtils.execute(new DataHandler());
					}

					@Override
					public void onHandingFail(String errcode, String err) {
						// TODO Auto-generated method stub

					}
				}, String.valueOf(serviceID));
	}

	protected void onConversationInit() {
		if (chatType == CHATTYPE_SINGLE) {
			conversation = EMChatManager.getInstance().getConversationByType(
					toChatUsername, EMConversationType.Chat);
		} else if (chatType == CHATTYPE_GROUP) {
			conversation = EMChatManager.getInstance().getConversationByType(
					toChatUsername, EMConversationType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			conversation = EMChatManager.getInstance().getConversationByType(
					toChatUsername, EMConversationType.ChatRoom);
		}

		// 把此会话的未读数置为0
		conversation.markAllMessagesAsRead();

		// 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
		// 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
		final List<EMMessage> msgs = conversation.getAllMessages();
		int msgCount = msgs != null ? msgs.size() : 0;
		if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
			String msgId = null;
			if (msgs != null && msgs.size() > 0) {
				msgId = msgs.get(0).getMsgId();
			}
			if (chatType == CHATTYPE_SINGLE) {
				conversation.loadMoreMsgFromDB(msgId, pagesize);
			} else {
				conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
			}
		}

		EMChatManager.getInstance().addChatRoomChangeListener(
				new EMChatRoomChangeListener() {

					@Override
					public void onChatRoomDestroyed(String roomId,
							String roomName) {
						if (roomId.equals(toChatUsername)) {
							finish();
						}
					}

					@Override
					public void onMemberJoined(String roomId, String participant) {
					}

					@Override
					public void onMemberExited(String roomId, String roomName,
							String participant) {

					}

					@Override
					public void onMemberKicked(String roomId, String roomName,
							String participant) {
						if (roomId.equals(toChatUsername)) {
							String curUser = EMChatManager.getInstance()
									.getCurrentUser();
							if (curUser.equals(participant)) {
								EMChatManager.getInstance().leaveChatRoom(
										toChatUsername);
								finish();
							}
						}
					}

				});
	}

	protected void onListViewCreation() {
		adapter = new MyMessqgeAdapter();
		// 显示消息
		listView.setAdapter(adapter);

		listView.setOnScrollListener(new ListScrollListener());
		adapter.notifyDataSetChanged();

		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				btnContainer.setVisibility(View.GONE);
				return false;
			}
		});
	}

	protected void onGroupViewCreation() {
		group = EMGroupManager.getInstance().getGroup(toChatUsername);

		if (group != null) {
			((TextView) findViewById(R.id.name)).setText(group.getGroupName());
		} else {
			((TextView) findViewById(R.id.name)).setText(toChatUsername);
		}

		// 监听当前会话的群聊解散被T事件
		groupListener = new GroupListener();
		EMGroupManager.getInstance().addGroupChangeListener(groupListener);
	}

	protected void onChatRoomViewCreation() {

		final ProgressDialog pd = ProgressDialog
				.show(this, "", "Joining......");
		EMChatManager.getInstance().joinChatRoom(toChatUsername,
				new EMValueCallBack<EMChatRoom>() {

					@Override
					public void onSuccess(EMChatRoom value) {
						// TODO Auto-generated method stub
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
								room = EMChatManager.getInstance().getChatRoom(
										toChatUsername);
								if (room != null) {
									((TextView) findViewById(R.id.name))
											.setText(room.getName());
								} else {
									((TextView) findViewById(R.id.name))
											.setText(toChatUsername);
								}
								EMLog.d(TAG,
										"join room success : " + room.getName());

								onConversationInit();

								onListViewCreation();
							}
						});
					}

					@Override
					public void onError(final int error, String errorMsg) {
						// TODO Auto-generated method stub
						EMLog.d(TAG, "join room failure : " + error);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								pd.dismiss();
							}
						});
						finish();
					}
				});
	}

	/**
	 * onActivityResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data
						.getIntExtra("position", -1)));
				// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
				// ((TextMessageBody) copyMsg.getBody()).getMessage()));
				clipboard.setText(((TextMessageBody) copyMsg.getBody())
						.getMessage());
				break;
			// case RESULT_CODE_DELETE: // 删除消息
			// EMMessage deleteMsg = (EMMessage)
			// adapter.getItem(data.getIntExtra("position", -1));
			// conversation.removeMessage(deleteMsg.getMsgId());
			// adapter.refreshSeekTo(data.getIntExtra("position",
			// adapter.getCount()) - 1);
			// break;

			case RESULT_CODE_FORWARD: // 转发消息
				EMMessage forwardMsg = (EMMessage) adapter.getItem(data
						.getIntExtra("position", 0));
				Intent intent = new Intent(this, ForwardMessageActivity.class);
				intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				startActivity(intent);

				break;

			default:
				break;
			}
		}
		if (resultCode == RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话

				EMChatManager.getInstance().clearConversation(toChatUsername);
				// adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists())
					// sendPicture(cameraFile.getAbsolutePath());
					// 剪一下，防止测试的时候上传的文件太大
					sendImage(cameraFile.getAbsolutePath());
			}
			// else if (requestCode == REQUEST_CODE_SELECT_VIDEO)
			// { // 发送本地选择的视频
			//
			// int duration = data.getIntExtra("dur", 0);
			// String videoPath = data.getStringExtra("path");
			// File file = new File(PathUtil.getInstance().getImagePath(),
			// "thvideo" + System.currentTimeMillis());
			// Bitmap bitmap = null;
			// FileOutputStream fos = null;
			// try
			// {
			// if (!file.getParentFile().exists())
			// {
			// file.getParentFile().mkdirs();
			// }
			// bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
			// if (bitmap == null)
			// {
			// EMLog.d("chatactivity",
			// "problem load video thumbnail bitmap,use default icon");
			// bitmap = BitmapFactory.decodeResource(getResources(),
			// R.drawable.app_panel_video_icon);
			// }
			// fos = new FileOutputStream(file);
			//
			// bitmap.compress(CompressFormat.JPEG, 100, fos);
			//
			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// }
			// finally
			// {
			// if (fos != null)
			// {
			// try
			// {
			// fos.close();
			// }
			// catch (IOException e)
			// {
			// e.printStackTrace();
			// }
			// fos = null;
			// }
			// if (bitmap != null)
			// {
			// bitmap.recycle();
			// bitmap = null;
			// }
			//
			// }
			// sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);
			//
			// }
			else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null) {
						// sendPicByUri(selectedImage);
						// 剪一下，防止测试的时候上传的文件太大
						sendImage(getPath(selectedImage));
					}
				}
			}
			// else if (requestCode == REQUEST_CODE_SELECT_FILE)
			// { // 发送选择的文件
			// if (data != null)
			// {
			// Uri uri = data.getData();
			// if (uri != null)
			// {
			// sendFile(uri);
			// }
			// }
			//
			// }
			// else if (requestCode == REQUEST_CODE_MAP)
			// { // 地图
			// double latitude = data.getDoubleExtra("latitude", 0);
			// double longitude = data.getDoubleExtra("longitude", 0);
			// String locationAddress = data.getStringExtra("address");
			// if (locationAddress != null && !locationAddress.equals(""))
			// {
			// toggleMore(more);
			// sendLocationMsg(latitude, longitude, "", locationAddress);
			// }
			// else
			// {
			// String st =
			// getResources().getString(R.string.unable_to_get_loaction);
			// Toast.makeText(this, st, 0).show();
			// }
			// // 重发消息
			// }
			// else if (requestCode == REQUEST_CODE_TEXT || requestCode ==
			// REQUEST_CODE_VOICE
			// || requestCode == REQUEST_CODE_PICTURE || requestCode ==
			// REQUEST_CODE_LOCATION
			// || requestCode == REQUEST_CODE_VIDEO || requestCode ==
			// REQUEST_CODE_FILE)
			// {
			// resendMessage();
			// }
			// else if (requestCode == REQUEST_CODE_COPY_AND_PASTE)
			// {
			// // 粘贴
			// if (!TextUtils.isEmpty(clipboard.getText()))
			// {
			// String pasteText = clipboard.getText().toString();
			// if (pasteText.startsWith(COPY_IMAGE))
			// {
			// // 把图片前缀去掉，还原成正常的path
			// sendPicture(pasteText.replace(COPY_IMAGE, ""));
			// }
			//
			// }
			// }
			// else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST)
			// { // 移入黑名单
			// EMMessage deleteMsg = (EMMessage)
			// adapter.getItem(data.getIntExtra("position", -1));
			// addUserToBlacklist(deleteMsg.getFrom());
			// }
			// else if (conversation.getMsgCount() > 0)
			// {
			// // adapter.refresh();
			// setResult(RESULT_OK);
			// }
			// else if (requestCode == REQUEST_CODE_GROUP_DETAIL)
			// {
			// // adapter.refresh();
			// }
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (StringUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}

	/**
	 * 消息图标点击事件
	 * 
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		String st1 = getResources().getString(R.string.not_connect_to_server);
		int id = view.getId();
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			String s = mEditTextContent.getText().toString();
			Log.i("1error1", s);
			sendText(replaceBlank(s));
			mEditTextContent.setText("");
		} else if (id == R.id.btn_take_picture) {
			selectPicFromCamera();// 点击照相图标
		} else if (id == R.id.btn_picture) {
			selectPicFromLocal(); // 点击图片图标
		} else if (id == R.id.btn_location) { // 位置
			startActivityForResult(new Intent(this, BaiduMapActivity.class),
					REQUEST_CODE_MAP);
		} else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
			more.setVisibility(View.VISIBLE);
			hideKeyboard();
		} else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
			more.setVisibility(View.GONE);

		} else if (id == R.id.btn_video) {
			// 点击摄像图标
			Intent intent = new Intent(InquiryHistoryActivity.this,
					ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
		} else if (id == R.id.btn_file) { // 点击文件图标
			selectFileFromLocal();
		}
	}

	/**
	 * 事件监听
	 * 
	 * see {@link EMNotifierEvent}
	 */

	private void refreshUIWithNewMessage() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				// adapter.refreshSelectLast();
			}
		});
	}

	private void refreshUI() {
		if (adapter == null) {
			return;
		}

		runOnUiThread(new Runnable() {
			public void run() {
				// adapter.refresh();
			}
		});
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		if (!CommonUtils.isExitsSdcard()) {
			String st = getResources().getString(
					R.string.sd_card_does_not_exist);
			Toast.makeText(getApplicationContext(), st, 0).show();
			return;
		}

		cameraFile = new File(PathUtil.getInstance().getImagePath(),
				MyApplication.getInstance().getUserName()
						+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 选择文件
	 */
	private void selectFileFromLocal() {
		Intent intent = null;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("*/*");
			intent.addCategory(Intent.CATEGORY_OPENABLE);

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	public void sendText(String content) {

		if (content.length() > 0) {
			sendCon(gravidaID, doctorID, assistantID, content, 1, serviceID,
					null);

		}
	}

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length,
			boolean isResend) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP) {
				message.setChatType(ChatType.GroupChat);
			} else if (chatType == CHATTYPE_CHATROOM) {
				message.setChatType(ChatType.ChatRoom);
			}
			message.setReceipt(toChatUsername);
			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					len);
			message.addBody(body);
			if (isRobot) {
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			// adapter.refreshSelectLast();
			setResult(RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param filePath
	 */
	private void sendImage(final String filePath) {
		try {
			final String newFileName = MyTimeUtil.DateToStr(
					MyTimeUtil.yyyy_MM_dd, new Date())
					+ "-"
					+ UUIDGenerator.getFullUUID() + ".jpg";

			Bitmap bitmap = PictureUtil.getSmallBitmap(filePath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] dataToUpload = baos.toByteArray();
			OSSData data = ossService.getOssData(bucket, "inquiry/"
					+ newFileName);
			data.setData(dataToUpload, "text/plain");

			// 直接从数据流上传
			// InputStream is = new ByteArrayInputStream(dataToUpload);
			// data.setInputstream(is, dataToUpload.length, "text/txt");

			data.uploadInBackground(new SaveCallback() {

				@Override
				public void onSuccess(String objectKey) {
					Log.d(TAG, "[onSuccess] - " + objectKey
							+ " upload success!");
					sendCon(gravidaID, doctorID, assistantID,
							MyApplication.getInstance().OSSPATH + newFileName,
							2, serviceID, filePath);
				}

				@Override
				public void onProgress(String objectKey, int byteCount,
						int totalSize) {
					Log.d(TAG, "[onProgress] - current upload " + objectKey
							+ " bytes: " + byteCount + " in total: "
							+ totalSize);
				}

				@Override
				public void onFailure(String objectKey,
						OSSException ossException) {
					Log.e(TAG, "[onFailure] - upload " + objectKey
							+ " failed!\n" + ossException.toString());
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void sendCon(int gravidaID, int doctorID, int assistantID,
			String content, int contentType, int serviceID, String filePath) {
		con = new Content();
		// con.setAssistantID(assistantID);
		con.setContent(content);
		con.setContentType(contentType);
		con.setRecNo(1);
		con.setServiceID(serviceID);
		con.setGravidaID(gravidaID);
		ThreadPoolUtils.execute(new MessageHandler());
	}

	/**
	 * 发送图片
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath) {
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.IMAGE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(to);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
		// body.setSendOriginalImage(true);
		message.addBody(body);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);

		listView.setAdapter(adapter);
		// adapter.refreshSelectLast();
		setResult(RESULT_OK);
		// more(more);
	}

	/**
	 * 发送视频消息
	 */
	private void sendVideo(final String filePath, final String thumbPath,
			final int length) {
		final File videoFile = new File(filePath);
		if (!videoFile.exists()) {
			return;
		}
		try {
			EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VIDEO);
			// 如果是群聊，设置chattype,默认是单聊
			if (chatType == CHATTYPE_GROUP) {
				message.setChatType(ChatType.GroupChat);
			} else if (chatType == CHATTYPE_CHATROOM) {
				message.setChatType(ChatType.ChatRoom);
			}
			String to = toChatUsername;
			message.setReceipt(to);
			VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
					length, videoFile.length());
			message.addBody(body);
			if (isRobot) {
				message.setAttribute("em_robot_message", true);
			}
			conversation.addMessage(message);
			listView.setAdapter(adapter);
			// adapter.refreshSelectLast();
			setResult(RESULT_OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(selectedImage, null, null,
				null, null);
		String st8 = getResources().getString(R.string.cant_find_pictures);
		if (cursor != null) {
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex("_data");
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			cursor = null;

			if (picturePath == null || picturePath.equals("null")) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;
			}
			sendPicture(picturePath);
		} else {
			File file = new File(selectedImage.getPath());
			if (!file.exists()) {
				Toast toast = Toast.makeText(this, st8, Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				return;

			}
			sendPicture(file.getAbsolutePath());
		}

	}

	/**
	 * 发送位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude,
			String imagePath, String locationAddress) {
		EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.LOCATION);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}
		LocationMessageBody locBody = new LocationMessageBody(locationAddress,
				latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		// adapter.refreshSelectLast();
		setResult(RESULT_OK);

	}

	/**
	 * 发送文件
	 * 
	 * @param uri
	 */
	private void sendFile(Uri uri) {
		String filePath = null;
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = getContentResolver().query(uri, projection, null,
						null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			filePath = uri.getPath();
		}
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			String st7 = getResources().getString(R.string.File_does_not_exist);
			Toast.makeText(getApplicationContext(), st7, 0).show();
			return;
		}
		if (file.length() > 10 * 1024 * 1024) {
			String st6 = getResources().getString(
					R.string.The_file_is_not_greater_than_10_m);
			Toast.makeText(getApplicationContext(), st6, 0).show();
			return;
		}

		// 创建一个文件消息
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
		// 如果是群聊，设置chattype,默认是单聊
		if (chatType == CHATTYPE_GROUP) {
			message.setChatType(ChatType.GroupChat);
		} else if (chatType == CHATTYPE_CHATROOM) {
			message.setChatType(ChatType.ChatRoom);
		}

		message.setReceipt(toChatUsername);
		// add message body
		NormalFileMessageBody body = new NormalFileMessageBody(new File(
				filePath));
		message.addBody(body);
		if (isRobot) {
			message.setAttribute("em_robot_message", true);
		}
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		// adapter.refreshSelectLast();
		setResult(RESULT_OK);
	}

	/**
	 * 重发消息
	 */
	private void resendMessage() {
		EMMessage msg = null;
		msg = conversation.getMessage(resendPos);
		// msg.setBackSend(true);
		msg.status = EMMessage.Status.CREATE;

		// adapter.refreshSeekTo(resendPos);
	}

	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		hideKeyboard();
		edittext_layout.setVisibility(View.GONE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		buttonSend.setVisibility(View.GONE);
		btnMore.setVisibility(View.VISIBLE);
		btnContainer.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {

		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(mEditTextContent.getText())) {
			btnMore.setVisibility(View.VISIBLE);
			buttonSend.setVisibility(View.GONE);
		} else {
			btnMore.setVisibility(View.GONE);
			buttonSend.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击清空聊天记录
	 * 
	 * @param view
	 */
	public void emptyHistory(View view) {
		String st5 = getResources().getString(
				R.string.Whether_to_empty_all_chats);
		startActivityForResult(
				new Intent(this, AlertDialog.class)
						.putExtra("titleIsCancel", true).putExtra("msg", st5)
						.putExtra("cancel", true), REQUEST_CODE_EMPTY_HISTORY);
	}

	/**
	 * 点击进入群组详情
	 * 
	 * @param view
	 */
	public void toGroupDetails(View view) {
		if (room == null && group == null) {
			Toast.makeText(getApplicationContext(), R.string.gorup_not_found, 0)
					.show();
			return;
		}
		if (chatType == CHATTYPE_GROUP) {
			startActivityForResult(
					(new Intent(this, GroupDetailsActivity.class).putExtra(
							"groupId", toChatUsername)),
					REQUEST_CODE_GROUP_DETAIL);
		} else {
			startActivityForResult((new Intent(this,
					ChatRoomDetailsActivity.class).putExtra("roomId",
					toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
		}
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void toggleMore(View view) {
		if (more.getVisibility() == View.GONE) {
			EMLog.d(TAG, "more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
		} else {
			if (btnContainer.getVisibility() == View.VISIBLE) {
				btnContainer.setVisibility(View.GONE);
			} else {
				btnContainer.setVisibility(View.VISIBLE);
			}
		}

	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
		}

	}

	private PowerManager.WakeLock wakeLock;

	/**
	 * 按住说话listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!CommonUtils.isExitsSdcard()) {
					String st4 = getResources().getString(
							R.string.Send_voice_need_sdcard_support);
					Toast.makeText(InquiryHistoryActivity.this, st4,
							Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					wakeLock.acquire();
					if (VoicePlayClickListener.isPlaying)
						VoicePlayClickListener.currentPlayListener
								.stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername,
							getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					if (wakeLock.isHeld())
						wakeLock.release();
					if (voiceRecorder != null)
						voiceRecorder.discardRecording();
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(InquiryHistoryActivity.this,
							R.string.recoding_fail, Toast.LENGTH_SHORT).show();
					return false;
				}

				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
				} else {
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (wakeLock.isHeld())
					wakeLock.release();
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();

				} else {
					// stop recording and send voice file
					String st1 = getResources().getString(
							R.string.Recording_without_permission);
					String st2 = getResources().getString(
							R.string.The_recording_time_is_too_short);
					String st3 = getResources().getString(
							R.string.send_failure_please);
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder
											.getVoiceFileName(toChatUsername),
									Integer.toString(length), false);
						} else if (length == EMError.INVALID_FILE) {
							Toast.makeText(getApplicationContext(), st1,
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), st2,
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(InquiryHistoryActivity.this, st3,
								Toast.LENGTH_SHORT).show();
					}

				}
				return true;
			default:
				recordingContainer.setVisibility(View.INVISIBLE);
				if (voiceRecorder != null)
					voiceRecorder.discardRecording();
				return false;
			}
		}
	}

	/**
	 * 获取表情的gridview的子view
	 * 
	 * @param i
	 * @return
	 */
	private View getGridChildView(int i) {
		View view = View.inflate(this, R.layout.expression_gridview, null);
		ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
		List<String> list = new ArrayList<String>();
		if (i == 1) {
			List<String> list1 = reslist.subList(0, 20);
			list.addAll(list1);
		} else if (i == 2) {
			list.addAll(reslist.subList(20, reslist.size()));
		}
		list.add("delete_expression");
		final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
				1, list);
		gv.setAdapter(expressionAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String filename = expressionAdapter.getItem(position);
				try {
					// 文字输入框可见时，才可输入表情
					// 按住说话可见，不让输入表情
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

						if (filename != "delete_expression") { // 不是删除键，显示表情
																// 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
							Class clz = Class
									.forName("com.easemob.chatuidemo.utils.SmileUtils");
							Field field = clz.getField(filename);
							mEditTextContent.append(SmileUtils.getSmiledText(
									InquiryHistoryActivity.this,
									(String) field.get(null)));
						} else { // 删除文字或者表情
							if (!TextUtils.isEmpty(mEditTextContent.getText())) {

								int selectionStart = mEditTextContent
										.getSelectionStart();// 获取光标的位置
								if (selectionStart > 0) {
									String body = mEditTextContent.getText()
											.toString();
									String tempStr = body.substring(0,
											selectionStart);
									int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
									if (i != -1) {
										CharSequence cs = tempStr.substring(i,
												selectionStart);
										if (SmileUtils.containsKey(cs
												.toString()))
											mEditTextContent.getEditableText()
													.delete(i, selectionStart);
										else
											mEditTextContent.getEditableText()
													.delete(selectionStart - 1,
															selectionStart);
									} else {
										mEditTextContent.getEditableText()
												.delete(selectionStart - 1,
														selectionStart);
									}
								}
							}

						}
					}
				} catch (Exception e) {
				}

			}
		});
		return view;
	}

	public List<String> getExpressionRes(int getSum) {
		List<String> reslist = new ArrayList<String>();
		for (int x = 1; x <= getSum; x++) {
			String filename = "ee_" + x;

			reslist.add(filename);

		}
		return reslist;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
		if (groupListener != null) {
			EMGroupManager.getInstance().removeGroupChangeListener(
					groupListener);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (group != null)
			((TextView) findViewById(R.id.name)).setText(group.getGroupName());

		if (adapter != null) {
			// adapter.refresh();
		}

		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();
		sdkHelper.pushActivity(this);

	}

	@Override
	protected void onStop() {

		// EMChatManager.getInstance().unregisterEventListener(this);
		Map<String, Integer> map = SharedPreferencesUtils
				.getSharedPreferencesTotalUnReadCount(oThis);
		int sum = map.get("TOTALUNREADCOUNT");
		Intent intent = new Intent("reload_newmessage");
		sendBroadcast(intent);
		SharedPreferencesUtils.setSharedPreferencesTotalUnReadCount(oThis, sum,
				0);
		DemoHXSDKHelper sdkHelper = (DemoHXSDKHelper) DemoHXSDKHelper
				.getInstance();

		sdkHelper.popActivity(this);

		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (wakeLock.isHeld())
			wakeLock.release();
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 加入到黑名单
	 * 
	 * @param username
	 */
	private void addUserToBlacklist(final String username) {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage(getString(R.string.Is_moved_into_blacklist));
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMContactManager.getInstance().addUserToBlackList(username,
							false);
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(),
									R.string.Move_into_blacklist_success, 0)
									.show();
						}
					});
				} catch (EaseMobException e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getApplicationContext(),
									R.string.Move_into_blacklist_failure, 0)
									.show();
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		// EMChatManager.getInstance().unregisterEventListener(this);
		if (chatType == CHATTYPE_CHATROOM) {
			EMChatManager.getInstance().leaveChatRoom(toChatUsername);
		}
		finish();
	}

	/**
	 * 覆盖手机返回键
	 */

	/**
	 * listview滑动监听listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:

				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	/**
	 * 监测群组解散或者被T事件
	 * 
	 */
	class GroupListener extends GroupRemoveListener {

		@Override
		public void onUserRemoved(final String groupId, String groupName) {
			runOnUiThread(new Runnable() {
				String st13 = getResources().getString(R.string.you_are_group);

				public void run() {
					if (toChatUsername.equals(groupId)) {
						Toast.makeText(InquiryHistoryActivity.this, st13, 1)
								.show();
						if (GroupDetailsActivity.instance != null)
							GroupDetailsActivity.instance.finish();
						finish();
					}
				}
			});
		}

		@Override
		public void onGroupDestroy(final String groupId, String groupName) {
			// 群组解散正好在此页面，提示群组被解散，并finish此页面
			runOnUiThread(new Runnable() {
				String st14 = getResources().getString(
						R.string.the_current_group);

				public void run() {
					if (toChatUsername.equals(groupId)) {
						Toast.makeText(InquiryHistoryActivity.this, st14, 1)
								.show();
						if (GroupDetailsActivity.instance != null)
							GroupDetailsActivity.instance.finish();
						finish();
					}
				}
			});
		}

	}

	public String getToChatUsername() {
		return toChatUsername;
	}

	public ListView getListView() {
		return listView;
	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case WHAT_DID_LOAD_SUCC: {
				if (msg.obj != null) {
					List<InquiryMessage> list = (List<InquiryMessage>) msg.obj;
					if (list.size() > 0 && !list.isEmpty()) {
						listmain.add(list.get(0));
					}

				}
				listView.setSelection(listView.getBottom());
				adapter.notifyDataSetChanged();
				mEditTextContent.setText("");
				break;
			}
			case WHAT_DID_LOAD_FAIL: {
				if (msg.obj != null) {
					MessageUtils.showMsgToastBottom(oThis, (String) msg.obj);
				}
				break;
			}
			case WHAT_DID_MSGLOADALL_SUCC: {
				if (msg.obj != null) {
					listmain = (List<InquiryMessage>) msg.obj;
				}
				adapter.notifyDataSetChanged();
				break;
			}
			case WHAT_DID_MSGLOADALL_FAIL: {
				if (msg.obj != null) {
					MessageUtils.showMsgToastBottom(oThis, (String) msg.obj);
				}
				break;
			}

			case WHAT_DID_MSGLOAD_SUCC: {
				if (msg.obj != null) {
					List<InquiryMessage> list = (List<InquiryMessage>) msg.obj;
					if (list.size() > 0 && !list.isEmpty()) {
						listmain.add(list.get(0));
					}

				}
				listView.setSelection(listView.getBottom());
				adapter.notifyDataSetChanged();
				break;
			}
			case WHAT_DID_MSGLOAD_FAIL: {
				if (msg.obj != null) {
					MessageUtils.showMsgToastBottom(oThis, (String) msg.obj);
				}
				break;
			}
			}

		}

	};
	private com.winning.pregnancy.util.AlertDialog dialog;

	// ////////////////////////////////////////////////////////////////////////

	private class MessageHandler implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			messageLoading();
			Looper.loop();
		}

	}

	private void messageLoading() {
		Message msg = myHandler.obtainMessage();
		List<InquiryMessage> list = new ArrayList<InquiryMessage>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// {"assistantID":0,"content":"欲迎还拒","contentType":1,"doctorID":0,"gravidaID":34,"recNo":1,"serviceID":693}

		Content contentString = JSON.parseObject(JSON.toJSONString(con),
				Content.class);
		// {"serviceID":696,"gravidaID":34,"content":"æåé¥­","contentType":1,"recNo":5}
		// {"serviceID":697,"content":滚滚滚,"contentType":1,"recNo":1,"gravidaID":34}
		int serviceID = contentString.getServiceID();
		String content = contentString.getContent();
		int contentType = contentString.getContentType();
		int recNo = contentString.getRecNo();
		int assistantID = contentString.getAssistantID();
		int gravidaID = contentString.getGravidaID();
		String contentJsonn = "{\"serviceID\":" + serviceID + ",\"content\":\""
				+ content + "\",\"contentType\":" + contentType + ",\"recNo\":"
				+ recNo + ",\"doctorID\":" + doctorID + "}";
		// Log.i("contentJsonn", contentJsonn);
		// {"serviceID":36,"content":"可能是腰椎盘突出，建议去医院拍片检查","contentType":1,"recNo":1,"doctorID":1}
		params.add(new BasicNameValuePair("contentJson", contentJsonn));
		// params.add(new BasicNameValuePair("contentJson",
		// JSON.toJSONString(con)));
		JSONObject json = HTTPGetTool.getTool().post(Urls.COMMITCONTENT_URL,
				params);
		// {"data":[{"content":"会好好个","id":3898,"lastModify":"2015-12-26 15:27:33","serviceID":886,"doctorID":34,"contentType":1,"createDate":"2015-12-26 15:27:33","recNo":1,"activity":1}],"err":"","success":0}
		try {
			if (json != null) {
				if (json.getIntValue("success") == 0) {
					list = JSON.parseArray(json.getString("data"),
							InquiryMessage.class);
					msg.obj = list;
					msg.what = WHAT_DID_LOAD_SUCC;
				} else if (json.getIntValue("success") == -1000) {
					msg.what = WHAT_DID_LOAD_FAIL;
					msg.obj = json.getString("err");
					Log.i("error", json.getString("err"));
				}
			} else {
				msg.what = WHAT_DID_LOAD_FAIL;
				msg.obj = "连接服务器失败！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg.what = WHAT_DID_LOAD_FAIL;
			msg.obj = "连接服务器失败！";
		}
		myHandler.sendMessage(msg);
	}

	// ////////////////////////////////////////////////////////////////////////////
	private class DataHandler implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			dataLoading();
			Looper.loop();
		}

	}

	private void dataLoading() {
		Message msg = myHandler.obtainMessage();
		List<InquiryMessage> list = new ArrayList<InquiryMessage>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("serviceID", String
				.valueOf(serviceID)));
		params.add(new BasicNameValuePair("pageNo", String.valueOf(pageNo)));
		params.add(new BasicNameValuePair("pageSize", String.valueOf(pageSize)));
		JSONObject json = HTTPGetTool.getTool().post(
				URLUtils.HOSTMAIN + URLUtils.URLCONTENT, params);
		try {
			if (json != null) {
				if (json.getIntValue("success") == 0) {
					list = JSON.parseArray(json.getString("data"),
							InquiryMessage.class);
					msg.obj = list;
					msg.what = WHAT_DID_MSGLOADALL_SUCC;
				} else if (json.getIntValue("success") == -1000) {
					msg.what = WHAT_DID_MSGLOADALL_FAIL;
					msg.obj = json.getString("err");
				}
			} else {
				msg.what = WHAT_DID_MSGLOADALL_FAIL;
				msg.obj = "连接服务器失败！";
			}
		} catch (Exception e) {
			msg.what = WHAT_DID_MSGLOADALL_FAIL;
			msg.obj = "连接服务器失败！";
		}
		myHandler.sendMessage(msg);
	}

	// //////////////////////////////////////////////////////////////////////////
	private class DataSingleHandler implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			dataSingleLoading();
			Looper.loop();
		}

	}

	private void dataSingleLoading() {
		Message msg = myHandler.obtainMessage();
		List<InquiryMessage> list = new ArrayList<InquiryMessage>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("contentID", contentID));
		JSONObject json = HTTPGetTool.getTool().post(
				URLUtils.HOSTMAIN + URLUtils.URLSINGLECONTENT, params);
		try {
			if (json != null) {
				if (json.getIntValue("success") == 0) {
					list = JSON.parseArray(json.getString("data"),
							InquiryMessage.class);
					msg.obj = list;
					msg.what = WHAT_DID_MSGLOAD_SUCC;
				} else if (json.getIntValue("success") == -1000) {
					msg.what = WHAT_DID_MSGLOAD_FAIL;
					msg.obj = json.getString("err");
				}
			} else {
				msg.what = WHAT_DID_MSGLOAD_FAIL;
				msg.obj = "连接服务器失败！";
			}
		} catch (Exception e) {
			msg.what = WHAT_DID_MSGLOAD_FAIL;
			msg.obj = "连接服务器失败！";
		}
		myHandler.sendMessage(msg);
	}

	class MyMessqgeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listmain.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listmain.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			// TODO Auto-generated method stub
			InquiryMessage message = (InquiryMessage) getItem(position);
			StringBuffer sBuffer = new StringBuffer();
			if (message.getContentType() == 3
					&& message.getContent().contains("[")) {
				String content = message.getContent();
				List<ContentString> listContent = new ArrayList<ContentString>();
				listContent = JSON.parseArray(content, ContentString.class);
				ContentString str1 = listContent.get(0);
				String yuchanqi = str1.getValue();
				sBuffer.append("我的基本体征数据:\n预产期:" + yuchanqi);
				ContentString str2 = listContent.get(2);
				String height = str2.getValue();
				sBuffer.append("\n身高:" + height);
				ContentString str3 = listContent.get(1);
				String age = str3.getValue();
				sBuffer.append("\n年龄:" + age);
				String urineKetone = null;
				String temperature = null;
				String blood = null;
				String bloodSuger = null;
				String weight = null;

				if (listContent.size() > 3) {
					ContentString str4 = listContent.get(3);
					if (str4.getValue().equals("")) {
						blood = "";
					} else if (str4.getValue() == null) {
						blood = "";
					} else if (str4.getValue().equals("null")) {
						blood = "";
					} else {
						blood = "血压:" + str4.getValue() + "mmHg\n";
						sBuffer.append("\n" + blood);
					}

				}
				if (listContent.size() > 4) {
					ContentString str5 = listContent.get(4);
					if (str5.getValue().equals("")) {
						bloodSuger = "";
					} else if (str5.getValue() == null) {
						bloodSuger = "";
					} else if (str5.equals("null")) {
						bloodSuger = "";
					} else {
						bloodSuger = "血糖:" + str5.getValue() + "mmol/L\n";
						sBuffer.append(bloodSuger);
					}

				}
				if (listContent.size() > 5) {
					ContentString str6 = listContent.get(5);
					if (str6.getValue().equals("")) {
						weight = "";
					} else if (str6.getValue() == null) {
						weight = "";
					} else if (str6.getValue().equals("null")) {
						weight = "";
					} else {
						weight = "体重:" + str6.getValue() + "Kg\n";
						sBuffer.append(weight);
					}

				}
				if (listContent.size() > 6) {
					ContentString str7 = listContent.get(6);
					if (str7.getValue().equals("")) {
						temperature = "";
					} else if (str7.getValue() == null) {
						temperature = "";
					} else if (str7.equals("null")) {
						temperature = "";
					} else {
						temperature = "体温:" + str7.getValue() + "℃\n";
						sBuffer.append(temperature);
					}

				}
				if (listContent.size() > 7) {
					ContentString str8 = listContent.get(7);
					if (str8.getValue().equals("")) {
						urineKetone = "";
					} else if (str8.getValue() == null) {
						urineKetone = "";
					} else if (str8.getValue().equals("null")) {
						urineKetone = "";
					} else {
						urineKetone = "丙酮:" + str8.getValue();
						sBuffer.append(urineKetone);
					}

				}
				String count = sBuffer.toString();
				message.setContent(count);

			}
			ViewHolder holder = null;

			holder = new ViewHolder();
			try {

				convertView = createViewByMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}

			if (message.getContentType() == 2) {
				try {
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_userhead);

					holder.tv = (TextView) convertView
							.findViewById(R.id.percentage);
					holder.tv_usernick = (TextView) convertView
							.findViewById(R.id.tv_userid);
					holder.iv = ((ImageView) convertView
							.findViewById(R.id.iv_sendPicture));

				} catch (Exception e) {
				}

			} else if (message.getContentType() == 1) {

				try {
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					// 这里是文字内容
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);
					holder.tv_usernick = (TextView) convertView
							.findViewById(R.id.tv_userid);

					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.tvTitle);
					holder.tvList = (LinearLayout) convertView
							.findViewById(R.id.ll_layout);
				} catch (Exception e) {
				}
			} else if (message.getContentType() == 3) {

				try {
					holder.pb = (ProgressBar) convertView
							.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView
							.findViewById(R.id.msg_status);
					holder.iv_avatar = (ImageView) convertView
							.findViewById(R.id.iv_userhead);
					// 这里是文字内容
					holder.tv = (TextView) convertView
							.findViewById(R.id.tv_chatcontent);
					holder.tv_usernick = (TextView) convertView
							.findViewById(R.id.tv_userid);

					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.tvTitle);
					holder.tvList = (LinearLayout) convertView
							.findViewById(R.id.ll_layout);
				} catch (Exception e) {
				}
			}

			if (message.getContentType() == 2) {
				handleImageMessage(message, holder, position);
			} else {
				handleTextMessage(message, holder);
			}
			Log.i("image",
					MyApplication.getInstance().OSSHEADPATH
							+ SharedPreferencesUtils
									.getSharedPreferencesForAppConfig(oThis)
									.get("assistantID"));
			if (message.getGravidaID() == 0) {
				if (StringUtil.isNotEmpty(doctorInfo.getDoctorHeadPhoto())) {
					ImageLoader.getInstances().displayImage(
							MyApplication.getInstance().OSSHEADPATH
									+ SharedPreferencesUtils
											.getSharedPreferencesForAppConfig(
													oThis).get("headPhoto"),
							holder.iv_avatar,
							new ImageLoader.OnImageLoaderListener() {
								@Override
								public void onProgressImageLoader(
										ImageView imageView, int currentSize,
										int totalSize) {
								}

								@Override
								public void onFinishedImageLoader(
										ImageView imageView, Bitmap bitmap) {
								}
							});
				}

			} else {
				if (StringUtil.isNotEmpty(doctorInfo.getGravidaHeadPhoto())) {
					ImageLoader.getInstances().displayImage(
							MyApplication.getInstance().OSSHEADPATH
									+ doctorInfo.getGravidaHeadPhoto(),
							holder.iv_avatar,
							new ImageLoader.OnImageLoaderListener() {
								@Override
								public void onProgressImageLoader(
										ImageView imageView, int currentSize,
										int totalSize) {
								}

								@Override
								public void onFinishedImageLoader(
										ImageView imageView, Bitmap bitmap) {
								}
							});
				}

			}

			TextView timestamp = (TextView) convertView
					.findViewById(R.id.timestamp);

			timestamp.setText(message.getCreateDate());

			return convertView;
		}

	}

	public class ViewHolder {
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView iv_avatar;
		TextView tv_usernick;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// 显示已读回执状态
		TextView tv_ack;
		// 显示送达回执状态
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;

		TextView tvTitle;
		LinearLayout tvList;
	}

	private View createViewByMessage(InquiryMessage message) {
		switch (message.getContentType()) {
		case 2:
			return message.getGravidaID() == 0 ? inflater.inflate(
					R.layout.row_sent_picture, null) : inflater.inflate(
					R.layout.row_received_picture, null);
		default:
			// 含有菜单的消息
			return message.getGravidaID() == 0 ? inflater.inflate(
					R.layout.row_sent_message, null) : inflater.inflate(
					R.layout.row_received_message, null);
		}
	}

	/**
	 * 图片消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleImageMessage(final InquiryMessage message,
			final ViewHolder holder, final int position) {

		if (StringUtil.isNotEmpty(message.getContent())) {
			ImageLoader.getInstances().displayImage(message.getContent(),
					holder.iv, new ImageLoader.OnImageLoaderListener() {
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
							holder.iv.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									// Toast.makeText(oThis, "1223", 0).show();
									File file = new File(message.getContent());
									Log.i("1wait1", message.getContent());
									Uri uri = Uri.fromFile(file);
									// remotePath = ;
									EMMessage emMessage = conversation
											.getMessage(position);
									Intent intent = new Intent(oThis,
											ShowBigImage.class);
									intent.putExtra("remotepath",
											message.getContent());
									oThis.startActivity(intent);

								}
							});
						}
					});
		}

	}

	/**
	 * 文本消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(InquiryMessage message, ViewHolder holder) {
		// Spannable span = SmileUtils.getSmiledText(oThis,
		// message.getContent());
		// // 设置内容
		// holder.tv.setText(span, BufferType.SPANNABLE);
		if (holder.tv != null) {
			holder.tv.setText(message.getContent());
		}

		// 设置长按事件监听
	}

	public String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	public void showToast() {
		String st = "错误提示";
		String msg1 = "似乎已断开与互联网的连接。";
		dialog = new com.winning.pregnancy.util.AlertDialog(this);
		dialog.builder();
		dialog.setTitle(st);
		dialog.setMsg(msg1);
		dialog.setPositiveButton("好", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
					// finish();

				}
				return false;
			}
		});

	}
}
