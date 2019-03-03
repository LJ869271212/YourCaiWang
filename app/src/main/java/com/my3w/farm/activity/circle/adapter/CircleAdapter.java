package com.my3w.farm.activity.circle.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.circle.entity.CircleListCommentListEntity;
import com.my3w.farm.activity.circle.entity.CircleListEntity;
import com.my3w.farm.activity.circle.entity.CircleListImageListEntity;
import com.my3w.farm.activity.circle.view.NoScrollGridView;
import com.my3w.farm.activity.circle.view.NoScrollListView;
import com.my3w.farm.activity.user.UserFriendActivity;
import com.my3w.farm.comment.image.ImageActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CircleAdapter extends BaseAdapter {

	private Context context;

	private LayoutInflater mInflater;

	private ArrayList<CircleListEntity> data;

	private onclickItemListener listener;

	public interface onclickItemListener {
		public void clickDItem(int position);

		public void clickItem(int position, int index);

		public void clickUser(int uid);
	}

	public CircleAdapter(Context context, ArrayList<CircleListEntity> data) {
		this.context = context;
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
		final CircleListEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_circle_list_content_view, null);
				holder.user_icon = (RoundImageView) convertView.findViewById(R.id.user_icon);
				holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
				holder.circle_content = (TextView) convertView.findViewById(R.id.circle_content);
				holder.repay_button = (TextView) convertView.findViewById(R.id.repay_button);
				holder.imagelist = (NoScrollGridView) convertView.findViewById(R.id.imagelist);
				holder.commentlist = (NoScrollListView) convertView.findViewById(R.id.commentlist);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ImageMagent.getInstance().displayImage(getData.getUsericon(), holder.user_icon);

			holder.user_icon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserFriendActivity.class);
					intent.putExtra("uid", String.valueOf(getData.getUid()));
					context.startActivity(intent);
					((Activity) context).overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
				}
			});

			holder.user_name.setText(getData.getUsername());

			holder.circle_content.setText(getData.getContent());

			final ArrayList<CircleListImageListEntity> imagelist = getData.getImagelist();
			if (null == imagelist || imagelist.size() == 0) {
				holder.imagelist.setVisibility(View.GONE);
			} else {
				holder.imagelist.setVisibility(View.VISIBLE);
				holder.imagelist.setAdapter(new CircleAdapterNoScrollGridAdapter(context, imagelist));
				holder.imagelist.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(context, ImageActivity.class);
						intent.putExtra("path", imagelist.get(position).getImg_un());
						context.startActivity(intent);
					}
				});
			}

			ArrayList<CircleListCommentListEntity> commentlist = getData.getCommentlist();
			if (null == commentlist || commentlist.size() == 0) {
				holder.commentlist.setVisibility(View.GONE);
			} else {
				CircleAdapterNoScrollListAdapter adapter = new CircleAdapterNoScrollListAdapter(context, commentlist);
				adapter.setOnClickItemListener(new CircleAdapterNoScrollListAdapter.onclickItemListener() {

					@Override
					public void clickItem(int index) {
						if (listener != null)
							listener.clickItem(position, index);
					}

					@Override
					public void clickUser(int uid) {
						if (listener != null)
							listener.clickUser(uid);
					}
				});
				holder.commentlist.setVisibility(View.VISIBLE);
				holder.commentlist.setAdapter(adapter);
			}

			holder.repay_button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (listener != null)
						listener.clickDItem(position);
				}
			});
		}
		return convertView;
	}

	public void setOnClickItemListener(onclickItemListener listener) {
		this.listener = listener;
	}

	public class ViewHolder {
		private RoundImageView user_icon;
		private TextView user_name;
		private TextView circle_content;
		private TextView repay_button;
		public NoScrollGridView imagelist;
		public NoScrollListView commentlist;
	}

}
