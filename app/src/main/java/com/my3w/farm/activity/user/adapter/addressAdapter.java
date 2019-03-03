package com.my3w.farm.activity.user.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.user.entity.AddressEntity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class addressAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private ArrayList<AddressEntity> list;

	public addressAdapter(Context context, ArrayList<AddressEntity> list) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		AddressEntity data = list.get(position);
		if (data != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_user_address_list_content_view, null);
				holder.address_name = (TextView) convertView.findViewById(R.id.address_name);
				holder.address_index = (TextView) convertView.findViewById(R.id.address_index);
				holder.address_edit = (TextView) convertView.findViewById(R.id.address_edit);
				holder.address_delete = (TextView) convertView.findViewById(R.id.address_delete);
				holder.address_content = (TextView) convertView.findViewById(R.id.address_content);
				holder.address_phone = (TextView) convertView.findViewById(R.id.address_phone);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.address_name.setText(data.getUserName());
			if (data.getUserchecked() == 1) {
				holder.address_index.setVisibility(View.VISIBLE);
			} else {
				holder.address_index.setVisibility(View.GONE);
			}
			holder.address_content.setText(data.getProvince() + data.getCity()  + data.getCounty() + data.getAddress());

			holder.address_phone.setText("手机：" + data.getPhone());

			// 修改
			holder.address_edit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});

			// 删除
			holder.address_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

				}
			});
		}
		return convertView;
	}

	public class ViewHolder {
		public TextView address_name;
		public TextView address_index;
		public TextView address_edit;
		public TextView address_delete;
		public TextView address_content;
		public TextView address_phone;
	}

}
