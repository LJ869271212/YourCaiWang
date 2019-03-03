package com.my3w.farm.activity.shop.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.shop.entity.SelectListLand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class LandAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private ArrayList<SelectListLand> landList;

	public LandAdapter(Context context, ArrayList<SelectListLand> landList) {
		this.mInflater = LayoutInflater.from(context);
		this.landList = landList;
	}

	@Override
	public int getCount() {
		return landList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SelectListLand data = landList.get(position);
		if (data != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_shop_land_list_content_view, null);
				holder.land_item = (LinearLayout) convertView.findViewById(R.id.land_item);
				holder.digital = (TextView) convertView.findViewById(R.id.digital);
				holder.acreage = (TextView) convertView.findViewById(R.id.acreage);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.land_item.setBackgroundResource(data.getBackgroud());
			holder.digital.setText(data.getDigital());
			holder.acreage.setText(data.getAcreage() + "㎡");
		}
		return convertView;
	}

	public class ViewHolder {
		public LinearLayout land_item;
		public TextView digital;
		public TextView acreage;
	}

}
