package onsiteservice;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import utils.DateUtils;
import utils.NetworkUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bean.DoneDetail;

import com.arvin.physican.R;
import com.lidroid.xutils.BitmapUtils;

import config.Urls;
import customview.RoundImageView;

public class DoneAdapter extends BaseAdapter {

	private List<DoneDetail> list;
	private Context mContext;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;

	public DoneAdapter(List<DoneDetail> list, Context mContext) {
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

		private RoundImageView imageView_onsite_photo;
		private TextView textView_onsite_name;
		private TextView textView_onsite_date;
		private TextView textView_onsite_content;
		private TextView textView_onsite_showLocation;
		private ImageView imageView_onsite_status;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_onsite, parent, false);
			vh.imageView_onsite_photo = (RoundImageView) convertView
					.findViewById(R.id.imageView_onsite_photo);
			vh.textView_onsite_name = (TextView) convertView
					.findViewById(R.id.textView_onsite_name);
			vh.textView_onsite_date = (TextView) convertView
					.findViewById(R.id.textView_onsite_date);
			vh.imageView_onsite_status = (ImageView) convertView
					.findViewById(R.id.imageView_onsite_status);

			vh.textView_onsite_content = (TextView) convertView
					.findViewById(R.id.textView_onsite_content);
			vh.textView_onsite_showLocation = (TextView) convertView
					.findViewById(R.id.textView_onsite_showLocation);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.imageView_onsite_status.setVisibility(View.INVISIBLE);
		vh.textView_onsite_name.setText(list.get(position).getGravidaName());
		vh.textView_onsite_content.setText(list.get(0).getTitle());
		String brithday = list.get(position).getBirthday();
		String locationString = list.get(position).getDestination();
		try {
			JSONObject jsonObject = new JSONObject(locationString);
			String address = jsonObject.getString("address");
			vh.textView_onsite_showLocation.setText(address);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (TextUtils.isEmpty(brithday)) {
			vh.textView_onsite_date.setText("--岁" + "  " + "预产期： "
					+ list.get(position).getDueDate());
		} else {
			String current = DateUtils.getCurrentDate();
			vh.textView_onsite_date.setText(DateUtils.yearsBetween(current,
					brithday)
					+ "岁"
					+ "  "
					+ "预产期： "
					+ list.get(position).getDueDate());

		}

		String url = list.get(position).getGravidaHeadPhoto();
		if (TextUtils.isEmpty(url)) {
			vh.imageView_onsite_photo.setImageResource(R.drawable.headphoto);
		} else {
			bitmapUtils.display(vh.imageView_onsite_photo,
					Urls.GRAVIDAHEADPHOTO_URL + url);
		}
		return convertView;
	}

	public void reloadAdapter(List<DoneDetail> list, boolean isClear) {
		if (isClear) {
			this.list.clear();
		}
		this.list.addAll(list);
		notifyDataSetChanged();
	}
}
