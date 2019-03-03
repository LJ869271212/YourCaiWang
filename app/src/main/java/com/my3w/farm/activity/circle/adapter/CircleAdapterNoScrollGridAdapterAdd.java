package com.my3w.farm.activity.circle.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.circle.entity.CircleListImageListEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.SquareImageView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CircleAdapterNoScrollGridAdapterAdd extends BaseAdapter {

	private ArrayList<CircleListImageListEntity> data;
	private LayoutInflater mInflater;

	public CircleAdapterNoScrollGridAdapterAdd(Context context, ArrayList<CircleListImageListEntity> data) {
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
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
		final CircleListImageListEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_circle_list_content_view_image_list_view, null);
				holder.image = (SquareImageView) convertView.findViewById(R.id.image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageMagent.getInstance().displayImage(getData.getImg_180(), holder.image);
		}
		return convertView;
	}

	public class ViewHolder {
		private SquareImageView image;
	}

}
