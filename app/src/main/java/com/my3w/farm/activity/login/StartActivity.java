package com.my3w.farm.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.hash.Md5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 启动
 * 
 * @author Administrator
 *
 */
public class StartActivity extends baseActivity {

	private HttpRequest http;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_star);

		new StarRun().start();
	}

	private class StarRun extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}

			String username = DataCache.get(StartActivity.this).getAsString("username");
			String userpass = DataCache.get(StartActivity.this).getAsString("userpass");
			if (username != null && userpass != null) {
				http = new HttpRequest(StartActivity.this, handler);
				http.execute(getResources().getString(R.string.config_host_url) + "login.php?username=" + username + "&userpass="
						+ Md5.StringMd5(userpass));
			} else {
				Message msg = new Message();
				msg.what = 10000;
				handler.sendMessage(msg);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					int status = jsonObject.getInt("errCode");
					if (status == 0) {
						String token = jsonObject.getString("token");
						DataCache.get(StartActivity.this).put("token", token);

						DataCache.get(StartActivity.this).put("user_nick", jsonObject.getString("name"));
						DataCache.get(StartActivity.this).put("user_id", jsonObject.getString("id"));
						DataCache.get(StartActivity.this).put("user_sex", jsonObject.getString("sex"));
						DataCache.get(StartActivity.this).put("user_age", jsonObject.getString("age"));
						DataCache.get(StartActivity.this).put("user_tel", jsonObject.getString("tel"));
						DataCache.get(StartActivity.this).put("user_pic", jsonObject.getString("pic"));
						DataCache.get(StartActivity.this).put("user_level", jsonObject.getString("level"));

						Intent intent = new Intent(StartActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else {
						Login();
					}
				} catch (JSONException e) {
					Login();
				}
			} else {
				Login();
			}
			super.handleMessage(msg);
		}
	};

	private void Login() {
		Intent intent = new Intent(StartActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
	}
}
