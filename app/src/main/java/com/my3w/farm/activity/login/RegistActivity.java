package com.my3w.farm.activity.login;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.hash.Md5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册
 * 
 * @author Administrator
 *
 */
public class RegistActivity extends baseActivity {

	private ImageView title_back;
	private TextView title_name;
	private ImageView title_other;

	private EditText username;
	private EditText usercode;
	private Button usercodebutton;
	private EditText name;
	private EditText userpass;
	private Button submit;

	private LoadingDialog dialog;

	private HttpRequest HttpCode;

	private final int CodeClickTime = 60;

	private CountDownTimer countTime;

	private HttpRequest HttpRegist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		initView();
		initEvent();
	}

	private void initView() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("新用户注册");
		title_other = (ImageView) findViewById(R.id.title_other);
		title_other.setVisibility(View.GONE);

		username = (EditText) findViewById(R.id.username);
		usercode = (EditText) findViewById(R.id.usercode);
		usercodebutton = (Button) findViewById(R.id.usercodebutton);
		userpass = (EditText) findViewById(R.id.userpass);
		name = (EditText) findViewById(R.id.name);
		submit = (Button) findViewById(R.id.submit);
	}

	private void initEvent() {

		// 返回到注册界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 开始注册
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.length() <= 0) {
					Toast.makeText(RegistActivity.this, "手机号码不能为空，请重新输入", Toast.LENGTH_LONG).show();
				} else if (usercode.length() <= 0) {
					Toast.makeText(RegistActivity.this, "验证码不能为空，请输入验证码", Toast.LENGTH_LONG).show();
				} else if (name.length() <= 0) {
					Toast.makeText(RegistActivity.this, "昵称不能为空，请输入昵称", Toast.LENGTH_LONG).show();
				} else if (userpass.length() <= 0) {
					Toast.makeText(RegistActivity.this, "密码不能为空，请输入密码", Toast.LENGTH_LONG).show();
				} else {
					if (dialog == null)
						dialog = new LoadingDialog(RegistActivity.this);
					dialog.show("注册中，请稍候…");

					HttpRegist = new HttpRequest(RegistActivity.this, handlerRegist);
					HttpRegist.execute(getResources().getString(R.string.config_host_url) + "registered.php?username="
							+ username.getText().toString() + "&userpass=" + Md5.StringMd5(userpass.getText().toString()) + "&code="
							+ usercode.getText().toString() + "&name=" + name.getText().toString());
				}
			}
		});

		// 发送验证码
		usercodebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (username.length() <= 0) {
					Toast.makeText(RegistActivity.this, "手机号码不能为空，请重新输入", Toast.LENGTH_LONG).show();
				} else {
					if (dialog == null)
						dialog = new LoadingDialog(RegistActivity.this);
					dialog.show("验证码发送中，请稍候…");

					HttpCode = new HttpRequest(RegistActivity.this, handlerCode);
					HttpCode.execute(getResources().getString(R.string.config_host_url) + "verification.php?username="
							+ username.getText().toString());

					countTime = new CountDownTimer(1000 * CodeClickTime, 1000) {

						@Override
						public void onTick(long millisUntilFinished) {
							usercodebutton.setText(String.valueOf(millisUntilFinished / 1000) + "秒后重发");
							usercodebutton.setBackgroundColor(Color.parseColor("#ffeeeeee"));
							usercodebutton.setClickable(false);
						}

						@Override
						public void onFinish() {
							TimeCancel();
						}
					};
					countTime.start();
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler handlerCode = new Handler() {
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
						Toast.makeText(RegistActivity.this, "验证码发送成功，请查注意您的短信", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(RegistActivity.this, "服务器出现了问题，请稍后访问", Toast.LENGTH_SHORT).show();
						TimeCancel();
					}
				} catch (JSONException e) {
					Toast.makeText(RegistActivity.this, "数据出现了点小问题，请重试", Toast.LENGTH_SHORT).show();
					TimeCancel();
				}
			} else {
				Toast.makeText(RegistActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
				TimeCancel();
			}
			super.handleMessage(msg);
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handlerRegist = new Handler() {
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
						String token = jsonObject.getString("token");
						DataCache.get(RegistActivity.this).put("username", username.getText().toString());
						DataCache.get(RegistActivity.this).put("userpass", userpass.getText().toString());
						DataCache.get(RegistActivity.this).put("token", token);

						DataCache.get(RegistActivity.this).put("user_nick", jsonObject.getString("name"));
						DataCache.get(RegistActivity.this).put("user_id", jsonObject.getString("id"));
						DataCache.get(RegistActivity.this).put("user_sex", jsonObject.getString("sex"));
						DataCache.get(RegistActivity.this).put("user_age", jsonObject.getString("age"));
						DataCache.get(RegistActivity.this).put("user_tel", jsonObject.getString("tel"));
						DataCache.get(RegistActivity.this).put("user_pic", jsonObject.getString("pic"));
						DataCache.get(RegistActivity.this).put("user_level", jsonObject.getString("level"));

						Intent intent = new Intent(RegistActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else if (status == 10001) {
						Toast.makeText(RegistActivity.this, "您已经注册过了，请直接登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					} else if (status == 10003) {
						Toast.makeText(RegistActivity.this, "输入验证码不正确", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(RegistActivity.this, "服务器出现了问题，请稍后访问", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Toast.makeText(RegistActivity.this, "数据出现了点小问题，请重试", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(RegistActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
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

	private void TimeCancel() {
		if (countTime != null)
			countTime.cancel();
		usercodebutton.setText("重新发送");
		usercodebutton.setBackgroundColor(Color.parseColor("#ff58bf42"));
		usercodebutton.setClickable(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
