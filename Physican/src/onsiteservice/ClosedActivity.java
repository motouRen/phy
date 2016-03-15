package onsiteservice;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.NetworkUtils;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import base.BaseActivity;
import bean.Done;
import bean.DoneDetail;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.R;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.winning.pregnancy.util.AlertDialog;
import com.winning.pregnancy.util.AnimUtils;

import config.Urls;
import customview.RoundImageView;

public class ClosedActivity extends BaseActivity implements OnClickListener {

	private DoneDetail detail;
	private RelativeLayout closed_back;
	private RoundImageView imageView_closed_photo;
	private TextView textView_closed_name;
	private TextView textView_closed_date;
	private TextView moblie_closed_detail;
	private TextView zhusu_closed_detail;
	private TextView location_closed_detail;
	private BitmapUtils bitmapUtils;
	private EditText et_closed_onsite;

	private HttpHandler<String> httpHandler;
	private Toast toast;
	private Toast toast2;
	private AlertDialog dialog;

	private Context mContext = this;
	private String lat;
	private String lon;
	private Map<String, String> map;

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_closed);

		closed_back = (RelativeLayout) findViewById(R.id.closed_back);
		imageView_closed_photo = (RoundImageView) findViewById(R.id.imageView_closed_photo);
		textView_closed_name = (TextView) findViewById(R.id.textView_closed_name);
		textView_closed_date = (TextView) findViewById(R.id.textView_closed_date);
		moblie_closed_detail = (TextView) findViewById(R.id.moblie_closed_detail);
		location_closed_detail = (TextView) findViewById(R.id.location_closed_detail);
		zhusu_closed_detail = (TextView) findViewById(R.id.zhusu_closed_detail);
		Button button_closeService = (Button) findViewById(R.id.button_closeService);
		et_closed_onsite = (EditText) findViewById(R.id.et_closed_onsite);
		closed_back.setOnClickListener(this);
		location_closed_detail.setOnClickListener(this);
		moblie_closed_detail.setOnClickListener(this);
		button_closeService.setOnClickListener(this);

		et_closed_onsite.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				arg0.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		toast = Toast.makeText(mContext, "关闭当前服务失败，请稍后重试", Toast.LENGTH_SHORT);
		toast2 = Toast.makeText(ClosedActivity.this, "服务摘要不能为空",
				Toast.LENGTH_SHORT);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String json = bundle.getString("json");
		int position = bundle.getInt("position");
		Done done = JSON.parseObject(json, Done.class);
		List<DoneDetail> data = done.getData();
		detail = data.get(position);
		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(this);
	}

	@Override
	protected void setViewData() {
		// TODO Auto-generated method stub
		textView_closed_name.setText(detail.getGravidaName());
		textView_closed_date.setText("预产期： "
				+ detail.getDueDate().split(" ")[0]);
		moblie_closed_detail.setText(detail.getGravidaMobile());

		String destination = detail.getDestination();
		try {
			JSONObject jsonObject = new JSONObject(destination);
			String adress = jsonObject.getString("address");
			location_closed_detail.setText(adress);
			lat = jsonObject.optString("lat");
			lon = jsonObject.optString("lon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zhusu_closed_detail.setText(detail.getTitle());
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);
		bitmapUtils.display(imageView_closed_photo, Urls.GRAVIDAHEADPHOTO_URL
				+ detail.getGravidaHeadPhoto());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.closed_back:
			finish();
			break;
		case R.id.moblie_closed_detail:
			// 打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ detail.getGravidaMobile()));

			startActivity(intent);
			break;
		case R.id.location_closed_detail:
			// 调用百度地图
			if (lon == null || lat == null) {
				Toast.makeText(mContext, "该用户没有提供坐标，无法导航", 2000).show();
			} else {
				if (lon.equals("") || lat.equals("")) {
					Toast.makeText(mContext, "该用户没有提供坐标，无法导航", 2000).show();
				} else {

					String latitude = map.get("latitude");
					String longitude = map.get("longitude");
					if (latitude.equals("") || longitude.equals("")) {
						Toast.makeText(mContext, "该用户没有提供坐标，无法导航", 2000).show();
					} else {
						BaiduMapRoutePlan.setSupportWebRoute(false);
						double mLat1 = Double.parseDouble(latitude);
						double mLon1 = Double.parseDouble(longitude);
						// 百度大厦坐标
						double mLat2 = Double.parseDouble(lat);
						double mLon2 = Double.parseDouble(lon);
						LatLng pt_start = new LatLng(mLat1, mLon1);
						LatLng pt_end = new LatLng(mLat2, mLon2);
						// 构建 route搜索参数以及策略，起终点也可以用name构造
						RouteParaOption para = new RouteParaOption()
								.startPoint(pt_start)
								.endPoint(pt_end)
								.busStrategyType(
										EBusStrategyType.bus_recommend_way);
						try {
							BaiduMapRoutePlan.openBaiduMapTransitRoute(para,
									this);
						} catch (Exception e) {
							e.printStackTrace();
							Toast.makeText(mContext, "请先安装百度地图app", 2000)
									.show();
						}

					}

				}

			}

			break;
		default:
			String serviceID = detail.getId() + "";
			String summary = et_closed_onsite.getText() + "";
			if (TextUtils.isEmpty(summary)) {
				if (toast2 != null) {
					toast2.show();

				} else {
					toast2 = Toast.makeText(ClosedActivity.this, "服务摘要不能为空",
							Toast.LENGTH_SHORT);
					toast2.show();
				}

			} else {
				httpHandler = ClosedRequest.close(this, new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						super.handleMessage(msg);
						switch (msg.what) {
						case 0:
							Intent intent = new Intent("reload_sencondpage");
							sendBroadcast(intent);
							finish();

							break;

						default:

							showToast();
							break;
						}
					}
				}, map, serviceID, summary);
			}
			// 提交关闭服务请求

			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (httpHandler != null) {
			httpHandler.cancel();
		}
		BaiduMapRoutePlan.finish(this);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AnimUtils.inRightOutleft(this);
		super.onBackPressed();

	}

	public void showToast() {
		String st = "错误提示";
		String msg1 = "似乎已断开与互联网的连接。";
		dialog = new AlertDialog(mContext);
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
