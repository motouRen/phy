package com.winning.pregnancy.util;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.arvin.physican.R;
import com.winning.wheelview.OnWheelScrollListener;
import com.winning.wheelview.WheelView;
import com.winning.wheelview.adapter.ArrayWheelAdapter;
import com.winning.wheelview.adapter.NumericWheelAdapter;

public class TimeDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private LinearLayout txt_msg;
	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
	int screenWidth = 0;
	int screenHeight = 0;
	private LayoutInflater inflater = null;
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView time;
	private WheelView min;
	private WheelView sec;

	private int mYear = 2015;
	private int mMonth = 0;
	private int mDay = 1;
	private int mHour = 1;
	private int mMin = 1;

	View view = null;
	private String initDate;
	private String lastDate = MyTimeUtil.DateToStr(
			MyTimeUtil.yyyy_MM_dd_HH_mm_ss, new Date());

	private TimeDialogValue timeDialogValue;

	/**
	 * 日期格式 yyyy-MM-dd <默认构造函数>
	 */
	public TimeDialog(Context context, String initDate,
			TimeDialogValue timeDialogValue) {
		this.context = context;
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		this.initDate = initDate;
		this.timeDialogValue = timeDialogValue;
	}

	public TimeDialog builder() {
		// 获取Dialog布局
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_timedialog, null);
		inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		txt_msg = (LinearLayout) view.findViewById(R.id.txt_msg);
		txt_msg.addView(getDataPick());

		// 获取自定义Dialog布局中的控件
		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (LinearLayout) view.findViewById(R.id.txt_msg);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		// 定义Dialog布局和参数
		dialog = new Dialog(context, R.style.AlertDialogStyle);
		// dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setContentView(view);

		// 调整dialog背景大小
		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams(screenWidth,
				LayoutParams.WRAP_CONTENT));

		return this;
	}

	private View getDataPick() {
		Calendar c = Calendar.getInstance();
		int norYear = c.get(Calendar.YEAR);
		// int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
		// int curDate = c.get(Calendar.DATE);

		int curYear = mYear;
		int curMonth = mMonth + 1;
		int curDate = mDay;
		int curHour = mHour;
		int curMin = mMin;
		if (StringUtil.isNotEmpty(initDate)) {
			lastDate = initDate;
			Date d = MyTimeUtil.strToDate(MyTimeUtil.yyyy_MM_dd_HH_mm_ss,
					initDate);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			curYear = calendar.get(Calendar.YEAR);
			curMonth = calendar.get(Calendar.MONTH) + 1;
			curDate = calendar.get(Calendar.DATE);
			curHour = calendar.get(Calendar.HOUR_OF_DAY);
			curMin = calendar.get(Calendar.MINUTE);
		} else {
			lastDate = MyTimeUtil.DateToStr(MyTimeUtil.yyyy_MM_dd_HH_mm_ss,
					new Date());
			Date d = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			curYear = calendar.get(Calendar.YEAR);
			curMonth = calendar.get(Calendar.MONTH) + 1;
			curDate = calendar.get(Calendar.DATE);
			curHour = calendar.get(Calendar.HOUR_OF_DAY);
			curMin = calendar.get(Calendar.MINUTE);
		}
		view = inflater.inflate(R.layout.wheel_time_picker, null);

		year = (WheelView) view.findViewById(R.id.year);
		NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(
				context, 1950, norYear + 50);
		numericWheelAdapter1.setLabel("");
		year.setViewAdapter(numericWheelAdapter1);
		year.setCyclic(true);// 是否可循环滑动
		year.addScrollingListener(scrollListener);

		month = (WheelView) view.findViewById(R.id.month);
		NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(
				context, 1, 12, "%02d");
		numericWheelAdapter2.setLabel("");
		month.setViewAdapter(numericWheelAdapter2);
		month.setCyclic(true);
		month.addScrollingListener(scrollListener);

		day = (WheelView) view.findViewById(R.id.day);
		initDay(curYear, curMonth);
		day.setCyclic(true);
		day.addScrollingListener(scrollListener);

		time = (WheelView) view.findViewById(R.id.time);
		String[] times = { "上午", "下午" };
		ArrayWheelAdapter<String> arrayWheelAdapter = new ArrayWheelAdapter<String>(
				context, times);
		time.setViewAdapter(arrayWheelAdapter);
		time.setCyclic(false);
		time.addScrollingListener(scrollListener);

		min = (WheelView) view.findViewById(R.id.min);
		NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(
				context, 1, 23, "%02d");
		numericWheelAdapter3.setLabel("");
		min.setViewAdapter(numericWheelAdapter3);
		min.setCyclic(true);
		min.addScrollingListener(scrollListener);

		sec = (WheelView) view.findViewById(R.id.sec);
		NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(
				context, 1, 59, "%02d");
		numericWheelAdapter4.setLabel("");
		sec.setViewAdapter(numericWheelAdapter4);
		sec.setCyclic(true);
		sec.addScrollingListener(scrollListener);

		year.setVisibleItems(7);// 设置显示行数
		month.setVisibleItems(7);
		day.setVisibleItems(7);
		time.setVisibleItems(7);
		min.setVisibleItems(7);
		sec.setVisibleItems(7);

		year.setCurrentItem(curYear - 1950);
		month.setCurrentItem(curMonth - 1);
		day.setCurrentItem(curDate - 1);
		min.setCurrentItem(curHour - 1);
		sec.setCurrentItem(curMin - 1);

		return view;
	}

	OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
		@Override
		public void onScrollingStarted(WheelView wheel) {

		}

		@Override
		public void onScrollingFinished(WheelView wheel) {
			int n_year = year.getCurrentItem() + 1950;// 年
			int n_month = month.getCurrentItem() + 1;// 月
			int n_hour = min.getCurrentItem();
			if (n_hour <= 12) {
				time.setCurrentItem(0, true);
			} else {
				time.setCurrentItem(1, true);
			}

			initDay(n_year, n_month);
			String date = new StringBuilder()
					.append((year.getCurrentItem() + 1950))
					.append("-")
					.append((month.getCurrentItem() + 1) < 10 ? "0"
							+ (month.getCurrentItem() + 1) : (month
							.getCurrentItem() + 1))
					.append("-")
					.append(((day.getCurrentItem() + 1) < 10) ? "0"
							+ (day.getCurrentItem() + 1) : (day
							.getCurrentItem() + 1))
					.append(" ")
					.append(((min.getCurrentItem() + 1) < 10) ? "0"
							+ (min.getCurrentItem() + 1) : (min
							.getCurrentItem() + 1))
					.append(":")
					.append(((sec.getCurrentItem() + 1) < 10) ? "0"
							+ (sec.getCurrentItem() + 1) : (sec
							.getCurrentItem() + 1)).append(":00").toString();
			lastDate = date;
		}
	};

	/**
     */
	private void initDay(int arg1, int arg2) {
		NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(
				context, 1, getDay(arg1, arg2), "%02d");
		numericWheelAdapter.setLabel("");
		day.setViewAdapter(numericWheelAdapter);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	private int getDay(int year, int month) {
		int day = 30;
		boolean flag = false;
		switch (year % 4) {
		case 0:
			flag = true;
			break;
		default:
			flag = false;
			break;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			day = 31;
			break;
		case 2:
			day = flag ? 29 : 28;
			break;
		default:
			day = 30;
			break;
		}
		return day;
	}

	public TimeDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("标题");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public TimeDialog setMsg(String msg) {
		showMsg = true;
		return this;
	}

	public TimeDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public TimeDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (timeDialogValue != null) {
					timeDialogValue.findTimeDialogValue(lastDate);
				}
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	public TimeDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("日期");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
		}

		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}

	public interface TimeDialogValue {
		void findTimeDialogValue(String date);
	}
}
