package com.my3w.farm.activity.shop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.adapter.SeedAdapter;
import com.my3w.farm.activity.shop.entity.SeedEntity;
import com.my3w.farm.activity.shop.entity.SelectListEntity;
import com.my3w.farm.activity.shop.entity.SelectValueEntity;
import com.my3w.farm.activity.shop.sqlite.GetCartNumber;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.GridExtView;
import com.westars.framework.view.list.GridExtView.onGridViewListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 买种子
 * 
 * @author Administrator
 *
 */
public class ShopSeedActivity extends baseActivity {

	private LoadingDialog dialog;

	// View
	private ImageView title_back;
	private TextView title_name;
	private LinearLayout title_cart;
	private TextView title_cart_number;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	// Select View
	private Button select_seed;

	// Select List
	private SelectListEntity listData;

	// Select Data
	private int pingzhong_id;
	private String pingzhong_name;
	private String pingzhong_key = "all";
	private String page = "1";

	// List View
	private EditText select_txt;
	private Button select_button;
	private GridExtView datas;

	// List Data;
	private SeedAdapter seedAdapter;
	private ArrayList<SeedEntity> listSeed = new ArrayList<SeedEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_seed);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		// 请求loading
		if (dialog == null)
			dialog = new LoadingDialog(this);
		dialog.show("数据加载中，请稍候…");

		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("买种子");
		title_cart = (LinearLayout) findViewById(R.id.title_cart);
		title_cart.setVisibility(View.VISIBLE);
		title_cart_number = (TextView) findViewById(R.id.title_cart_number);
		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);
		title_icon = (RoundImageView) findViewById(R.id.title_icon);

		String user_picString = DataCache.get(ShopSeedActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		select_seed = (Button) findViewById(R.id.select_seed);

		select_txt = (EditText) findViewById(R.id.select_txt);
		select_button = (Button) findViewById(R.id.select_button);

		datas = (GridExtView) findViewById(R.id.datas);
		datas.setSelector(new ColorDrawable(Color.TRANSPARENT));
		seedAdapter = new SeedAdapter(this, listSeed);
		datas.setAdapter(seedAdapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 获取购物车数字
		cartNumber();
	}

	private void initData() {
		// 开始请求数据
		HttpRequest http = new HttpRequest(this, screenHandler);
		http.execute(getResources().getString(R.string.config_host_url) + "seedshop.php?action=screen&token="
				+ DataCache.get(this).getAsString("token"));
	}

	private void initEvent() {

		// 返回主界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopSeedActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopSeedActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 选择筛选
		select_seed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SelectValueEntity[] varieties = listData.getVarieties();
				final String[] pingzhong = new String[varieties.length];
				for (int i = 0; i < varieties.length; i++) {
					pingzhong[i] = varieties[i].getName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(ShopSeedActivity.this);
				builder.setTitle("请选择品种");
				builder.setItems(pingzhong, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pingzhong_id = which;
						pingzhong_name = varieties[pingzhong_id].getName();
						pingzhong_key = varieties[pingzhong_id].getKey();

						select_seed.setText(pingzhong_name);

						page = "1";
						getSeedIndex();
					}
				});
				builder.show();
			}
		});

		// 下拉刷新和上拉刷新
		datas.setOnGridViewListener(new onGridViewListener() {

			@Override
			public void Refresh() {
				page = "1";
				getSeedIndex();
			}

			@Override
			public void Load() {
				int IndexPage = Integer.parseInt(page);
				IndexPage = IndexPage + 1;
				page = String.valueOf(IndexPage);
				getSeedIndex();
			}
		});

		// 搜索
		select_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				page = "1";
				getSeedIndex();
			}
		});

		// 进入种子详情
		datas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ShopSeedActivity.this, ShopSeedInfoActivity.class);
				intent.putExtra("id", listSeed.get(position).getId());
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 查看购物车
		title_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopSeedActivity.this, ShopCartActivity.class);
				intent.putExtra("action", "com.my3w.farm.activity.shop.ShopSeedActivity");
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler screenHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						listData = new SelectListEntity();

						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							JSONArray jsonArrayVarieties = jsonObject.optJSONArray("varieties");
							SelectValueEntity[] varieties = new SelectValueEntity[jsonArrayVarieties.length() + 1];
							varieties[0] = new SelectValueEntity();
							varieties[0].setKey("all");
							varieties[0].setName("全部");
							varieties[0].setValue("");
							for (int i = 0; i < jsonArrayVarieties.length(); i++) {
								JSONObject jsonOpt = jsonArrayVarieties.optJSONObject(i);
								varieties[i + 1] = new SelectValueEntity();
								varieties[i + 1].setKey(jsonOpt.optString("key"));
								varieties[i + 1].setName(jsonOpt.optString("name"));
								varieties[i + 1].setValue(jsonOpt.optString("value"));
							}
							listData.setVarieties(varieties);

							// 默认数据
							getSeedIndex();
						} else if (result == 1) {
							Toast.makeText(ShopSeedActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopSeedActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(ShopSeedActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(ShopSeedActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(ShopSeedActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(ShopSeedActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	private void getSeedIndex() {
		String url = getResources().getString(R.string.config_host_url) + "seedshop.php?action=seed&token="
				+ DataCache.get(this).getAsString("token") + "&key=" + pingzhong_key + "&keyword=" + select_txt.getText().toString()
				+ "&page=" + page;
		Log.e("xxxx", url);
		HttpRequest http = new HttpRequest(this, dataHandler);
		http.execute(url);
	}

	@SuppressLint("HandlerLeak")
	private Handler dataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				if (dialog != null)
					dialog.cancel();

				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {

						if (page.equals("1"))
							if (listSeed != null)
								listSeed.clear();

						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {
							JSONArray jsonArraySeed = jsonObject.optJSONArray("seed");
							for (int i = 0; i < jsonArraySeed.length(); i++) {
								JSONObject jsonOpt = jsonArraySeed.optJSONObject(i);
								SeedEntity data = new SeedEntity();
								data.setId(jsonOpt.optString("id"));
								data.setPrice(jsonOpt.optString("price"));
								data.setThumb(jsonOpt.optString("thumb"));
								data.setTitle(jsonOpt.optString("title"));
								listSeed.add(data);
							}

							if (seedAdapter != null)
								seedAdapter.notifyDataSetChanged();
						} else if (result == 1) {
							Toast.makeText(ShopSeedActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopSeedActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(ShopSeedActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(ShopSeedActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}

			if (datas != null) {
				datas.onCompleteRefresh();
				datas.onCompleteLoad();
			}
		}
	};

	private void cartNumber() {
		// 获取购物车数字
		title_cart_number.setText("(" + GetCartNumber.getInstance(this).get() + ")");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ShopSeedActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}