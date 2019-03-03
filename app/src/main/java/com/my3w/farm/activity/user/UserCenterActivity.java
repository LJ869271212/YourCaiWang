package com.my3w.farm.activity.user;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.circle.CircleActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.ShopCartActivity;
import com.my3w.farm.activity.shop.ShopLandActivity;
import com.my3w.farm.activity.shop.ShopSeedActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.view.image.RoundImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 个人中心
 * 
 * @author Administrator
 *
 */
public class UserCenterActivity extends baseActivity {

	private ImageView tools_btn;

	private RoundImageView title_icon;
	private TextView title_name;
	private TextView title_fen;
	private TextView title_username;

	private LinearLayout xiangce_btn;
	private LinearLayout shipin_btn;

	private LinearLayout gouwuche_btn;
	private LinearLayout tudi_btn;
	private LinearLayout zhongzi_btn;
	private LinearLayout caimiao_btn;

	private LinearLayout shoucang_btn;
	private LinearLayout dingdan_btn;
	private LinearLayout zhongzhi_dingdan_btn;
	private LinearLayout shouge_dingdan_btn;
	private LinearLayout dizhi_btn;

	private TextView back_btn;
	private TextView c_btn;
	private TextView f_btn;
	private TextView n_btn;

	private Button exit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);

		initView();
		initEvent();
	}

	private void initView() {
		String user_picString = DataCache.get(this).getAsString("user_pic");
		String user_nickString = DataCache.get(this).getAsString("user_nick");

		tools_btn = (ImageView) findViewById(R.id.tools_btn);

		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_name = (TextView) findViewById(R.id.title_name);
		if (user_nickString != null && !user_nickString.equals(""))
			title_name.setText(user_nickString);
		else
			title_name.setText(user_nickString);

		title_fen = (TextView) findViewById(R.id.title_fen);
		title_fen.setText("友菜积分: 0");

		title_username = (TextView) findViewById(R.id.title_username);
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText("友菜账号: " + user_nickString);
		else
			title_username.setText("友菜账号: " + user_nickString);

		xiangce_btn = (LinearLayout) findViewById(R.id.xiangce_btn);
		shipin_btn = (LinearLayout) findViewById(R.id.shipin_btn);

		gouwuche_btn = (LinearLayout) findViewById(R.id.gouwuche_btn);
		tudi_btn = (LinearLayout) findViewById(R.id.tudi_btn);
		zhongzi_btn = (LinearLayout) findViewById(R.id.zhongzi_btn);
		caimiao_btn = (LinearLayout) findViewById(R.id.caimiao_btn);

		shoucang_btn = (LinearLayout) findViewById(R.id.shoucang_btn);
		dingdan_btn = (LinearLayout) findViewById(R.id.dingdan_btn);
		zhongzhi_dingdan_btn = (LinearLayout) findViewById(R.id.zhongzhi_dingdan_btn);
		shouge_dingdan_btn = (LinearLayout) findViewById(R.id.shouge_dingdan_btn);
		dizhi_btn = (LinearLayout) findViewById(R.id.dizhi_btn);

		back_btn = (TextView) findViewById(R.id.back_btn);
		c_btn = (TextView) findViewById(R.id.c_btn);
		f_btn = (TextView) findViewById(R.id.f_btn);
		n_btn = (TextView) findViewById(R.id.n_btn);

		exit_btn = (Button) findViewById(R.id.exit_btn);

	}

	private void initEvent() {

		// 返回
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 设置
		tools_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 相册
		xiangce_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 视频
		shipin_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 购物车
		gouwuche_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, ShopCartActivity.class);
				intent.putExtra("action", "com.my3w.farm.activity.user.UserCenterActivity");
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 土地
		tudi_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, ShopLandActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 种子
		zhongzi_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, ShopSeedActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 菜苗
		caimiao_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, ShopSeedActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 收藏
		shoucang_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 购物订单
		dingdan_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, UserGoodsOrderActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 种植订单
		zhongzhi_dingdan_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 收割订单
		shouge_dingdan_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 地址
		dizhi_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, UserAddressActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 友菜圈
		c_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, CircleActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 好友
		f_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 消息
		n_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, UserMessageActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 退出
		exit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserCenterActivity.this, LoginActivity.class);
				startActivity(intent);
				sendBroadcast(new Intent("finish"));
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(UserCenterActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
