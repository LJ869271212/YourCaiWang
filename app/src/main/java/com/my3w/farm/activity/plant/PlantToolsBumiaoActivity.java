package com.my3w.farm.activity.plant;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.ShopLandActivity;
import com.my3w.farm.activity.shop.ShopSeedActivity;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.toast.Toasts;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 补苗
 * 
 * @author Administrator
 *
 */
public class PlantToolsBumiaoActivity extends baseActivity {

	private String[] toolsList = new String[] { "播种", "杀虫", "除草", "游戏", "收割" };

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	private LinearLayout back_button;
	private LinearLayout noit_button;
	private LinearLayout user_btton;

	private LinearLayout select_unit;
	private String[] unitList = new String[] { "颗", "棵", "株", "斤", "克", "千克" };
	private String unitText = "颗";

	private String IndexLandId;
	private String IndexLandNo;
	private String[] LandIdList;
	private String[] LandNoList;

	private String IndexSeedsId;
	private String IndexSeedsName;
	private String[] SeedsIdList;
	private String[] SeedsNameList;

	private TextView select_land;
	private TextView select_seed;
	private EditText seeds_number;
	private TextView goods_unit;
	private TextView seeds_date;
	private EditText seeds_content;
	private Button submit_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tools_bumiao);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("补苗");

		title_username = (TextView) findViewById(R.id.title_username);
		String user_nickString = DataCache.get(PlantToolsBumiaoActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText(user_nickString);

		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);

		title_icon = (RoundImageView) findViewById(R.id.title_icon);
		String user_picString = DataCache.get(PlantToolsBumiaoActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		back_button = (LinearLayout) findViewById(R.id.back_button);
		noit_button = (LinearLayout) findViewById(R.id.noit_button);
		user_btton = (LinearLayout) findViewById(R.id.user_btton);

		select_land = (TextView) findViewById(R.id.select_land);
		select_seed = (TextView) findViewById(R.id.select_seed);
		seeds_number = (EditText) findViewById(R.id.seeds_number);
		select_unit = (LinearLayout) findViewById(R.id.select_unit);
		goods_unit = (TextView) findViewById(R.id.goods_unit);
		seeds_date = (TextView) findViewById(R.id.seeds_date);
		seeds_content = (EditText) findViewById(R.id.seeds_content);
		submit_button = (Button) findViewById(R.id.submit_button);

	}

	private void initData() {
		HttpRequest http = new HttpRequest(PlantToolsBumiaoActivity.this, SelectLandhandler);
		http.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=getland&token="
				+ DataCache.get(PlantToolsBumiaoActivity.this).getAsString("token"));
	}

	private void initEvent() {

		// 种植列表
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(PlantToolsBumiaoActivity.this).setTitle("点击选择操作")
						.setItems(toolsList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						if (which == 0) {
							intent = new Intent(PlantToolsBumiaoActivity.this, PlantToolsSowActivity.class);
						} else if (which == 1) {
							intent = new Intent(PlantToolsBumiaoActivity.this, PlantToolsInsecticidalActivity.class);
						} else if (which == 2) {
							intent = new Intent(PlantToolsBumiaoActivity.this, PlantToolsWeedActivity.class);
						} else if (which == 3) {
							intent = null;
						} else if (which == 4) {
							intent = new Intent(PlantToolsBumiaoActivity.this, PlantToolsHarvestActivity.class);
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
				Intent intent = new Intent(PlantToolsBumiaoActivity.this, PlantActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PlantToolsBumiaoActivity.this, UserCenterActivity.class);
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
					new AlertDialog.Builder(PlantToolsBumiaoActivity.this).setTitle("选择土地编号")
							.setItems(LandNoList, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexLandId = LandIdList[which];
							IndexLandNo = LandNoList[which];
							select_land.setText(IndexLandNo);

							// TODO 获取种子
							getSeeds(IndexLandId);
						}
					}).show();
				} else {
					Toasts.showToast(PlantToolsBumiaoActivity.this, "土地获取中，请稍后……");
				}
			}
		});

		// 选择种子
		select_seed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SeedsNameList != null && SeedsNameList.length > 0) {
					new AlertDialog.Builder(PlantToolsBumiaoActivity.this).setTitle("选择种子")
							.setItems(SeedsNameList, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexSeedsId = SeedsIdList[which];
							IndexSeedsName = SeedsNameList[which];
							select_seed.setText(IndexSeedsName);
						}
					}).show();
				} else {
					Toasts.showToast(PlantToolsBumiaoActivity.this, "种子获取中，请稍后……");
				}
			}
		});

		// 选择单位
		select_unit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(PlantToolsBumiaoActivity.this).setTitle("选择单位")
						.setItems(unitList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						unitText = unitList[which];
						goods_unit.setText(unitText);
					}
				}).show();
			}
		});

		// 选择日期
		seeds_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int year = 0;
				int month = 0;
				int day = 0;
				if (seeds_date.getText().toString().equals("")) {
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					Date date = new Date();
					calendar.setTime(date);
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				} else {
					String getDate = seeds_date.getText().toString();
					String[] DateList = getDate.split("-");
					year = Integer.parseInt(DateList[0]);
					month = Integer.parseInt(DateList[1]) - 1;
					day = Integer.parseInt(DateList[2]);
				}
				new DatePickerDialog(PlantToolsBumiaoActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						seeds_date.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth));
					}
				}, year, month, day).show();
			}
		});

		// 提交
		submit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (IndexLandId == null || "".equals(IndexLandId)) {
					Toast.makeText(PlantToolsBumiaoActivity.this, "请选择土地", Toast.LENGTH_SHORT).show();
				} else if (IndexSeedsId == null || "".equals(IndexSeedsId)) {
					Toast.makeText(PlantToolsBumiaoActivity.this, "请选择种子", Toast.LENGTH_SHORT).show();
				} else if ("".equals(seeds_number.getText().toString())) {
					Toast.makeText(PlantToolsBumiaoActivity.this, "请输入补苗数量", Toast.LENGTH_SHORT).show();
				} else if ("".equals(seeds_date.getText().toString())) {
					Toast.makeText(PlantToolsBumiaoActivity.this, "请选择种植日期", Toast.LENGTH_SHORT).show();
				} else {
					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("seed_id", IndexSeedsId);
					headerList.put("content", seeds_content.getText().toString());
					headerList.put("seed_count", seeds_number.getText().toString());
					headerList.put("seed_date", seeds_date.getText().toString());
					headerList.put("land_id", IndexLandId);
					headerList.put("land_unit", unitText);
					HttpRequest http = new HttpRequest(PlantToolsBumiaoActivity.this, headerList, Submithandler);
					http.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=bumiao&token="
							+ DataCache.get(PlantToolsBumiaoActivity.this).getAsString("token"));
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
				Intent intent = new Intent(PlantToolsBumiaoActivity.this, UserCenterActivity.class);
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

							// TODO 获取种子
							getSeeds(IndexLandId);
						} else {
							Toast.makeText(PlantToolsBumiaoActivity.this, "还没有土地，快去买一块吧", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(PlantToolsBumiaoActivity.this, ShopLandActivity.class);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} else if (result == 1) {
						Toast.makeText(PlantToolsBumiaoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsBumiaoActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsBumiaoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(PlantToolsBumiaoActivity.this, "补苗申请成功，请等待工人施工", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsBumiaoActivity.this, PlantActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					} else if (result == 1) {
						Toast.makeText(PlantToolsBumiaoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsBumiaoActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsBumiaoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// 获取种子
	private void getSeeds(String id) {
		HttpRequest http = new HttpRequest(PlantToolsBumiaoActivity.this, SelectSeedshandler);
		http.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=getseed&token="
				+ DataCache.get(PlantToolsBumiaoActivity.this).getAsString("token") + "&goods_id=" + id);
		Log.e("xxx", getResources().getString(R.string.config_host_url) + "tools.php?action=getseed&token="
				+ DataCache.get(PlantToolsBumiaoActivity.this).getAsString("token") + "&goods_id=" + id);
	}

	// 选择种子
	@SuppressLint("HandlerLeak")
	private Handler SelectSeedshandler = new Handler() {
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
							SeedsIdList = new String[jsonArray.length()];
							SeedsNameList = new String[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObj = jsonArray.optJSONObject(i);
								SeedsIdList[i] = jsonObj.optString("goods_id");
								SeedsNameList[i] = jsonObj.optString("goods_name");
							}

							// 设置默认土地信息
							IndexSeedsId = SeedsIdList[0];
							IndexSeedsName = SeedsNameList[0];
							select_seed.setText(IndexSeedsName);
						}
					} else if (result == 1) {
						Toast.makeText(PlantToolsBumiaoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsBumiaoActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else if (result == 10001) {
						Toast.makeText(PlantToolsBumiaoActivity.this, "还没有种子呢，快去买点吧", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(PlantToolsBumiaoActivity.this, ShopSeedActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PlantToolsBumiaoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(PlantToolsBumiaoActivity.this, PlantActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
