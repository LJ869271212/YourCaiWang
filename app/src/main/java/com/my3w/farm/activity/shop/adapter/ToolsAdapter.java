package com.my3w.farm.activity.shop.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.shop.entity.SeedEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.SquareImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ToolsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<SeedEntity> listSeed;

	public ToolsAdapter(Context context, ArrayList<SeedEntity> listSeed) {
		this.mInflater = LayoutInflater.from(context);
		this.listSeed = listSeed;
	}

	@Override
	public int getCount() {
		return listSeed.size();
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
		SeedEntity data = listSeed.get(position);
		if (data != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_shop_tools_list_content_view, null);
				holder.content_image = (SquareImageView) convertView.findViewById(R.id.content_image);
				holder.content_title = (TextView) convertView.findViewById(R.id.content_title);
				holder.content_price = (TextView) convertView.findViewById(R.id.content_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (data.getThumb() != null && !data.getThumb().equals(""))
				ImageMagent.getInstance().displayImage(data.getThumb(), holder.content_image);

			holder.content_title.setText(data.getTitle());
			holder.content_price.setText("￥" + data.getPrice());
		}
		return convertView;
	}

	public class ViewHolder {
		public SquareImageView content_image;
		public TextView content_title;
		public TextView content_price;
	}

}
