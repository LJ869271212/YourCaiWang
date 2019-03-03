package com.my3w.farm.activity.plant;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.ShopLandActivity;
import com.my3w.farm.activity.shop.ShopToolsActivity;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.toast.Toasts;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 除草
 * 
 * @author Administrator
 *
 */
public class PlantToolsWeedActivity extends baseActivity {

	private String[] toolsList = new String[] { "播种", "补苗", "杀虫", "游戏", "收割" };

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	private LinearLayout back_button;
	private LinearLayout noit_button;
	private LinearLayout user_btton;

	private String IndexLandId;
	private String IndexLandNo;
	private String[] LandIdList;
	private String[] LandNoList;

	private String IndexToolsName;
	private String IndexToolsDilution;
	private String IndexToolsWater;
	private String[] ToolsIdList;
	private String[] ToolsNameList;
	private String[] ToolsDilutionList;
	private String[] ToolsWaterList;

	private TextView select_land;
	private TextView select_tools;
	private TextView tools_dilution;
	private TextView tools_water;

	private EditText add_num;
	private EditText add_water;
	private Button add_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools_weed);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("除草");

		title_username = (TextView) findViewById(R.id.title_username);
		String user_nickString = DataCache.get(PlantToolsWeedActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText(user_nickString);

		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);

		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		String user_picString = DataCache.get(PlantToolsWeedActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		back_button = (LinearLayout) findViewById(R.id.back_button);
		noit_button = (LinearLayout) findViewById(R.id.noit_button);
		user_btton = (LinearLayout) findViewById(R.id.user_btton);

		select_land = (TextView) findViewById(R.id.select_land);
		select_tools = (TextView) findViewById(R.id.select_tools);
		tools_dilution = (TextView) findViewById(R.id.tools_dilution);
		tools_water = (TextView) findViewById(R.id.tools_water);

		add_num = (EditText) findViewById(R.id.add_num);
		add_water = (EditText) findViewById(R.id.add_water);
		add_btn = (Button) findViewById(R.id.add_btn);

	}

	private void initData() {
		HttpRequest httpland = new HttpRequest(PlantToolsWeedActivity.this, SelectLandhandler);
		httpland.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=getland&token="
				+ DataCache.get(PlantToolsWeedActivity.this).getAsString("token"));

		HttpRequest httptools = new HttpRequest(PlantToolsWeedActivity.this, SelectToolshandler);
		httptools.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=getweed&token="
				+ DataCache.get(PlantToolsWeedActivity.this).getAsString("token"));
	}

	private void initEvent() {

		// 种植列表
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(PlantToolsWeedActivity.this).setTitle("点击选择操作")
						.setItems(toolsList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						if (which == 0) {
							intent = new Intent(PlantToolsWeedActivity.this, PlantToolsSowActivity.class);
						} else if (which == 1) {
							intent = new Intent(PlantToolsWeedActivity.this, PlantToolsBumiaoActivity.class);
						} else if (which == 2) {
							intent = new Intent(PlantToolsWeedActivity.this, PlantToolsInsecticidalActivity.class);
						} else if (which == 3) {
							intent = null;
						} else if (which == 4) {
							intent = new Intent(PlantToolsWeedActivity.this, PlantToolsHarvestActivity.class);
						}

						if (intent != null) {
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					}
				}).show();
			}
		});

		// 返回
		back_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlantToolsWeedActivity.this, PlantActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlantToolsWeedActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 选择土地
		select_land.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LandNoList != null && LandNoList.length > 0) {
					new AlertDialog.Builder(PlantToolsWeedActivity.this).setTitle("选择土地编号")
							.setItems(LandNoList, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexLandId = LandIdList[which];
							IndexLandNo = LandNoList[which];
							select_land.setText(IndexLandNo);
						}
					}).show();
				} else {
					Toasts.showToast(PlantToolsWeedActivity.this, "土地获取中，请稍后……");
				}
			}
		});

		// 选择杀虫剂
		select_tools.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ToolsNameList != null && ToolsNameList.length > 0) {
					new AlertDialog.Builder(PlantToolsWeedActivity.this).setTitle("选择杀虫剂")
							.setItems(ToolsNameList, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexToolsName = ToolsNameList[which];
							IndexToolsDilution = ToolsDilutionList[which];
							IndexToolsWater = ToolsWaterList[which];

							select_tools.setText(IndexToolsName);

							if ("0".equals(IndexToolsDilution)) {
								tools_dilution.setText("");
							} else {
								tools_dilution.setText("参考值：" + IndexToolsDilution + "ml/㎡");
							}

							if ("0".equals(IndexToolsWater)) {
								tools_water.setText("");
							} else {
								tools_water.setText("参考值：" + IndexToolsWater + "ml/g");
							}
						}
					}).show();
				} else {
					Toasts.showToast(PlantToolsWeedActivity.this, "杀虫剂获取中，请稍后……");
				}
			}
		});

		// 开始杀虫
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IndexLandId == null || "".equals(IndexLandId)) {
					Toast.makeText(PlantToolsWeedActivity.this, "请选择土地", Toast.LENGTH_SHORT).show();
				} else if (IndexToolsName == null || "".equals(IndexToolsName)) {
					Toast.makeText(PlantToolsWeedActivity.this, "请选择除草剂", Toast.LENGTH_SHORT).show();
				} else if ("".equals(add_num.getText().toString())) {
					Toast.makeText(PlantToolsWeedActivity.this, "请输入用量", Toast.LENGTH_SHORT).show();
				} else if ("".equals(add_water.getText().toString())) {
					Toast.makeText(PlantToolsWeedActivity.this, "请输入稀释水用量", Toast.LENGTH_SHORT).show();
				} else {
					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("land_bnum", IndexLandNo);
					headerList.put("ncount", add_num.getText().toString());
					headerList.put("add_water", add_water.getText().toString());
					headerList.put("tool_name", IndexToolsName);
					HttpRequest http = new HttpRequest(PlantToolsWeedActivity.this, headerList, Submithandler);
					http.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=weed&token="
							+ DataCache.get(PlantToolsWeedActivity.this).getAsString("token"));
				}
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
				Intent intent = new Intent(PlantToolsWeedActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});
	}

	// 选择土地
	@SuppressLint("HandlerLeak")
	private Handler SelectLandhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
						if (jsonArray.length() > 0) {
							LandIdList = new String[jsonArray.length()];
							LandNoList = new String[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObj = jsonArray.optJSONObject(i);
								LandIdList[i] = jsonObj.optString("id");
								LandNoList[i] = jsonObj.optString("no");
							}

							// 设置默认土地信息
							IndexLandId = LandIdList[0];
							IndexLandNo = LandNoList[0];
							select_land.setText(IndexLandNo);
						} else {
							Toast.makeText(PlantToolsWeedActivity.this, "还没有土地，快去买一块吧", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(PlantToolsWeedActivity.this, ShopLandActivity.class);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} else if (result == 1) {
						Toast.makeText(PlantToolsWeedActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsWeedActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsWeedActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// 选择除草剂
	@SuppressLint("HandlerLeak")
	private Handler SelectToolshandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
						if (jsonArray.length() > 0) {
							ToolsIdList = new String[jsonArray.length()];
							ToolsNameList = new String[jsonArray.length()];
							ToolsDilutionList = new String[jsonArray.length()];
							ToolsWaterList = new String[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObj = jsonArray.optJSONObject(i);
								ToolsIdList[i] = jsonObj.optString("goods_id");
								ToolsNameList[i] = jsonObj.optString("goods_name");
								ToolsDilutionList[i] = jsonObj.optString("dilution_value");
								ToolsWaterList[i] = jsonObj.optString("water_value");
							}

							// 设置默认杀虫剂
							IndexToolsName = ToolsNameList[0];
							IndexToolsDilution = ToolsDilutionList[0];
							IndexToolsWater = ToolsWaterList[0];

							select_tools.setText(IndexToolsName);
							if ("0".equals(IndexToolsDilution)) {
								tools_dilution.setText("");
							} else {
								tools_dilution.setText("参考值：" + IndexToolsDilution + "ml/㎡");
							}

							if ("0".equals(IndexToolsWater)) {
								tools_water.setText("");
							} else {
								tools_water.setText("参考值：" + IndexToolsWater + "ml/g");
							}
						}
					} else if (result == 10001) {
						Toast.makeText(PlantToolsWeedActivity.this, "还没有除草剂，快去买一点吧", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsWeedActivity.this, ShopToolsActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsWeedActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// 提交播种
	@SuppressLint("HandlerLeak")
	private Handler Submithandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						Toast.makeText(PlantToolsWeedActivity.this, "除草 申请成功，请等待工人施工", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsWeedActivity.this, PlantActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					} else if (result == 1) {
						Toast.makeText(PlantToolsWeedActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsWeedActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsWeedActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(PlantToolsWeedActivity.this, PlantActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
