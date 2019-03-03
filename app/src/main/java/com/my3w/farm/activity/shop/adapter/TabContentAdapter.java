package com.my3w.farm.activity.shop.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.shop.entity.SelectListIner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TabContentAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private ArrayList<SelectListIner> selectListIner;

	public TabContentAdapter(Context context, ArrayList<SelectListIner> selectListIner) {
		this.mInflater = LayoutInflater.from(context);
		this.selectListIner = selectListIner;
	}

	@Override
	public int getCount() {
		return selectListIner.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SelectListIner data = selectListIner.get(position);
		Log.e("zzzz", "bbbbb::" + data.toString());
		if (data != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_shop_land_tab_content_view, null);
				holder.contents = (TextView) convertView.findViewById(R.id.contents);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (null != holder.contents)
				holder.contents.setText(data.getNo());
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView contents;
	}

}
