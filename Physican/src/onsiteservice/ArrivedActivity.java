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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.winning.pregnancy.util.AlertDialog;
import com.winning.pregnancy.util.AnimUtils;
import com.zxing.android.CaptureActivity;

import config.Urls;
import customview.RoundImageView;

public class ArrivedActivity extends BaseActivity implements OnClickListener {
	public static final String TAG = "ArrivedActivity";
	private DoneDetail detail;
	private RoundImageView imageView_arrived_photo;
	private TextView textView_arrived_name;
	private TextView textView_arrived_date;
	private TextView moblie_arrived_detail;
	private TextView location_arrived_detail;
	private TextView zhusu_arrived_detail;
	private BitmapUtils bitmapUtils;
	private RelativeLayout arrived_back;
	private int state;
	private Button button_checkNow;
	private String id;
	private String code;
	private AlertDialog dialog;
	private String json;

	private Context mContext = this;
	private String lat;
	private String lon;
	private Map<String, String> map;

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_arrived);
		arrived_back = (RelativeLayout) findViewById(R.id.arrived_back);
		imageView_arrived_photo = (RoundImageView) findViewById(R.id.imageView_arrived_photo);
		textView_arrived_name = (TextView) findViewById(R.id.textView_arrived_name);
		textView_arrived_date = (TextView) findViewById(R.id.textView_arrived_date);
		moblie_arrived_detail = (TextView) findViewById(R.id.moblie_arrived_detail);
		location_arrived_detail = (TextView) findViewById(R.id.location_arrived_detail);
		zhusu_arrived_detail = (TextView) findViewById(R.id.zhusu_arrived_detail);
		button_checkNow = (Button) findViewById(R.id.button_checkNow);
		arrived_back.setOnClickListener(this);
		location_arrived_detail.setOnClickListener(this);
		moblie_arrived_detail.setOnClickListener(this);
		button_checkNow.setOnClickListener(this);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		json = bundle.getString("json");
		int position = bundle.getInt("position");

		Done done = JSON.parseObject(json, Done.class);
		List<DoneDetail> data = done.getData();
		detail = data.get(position);
		state = data.get(position).getState();
		id = data.get(position).getId() + "";
		code = data.get(position).getVerificationCode();

		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(mContext);

	}

	@Override
	protected void setViewData() {
		// TODO Auto-generated method stub
		textView_arrived_name.setText(detail.getGravidaName());
		textView_arrived_date.setText("预产期： "
				+ detail.getDueDate().split(" ")[0]);
		moblie_arrived_detail.setText(detail.getGravidaMobile());

		String destination = detail.getDestination();
		try {
			JSONObject jsonObject = new JSONObject(destination);
			String adress = jsonObject.getString("address");
			location_arrived_detail.setText(adress);
			lat = jsonObject.optString("lat");
			lon = jsonObject.optString("lon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		zhusu_arrived_detail.setText(detail.getTitle());
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);
		bitmapUtils.display(imageView_arrived_photo, Urls.GRAVIDAHEADPHOTO_URL
				+ detail.getGravidaHeadPhoto());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.arrived_back:
			finish();
			break;
		case R.id.moblie_arrived_detail:
			// 打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ detail.getGravidaMobile()));

			startActivity(intent);
			break;
		case R.id.location_arrived_detail:
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
		case R.id.button_checkNow:

			// 开启扫描功能
			Intent intent2 = new Intent();
			Bundle extras = new Bundle();
			extras.putString("serviceID", id);
			extras.putString("verificationCode", code);
			intent2.putExtras(extras);
			intent2.setClass(this, CaptureActivity.class);
			startActivityForResult(intent2, 1);
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BaiduMapRoutePlan.finish(this);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg1 == 1) {
			finish();
		}
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
		dialog = new AlertDialog(this);
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
