package com.my3w.farm.activity.user;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.ListExtView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 购物订单信息
 * 
 * @author Administrator
 *
 */
public class UserGoodsOrderActivity extends baseActivity {

	private TextView title_name;
	private TextView title_username;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	private ListExtView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_goods_order);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("购物订单");

		title_username = (TextView) findViewById(R.id.title_username);
		String user_nickString = DataCache.get(UserGoodsOrderActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);

		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		String user_picString = DataCache.get(UserGoodsOrderActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		listview = (ListExtView) findViewById(R.id.listview);
		listview.setShowHeaderView(false);
		listview.setShowFooterView(false);

	}

	private void initData() {
		
	}

	private void initEvent() {

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
