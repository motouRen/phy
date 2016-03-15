package com.zxing.android;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import onsiteservice.ArrivedRequest;
import utils.SharedPreferencesUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arvin.physican.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.http.HttpHandler;
import com.zxing.android.camera.CameraManager;
import com.zxing.android.decoding.CaptureActivityHandler;
import com.zxing.android.decoding.InactivityTimer;
import com.zxing.android.view.ViewfinderView;

public class CaptureActivity extends Activity implements Callback {
	public static final String QR_RESULT = "RESULT";
	private Context mContext = this;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private SurfaceView surfaceView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	// private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	CameraManager cameraManager;

	private TextView txtResult;

	private RelativeLayout rl_sao1sao;

	private String serviceID;

	private String verificationCode;

	private HttpHandler<String> httpHandler;
	private Map<String, String> map;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
		 * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		 * 
		 * RelativeLayout layout = new RelativeLayout(this);
		 * layout.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * this.surfaceView = new SurfaceView(this); this.surfaceView
		 * .setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * layout.addView(this.surfaceView);
		 * 
		 * this.viewfinderView = new ViewfinderView(this);
		 * this.viewfinderView.setBackgroundColor(0x00000000);
		 * this.viewfinderView.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT)); layout.addView(this.viewfinderView);
		 * 
		 * TextView status = new TextView(this); RelativeLayout.LayoutParams
		 * params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * status.setLayoutParams(params);
		 * status.setBackgroundColor(0x00000000);
		 * status.setTextColor(0xFFFFFFFF); status.setText("请将条码置于取景框内扫描。");
		 * status.setTextSize(14.0f);
		 * 
		 * layout.addView(status); setContentView(layout);
		 */

		setContentView(R.layout.main);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		// imageView_sao1sao_back
		rl_sao1sao = (RelativeLayout) findViewById(R.id.rl_sao1sao);
		rl_sao1sao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle == null) {

		} else {
			serviceID = bundle.getString("serviceID");
			verificationCode = bundle.getString("verificationCode");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// CameraManager.init(getApplication());
		cameraManager = new CameraManager(getApplication());

		viewfinderView.setCameraManager(cameraManager);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		cameraManager.closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			// CameraManager.get().openDriver(surfaceHolder);
			cameraManager.openDriver(surfaceHolder);
		} catch (IOException ioe) {
			// Toast.makeText(mContext, ioe.getMessage(), 0).show();
			return;
		} catch (RuntimeException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

			View view = LayoutInflater.from(mContext).inflate(
					R.layout.alert_layout, null);
			TextView textView_ok = (TextView) view
					.findViewById(R.id.textView_ok);
			textView_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();

				}
			});
			builder.setView(view).setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					finish();

				}
			});
			builder.create().show();
			// root)
			// builder.setView(view)
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		showResult(obj, barcode);
	}

	@SuppressLint("HandlerLeak")
	private void showResult(final Result rawResult, Bitmap barcode) {
		if (serviceID == null || TextUtils.isEmpty(serviceID)) {
			txtResult.setText(rawResult.getText());
			surfaceView.setVisibility(View.GONE);
			viewfinderView.setVisibility(View.GONE);
		} else {

			map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(this);
			String s = rawResult.getText();
			String sas = s.substring(s.indexOf(" "));

			httpHandler = ArrivedRequest.arrive(this, new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					switch (msg.what) {
					case 0:
						Intent intent = new Intent("reload_sencondpage");
						sendBroadcast(intent);
						CaptureActivity.this.setResult(1);
						finish();
						break;

					default:
						Toast.makeText(mContext, "提交上门服务失败，请稍后",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, map, serviceID, sas.trim());
		}

		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(this);
		 * 
		 * Drawable drawable = new BitmapDrawable(barcode);
		 * builder.setIcon(drawable);
		 * 
		 * builder.setTitle("类型:" + rawResult.getBarcodeFormat() + "\n 结果：" +
		 * rawResult.getText()); builder.setPositiveButton("确定", new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); Intent intent = new Intent();
		 * intent.putExtra("result", rawResult.getText()); setResult(RESULT_OK,
		 * intent); finish(); } }); builder.setNegativeButton("重新扫描", new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); restartPreviewAfterDelay(0L); } });
		 * builder.setCancelable(false); builder.show();
		 */

		// Intent intent = new Intent();
		// intent.putExtra(QR_RESULT, rawResult.getText());
		// setResult(RESULT_OK, intent);
		// finish();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			try {
				AssetFileDescriptor fileDescriptor = getAssets().openFd(
						"qrbeep.ogg");
				this.mediaPlayer.setDataSource(
						fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				this.mediaPlayer.setVolume(0.1F, 0.1F);
				this.mediaPlayer.prepare();
			} catch (IOException e) {
				this.mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}