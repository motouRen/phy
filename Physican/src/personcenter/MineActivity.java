package personcenter;

import java.util.Map;

import utils.NetworkUtils;
import utils.SharedPreferencesUtils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import base.BaseActivity;
import bean.Doctor;
import bean.DoctorData;

import com.alibaba.fastjson.JSON;
import com.arvin.physican.R;
import com.lidroid.xutils.BitmapUtils;
import com.winning.pregnancy.util.AlertDialog;

import config.Urls;
import customview.RoundImageView;

public class MineActivity extends BaseActivity {

	private TextView textView_mine_location;
	private TextView textView_mine_info;
	private TextView info_detail;
	private TextView show_inquiryCount;
	private TextView show_moneyCount;
	private RoundImageView imageView_mine_photo;
	private DoctorData doctorData;
	private BitmapUtils bitmapUtils;
	private Map<String, String> map;
	private Context mContext = this;

	private AlertDialog dialog;

	@Override
	protected void setCurrentView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_mine);

		RelativeLayout rl_mine_back = (RelativeLayout) findViewById(R.id.rl_mine_back);
		textView_mine_location = (TextView) findViewById(R.id.textView_mine_location);
		textView_mine_info = (TextView) findViewById(R.id.textView_mine_info);
		info_detail = (TextView) findViewById(R.id.info_detail);
		show_inquiryCount = (TextView) findViewById(R.id.show_inquiryCount);
		show_moneyCount = (TextView) findViewById(R.id.show_moneyCount);
		imageView_mine_photo = (RoundImageView) findViewById(R.id.imageView_mine_photo);
		rl_mine_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

		map = SharedPreferencesUtils.getSharedPreferencesForAppConfig(this);
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(this);

	}

	@Override
	protected void setViewData() {

		DoctorInfoRequest.obtainDoctorInfo(this, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				switch (msg.what) {
				case 0:
					Doctor doctor = JSON
							.parseObject(msg.obj + "", Doctor.class);

					doctorData = doctor.getData().get(0);
					;
					textView_mine_location.setText(doctorData.getHospitalName()
							+ " " + doctorData.getDepartmentName() + " "
							+ doctorData.getTitleName());
					textView_mine_info.setText(doctorData.getDoctorName() + " "
							+ doctorData.getMobile());
					info_detail.setText(doctorData.getSummary());

					String inquiryCount = doctorData.getInquiryCount();
					String moneyCount = doctorData.getCoinBalance();
					show_inquiryCount.setText(sureString(inquiryCount));
					show_moneyCount.setText(sureString(moneyCount));
					String headPhotoString = doctorData.getHeadPhoto();
					// String url = sureString(headPhotoString);
					// Log.i("image", headPhotoString + "");
					if (!TextUtils.isEmpty(headPhotoString)) {

						bitmapUtils.display(imageView_mine_photo,
								Urls.GRAVIDAHEADPHOTO_URL + headPhotoString);

					}

					break;

				default:
					showToast();
					break;
				}

			}
		}, map);

		// TODO Auto-generated method stub

	}

	private String sureString(String current) {
		if (TextUtils.isEmpty(current)) {
			return "";
		}

		else {
			if (current.equals("0") || current.equals(null)) {
				return "";
			} else {
				return current;
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

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
