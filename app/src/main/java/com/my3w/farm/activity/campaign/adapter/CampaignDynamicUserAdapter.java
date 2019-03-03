package com.my3w.farm.activity.campaign.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.campaign.entity.SignUpEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CampaignDynamicUserAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<SignUpEntity> data;

	public CampaignDynamicUserAdapter(Context context, ArrayList<SignUpEntity> data) {
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
		final SignUpEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_campaign_dynmic_user_list, null);
				holder.icon = (RoundImageView) convertView.findViewById(R.id.icon);
				holder.username = (TextView) convertView.findViewById(R.id.username);
				holder.usercount = (TextView) convertView.findViewById(R.id.usercount);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageMagent.getInstance().displayImage(getData.getNameicon(), holder.icon);

			holder.username.setText(getData.getName());

			holder.usercount.setText(String.valueOf(getData.getRenshu()) + "人");
		}
		return convertView;
	}

	public class ViewHolder {
		private RoundImageView icon;
		private TextView username;
		private TextView usercount;
	}

}
