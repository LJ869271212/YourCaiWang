package com.my3w.farm.activity.game.borrow.jiecaixuqiu;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.image.SquareImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 借菜需求详情
 * 
 * @author Administrator
 *
 */
public class BorrowJieCaiXuQiuInfoActivity extends baseActivity {

	private LoadingDialog dialog;

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private String id;

	private SquareImageView info_image;
	private TextView info_user;
	private TextView info_title;
	private TextView info_count;
	private TextView info_price;
	private TextView info_yuyue_data;
	private TextView info_yuyue_info;
	private TextView info_huancai_data;
	private TextView info_tel;
	private TextView info_address;
	private TextView info_zipcode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_1_borrow_info_3);

		id = getIntent().getStringExtra("id");

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		// 请求loading
		if (dialog == null)
			dialog = new LoadingDialog(this);
		dialog.show("数据加载中，请稍候…");

		title_back = (ImageView) this.findViewById(R.id.title_back);
		title_back.setImageResource(R.drawable.r);
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("借菜需求详情");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(BorrowJieCaiXuQiuInfoActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(BorrowJieCaiXuQiuInfoActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		info_image = (SquareImageView) this.findViewById(R.id.info_image);
		info_user = (TextView) this.findViewById(R.id.info_user);
		info_title = (TextView) this.findViewById(R.id.info_title);
		info_count = (TextView) this.findViewById(R.id.info_count);
		info_price = (TextView) this.findViewById(R.id.info_price);
		info_yuyue_data = (TextView) this.findViewById(R.id.info_yuyue_data);
		info_yuyue_info = (TextView) this.findViewById(R.id.info_yuyue_info);
		info_huancai_data = (TextView) this.findViewById(R.id.info_huancai_data);
		info_tel = (TextView) this.findViewById(R.id.info_tel);
		info_address = (TextView) this.findViewById(R.id.info_address);
		info_zipcode = (TextView) this.findViewById(R.id.info_zipcode);

	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "borrow.php?action=info&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);
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
						if (result == 0) {
							ImageMagent.getInstance().displayImage(jsonObject.optString("thumb_pic"), info_image);
							info_user.setText(jsonObject.optString("fromuser"));
							info_title.setText(jsonObject.optString("borrow_name"));
							info_count.setText(jsonObject.optString("borrow_num") + jsonObject.optString("borrow_unit"));
							info_price.setText(jsonObject.optString("borrow_price") + "元");
							info_yuyue_data.setText(jsonObject.optString("br_date"));
							info_tel.setText(jsonObject.optString("phone"));
							info_address.setText(jsonObject.optString("address"));
							info_zipcode.setText(jsonObject.optString("cade"));
							info_yuyue_info.setText(jsonObject.optString("description"));
							info_huancai_data.setText(jsonObject.optString("re_date"));

						} else if (result == 1) {
							Toast.makeText(BorrowJieCaiXuQiuInfoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(BorrowJieCaiXuQiuInfoActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(BorrowJieCaiXuQiuInfoActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(BorrowJieCaiXuQiuInfoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

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
