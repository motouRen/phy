package inquirymessage;

import java.util.List;

import utils.DateUtils;
import utils.NetworkUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.InquiryByDoctorDetail;

import com.arvin.physican.R;
import com.lidroid.xutils.BitmapUtils;

import config.Urls;
import customview.RoundImageView;

public class InquiryListViewAdapter extends BaseAdapter {
	private List<InquiryByDoctorDetail> list;
	private Context mContext;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;

	public InquiryListViewAdapter(List<InquiryByDoctorDetail> list,
			Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	private class ViewHolder {

		private RoundImageView imageView_iquiryMessage_photo;
		private TextView textView_inquiryMessage_name;
		private TextView textView_inquiryMessage_date;
		private TextView textView_inquiryMessage_num;
		private TextView textView_inquiryMessage_content;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_inquirymessage,
					parent, false);
			// 图片记得去下载
			vh.imageView_iquiryMessage_photo = (RoundImageView) convertView
					.findViewById(R.id.imageView_iquiryMessage_photo);
			vh.textView_inquiryMessage_name = (TextView) convertView
					.findViewById(R.id.textView_inquiryMessage_name);
			vh.textView_inquiryMessage_date = (TextView) convertView
					.findViewById(R.id.textView_inquiryMessage_date);
			// 要去判断有多少个未处理消息
			vh.textView_inquiryMessage_num = (TextView) convertView
					.findViewById(R.id.textView_inquiryMessage_num);
			vh.textView_inquiryMessage_content = (TextView) convertView
					.findViewById(R.id.textView_inquiryMessage_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.textView_inquiryMessage_name.setText(list.get(position)
				.getGravidaName());
		String brithday = list.get(position).getBirthday();
		if (TextUtils.isEmpty(brithday)) {
			vh.textView_inquiryMessage_date.setText("--岁" + "  " + "预产期： "
					+ list.get(position).getDueDate());
		} else {
			String current = DateUtils.getCurrentDate();
			vh.textView_inquiryMessage_date.setText(DateUtils.yearsBetween(
					current, brithday)
					+ "岁"
					+ "  "
					+ "预产期： "
					+ list.get(position).getDueDate());
		}

		vh.textView_inquiryMessage_content.setText(list.get(position)
				.getTitle());
		vh.textView_inquiryMessage_num.setVisibility(View.GONE);
		String url = list.get(position).getGravidaHeadPhoto();
		if (TextUtils.isEmpty(url)) {
			vh.imageView_iquiryMessage_photo
					.setImageResource(R.drawable.headphoto);
		} else {
			bitmapUtils.display(vh.imageView_iquiryMessage_photo,
					Urls.GRAVIDAHEADPHOTO_URL + url);
		}
		return convertView;
	}

	public void reloadAdapter(List<InquiryByDoctorDetail> list, boolean isClear) {
		if (isClear) {
			this.list.clear();
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}

}
