package customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arvin.physican.R;

public class MyTitleBar extends RelativeLayout {
	private int titleColor;
	private Drawable titleImg;
	private String titleText;
	private float titleSize;
	private boolean leftVisiable;
	private float leftWidth;
	private float leftHeight;
	private Drawable leftImg;
	private boolean rightVisiable;
	private float rightWidth;
	private float rightHeight;
	private Drawable rightImg;
	private TextView title;
	private ImageView rightImage;
	private ImageView leftImage;
	private LayoutParams titleLp, leftLp, rightLp;
	private topBarClickListener listener;
	public static Typeface jtz;

	// 接口回调
	public interface topBarClickListener {
		public void leftClick();

		public void rightClick();
	}

	public void setOnTopBarClickListener(topBarClickListener listener) {
		this.listener = listener;
	}

	public MyTitleBar(Context context) {
		this(context, null);
	}

	public MyTitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	@SuppressWarnings("deprecation")
	private void init(Context context, AttributeSet attrs, int defStyle) {
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.MyTitlebar);
		// 标题颜色
		titleColor = array.getColor(R.styleable.MyTitlebar_titleColor,
				Color.WHITE);
		// 标题图片
		titleImg = array.getDrawable(R.styleable.MyTitlebar_titleImg);
		// 标题文本
		titleText = array.getString(R.styleable.MyTitlebar_titleText);
		// 标题文本大小
		titleSize = array.getDimension(R.styleable.MyTitlebar_titleSize, 18);

		// 左布局
		// 左视图的可见性
		leftVisiable = array.getBoolean(R.styleable.MyTitlebar_leftVisiable,
				true);
		leftWidth = array.getDimension(R.styleable.MyTitlebar_leftWidth,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		leftHeight = array.getDimension(R.styleable.MyTitlebar_leftHeight,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		leftImg = array.getDrawable(R.styleable.MyTitlebar_leftImg);

		// 右布局
		// 左视图的可见性
		rightVisiable = array.getBoolean(R.styleable.MyTitlebar_rightVisiable,
				true);
		rightWidth = array.getDimension(R.styleable.MyTitlebar_rightWidth,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rightHeight = array.getDimension(R.styleable.MyTitlebar_rightHeight,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rightImg = array.getDrawable(R.styleable.MyTitlebar_rightImg);

		array.recycle();
		// 创建三个控件
		title = new TextView(context);
		leftImage = new ImageView(context);
		rightImage = new ImageView(context);

		// 对标题进行设置
		title.setSingleLine();
		title.setPadding(10, 10, 10, 10);

		//
		title.setEllipsize(TruncateAt.END);
		title.setMaxWidth(getResources().getDisplayMetrics().widthPixels - 200);

		if (!leftVisiable) {
			leftImage.setVisibility(GONE);
		} else {
			leftImage.setVisibility(View.VISIBLE);
		}
		if (rightVisiable) {
			rightImage.setVisibility(View.VISIBLE);
		} else {
			rightImage.setVisibility(GONE);
		}

		title.setText(titleText);
		title.setTextColor(titleColor);
		title.setTextSize((int) titleSize);
		title.setBackgroundDrawable(titleImg);
		title.setGravity(Gravity.CENTER);
		// 在左imageView设置图片及缩放模式
		leftImage.setImageDrawable(leftImg);
		leftImage.setScaleType(ScaleType.CENTER_INSIDE);

		// 在右imageView设置图片及缩放模式
		rightImage.setImageDrawable(rightImg);
		rightImage.setScaleType(ScaleType.CENTER_INSIDE);
		// 中间textView的布局
		titleLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		titleLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
		// 左面imageView的布局
		leftLp = new LayoutParams((int) leftWidth, (int) leftHeight);
		leftLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
		leftLp.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		// leftLp.addR
		// 右面imageView的布局
		rightLp = new LayoutParams((int) rightWidth, (int) rightHeight);
		rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
		rightLp.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
		// 将三个控件放入三个布局中显示
		addView(title, titleLp);
		addView(leftImage, leftLp);
		addView(rightImage, rightLp);

		/**
		 * 对左图
		 * */
		leftImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View v) {

				if (listener != null) {
					listener.leftClick();

				}
			}
		});

		rightImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.rightClick();
				}
			}
		});
	}

	public void setLeftVisiable(boolean flag) {
		if (flag) {
			leftImage.setVisibility(VISIBLE);
		} else {
			leftImage.setVisibility(GONE);
		}
	}

	public void setRightVisiable(boolean flag) {
		if (flag) {
			rightImage.setVisibility(VISIBLE);
		} else {
			rightImage.setVisibility(GONE);
		}
	}

	public void setTitle(String s) {
		title.setText(s);
	}

	public ImageView getLeftImageView() {
		return leftImage;
	}

	public ImageView getRightImageView() {
		return rightImage;
	}

	public TextView getTitleTextView() {
		return title;
	}

	public void setTypeface(Typeface tf) {
		title.setTypeface(tf);
	}
}
