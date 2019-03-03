package com.my3w.farm.activity.game.borrow.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.game.borrow.entity.BorrowInfoEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.core.CoreImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BorrowAdapter extends BaseAdapter {
	private LayoutInflater mInflater;

	private ArrayList<BorrowInfoEntity> data;

	public BorrowAdapter(Context context, ArrayList<BorrowInfoEntity> data) {
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

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final BorrowInfoEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_game_1_borrow_list, null);
				holder.images = (CoreImageView) convertView.findViewById(R.id.images);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.content = (TextView) convertView.findViewById(R.id.content);
				holder.data = (TextView) convertView.findViewById(R.id.data);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageMagent.getInstance().displayImage(getData.getThumb_pic(), holder.images);

			holder.title.setText(getData.getTitle());

			holder.content.setText(Html.fromHtml(getData.getDescription()).toString());

			holder.data.setText("发布日期：" + getData.getAddtime());
		}
		return convertView;
	}

	public class ViewHolder {
		private CoreImageView images;
		private TextView title;
		private TextView content;
		private TextView data;
	}

}