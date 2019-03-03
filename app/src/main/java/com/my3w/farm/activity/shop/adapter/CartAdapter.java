package com.my3w.farm.activity.shop.adapter;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.shop.entity.CartEntity;
import com.my3w.farm.activity.shop.sqlite.DBManager;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.view.image.SquareImageView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CartAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private ArrayList<CartEntity> data;

	private TextView countPrice;

	private TextView countNumber;

	private Context context;

	public CartAdapter(Context context, ArrayList<CartEntity> data, TextView countPrice, TextView countNumber) {
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
		this.countPrice = countPrice;
		this.countNumber = countNumber;
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
		final CartEntity getData = data.get(position);
		if (getData != null) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_shop_cart_content_view, null);
				holder.info_background = (LinearLayout) convertView.findViewById(R.id.info_background);
				holder.info_delete = (ImageView) convertView.findViewById(R.id.info_delete);
				holder.info_land = (LinearLayout) convertView.findViewById(R.id.info_land);
				holder.info_digital = (TextView) convertView.findViewById(R.id.info_digital);
				holder.info_acreage = (TextView) convertView.findViewById(R.id.info_acreage);
				holder.info_image = (SquareImageView) convertView.findViewById(R.id.info_image);
				holder.info_title = (TextView) convertView.findViewById(R.id.info_title);
				holder.info_content = (TextView) convertView.findViewById(R.id.info_content);
				holder.info_price = (TextView) convertView.findViewById(R.id.info_price);
				holder.info_number = (TextView) convertView.findViewById(R.id.info_number);
				holder.info_number_left = (Button) convertView.findViewById(R.id.info_number_left);
				holder.info_number_right = (Button) convertView.findViewById(R.id.info_number_right);
				holder.info_number_txt = (TextView) convertView.findViewById(R.id.info_number_txt);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.info_background.setBackgroundColor(Color.parseColor(getData.getBackground()));

			if (getData.getDataname().equals("LandCart")) {
				holder.info_land.setVisibility(View.VISIBLE);
				holder.info_image.setVisibility(View.GONE);

				holder.info_digital.setText(getData.getDigital());
				holder.info_acreage.setText(getData.getArg() + "㎡");
				holder.info_title.setText("土地编号：" + getData.getTitle());
				holder.info_number_txt.setText(getData.getNumber() + "年");
			} else {
				holder.info_land.setVisibility(View.GONE);
				holder.info_image.setVisibility(View.VISIBLE);
				holder.info_number_txt.setText(getData.getNumber() + getData.getUnit());

				holder.info_title.setText(getData.getTitle());
				ImageMagent.getInstance().displayImage(getData.getPic(), holder.info_image);
			}
			holder.info_content.setText(getData.getContent());
			holder.info_price.setText("￥" + getData.getPrice());

			holder.info_number.setText(getData.getNumber());
			holder.info_number_left.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int number = Integer.parseInt(getData.getNumber());
					if (number > 1) {
						try {
							ContentValues contentValues = new ContentValues();
							contentValues.put("number", (number - 1));
							DBManager sqlite = new DBManager();
							sqlite.open(context);
							if (sqlite.udpate(getData.getDataname(), new String[] { "id" },
									new String[] { String.valueOf(getData.getId()) }, contentValues)) {
								getData.setNumber(String.valueOf(number - 1));
								notifyDataSetChanged();
							}
							sqlite.close();
							sqlite = null;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

			});
			holder.info_number_right.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					int number = Integer.parseInt(getData.getNumber());
					try {
						ContentValues contentValues = new ContentValues();
						contentValues.put("number", (number + 1));
						DBManager sqlite = new DBManager();
						sqlite.open(context);
						if (sqlite.udpate(getData.getDataname(), new String[] { "id" }, new String[] { String.valueOf(getData.getId()) },
								contentValues)) {
							getData.setNumber(String.valueOf(number + 1));
							notifyDataSetChanged();
						}
						sqlite.close();
						sqlite = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			});

			// 删除
			holder.info_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					try {
						DBManager sqlite = new DBManager();
						sqlite.open(context);
						long delteCount = sqlite.delete(getData.getDataname(), "id", Integer.parseInt(getData.getId()));
						if (delteCount > 0) {
							data.remove(position);
							notifyDataSetChanged();
						}
						sqlite.close();
						sqlite = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		}
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

		float CartPrice = 0;
		for (int i = 0; i < data.size(); i++) {
			CartPrice += (Float.parseFloat(data.get(i).getPrice()) * Integer.parseInt(data.get(i).getNumber()));
		}
		countPrice.setText("￥" + String.valueOf(CartPrice));
		countNumber.setText("(" + String.valueOf(data.size()) + ")");
	}

	public class ViewHolder {
		private LinearLayout info_background;
		private ImageView info_delete;
		public LinearLayout info_land;
		public TextView info_digital;
		public TextView info_acreage;
		public SquareImageView info_image;
		public TextView info_title;
		public TextView info_content;
		public TextView info_price;
		public TextView info_number;
		public Button info_number_left;
		public Button info_number_right;
		public TextView info_number_txt;
	}

}
