package onsiteservice;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import utils.NetworkUtils;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.winning.pregnancy.util.AnimUtils;

import config.Urls;
import customview.RoundImageView;

/*
 * 已完成的上门服务详情页面
 * */
public class DoneActivity extends BaseActivity implements OnClickListener {

	private DoneDetail detail;
	private RelativeLayout done_back;
	private RoundImageView imageView_done_photo;
	private TextView textView_done_name;
	private TextView textView_done_date;
	private TextView moblie_done_detail;
	private TextView location_done_detail;
	private TextView zhusu_done_detail;
	private BitmapUtils bitmapUtils;
	private TextView serviceinfo_done_detail;
	private Context mContext = this;
	private String lat;
	private String lon;
	private Map<String, String> map;

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_done);
		done_back = (RelativeLayout) findViewById(R.id.done_back);
		imageView_done_photo = (RoundImageView) findViewById(R.id.imageView_done_photo);
		textView_done_name = (TextView) findViewById(R.id.textView_done_name);
		textView_done_date = (TextView) findViewById(R.id.textView_done_date);
		moblie_done_detail = (TextView) findViewById(R.id.moblie_done_detail);
		location_done_detail = (TextView) findViewById(R.id.location_done_detail);
		zhusu_done_detail = (TextView) findViewById(R.id.zhusu_done_detail);
		serviceinfo_done_detail = (TextView) findViewById(R.id.serviceinfo_done_detail);

		done_back.setOnClickListener(this);
		location_done_detail.setOnClickListener(this);
		moblie_done_detail.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String json = bundle.getString("json");
		int position = bundle.getInt("position");
		Done done = JSON.parseObject(json, Done.class);
		List<DoneDetail> data = done.getData();
		detail = data.get(position);
		Log.i("Done", json);
		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(mContext);

	}

	@Override
	protected void setViewData() {
		textView_done_name.setText(detail.getGravidaName());
		textView_done_date.setText("预产期： " + detail.getDueDate().split(" ")[0]);
		moblie_done_detail.setText(detail.getGravidaMobile());
		serviceinfo_done_detail.setText(detail.getCloseSummary());

		String destination = detail.getDestination();
		try {
			JSONObject jsonObject = new JSONObject(destination);
			String adress = jsonObject.getString("address");
			location_done_detail.setText(adress);
			lat = jsonObject.optString("lat");
			lon = jsonObject.optString("lon");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		zhusu_done_detail.setText(detail.getTitle());
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);
		bitmapUtils.display(imageView_done_photo, Urls.GRAVIDAHEADPHOTO_URL
				+ detail.getGravidaHeadPhoto());

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.done_back:
			finish();
			break;
		case R.id.moblie_done_detail:
			// 打电话
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ detail.getGravidaMobile()));

			startActivity(intent);
			break;
		case R.id.location_done_detail:
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

			// 开启扫描功能

			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AnimUtils.inRightOutleft(this);
		super.onBackPressed();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		BaiduMapRoutePlan.finish(this);
	}

}
