package com.my3w.farm.activity.campaign;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.campaign.adapter.CampaignDynamicAdapter;
import com.my3w.farm.activity.campaign.entity.DynamicEntity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.ListExtView;
import com.westars.framework.view.list.ListExtView.onListViewListener;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 活动动态
 * 
 * @author Administrator
 *
 */
public class CampaignDynamicActivity extends baseActivity {

	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private TextView chufariqi;
	private TextView huodongricheng;
	private TextView huodongdidian;
	private ListExtView listview;
	private TextView quanbuhuodong;
	private TextView xianshanghuodong;
	private TextView xianxiahuodong;
	private TextView huodongxiangce;

	private CampaignDynamicAdapter adapter;
	private ArrayList<DynamicEntity> data = new ArrayList<DynamicEntity>();

	private String page = "1";
	private String active = "";

	private String[] Month;
	private String[] Day;
	private String[] Didian;

	private String s_chufariqi = "0";
	private String s_huodongricheng = "0";
	private String s_huodongdidian = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("活动中心");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(CampaignDynamicActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(CampaignDynamicActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		chufariqi = (TextView) this.findViewById(R.id.chufariqi);
		huodongricheng = (TextView) this.findViewById(R.id.huodongricheng);
		huodongdidian = (TextView) this.findViewById(R.id.huodongdidian);

		listview = (ListExtView) findViewById(R.id.listview);
		listview.setShowHeaderView(true);
		listview.setShowFooterView(true);
		adapter = new CampaignDynamicAdapter(this, data);
		listview.setAdapter(adapter);

		quanbuhuodong = (TextView) this.findViewById(R.id.quanbuhuodong);
		xianshanghuodong = (TextView) this.findViewById(R.id.xianshanghuodong);
		xianxiahuodong = (TextView) this.findViewById(R.id.xianxiahuodong);
		huodongxiangce = (TextView) this.findViewById(R.id.huodongxiangce);
	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=list&token="
				+ DataCache.get(this).getAsString("token") + "&active=" + active);

		HttpRequest Stretch = new HttpRequest(this, Stretchhandler);
		Stretch.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=stretch&token="
				+ DataCache.get(this).getAsString("token") + "&active=" + active);
	}

	private void getToolsIndex() {
		String url = getResources().getString(R.string.config_host_url) + "stretch.php?action=list&token="
				+ DataCache.get(this).getAsString("token") + "&active=" + active + "&page=" + page + "&chufariqi=" + s_chufariqi
				+ "&huodongricheng=" + s_huodongricheng + "&huodongdidian=" + s_huodongdidian;
		HttpRequest http = new HttpRequest(this, dataHandler);
		http.execute(url);
	}

	private void initEvent() {

		// 出发日期筛选
		chufariqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CampaignDynamicActivity.this).setTitle("出发日期")
						.setItems(Month, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String viewString = Month[which];
						if ("全部".equals(viewString)) {
							chufariqi.setText("出发日期");
						} else {
							chufariqi.setText(viewString);
						}
						s_chufariqi = String.valueOf(which);
						getToolsIndex();
					}
				}).show();
			}
		});

		// 活动日程筛选
		huodongricheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CampaignDynamicActivity.this).setTitle("活动日程").setItems(Day, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String viewString = Day[which];
						if ("全部".equals(viewString)) {
							huodongricheng.setText("活动日程");
						} else {
							huodongricheng.setText(viewString);
						}
						s_huodongricheng = String.valueOf(which);
						getToolsIndex();
					}
				}).show();
			}
		});

		// 活动地点筛选
		huodongdidian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(CampaignDynamicActivity.this).setTitle("活动地点")
						.setItems(Didian, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String viewString = Didian[which];
						if ("全部".equals(viewString)) {
							huodongdidian.setText("活动地点");
						} else {
							huodongdidian.setText(viewString);
						}
						s_huodongdidian = String.valueOf(which);
						getToolsIndex();
					}
				}).show();
			}
		});

		// 全部活动
		quanbuhuodong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (data != null)
					data.clear();

				active = "";

				initData();
			}
		});

		// 线上活动
		xianshanghuodong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (data != null)
					data.clear();

				active = "线上活动";

				initData();
			}
		});

		// 线下活动
		xianxiahuodong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (data != null)
					data.clear();

				active = "线下活动";

				initData();
			}
		});

		// 下拉刷新和上拉刷新
		listview.setOnListViewListener(new onListViewListener() {

			@Override
			public void Refresh() {
				page = "1";
				getToolsIndex();
			}

			@Override
			public void Load() {
				int IndexPage = Integer.parseInt(page);
				IndexPage = IndexPage + 1;
				page = String.valueOf(IndexPage);
				getToolsIndex();
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(CampaignDynamicActivity.this, CampaignDynamicInfoActivity.class);
				intent.putExtra("id", String.valueOf(data.get(position - 1).getId()));
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 活动相册
		huodongxiangce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler Stretchhandler = new Handler() {
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
							JSONObject jsonObjects = jsonObject.optJSONObject("errMsg");

							// 出发日期
							JSONArray jsonArrayMonth = jsonObjects.optJSONArray("month");
							Month = new String[jsonArrayMonth.length()];
							for (int i = 0; i < jsonArrayMonth.length(); i++) {
								Month[i] = jsonArrayMonth.getString(i);
							}

							// 活动日程
							JSONArray jsonArrayDay = jsonObjects.optJSONArray("day");
							Day = new String[jsonArrayDay.length()];
							for (int i = 0; i < jsonArrayDay.length(); i++) {
								Day[i] = jsonArrayDay.getString(i);
							}

							// 活动地点
							JSONArray jsonArrayDidian = jsonObjects.optJSONArray("didian");
							Didian = new String[jsonArrayDidian.length()];
							for (int i = 0; i < jsonArrayDidian.length(); i++) {
								Didian[i] = jsonArrayDidian.getString(i);
							}
						}
					} catch (JSONException e) {

					}
				}
			} else {
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
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
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObjects = jsonArray.optJSONObject(i);
								DynamicEntity dataEntity = new DynamicEntity();
								dataEntity.setId(jsonObjects.optInt("id"));
								dataEntity.setImage(jsonObjects.optString("litle_img"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setHuodongjieshao(jsonObjects.optString("introdution"));
								dataEntity.setDate(jsonObjects.optString("add_time"));
								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CampaignDynamicActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CampaignDynamicActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CampaignDynamicActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler dataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						if (page.equals("1"))
							if (data != null)
								data.clear();

						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {

							JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObjects = jsonArray.optJSONObject(i);
								DynamicEntity dataEntity = new DynamicEntity();
								dataEntity.setId(jsonObjects.optInt("id"));
								dataEntity.setImage(jsonObjects.optString("litle_img"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setHuodongjieshao(jsonObjects.optString("introdution"));
								dataEntity.setDate(jsonObjects.optString("add_time"));
								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CampaignDynamicActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CampaignDynamicActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CampaignDynamicActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}

			if (listview != null) {
				listview.onCompleteRefresh();
				listview.onCompleteLoad();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(CampaignDynamicActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
