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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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

import config.Urls;
import customview.RoundImageView;

/*
 * 准备出发上门服务详情页面
 * */
public class BeginOnsite extends BaseActivity implements OnClickListener {

	private ImageView imageView_begin_back;
	private RoundImageView imageView_begin_photo;
	private TextView textView_begin_name;
	private TextView textView_begin_date;
	private TextView moblie_begin_detail;
	private TextView location_begin_detail;
	private TextView zhusu_begin_detail;
	private DoneDetail detail;
	private BitmapUtils bitmapUtils;

	private int position;
	private Button button_beginOnsite;
	private RelativeLayout rl_begin_back;

	private AlertDialog dialog;
	private Context mContext = this;
	private String lat;
	private String lon;
	private Map<String, String> map;

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_begin);
		rl_begin_back = (RelativeLayout) findViewById(R.id.rl_begin_back);
		imageView_begin_photo = (RoundImageView) findViewById(R.id.imageView_begin_photo);
		textView_begin_name = (TextView) findViewById(R.id.textView_begin_name);
		textView_begin_date = (TextView) findViewById(R.id.textView_begin_date);
		moblie_begin_detail = (TextView) findViewById(R.id.moblie_begin_detail);
		location_begin_detail = (TextView) findViewById(R.id.location_begin_detail);
		zhusu_begin_detail = (TextView) findViewById(R.id.zhusu_begin_detail);
		button_beginOnsite = (Button) findViewById(R.id.button_beginOnsite);
		rl_begin_back.setOnClickListener(this);
		location_begin_detail.setOnClickListener(this);
		moblie_begin_detail.setOnClickListener(this);
		button_beginOnsite.setOnClickListener(this);

	}

	@Override
	protected void initData() {

		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String json = bundle.getString("json");
		position = bundle.getInt("position");
		Done done = JSON.parseObject(json, Done.class);
		List<DoneDetail> data = done.getData();
		detail = data.get(position);
		Log.i("ArrivedActivity", json);

		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(mContext);

	}

	@Override
	protected void setViewData() {
		// TODO Auto-generated method stub
		textView_begin_name.setText(detail.getGravidaName());
		textView_begin_date
				.setText("预产期： " + detail.getDueDate().split(" ")[0]);
		moblie_begin_detail.setText(detail.getGravidaMobile());

		String destination = detail.getDestination();
		try {
			JSONObject jsonObject = new JSONObject(destination);
			String adress = jsonObject.getString("address");
			location_begin_detail.setText(adress);
			lat = jsonObject.optString("lat");
			lon = jsonObject.optString("lon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		zhusu_begin_detail.setText(detail.getTitle());
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);
		bitmapUtils.display(imageView_begin_photo, Urls.GRAVIDAHEADPHOTO_URL
				+ detail.getGravidaHeadPhoto());

	}

	// destination": "{\"coorType\":\"bd09ll\",\"lat\":\"31.85729\",\"lon\":\"117.265975\",\"address\":\"合肥市肥西路66号\"}",
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rl_begin_back:
			finish();
			break;
		case R.id.moblie_begin_detail:
			// 打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ detail.getGravidaMobile()));

			startActivity(intent);
			break;
		case R.id.location_begin_detail:
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
		case R.id.button_beginOnsite:

			// 弹出对话框让其判断是否上门出发
			AlertDialog dialog = new AlertDialog(mContext);
			String st = "上门提示";
			String msg1 = "确信出发上门吗？";
			// dialog = new AlertDialog(this);
			dialog.builder();
			dialog.setTitle(st);
			dialog.setMsg(msg1);
			dialog.setPositiveButton("确定", new OnClickListener() {
				private HttpHandler<String> httpHandler;

				@Override
				public void onClick(View v) {
					String serviceID = detail.getId() + "";
					httpHandler = OnsiteRequest.begin(
							mContext,
							new Handler() {
								@Override
								public void handleMessage(Message msg) {
									// TODO Auto-generated
									// method stub
									super.handleMessage(msg);
									switch (msg.what) {
									case 0:
										Intent intent = new Intent(
												"reload_firstpage");
										sendBroadcast(intent);
										finish();
										break;

									default:
										showToast();
										break;
									}
								}
							},
							SharedPreferencesUtils
									.getSharedPreferencesForAppConfig(mContext),
							serviceID);

				}
			});
			dialog.setNegativeButton("取消", new OnClickListener() {

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

			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BaiduMapRoutePlan.finish(this);

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
