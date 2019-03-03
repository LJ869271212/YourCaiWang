package com.my3w.farm.activity.campaign;

import java.util.HashMap;

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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CampaignDynamicAddActivity extends baseActivity {

	private LoadingDialog dialog;

	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private TextView dynamicTitle;
	private TextView dynamicDate;

	private EditText namicName;
	private EditText namicIhpone;
	private EditText namicBig;
	private EditText namicMini;
	private EditText namicBiezhu;
	private Button add_btn;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic_add);

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

		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("我要报名");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(CampaignDynamicAddActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(CampaignDynamicAddActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		dynamicTitle = (TextView) this.findViewById(R.id.dynamicTitle);
		dynamicDate = (TextView) this.findViewById(R.id.dynamicDate);

		namicName = (EditText) this.findViewById(R.id.namicName);
		namicIhpone = (EditText) this.findViewById(R.id.namicIhpone);
		namicBig = (EditText) this.findViewById(R.id.namicBig);
		namicMini = (EditText) this.findViewById(R.id.namicMini);
		namicBiezhu = (EditText) this.findViewById(R.id.namicBiezhu);
		add_btn = (Button) this.findViewById(R.id.add_btn);
	}

	private void initData() {
		// 开始请求数据
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=info&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);
	}

	private void initEvent() {

		// 发布
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String namicNames = namicName.getText().toString();
				String namicIhpones = namicIhpone.getText().toString();
				String namicBigs = namicBig.getText().toString();
				String namicMinis = namicMini.getText().toString();
				String namicBiezhus = namicBiezhu.getText().toString();

				if ("".equals(namicNames) || "".equals(namicIhpones) || "".equals(namicBigs)) {
					Toast.makeText(CampaignDynamicAddActivity.this, "内容填写不正确，请检查内容是否填写成功", Toast.LENGTH_SHORT).show();
				} else {
					if (dialog == null)
						dialog = new LoadingDialog(CampaignDynamicAddActivity.this);
					dialog.show("报名提交中，请稍候…");

					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("regname", namicNames);
					headerList.put("party_id", id);
					headerList.put("photel", namicIhpones);
					headerList.put("remarks", namicBiezhus);
					headerList.put("bigman", namicBigs);
					headerList.put("childman", namicMinis);
					HttpRequest http = new HttpRequest(CampaignDynamicAddActivity.this, headerList, Posthandler);
					http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=registration&token="
							+ DataCache.get(CampaignDynamicAddActivity.this).getAsString("token"));
				}

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
							dynamicTitle.setText("活动名称：" + jsonObject.optString("title"));
							dynamicDate.setText("活动时间：" + jsonObject.optString("add_time"));
						} else if (result == 1) {
							Toast.makeText(CampaignDynamicAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicAddActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicAddActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler Posthandler = new Handler() {
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
							Toast.makeText(CampaignDynamicAddActivity.this, "报名成功！", Toast.LENGTH_SHORT).show();
							setResult(RESULT_OK);
							finish();
						} else if (result == 1) {
							Toast.makeText(CampaignDynamicAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicAddActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicAddActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

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
