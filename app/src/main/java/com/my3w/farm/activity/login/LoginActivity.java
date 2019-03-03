package com.my3w.farm.activity.login;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.entity.UserEntity;
import com.my3w.farm.activity.login.sqlite.UserSqlite;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.hash.Md5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 登录
 * 
 * @author Administrator
 *
 */
public class LoginActivity extends baseActivity {

	private EditText username;
	private EditText userpass;
	private CheckBox save_user;
	private Button submit;

	private TextView regist;
	private TextView reset;

	private HttpRequest http;

	private LoadingDialog dialog;

	private List<UserEntity> rows = new ArrayList<UserEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		initEvent();
	}

	private void initView() {
		username = (EditText) findViewById(R.id.username);
		userpass = (EditText) findViewById(R.id.userpass);
		save_user = (CheckBox) findViewById(R.id.save_user);
		submit = (Button) findViewById(R.id.submit);

		regist = (TextView) findViewById(R.id.regist);
		reset = (TextView) findViewById(R.id.reset);
	}

	private void initEvent() {

		// 开始登录
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.length() <= 0) {
					Toast.makeText(LoginActivity.this, "用户名不能为空，请输入用户名", Toast.LENGTH_LONG).show();
				} else if (userpass.length() <= 0) {
					Toast.makeText(LoginActivity.this, "密码不能为空，请输入密码", Toast.LENGTH_LONG).show();
				} else {
					if (dialog == null)
						dialog = new LoadingDialog(LoginActivity.this);
					dialog.show("登陆中，请稍候…");

					http = new HttpRequest(LoginActivity.this, handler);
					http.execute(getResources().getString(R.string.config_host_url) + "login.php?username=" + username.getText().toString()
							+ "&userpass=" + Md5.StringMd5(userpass.getText().toString()));
				}
			}
		});

		// 跳转到注册
		regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 忘记密码
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ResetActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		username.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Drawable drawableRight = username.getCompoundDrawables()[2];
					if (drawableRight != null && event.getRawX() >= (username.getRight() - drawableRight.getBounds().width())) {

						rows = UserSqlite.get(LoginActivity.this).getList();

						if (rows.size() > 0) {
							String[] name = new String[rows.size()];
							for (int i = 0; i < rows.size(); i++) {
								name[i] = rows.get(i).getUser();
							}

							new AlertDialog.Builder(LoginActivity.this).setTitle("选择保存的账户")
									.setItems(name, new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									username.setText(rows.get(which).getUser());
									userpass.setText(rows.get(which).getPass());
								}
							}).show();
						}

						return true;
					}
					break;
				}
				return false;
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (dialog != null)
				dialog.cancel();

			if (msg.what == 200) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					int status = jsonObject.getInt("errCode");
					if (status == 0) {
						DataCache.get(LoginActivity.this).put("username", username.getText().toString());
						DataCache.get(LoginActivity.this).put("userpass", userpass.getText().toString());
						DataCache.get(LoginActivity.this).put("token", jsonObject.getString("token"));

						DataCache.get(LoginActivity.this).put("user_nick", jsonObject.getString("name"));
						DataCache.get(LoginActivity.this).put("user_id", jsonObject.getString("id"));
						DataCache.get(LoginActivity.this).put("user_sex", jsonObject.getString("sex"));
						DataCache.get(LoginActivity.this).put("user_age", jsonObject.getString("age"));
						DataCache.get(LoginActivity.this).put("user_tel", jsonObject.getString("tel"));
						DataCache.get(LoginActivity.this).put("user_pic", jsonObject.getString("pic"));
						DataCache.get(LoginActivity.this).put("user_level", jsonObject.getString("level"));

						// 登录成功保存用户和密码
						if (save_user.isChecked()) {
							if (UserSqlite.get(LoginActivity.this).getCount(username.getText().toString(),
									userpass.getText().toString()) <= 0) {
								UserSqlite.get(LoginActivity.this).insert(username.getText().toString(), userpass.getText().toString());
							}
						}

						Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else if (status == 10001) {
						Toast.makeText(LoginActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(LoginActivity.this, "服务器出现了问题，请稍后访问", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Toast.makeText(LoginActivity.this, "数据出现了点小问题，请重试", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(LoginActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}