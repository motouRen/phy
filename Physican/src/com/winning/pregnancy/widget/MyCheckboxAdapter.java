package com.winning.pregnancy.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.arvin.physican.R;
import com.winning.pregnancy.model.Node;

/**
 * 自定义listview布局显示内容
 * 
 * @author zjj
 */
public class MyCheckboxAdapter extends BaseAdapter {
	private Context con;
	private LayoutInflater mInflater;
	private List<Node> list = new ArrayList<Node>();
	private List<Node> allsCache = new ArrayList<Node>();
	private MyCheckboxAdapter oThis = this;

	public MyCheckboxAdapter(Context context, List<Node> list) {
		super();
		this.con = context;
		this.mInflater = LayoutInflater.from(con);
		this.list = list;
		this.allsCache = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		CommViewHolder holder = null;
		if (convertView == null) {

			holder = new CommViewHolder();
			convertView = mInflater.inflate(R.layout.assess_item, null);
			holder.comm_list_checkbox_dialog_item_chk = (CheckBox) convertView
					.findViewById(R.id.list_tag);
			holder.comm_list_checkbox_dialog_item_chk
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Node n = (Node) v.getTag();
							checkNode(n, ((CheckBox) v).isChecked());
							if (((CheckBox) v).isChecked()) {
								v.setBackgroundColor(Color.GREEN);
							} else {
								v.setBackgroundColor(Color
										.parseColor("#AAAAAA"));
							}
							oThis.notifyDataSetChanged();
						}
					});
			convertView.setTag(holder);

		}

		else {
			holder = (CommViewHolder) convertView.getTag();

		}

		// 得到当前节点
		Node n = list.get(position);
		if (n != null) {
			if (n.isChecked()) {
				holder.comm_list_checkbox_dialog_item_chk
						.setBackgroundColor(Color.GREEN);
			} else {
				holder.comm_list_checkbox_dialog_item_chk
						.setBackgroundColor(Color.parseColor("#AAAAAA"));
			}
			holder.comm_list_checkbox_dialog_item_chk.setTag(n);
			holder.comm_list_checkbox_dialog_item_chk.setText(n.getText());
			holder.comm_list_checkbox_dialog_item_chk.setChecked(n.isChecked());
		}

		return convertView;
	}

	static class CommViewHolder {

		CheckBox comm_list_checkbox_dialog_item_chk;

	}

	// 复选框联动
	private void checkNode(Node node, boolean isChecked) {
		node.setChecked(isChecked);
	}

	/**
	 * 获得选中节点
	 * 
	 * @return
	 */
	public List<Node> getSeletedNodes() {
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < allsCache.size(); i++) {
			Node n = allsCache.get(i);
			if (n.isChecked()) {
				nodes.add(n);
			}
		}
		return nodes;
	}

}
