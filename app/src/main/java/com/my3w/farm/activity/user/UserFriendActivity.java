package com.my3w.farm.activity.user;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UserFriendActivity extends baseActivity {

	private LoadingDialog dialog;

	private ImageView title_back;
	private TextView title_name;
	private ImageView title_other;

	private RoundImageView title_icon;
	private TextView title_names;
	private TextView title_username;

	private TextView qianming;
	@SuppressWarnings("unused")
	private LinearLayout imageList;
	private TextView shoujihao;
	private TextView xingbie;
	private TextView diqu;

	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_friend);

		uid = this.getIntent().getStringExtra("uid");

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		// 请求loading
		if (dialog == null)
			dialog = new LoadingDialog(this);
		dialog.show("数据加载中，请稍候…");

		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("");
		title_other = (ImageView) findViewById(R.id.title_other);
		title_other.setVisibility(View.GONE);

		// 头像
		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		// 名称
		title_names = (TextView) findViewById(R.id.title_names);
		// 帐号
		title_username = (TextView) findViewById(R.id.title_username);

		qianming = (TextView) findViewById(R.id.qianming);
		imageList = (LinearLayout) findViewById(R.id.imageList);
		shoujihao = (TextView) findViewById(R.id.shoujihao);
		xingbie = (TextView) findViewById(R.id.xingbie);
		diqu = (TextView) findViewById(R.id.diqu);

	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "user.php?action=friend&token="
				+ DataCache.get(this).getAsString("token") + "&uid=" + uid);
	}

	private void initEvent() {

		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

	}

	// 获取区域handler
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				if (dialog != null)
					dialog.cancel();

				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						JSONObject jsonRet = jsonObject.optJSONObject("errMsg");
						if (result == 0) {
							ImageMagent.getInstance().displayImage(jsonRet.optString("icon"), title_icon);
							title_name.setText(jsonRet.optString("nick") + "资料");
							title_names.setText(jsonRet.optString("nick"));
							title_username.setText("友菜账号: " + jsonRet.optString("nick"));
							qianming.setText(jsonRet.optString("signature"));
							// imageList = (LinearLayout) findViewById(R.id.imageList);
							shoujihao.setText(jsonRet.optString("phone"));
							xingbie.setText(jsonRet.optString("sex"));
							diqu.setText(jsonRet.optString("diqu"));

						} else if (result == 1) {
							Toast.makeText(UserFriendActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(UserFriendActivity.this, UserCenterActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(UserFriendActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(UserFriendActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
