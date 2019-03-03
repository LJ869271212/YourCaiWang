package com.my3w.farm.activity.shop;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.adapter.CartAdapter;
import com.my3w.farm.activity.shop.entity.CartEntity;
import com.my3w.farm.activity.shop.sqlite.DBManager;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.list.ListExtView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 购物车
 * 
 * @author Administrator
 *
 */
public class ShopCartActivity extends baseActivity {

	private LoadingDialog dialog;

	// View
	private ImageView title_back;
	private TextView title_name;
	private LinearLayout title_cart;
	private LinearLayout title_icon_box;

	// Intent call back activity
	private String action;

	// Data View
	private ListExtView datas;
	private TextView cart_number;
	private TextView delete_cart;
	private TextView cart_count_price;
	private Button add_btn;

	// Data Data
	private ArrayList<CartEntity> data = new ArrayList<CartEntity>();
	private CartAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_cart);

		action = getIntent().getStringExtra("action");

		initView();
		initData();
		initEvent();
	}

	@SuppressLint("InflateParams")
	private void initView() {

		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("购物车");
		title_cart = (LinearLayout) findViewById(R.id.title_cart);
		title_cart.setVisibility(View.GONE);
		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);
		title_icon_box.setVisibility(View.GONE);

		cart_number = (TextView) findViewById(R.id.cart_number);
		delete_cart = (TextView) findViewById(R.id.delete_cart);

		datas = (ListExtView) findViewById(R.id.datas);
		datas.setShowHeaderView(false);
		datas.setShowFooterView(false);

		cart_count_price = (TextView) findViewById(R.id.cart_count_price);
		add_btn = (Button) findViewById(R.id.add_btn);

		adapter = new CartAdapter(this, data, cart_count_price, cart_number);
		datas.setAdapter(adapter);
	}

	private void initData() {

		// 读取数据
		try {
			DBManager sqlite = new DBManager();
			sqlite.open(this);
			Cursor LandCursors = sqlite.findAll("LandCart", null);
			if (LandCursors != null) {
				while (LandCursors.moveToNext()) {
					CartEntity listData = new CartEntity();
					listData.setArg(LandCursors.getString(LandCursors.getColumnIndex("arg")));
					listData.setContent(LandCursors.getString(LandCursors.getColumnIndex("content")));
					listData.setDigital(LandCursors.getString(LandCursors.getColumnIndex("dal")));
					listData.setId(String.valueOf(LandCursors.getInt(LandCursors.getColumnIndex("id"))));
					listData.setNo(LandCursors.getString(LandCursors.getColumnIndex("no")));
					listData.setNumber(String.valueOf(LandCursors.getInt(LandCursors.getColumnIndex("number"))));
					listData.setPic(LandCursors.getString(LandCursors.getColumnIndex("pic")));
					listData.setUnit(LandCursors.getString(LandCursors.getColumnIndex("unit")));
					listData.setUse_unit(LandCursors.getString(LandCursors.getColumnIndex("use_unit")));
					listData.setUse_number(LandCursors.getString(LandCursors.getColumnIndex("use_number")));
					listData.setPrice(String.valueOf(LandCursors.getFloat(LandCursors.getColumnIndex("price"))));
					listData.setTitle(LandCursors.getString(LandCursors.getColumnIndex("title")));
					listData.setWhere(LandCursors.getString(LandCursors.getColumnIndex("wheres")));
					String level = LandCursors.getString(LandCursors.getColumnIndex("level"));
					if (level.equals("Vip1")) {
						listData.setLevel("普通会员");
					} else {
						listData.setLevel("VIP会员");
					}
					listData.setDataname("LandCart");
					listData.setBackground("#ffffffff");
					data.add(listData);
				}
			}

			Cursor SeedCursors = sqlite.findAll("SeedCart", null);
			if (SeedCursors != null) {
				while (SeedCursors.moveToNext()) {
					CartEntity listData = new CartEntity();
					listData.setArg(SeedCursors.getString(SeedCursors.getColumnIndex("arg")));
					listData.setContent(SeedCursors.getString(SeedCursors.getColumnIndex("content")));
					listData.setDigital(SeedCursors.getString(SeedCursors.getColumnIndex("dal")));
					listData.setId(String.valueOf(SeedCursors.getInt(SeedCursors.getColumnIndex("id"))));
					listData.setNo(SeedCursors.getString(SeedCursors.getColumnIndex("no")));
					listData.setNumber(String.valueOf(SeedCursors.getInt(SeedCursors.getColumnIndex("number"))));
					listData.setPic(SeedCursors.getString(SeedCursors.getColumnIndex("pic")));
					listData.setUnit(SeedCursors.getString(SeedCursors.getColumnIndex("unit")));
					listData.setUse_unit(SeedCursors.getString(SeedCursors.getColumnIndex("use_unit")));
					listData.setUse_number(SeedCursors.getString(SeedCursors.getColumnIndex("use_number")));
					listData.setPrice(String.valueOf(SeedCursors.getFloat(SeedCursors.getColumnIndex("price"))));
					listData.setTitle(SeedCursors.getString(SeedCursors.getColumnIndex("title")));
					listData.setWhere(SeedCursors.getString(SeedCursors.getColumnIndex("wheres")));
					String level = SeedCursors.getString(SeedCursors.getColumnIndex("level"));
					if (level.equals("Vip1")) {
						listData.setLevel("普通会员");
					} else {
						listData.setLevel("VIP会员");
					}
					listData.setDataname("SeedCart");
					listData.setBackground("#ffffffff");
					data.add(listData);
				}
			}
			


			Cursor ToolsCursors = sqlite.findAll("ToolsCart", null);
			if (ToolsCursors != null) {
				while (ToolsCursors.moveToNext()) {
					CartEntity listData = new CartEntity();
					listData.setArg(ToolsCursors.getString(ToolsCursors.getColumnIndex("arg")));
					listData.setContent(ToolsCursors.getString(ToolsCursors.getColumnIndex("content")));
					listData.setDigital(ToolsCursors.getString(ToolsCursors.getColumnIndex("dal")));
					listData.setId(String.valueOf(ToolsCursors.getInt(ToolsCursors.getColumnIndex("id"))));
					listData.setNo(ToolsCursors.getString(ToolsCursors.getColumnIndex("no")));
					listData.setNumber(String.valueOf(ToolsCursors.getInt(ToolsCursors.getColumnIndex("number"))));
					listData.setPic(ToolsCursors.getString(ToolsCursors.getColumnIndex("pic")));
					listData.setUnit(ToolsCursors.getString(ToolsCursors.getColumnIndex("unit")));
					listData.setUse_unit(ToolsCursors.getString(ToolsCursors.getColumnIndex("use_unit")));
					listData.setUse_number(ToolsCursors.getString(ToolsCursors.getColumnIndex("use_number")));
					listData.setPrice(String.valueOf(ToolsCursors.getFloat(ToolsCursors.getColumnIndex("price"))));
					listData.setTitle(ToolsCursors.getString(ToolsCursors.getColumnIndex("title")));
					listData.setWhere(ToolsCursors.getString(ToolsCursors.getColumnIndex("wheres")));
					String level = ToolsCursors.getString(ToolsCursors.getColumnIndex("level"));
					if (level.equals("Vip1")) {
						listData.setLevel("普通会员");
					} else {
						listData.setLevel("VIP会员");
					}
					listData.setDataname("ToolsCart");
					listData.setBackground("#ffffffff");
					data.add(listData);
				}
			}
			sqlite.close();
			sqlite = null;

			if (adapter != null)
				adapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("xxx", e.toString());
			e.printStackTrace();
		}
	}

	private void initEvent() {

		// 返回主界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				try {
					intent = new Intent(ShopCartActivity.this, Class.forName(action));
				} catch (ClassNotFoundException e) {
					intent = new Intent(ShopCartActivity.this, HomeActivity.class);
				}
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 清空购物车
		delete_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteCart();

				if (data != null)
					data.clear();

				if (adapter != null)
					adapter.notifyDataSetChanged();
			}
		});

		// 开始去结算
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (data != null) {
					if (data.size() > 0) {
						// 请求loading
						if (dialog == null)
							dialog = new LoadingDialog(ShopCartActivity.this);
						dialog.show("生成订单中，请稍候…");

						// 获取所有ID
						String id = "";
						for (int i = 0; i < data.size(); i++) {
							if (data.get(i).getDataname().equals("LandCart")) {
								if (i == 0) {
									id += data.get(i).getId();
								} else {
									id += "," + data.get(i).getId();
								}
							}
						}

						// 判断是否有土地被锁定了
						HashMap<String, String> headerList = new HashMap<String, String>();
						headerList.put("id", id);
						HttpRequest http = new HttpRequest(ShopCartActivity.this, headerList, Lockhandler);
						http.execute(getResources().getString(R.string.config_host_url) + "cart.php?action=stock&token="
								+ DataCache.get(ShopCartActivity.this).getAsString("token"));
					} else {
						Toast.makeText(ShopCartActivity.this, "购物车中还没有东西，去买点什么吧", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ShopCartActivity.this, "购物车中还没有东西，去买点什么吧", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler Lockhandler = new Handler() {
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
							JSONArray jsonArrayErrMsg = jsonObject.optJSONArray("errMsg");
							if (jsonArrayErrMsg.length() > 0) {

								if (dialog != null)
									dialog.cancel();

								for (int i = 0; i < jsonArrayErrMsg.length(); i++) {
									JSONObject jsonObejct = jsonArrayErrMsg.optJSONObject(i);
									for (int j = 0; j < data.size(); j++) {
										if (jsonObejct.optInt("land_id") == Integer.parseInt(data.get(j).getId())) {
											data.get(j).setBackground("#fffcff00");
											break;
										}
									}
								}
								if (adapter != null)
									adapter.notifyDataSetChanged();

								Toast.makeText(ShopCartActivity.this, "出现黄色的列表土地已经被购买了，请删除了再提交", Toast.LENGTH_SHORT).show();
							} else {
								// 提交订单
								HashMap<String, String> headerList = new HashMap<String, String>();
								headerList.put("json", new Gson().toJson(data));
								HttpRequest http = new HttpRequest(ShopCartActivity.this, headerList, carthandler);
								http.execute(getResources().getString(R.string.config_host_url) + "cart.php?action=cart&token="
										+ DataCache.get(ShopCartActivity.this).getAsString("token"));
							}
						} else if (result == 1) {
							Toast.makeText(ShopCartActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							finish();
							Intent intent = new Intent(ShopCartActivity.this, LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {

						if (dialog != null)
							dialog.cancel();

						Toast.makeText(ShopCartActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {

				if (dialog != null)
					dialog.cancel();

				Toast.makeText(ShopCartActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler carthandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (dialog != null)
				dialog.cancel();

			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							// 购物车
							deleteCart();

							JSONObject jsonObjects = jsonObject.optJSONObject("errMsg");

							String order_id = jsonObjects.getString("order_id");
							String order_sn = jsonObjects.getString("order_sn");
							String order_name = jsonObjects.getString("order_name");
							String order_content = jsonObjects.getString("order_content");
							String order_price = jsonObjects.getString("order_price");

							Intent intent = new Intent(ShopCartActivity.this, ShopPlayActivity.class);
							intent.putExtra("order_id", order_id);
							intent.putExtra("order_sn", order_sn);
							intent.putExtra("order_name", order_name);
							intent.putExtra("order_content", order_content);
							intent.putExtra("order_price", order_price);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else if (result == 1) {
							Toast.makeText(ShopCartActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopCartActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							String errMsg = jsonObject.optString("errMsg");
							Toast.makeText(ShopCartActivity.this, errMsg, Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						Toast.makeText(ShopCartActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(ShopCartActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = null;
			try {
				intent = new Intent(ShopCartActivity.this, Class.forName(action));
			} catch (ClassNotFoundException e) {
				intent = new Intent(ShopCartActivity.this, HomeActivity.class);
			}
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void deleteCart() {
		try {
			DBManager sqlite = new DBManager();
			sqlite.open(ShopCartActivity.this);
			sqlite.deleteAll("LandCart");
			sqlite.deleteAll("SeedCart");
			sqlite.close();
			sqlite = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
