package com.my3w.farm.activity.campaign.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.campaign.entity.ImageInfoListEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.SquareImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CampaignDynamicImageAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<ImageInfoListEntity> data;

	public CampaignDynamicImageAdapter(Context context, ArrayList<ImageInfoListEntity> data) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ImageInfoListEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_campaign_dynamic_info_image_content_view, null);
				holder.list_image = (SquareImageView) convertView.findViewById(R.id.list_image);
				holder.list_title = (TextView) convertView.findViewById(R.id.list_title);
				holder.list_user = (TextView) convertView.findViewById(R.id.list_user);
				holder.list_pinglun = (TextView) convertView.findViewById(R.id.list_pinglun);
				holder.list_toupiao = (TextView) convertView.findViewById(R.id.list_toupiao);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageMagent.getInstance().displayImage(getData.getAll_pic(), holder.list_image);

			holder.list_title.setText(getData.getTitle());

			holder.list_user.setText("参赛者：" + getData.getUsername());

			holder.list_pinglun.setText(getData.getComcount());

			holder.list_toupiao.setText(getData.getToucount());
		}
		return convertView;
	}

	public class ViewHolder {
		private SquareImageView list_image;
		private TextView list_title;
		private TextView list_user;
		private TextView list_pinglun;
		private TextView list_toupiao;
	}
}
