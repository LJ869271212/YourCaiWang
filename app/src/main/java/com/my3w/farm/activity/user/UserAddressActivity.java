package com.my3w.farm.activity.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.user.adapter.addressAdapter;
import com.my3w.farm.activity.user.entity.AddressEntity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.ListExtView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserAddressActivity extends baseActivity {

	private TextView title_name;
	private TextView title_username;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	private ArrayList<AddressEntity> list = new ArrayList<AddressEntity>();
	private addressAdapter addressadapter;
	private ListExtView listview;

	private LinearLayout back_button;
	private LinearLayout noit_button;
	private LinearLayout user_btton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_address);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("地址管理");

		title_username = (TextView) findViewById(R.id.title_username);
		String user_nickString = DataCache.get(UserAddressActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);

		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		String user_picString = DataCache.get(UserAddressActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		listview = (ListExtView) findViewById(R.id.listview);
		listview.setShowHeaderView(false);
		listview.setShowFooterView(false);
		addressadapter = new addressAdapter(this, list);
		listview.setAdapter(addressadapter);

		back_button = (LinearLayout) findViewById(R.id.back_button);
		noit_button = (LinearLayout) findViewById(R.id.noit_button);
		user_btton = (LinearLayout) findViewById(R.id.user_btton);

	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "user.php?action=addresslist&token="
				+ DataCache.get(this).getAsString("token"));
	}

	private void initEvent() {

		// 返回
		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserAddressActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserAddressActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 消息
		noit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		// 个人中心
		user_btton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserAddressActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});
	}

	// 获取区域handler
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {

				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
						if (result == 0) {
							if (jsonArray.length() > 0) {
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject addressObject = jsonArray.getJSONObject(i);
									AddressEntity data = new AddressEntity();
									data.setId(addressObject.optInt("id"));
									data.setAddress(addressObject.optString("address"));
									data.setCity(addressObject.optString("city"));
									data.setCode(addressObject.optString("code"));
									data.setUserName(addressObject.optString("uname"));
									data.setPhone(addressObject.optString("phone"));
									data.setProvince(addressObject.optString("province"));
									data.setCounty(addressObject.optString("county"));
									data.setUserchecked(addressObject.optInt("userchecked"));
									list.add(data);
								}

								if (addressadapter != null)
									addressadapter.notifyDataSetChanged();
							}
						} else if (result == 1) {
							Toast.makeText(UserAddressActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(UserAddressActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(UserAddressActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(UserAddressActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(UserAddressActivity.this, UserCenterActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
