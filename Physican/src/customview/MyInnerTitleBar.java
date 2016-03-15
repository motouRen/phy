package customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arvin.physican.R;

public class MyInnerTitleBar extends RelativeLayout {
	private innerTopBarClickListener listener;

	public MyInnerTitleBar(Context context) {
		this(context, null);
	}

	public interface innerTopBarClickListener {
		public void backClick();
	}

	public MyInnerTitleBar(Context context, AttributeSet attrs) {

		super(context, attrs);

		View view = View.inflate(context, R.layout.topbar_inner, this);
		ImageView imageView = (ImageView) view
				.findViewById(R.id.imageView_topbar_back);
		TextView leftTextView = (TextView) view
				.findViewById(R.id.textView_topbar_left);
		TextView centerTextView = (TextView) view
				.findViewById(R.id.textView_topbar_center);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.MyInnerTopbar);
		leftTextView.setText(array
				.getString(R.styleable.MyInnerTopbar_leftText));
		centerTextView.setText(array
				.getString(R.styleable.MyInnerTopbar_centerText));
		imageView.setImageDrawable(array
				.getDrawable(R.styleable.MyInnerTopbar_leftSrc));
		array.recycle();
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(android.view.View v) {

				if (listener != null) {
					listener.backClick();

				}
			}
		});
	}

}
