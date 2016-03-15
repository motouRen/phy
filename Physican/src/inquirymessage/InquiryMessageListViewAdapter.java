package inquirymessage;

import java.util.List;
import java.util.Map;

import utils.DateUtils;
import utils.MySQLiteOpenHelper;
import utils.NetworkUtils;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import bean.InquiryMessageData;

import com.arvin.physican.R;
import com.lidroid.xutils.BitmapUtils;

import config.Urls;
import customview.RoundImageView;

public class InquiryMessageListViewAdapter extends BaseAdapter {
	private List<InquiryMessageData> list;
	private Context mContext;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	private MySQLiteOpenHelper helper;
	private int count = 0;

	public InquiryMessageListViewAdapter(List<InquiryMessageData> list,
			Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bitmapUtils = NetworkUtils.loadBitmapToExternalCache(mContext);
		helper = new MySQLiteOpenHelper(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
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
				.getGravidaname());
		String id = list.get(position).getId() + "";
		String sql = "select * from tb_doctors where serviceid=?";
		List<Map<String, String>> mapList = helper.selectList(sql,
				new String[] { id });
		if (mapList.size() <= 0) {
			String insertsql = "insert into tb_doctors(serviceid , count) values(? , ?)";
			boolean flag = helper.execData(insertsql,
					new String[] { list.get(position).getId() + "", 0 + "" });
			for (int i = 0; i < mapList.size(); i++) {
				mapList.get(i);
				Log.i("mapList", mapList.get(i).toString());

			}

		} else {
			Log.i("mapList", mapList.get(0).toString());
			// Toast.makeText(mContext, count + "个", 0).show();
			count = Integer.parseInt(mapList.get(0).get("count"));
		}

		vh.textView_inquiryMessage_num.setText(count + "");
		if (count > 0) {
			vh.textView_inquiryMessage_num.setVisibility(View.VISIBLE);
			// textView_redPoint_inquiryMessage.setVisibility(visibility);
		} else {
			vh.textView_inquiryMessage_num.setVisibility(View.INVISIBLE);
		}
		String brithday = list.get(position).getBirthday();
		if (TextUtils.isEmpty(brithday)) {
			vh.textView_inquiryMessage_date.setText("--岁" + "  " + "预产期： "
					+ list.get(position).getDuedate());
		} else {
			String current = DateUtils.getCurrentDate();
			vh.textView_inquiryMessage_date.setText(DateUtils.yearsBetween(
					current, brithday)
					+ "岁"
					+ "  "
					+ "预产期： "
					+ list.get(position).getDuedate());
		}

		vh.textView_inquiryMessage_content.setText(list.get(position)
				.getTitle());
		String url = list.get(position).getGravidaheadphoto();
		if (TextUtils.isEmpty(url)) {
			vh.imageView_iquiryMessage_photo
					.setImageResource(R.drawable.headphoto);
		} else {
			bitmapUtils.display(vh.imageView_iquiryMessage_photo,
					Urls.GRAVIDAHEADPHOTO_URL + url);
		}
		return convertView;
	}

	public void reloadAdapter(List<InquiryMessageData> list, boolean isClear) {
		if (isClear) {
			this.list.clear();
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}

}
