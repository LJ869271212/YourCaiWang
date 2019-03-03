package com.my3w.farm.activity.game.borrow;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.game.borrow.adapter.BorrowAdapter;
import com.my3w.farm.activity.game.borrow.entity.BorrowInfoEntity;
import com.my3w.farm.activity.game.borrow.huancai.BorrowHuanCaiAddActivity;
import com.my3w.farm.activity.game.borrow.huancai.BorrowHuanCaiInfoActivity;
import com.my3w.farm.activity.game.borrow.huancaixuqiu.BorrowHuanCaiXuQiuAddActivity;
import com.my3w.farm.activity.game.borrow.huancaixuqiu.BorrowHuanCaiXuQiuInfoActivity;
import com.my3w.farm.activity.game.borrow.jiecai.BorrowJieCaiAddActivity;
import com.my3w.farm.activity.game.borrow.jiecai.BorrowJieCaiInfoActivity;
import com.my3w.farm.activity.game.borrow.jiecaixuqiu.BorrowJieCaiXuQiuAddActivity;
import com.my3w.farm.activity.game.borrow.jiecaixuqiu.BorrowJieCaiXuQiuInfoActivity;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowActivity extends baseActivity {

	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private String[] Date;
	private String[] City;
	private String[] CityID = new String[] { "0", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29" };

	private TextView riqi;
	private TextView diqu;

	private String[] addType = new String[] { "发布借菜", "发布换菜", "发布借菜需求", "发布换菜需求" };
	private TextView add_borrow_btn;

	private String s_riqi = "";
	private String s_diqu = "";
	private String s_type = "1";

	private String page = "1";

	private ListExtView listview;

	private BorrowAdapter adapter;
	private ArrayList<BorrowInfoEntity> data = new ArrayList<BorrowInfoEntity>();

	private TextView jiecai;
	private TextView huancai;
	private TextView jiecaixuqiu;
	private TextView huancaixuqiu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_1_borrow);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("我要借菜");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(BorrowActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(BorrowActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		riqi = (TextView) this.findViewById(R.id.riqi);
		diqu = (TextView) this.findViewById(R.id.diqu);

		add_borrow_btn = (TextView) this.findViewById(R.id.add_borrow_btn);

		listview = (ListExtView) findViewById(R.id.listview);
		listview.setShowHeaderView(true);
		listview.setShowFooterView(true);
		adapter = new BorrowAdapter(this, data);
		listview.setAdapter(adapter);

		jiecai = (TextView) this.findViewById(R.id.jiecai);
		huancai = (TextView) this.findViewById(R.id.huancai);
		jiecaixuqiu = (TextView) this.findViewById(R.id.jiecaixuqiu);
		huancaixuqiu = (TextView) this.findViewById(R.id.huancaixuqiu);
	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "borrow.php?action=list&token="
				+ DataCache.get(this).getAsString("token") + "&bx_where=1");

		HttpRequest Stretch = new HttpRequest(this, Stretchhandler);
		Stretch.execute(getResources().getString(R.string.config_host_url) + "borrow.php?action=borrow&token="
				+ DataCache.get(this).getAsString("token"));

	}

	private void getToolsIndex() {
		String url = getResources().getString(R.string.config_host_url) + "borrow.php?action=list&token="
				+ DataCache.get(this).getAsString("token") + "&bx_where=" + s_type + "&page=" + page + "&riqi=" + s_riqi + "&city="
				+ s_diqu;
		Log.e("xxxxx", url);
		HttpRequest http = new HttpRequest(this, dataHandler);
		http.execute(url);
	}

	private void initEvent() {
		// 日期
		riqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(BorrowActivity.this).setTitle("日期").setItems(Date, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String viewString = Date[which];
						if ("全部".equals(viewString)) {
							riqi.setText("日期");
							s_riqi = "";
						} else {
							riqi.setText(viewString);
							s_riqi = String.valueOf(which);
						}
						getToolsIndex();
					}
				}).show();
			}
		});

		// 地区
		diqu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(BorrowActivity.this).setTitle("日期").setItems(City, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						String viewString = City[which];
						if ("全部".equals(viewString)) {
							diqu.setText("地区");
							s_diqu = "";
						} else {
							diqu.setText(viewString);
							s_diqu = CityID[which];
						}
						getToolsIndex();
					}
				}).show();
			}
		});

		// 发布
		add_borrow_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(BorrowActivity.this).setTitle("选择发布类型").setItems(addType, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent intent = new Intent(BorrowActivity.this, BorrowJieCaiAddActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else if (which == 1) {
							Intent intent = new Intent(BorrowActivity.this, BorrowHuanCaiAddActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else if (which == 2) {
							Intent intent = new Intent(BorrowActivity.this, BorrowJieCaiXuQiuAddActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else if (which == 3) {
							Intent intent = new Intent(BorrowActivity.this, BorrowHuanCaiXuQiuAddActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					}
				}).show();
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

		// 点击
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				BorrowInfoEntity info = data.get(position - 1);

				if (info != null) {
					Intent intent = null;
					if ("1".equals(info.getBx_where())) {
						intent = new Intent(BorrowActivity.this, BorrowJieCaiInfoActivity.class);
					} else if ("6".equals(info.getBx_where())) {
						intent = new Intent(BorrowActivity.this, BorrowHuanCaiInfoActivity.class);
					} else if ("3".equals(info.getBx_where())) {
						intent = new Intent(BorrowActivity.this, BorrowJieCaiXuQiuInfoActivity.class);
					} else if ("8".equals(info.getBx_where())) {
						intent = new Intent(BorrowActivity.this, BorrowHuanCaiXuQiuInfoActivity.class);
					}

					if (intent != null) {
						intent.putExtra("id", info.getId());
						startActivity(intent);
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				}
			}
		});

		// 我要借菜
		jiecai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_type = "1";
				title_name.setText("我要借菜");
				getToolsIndex();
			}
		});

		// 我要借菜
		huancai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_type = "2";
				title_name.setText("我要换菜");
				getToolsIndex();
			}
		});

		// 借换菜需求
		jiecaixuqiu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_type = "3";
				title_name.setText("借菜需求");
				getToolsIndex();
			}
		});

		// 借换菜需求
		huancaixuqiu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				s_type = "4";
				title_name.setText("换菜需求");
				getToolsIndex();
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
							Date = new String[jsonArrayMonth.length()];
							for (int i = 0; i < jsonArrayMonth.length(); i++) {
								Date[i] = jsonArrayMonth.getString(i);
							}

							// 活动日程
							JSONArray jsonArrayCity = jsonObjects.optJSONArray("city");
							City = new String[jsonArrayCity.length()];
							for (int i = 0; i < jsonArrayCity.length(); i++) {
								City[i] = jsonArrayCity.getString(i);
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
								BorrowInfoEntity dataEntity = new BorrowInfoEntity();
								dataEntity.setId(jsonObjects.optString("id"));
								dataEntity.setThumb_pic(jsonObjects.optString("thumb_pic"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setDescription(jsonObjects.optString("description"));
								dataEntity.setAddtime(jsonObjects.optString("addtime"));
								dataEntity.setBx_where(jsonObjects.optString("bx_where"));
								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(BorrowActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(BorrowActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(BorrowActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(BorrowActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(BorrowActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(BorrowActivity.this, HomeActivity.class);
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
								BorrowInfoEntity dataEntity = new BorrowInfoEntity();
								dataEntity.setId(jsonObjects.optString("id"));
								dataEntity.setThumb_pic(jsonObjects.optString("thumb_pic"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setDescription(jsonObjects.optString("description"));
								dataEntity.setAddtime(jsonObjects.optString("addtime"));
								dataEntity.setBx_where(jsonObjects.optString("bx_where"));
								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(BorrowActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(BorrowActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(BorrowActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(BorrowActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(BorrowActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(BorrowActivity.this, HomeActivity.class);
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
			Intent intent = new Intent(BorrowActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
