package com.my3w.farm.activity.campaign;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.campaign.adapter.CampaignDynamicUserAdapter;
import com.my3w.farm.activity.campaign.entity.SignUpEntity;
import com.my3w.farm.activity.campaign.view.NoScrollListView;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CampaignDynamicInfoActivity extends baseActivity {

	private final int SEND_SMS_REQUEST = 0;

	private LoadingDialog dialog;

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private ScrollView scroll;
	private TextView huodong_title;
	private TextView huodong_date;
	private Button woyaobaoming;
	private Button fabuzuoping;
	private TextView yujirenshu;
	private TextView baomingrenshu;
	private TextView shengyurenshu;
	private TextView huodongfeiyong;
	private TextView chufadi;
	private TextView chufashijian;
	private TextView fanhuishijian;
	private TextView mudidi;
	private TextView jiaotongfangshi;
	private TextView baomingjiezhiriqi;
	private SquareImageView images;
	private TextView huodongjieshao;
	private TextView huodongguize;
	private TextView xignchenganpai;
	private Button cansaizuoping;

	private CampaignDynamicUserAdapter adapter;
	private ArrayList<SignUpEntity> data = new ArrayList<SignUpEntity>();
	private NoScrollListView baominguser;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic_info);

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
		title_name.setText("活动详情");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(CampaignDynamicInfoActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(CampaignDynamicInfoActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		scroll = (ScrollView) this.findViewById(R.id.scroll);
		huodong_title = (TextView) this.findViewById(R.id.huodong_title);
		huodong_date = (TextView) this.findViewById(R.id.huodong_date);
		woyaobaoming = (Button) this.findViewById(R.id.woyaobaoming);
		fabuzuoping = (Button) this.findViewById(R.id.fabuzuoping);
		yujirenshu = (TextView) this.findViewById(R.id.yujirenshu);
		baomingrenshu = (TextView) this.findViewById(R.id.baomingrenshu);
		shengyurenshu = (TextView) this.findViewById(R.id.shengyurenshu);
		huodongfeiyong = (TextView) this.findViewById(R.id.huodongfeiyong);
		chufadi = (TextView) this.findViewById(R.id.chufadi);
		chufashijian = (TextView) this.findViewById(R.id.chufashijian);
		fanhuishijian = (TextView) this.findViewById(R.id.fanhuishijian);
		mudidi = (TextView) this.findViewById(R.id.mudidi);
		jiaotongfangshi = (TextView) this.findViewById(R.id.jiaotongfangshi);
		baomingjiezhiriqi = (TextView) this.findViewById(R.id.baomingjiezhiriqi);
		images = (SquareImageView) this.findViewById(R.id.images);
		huodongjieshao = (TextView) this.findViewById(R.id.huodongjieshao);
		huodongguize = (TextView) this.findViewById(R.id.huodongguize);
		xignchenganpai = (TextView) this.findViewById(R.id.xignchenganpai);
		cansaizuoping = (Button) this.findViewById(R.id.cansaizuoping);

		baominguser = (NoScrollListView) this.findViewById(R.id.baominguser);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent reData) {
		if (requestCode == SEND_SMS_REQUEST) {
			if (resultCode == RESULT_OK) {
				if (data != null)
					data.clear();
				initData();
			}
		}
	}

	private void initData() {
		// 开始请求数据
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=info&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);
	}

	private void initEvent() {

		// 返回退出
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 我要报名
		woyaobaoming.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CampaignDynamicInfoActivity.this, CampaignDynamicAddActivity.class);
				intent.putExtra("id", id);
				startActivityForResult(intent, SEND_SMS_REQUEST);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 发布作品
		fabuzuoping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CampaignDynamicInfoActivity.this, CampaignDynamicInfoImageAddActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 查看作品
		cansaizuoping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CampaignDynamicInfoActivity.this, CampaignDynamicInfoImageActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
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

							ImageMagent.getInstance().displayImage(jsonObject.optString("big_img"), images);

							huodong_title.setText(jsonObject.optString("title"));
							huodong_date.setText(jsonObject.optString("add_time"));
							yujirenshu.setText("预计" + jsonObject.optInt("man_count") + "人");
							baomingrenshu.setText("报名" + jsonObject.optInt("registrationCount") + "人");
							shengyurenshu.setText("剩余" + (jsonObject.optInt("man_count") - jsonObject.optInt("registrationCount")) + "人");
							huodongfeiyong.setText("活动费用" + jsonObject.optString("price") + "人");
							chufadi.setText("出发地：" + jsonObject.optString("from_area"));
							chufashijian.setText("出发时间：" + jsonObject.optString("to_time"));
							fanhuishijian.setText("返回时间：" + jsonObject.optString("return_time"));
							mudidi.setText("目的地：" + jsonObject.optString("to_area"));
							jiaotongfangshi.setText("交通方式：" + jsonObject.optString("traffice"));
							baomingjiezhiriqi.setText("报名截止时间：" + jsonObject.optString("end_time"));
							huodongjieshao.setText(jsonObject.optString("introdution"));
							huodongguize.setText(jsonObject.optString("rule"));
							xignchenganpai.setText(jsonObject.optString("schedule"));

							JSONArray userArray = jsonObject.optJSONArray("registration");
							for (int i = 0; i < userArray.length(); i++) {
								JSONObject jsonObjects = userArray.optJSONObject(i);
								SignUpEntity userData = new SignUpEntity();
								userData.setName(jsonObjects.optString("regname"));
								userData.setNameicon(jsonObjects.optString("usericon"));
								int renshu = Integer.parseInt(jsonObjects.optString("bigman"))
										+ Integer.parseInt(jsonObjects.optString("childman"));
								userData.setRenshu(renshu);
								data.add(userData);
							}

							adapter = new CampaignDynamicUserAdapter(CampaignDynamicInfoActivity.this, data);
							baominguser.setAdapter(adapter);
							scroll.smoothScrollTo(0, 0);

						} else if (result == 1) {
							Toast.makeText(CampaignDynamicInfoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicInfoActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicInfoActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicInfoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

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
