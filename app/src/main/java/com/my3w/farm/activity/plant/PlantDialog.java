package com.my3w.farm.activity.plant;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.toast.Toasts;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.progress.RoundProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlantDialog extends Dialog {

	public static int WATERING_FLAG = 1;

	public static int FILL_FLAG = 2;

	public static int APPLY_FLAG = 3;

	private Context context;

	private OnSuccessListener listener;

	private String keyon;

	private ImageView back;

	private TextView user_nick;

	private RoundImageView user_pic;

	// 湿度，浇水
	private LinearLayout shidu_tab;
	private LinearLayout shidu;
	private LinearLayout watering_content;
	private RoundProgressBar kongqishidu;
	private TextView kongqishidu_txt;
	private RoundProgressBar turangshidu;
	private TextView turangshidu_txt;
	private RoundProgressBar eryanhuatan;
	private TextView eryanhuatan_txt;
	private TextView jiaoshuilist;
	private EditText water_count;
	private Button watering_btn;

	private int watering_para = 1;

	// 光照，补光
	private LinearLayout guangzhao_tab;
	private LinearLayout guangzhao;
	private LinearLayout fill_content;
	private RoundProgressBar guangzhaodu;
	private TextView guangzhaodu_txt;
	private EditText fill_count;
	private Button fill_btn;

	// 温度
	private LinearLayout wendu_tab;
	private LinearLayout wendu;
	private LinearLayout heating_content;
	private RoundProgressBar kongqiwendu;
	private TextView kongqiwendu_txt;
	private RoundProgressBar turangwendu;
	private TextView turangwendu_txt;
	private RoundProgressBar eryanhuatan3;
	private TextView eryanhuatan3_txt;
	private EditText heating_count;
	private Button heating_btn;

	// ph值，施肥
	private LinearLayout ph_tab;
	private LinearLayout ph;
	private LinearLayout apply_content;
	private RoundProgressBar phzhi;
	private TextView phzhi_txt;
	private TextView shifei_cankao;
	private TextView xishi_cankao;
	private TextView xishilist;
	private LinearLayout xishilist3;
	private TextView shifeilist;
	private TextView seedlist;
	private EditText apply_count;
	private EditText apply_xishi_count;
	private Button apply_btn;

	private String[] FeiList;
	private String[] DilutionList;
	private String[] WaterList;
	private String[] SeedList;
	private String IndexXishi = "填埋";
	private String IndexFeiName;
	private String IndexFeiDilution;
	private String IndexFeiWater;
	private String IndexSeedName;

	private TextView sow;
	private TextView bumiao;
	private TextView insecticidal;
	private TextView weed;
	private TextView game;
	private TextView harvest;

	public PlantDialog(Context context, OnSuccessListener listener, String keyon) {
		super(context, R.style.dialog_fullscreen);
		this.context = context;
		this.listener = listener;
		this.keyon = keyon;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plant_dialog);

		initView();
		initEvent();
	}

	private void initView() {

		back = (ImageView) this.findViewById(R.id.back);

		user_nick = (TextView) findViewById(R.id.user_nick);
		String user_nickString = DataCache.get(context).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			user_nick.setText(user_nickString);
		else
			user_nick.setText(user_nickString);

		user_pic = (RoundImageView) findViewById(R.id.user_pic);
		String user_picString = DataCache.get(context).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, user_pic);

		// 浇水选项
		jiaoshuilist = (TextView) this.findViewById(R.id.jiaoshuilist);

		// 施肥选项
		shifeilist = (TextView) this.findViewById(R.id.shifeilist);

		// 种子列表
		seedlist = (TextView) this.findViewById(R.id.seedlist);

		// 施肥方式列表
		xishilist = (TextView) this.findViewById(R.id.xishilist);

		shidu_tab = (LinearLayout) this.findViewById(R.id.shidu_tab);
		shidu = (LinearLayout) this.findViewById(R.id.shidu);
		watering_content = (LinearLayout) this.findViewById(R.id.watering_content);
		kongqishidu = (RoundProgressBar) this.findViewById(R.id.kongqishidu);
		kongqishidu.setRotation(-90);
		kongqishidu_txt = (TextView) this.findViewById(R.id.kongqishidu_txt);
		turangshidu = (RoundProgressBar) this.findViewById(R.id.turangshidu);
		turangshidu.setRotation(-90);
		turangshidu_txt = (TextView) this.findViewById(R.id.turangshidu_txt);
		eryanhuatan = (RoundProgressBar) this.findViewById(R.id.eryanhuatan);
		eryanhuatan.setRotation(-90);
		eryanhuatan_txt = (TextView) this.findViewById(R.id.eryanhuatan_txt);
		water_count = (EditText) this.findViewById(R.id.water_count);
		watering_btn = (Button) this.findViewById(R.id.watering_btn);

		guangzhao_tab = (LinearLayout) this.findViewById(R.id.guangzhao_tab);
		guangzhao = (LinearLayout) this.findViewById(R.id.guangzhao);
		fill_content = (LinearLayout) this.findViewById(R.id.fill_content);
		guangzhaodu = (RoundProgressBar) this.findViewById(R.id.guangzhaodu);
		guangzhaodu.setRotation(-90);
		guangzhaodu.setMax(65535);
		guangzhaodu_txt = (TextView) this.findViewById(R.id.guangzhaodu_txt);
		fill_count = (EditText) this.findViewById(R.id.fill_count);
		fill_btn = (Button) this.findViewById(R.id.fill_btn);

		wendu_tab = (LinearLayout) this.findViewById(R.id.wendu_tab);
		wendu = (LinearLayout) this.findViewById(R.id.wendu);
		heating_content = (LinearLayout) this.findViewById(R.id.heating_content);
		kongqiwendu = (RoundProgressBar) this.findViewById(R.id.kongqiwendu);
		kongqiwendu.setRotation(-90);
		kongqiwendu_txt = (TextView) this.findViewById(R.id.kongqiwendu_txt);
		turangwendu = (RoundProgressBar) this.findViewById(R.id.turangwendu);
		turangwendu.setRotation(-90);
		turangwendu_txt = (TextView) this.findViewById(R.id.turangwendu_txt);
		eryanhuatan3 = (RoundProgressBar) this.findViewById(R.id.eryanhuatan3);
		eryanhuatan3.setRotation(-90);
		eryanhuatan3_txt = (TextView) this.findViewById(R.id.eryanhuatan3_txt);
		heating_count = (EditText) this.findViewById(R.id.heating_count);
		heating_btn = (Button) this.findViewById(R.id.heating_btn);

		ph_tab = (LinearLayout) this.findViewById(R.id.ph_tab);
		ph = (LinearLayout) this.findViewById(R.id.ph);
		apply_content = (LinearLayout) this.findViewById(R.id.apply_content);
		phzhi = (RoundProgressBar) this.findViewById(R.id.phzhi);
		phzhi.setRotation(-90);
		phzhi_txt = (TextView) this.findViewById(R.id.phzhi_txt);
		shifei_cankao = (TextView) this.findViewById(R.id.shifei_cankao);
		xishi_cankao = (TextView) this.findViewById(R.id.xishi_cankao);
		xishilist3 = (LinearLayout) this.findViewById(R.id.xishilist3);
		apply_count = (EditText) this.findViewById(R.id.apply_count);
		apply_xishi_count = (EditText) this.findViewById(R.id.apply_xishi_count);
		apply_btn = (Button) this.findViewById(R.id.apply_btn);

		sow = (TextView) this.findViewById(R.id.sow);
		bumiao = (TextView) this.findViewById(R.id.bumiao);
		insecticidal = (TextView) this.findViewById(R.id.insecticidal);
		weed = (TextView) this.findViewById(R.id.weed);
		game = (TextView) this.findViewById(R.id.game);
		harvest = (TextView) this.findViewById(R.id.harvest);

		// 获取土地信息
		HttpRequest http = new HttpRequest(context, controlApiHandler);
		http.execute(context.getResources().getString(R.string.config_host_url) + "control.php?action=sensor&token="
				+ DataCache.get(context).getAsString("token") + "&keynum=" + keyon);
		
		Log.e("xxx", context.getResources().getString(R.string.config_host_url) + "control.php?action=sensor&token="
				+ DataCache.get(context).getAsString("token") + "&keynum=" + keyon);

		// 获取肥料
		HttpRequest httpfei = new HttpRequest(context, toolsApiHandler);
		httpfei.execute(context.getResources().getString(R.string.config_host_url) + "tools.php?action=getfei&token="
				+ DataCache.get(context).getAsString("token"));

		// 获取种子
		HttpRequest httpseed = new HttpRequest(context, seedApiHandler);
		httpseed.execute(context.getResources().getString(R.string.config_host_url) + "control.php?action=getseed&token="
				+ DataCache.get(context).getAsString("token") + "&keynum=" + keyon);
	}

	private void initEvent() {
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		// 湿度标签
		shidu_tab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shidu.setVisibility(View.VISIBLE);
				watering_content.setVisibility(View.VISIBLE);
				guangzhao.setVisibility(View.GONE);
				fill_content.setVisibility(View.GONE);
				wendu.setVisibility(View.GONE);
				heating_content.setVisibility(View.GONE);
				ph.setVisibility(View.GONE);
				apply_content.setVisibility(View.GONE);
			}
		});

		// 光照标签
		guangzhao_tab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shidu.setVisibility(View.GONE);
				watering_content.setVisibility(View.GONE);
				guangzhao.setVisibility(View.VISIBLE);
				fill_content.setVisibility(View.VISIBLE);
				wendu.setVisibility(View.GONE);
				heating_content.setVisibility(View.GONE);
				ph.setVisibility(View.GONE);
				apply_content.setVisibility(View.GONE);
			}
		});

		// 加温
		wendu_tab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shidu.setVisibility(View.GONE);
				watering_content.setVisibility(View.GONE);
				guangzhao.setVisibility(View.GONE);
				fill_content.setVisibility(View.GONE);
				wendu.setVisibility(View.VISIBLE);
				heating_content.setVisibility(View.VISIBLE);
				ph.setVisibility(View.GONE);
				apply_content.setVisibility(View.GONE);
			}
		});

		// 光照标签
		ph_tab.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				shidu.setVisibility(View.GONE);
				watering_content.setVisibility(View.GONE);
				guangzhao.setVisibility(View.GONE);
				fill_content.setVisibility(View.GONE);
				wendu.setVisibility(View.GONE);
				heating_content.setVisibility(View.GONE);
				ph.setVisibility(View.VISIBLE);
				apply_content.setVisibility(View.VISIBLE);
			}
		});

		// 选择碰洒模式
		jiaoshuilist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] mItems = context.getResources().getStringArray(R.array.spinnername);
				new AlertDialog.Builder(context).setTitle("点击选择喷洒模式").setItems(mItems, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							watering_para = 1;
						} else {
							watering_para = 2;
						}
						jiaoshuilist.setText(mItems[which]);
					}
				}).show();
			}
		});

		// 选择肥料
		shifeilist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FeiList != null && FeiList.length > 0) {
					new AlertDialog.Builder(context).setTitle("点击选择肥料").setItems(FeiList, new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexFeiName = FeiList[which];
							IndexFeiDilution = DilutionList[which];
							IndexFeiWater = WaterList[which];
							shifeilist.setText(FeiList[which]);
							shifei_cankao.setText(DilutionList[which] + "g/㎡");
							xishi_cankao.setText(WaterList[which] + "ml/㎡");
						}
					}).show();
				} else {
					Toast.makeText(context, "还没有肥料，快去买一点吧", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 选择种子
		seedlist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SeedList != null && SeedList.length > 0) {
					new AlertDialog.Builder(context).setTitle("点击选择种子").setItems(SeedList, new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							IndexSeedName = SeedList[which];
							seedlist.setText(SeedList[which]);
						}
					}).show();
				} else {
					Toast.makeText(context, "土地还没有种任何种子，快去种点吧", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 选择稀释类型
		xishilist.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String[] mItems = context.getResources().getStringArray(R.array.spinnerxishi);
				new AlertDialog.Builder(context).setTitle("点击选择施肥方式").setItems(mItems, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						IndexXishi = mItems[which];
						xishilist.setText(mItems[which]);
						if (which == 2) {
							xishilist3.setVisibility(View.VISIBLE);
						} else {
							xishilist3.setVisibility(View.INVISIBLE);
						}
					}
				}).show();
			}
		});

		// 浇水
		watering_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String water_count_num = water_count.getText().toString();
				if (!water_count_num.equals("")) {
					int teltime = 0;

					// 判断浇水
					if (Integer.parseInt(water_count_num) >= 6) {
						teltime = Integer.parseInt(water_count_num);
					} else {
						Toasts.showToast(context, "浇水时间不能少于6秒");
						return;
					}

					HttpRequest http = new HttpRequest(context, wateringApiHandler);
					http.execute(context.getResources().getString(R.string.config_host_url) + "control.php?action=water&token="
							+ DataCache.get(context).getAsString("token") + "&keynum=" + keyon + "&para=" + watering_para + "&water_count="
							+ String.valueOf(teltime));
					dismiss();
				} else {
					Toasts.showToast(context, "请填写浇水时长");
				}
			}
		});

		// 补光
		fill_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!fill_count.getText().toString().equals("")) {
					int teltime = Integer.parseInt(fill_count.getText().toString()) * 60;
					HttpRequest http = new HttpRequest(context, fillApiHandler);
					http.execute(context.getResources().getString(R.string.config_host_url) + "control.php?action=light&token="
							+ DataCache.get(context).getAsString("token") + "&keynum=" + keyon + "&add_light=" + String.valueOf(teltime));
					dismiss();
				} else {
					Toasts.showToast(context, "请填写补光时长");
				}
			}
		});

		// 加温
		heating_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				heating_count.getText().toString();
			}
		});

		// 施肥
		apply_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HashMap<String, String> headerList = new HashMap<String, String>();
				headerList.put("land_bnum", keyon);
				headerList.put("ncount", apply_count.getText().toString());
				headerList.put("add_water", apply_xishi_count.getText().toString());
				headerList.put("tool_name", IndexFeiName);
				headerList.put("fangshi", IndexXishi);
				headerList.put("seed", IndexSeedName);
				HttpRequest http = new HttpRequest(context, headerList, Submithandler);
				http.execute(context.getResources().getString(R.string.config_host_url) + "tools.php?action=shifei&token="
						+ DataCache.get(context).getAsString("token"));
				dismiss();
			}
		});

		// 播种
		sow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(context, PlantToolsSowActivity.class);
				context.startActivity(intent);
				Activity activity = (Activity) context;
				activity.finish();
				activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 补苗
		bumiao.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(context, PlantToolsBumiaoActivity.class);
				context.startActivity(intent);
				Activity activity = (Activity) context;
				activity.finish();
				activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 杀虫
		insecticidal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(context, PlantToolsInsecticidalActivity.class);
				context.startActivity(intent);
				Activity activity = (Activity) context;
				activity.finish();
				activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 除草
		weed.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(context, PlantToolsWeedActivity.class);
				context.startActivity(intent);
				Activity activity = (Activity) context;
				activity.finish();
				activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 游戏
		game.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		// 收割
		harvest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				Intent intent = new Intent(context, PlantToolsHarvestActivity.class);
				context.startActivity(intent);
				Activity activity = (Activity) context;
				activity.finish();
				activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

	}

	// 浇水操作
	@SuppressLint("HandlerLeak")
	private Handler wateringApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							if (listener != null)
								listener.success(WATERING_FLAG);
						} else if (result == 1) {
							Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
							context.sendBroadcast(new Intent("finish"));
							Activity activity = (Activity) context;
							activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							if (listener != null)
								listener.error(WATERING_FLAG);
						}
					} catch (JSONException e) {
						if (listener != null)
							listener.error(WATERING_FLAG);
					}
				}
				Log.e("my3w", json);
			} else {
				Toast.makeText(context, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	// 补光操作
	@SuppressLint("HandlerLeak")
	private Handler fillApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							if (listener != null)
								listener.success(FILL_FLAG);
						} else if (result == 1) {
							Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
							context.sendBroadcast(new Intent("finish"));
							Activity activity = (Activity) context;
							activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							if (listener != null)
								listener.error(FILL_FLAG);
						}
					} catch (JSONException e) {
						if (listener != null)
							listener.error(FILL_FLAG);
					}
				}
				Log.e("my3w", json);
			} else {
				Toast.makeText(context, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	// 土地内容
	@SuppressLint("HandlerLeak")
	private Handler controlApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String json = (String) msg.obj;
			if (json != null) {
				try {
					JSONObject jsonObject = new JSONObject(json);
					int status = jsonObject.optInt("errCode");
					if (status == 0) {
						int Ph_d = jsonObject.optInt("ph");
						int tuw_count = jsonObject.optInt("land_wendu");
						int qwen_count = jsonObject.optInt("air_wendu");
						int light_count = jsonObject.optInt("light");
						int tushi_count = jsonObject.optInt("land_shidu");
						int kshi_count = jsonObject.optInt("air_shidu");
						int cocont_count = jsonObject.optInt("co2");

						int kshi_count_num = kshi_count / 100;
						kongqishidu.setProgress(kshi_count_num);
						kongqishidu_txt.setText(String.valueOf(kshi_count_num));

						int tushi_count_num = tushi_count / 100;
						turangshidu.setProgress(tushi_count_num);
						turangshidu_txt.setText(String.valueOf(tushi_count_num));

						guangzhaodu.setProgress(light_count);
						guangzhaodu_txt.setText(String.valueOf(light_count));

						int qwen_count_num = qwen_count / 100;
						kongqiwendu.setProgress(qwen_count_num);
						kongqiwendu_txt.setText(String.valueOf(qwen_count_num));

						int tuw_count_num = tuw_count / 100;
						turangwendu.setProgress(tuw_count_num);
						turangwendu_txt.setText(String.valueOf(tuw_count_num));

						int Ph_d_num = Ph_d / 100;
						phzhi.setProgress(Ph_d_num);
						float ph_d_num_f = Ph_d / 100f;
						phzhi_txt.setText(String.valueOf(ph_d_num_f));

						int cocont_count_num = cocont_count / 100;
						eryanhuatan.setProgress(cocont_count_num);
						eryanhuatan_txt.setText(String.valueOf(cocont_count));
						eryanhuatan3.setProgress(cocont_count_num);
						eryanhuatan3_txt.setText(String.valueOf(cocont_count));
					} else if (status == 1) {
						Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);
						context.sendBroadcast(new Intent("finish"));
						Activity activity = (Activity) context;
						activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else {
						Toasts.showToast(context, "土地数据获取失败，请重新查看");
						dismiss();
					}
				} catch (JSONException e) {
					Toasts.showToast(context, "土地数据获取失败，请重新查看");
					dismiss();
				}
			}
		};
	};

	// 获取肥料
	@SuppressLint("HandlerLeak")
	private Handler toolsApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
							if (jsonArray != null && jsonArray.length() > 0) {
								FeiList = new String[jsonArray.length()];
								DilutionList = new String[jsonArray.length()];
								WaterList = new String[jsonArray.length()];
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObj = jsonArray.optJSONObject(i);
									FeiList[i] = jsonObj.optString("goods_name");
									DilutionList[i] = jsonObj.optString("dilution_value");
									WaterList[i] = jsonObj.optString("water_value");
								}
								IndexFeiName = FeiList[0];
								IndexFeiDilution = DilutionList[0];
								IndexFeiWater = WaterList[0];
								shifeilist.setText(IndexFeiName);
								shifei_cankao.setText(IndexFeiDilution + "g/㎡");
								xishi_cankao.setText(IndexFeiWater + "ml/㎡");
							} else {
								shifeilist.setText("未购买肥料");
							}
						} else if (result == 1) {
							Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
							context.sendBroadcast(new Intent("finish"));
							Activity activity = (Activity) context;
							activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							shifeilist.setText("未购买肥料");
						}
					} catch (JSONException e) {
						shifeilist.setText("未购买肥料");
					}
				}
				Log.e("my3w", json);
			} else {
				Toast.makeText(context, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	// 获取种子列表
	@SuppressLint("HandlerLeak")
	private Handler seedApiHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
							if (jsonArray != null && jsonArray.length() > 0) {
								SeedList = new String[jsonArray.length()];
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsonObj = jsonArray.optJSONObject(i);
									SeedList[i] = jsonObj.optString("title");
								}
								IndexSeedName = SeedList[0];
								seedlist.setText(IndexSeedName);
							} else {
								seedlist.setText("未种植种子");
							}
						} else if (result == 1) {
							Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(context, LoginActivity.class);
							context.startActivity(intent);
							context.sendBroadcast(new Intent("finish"));
							Activity activity = (Activity) context;
							activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							seedlist.setText("未种植种子");
						}
					} catch (JSONException e) {
						seedlist.setText("未种植种子");
					}
				}
				Log.e("my3w", json);
			} else {
				Toast.makeText(context, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
	};

	// 提交施肥
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
						if (listener != null)
							listener.success(APPLY_FLAG);
					} else if (result == 1) {
						Toast.makeText(context, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);
						context.sendBroadcast(new Intent("finish"));
						Activity activity = (Activity) context;
						activity.overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					if (listener != null)
						listener.error(APPLY_FLAG);
				}
			} else {
				if (listener != null)
					listener.error(APPLY_FLAG);
			}
		};
	};

	public interface OnSuccessListener {
		public void success(int what);

		public void error(int what);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return super.onKeyDown(keyCode, event);
	}
}
