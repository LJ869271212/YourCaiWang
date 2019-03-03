package com.my3w.farm.activity.shop;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.sqlite.DBManager;
import com.my3w.farm.activity.shop.sqlite.GetCartNumber;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.image.SquareImageView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopToolsInfoActivity extends baseActivity {

	private LoadingDialog dialog;

	// View
	private ImageView title_back;
	private TextView title_name;
	private LinearLayout title_cart;
	private TextView title_cart_number;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	private SquareImageView info_image;
	private TextView info_title;
	private TextView info_price;
	private TextView info_type;
	private TextView info_number;
	private Button info_number_left;
	private Button info_number_right;
	private TextView info_content;

	private Button kf_button;
	private Button shop_btn;
	private Button add_btn;

	// Data
	private String id;
	private String content;
	private String title;
	private String price;
	private String pic;
	private String unit;
	private String use_unit;
	private String use_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_tools_info);

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

		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("买工具");
		title_cart = (LinearLayout) findViewById(R.id.title_cart);
		title_cart.setVisibility(View.VISIBLE);
		title_cart_number = (TextView) findViewById(R.id.title_cart_number);
		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);
		title_icon = (RoundImageView) findViewById(R.id.title_icon);

		String user_picString = DataCache.get(ShopToolsInfoActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		info_image = (SquareImageView) findViewById(R.id.info_image);
		info_title = (TextView) findViewById(R.id.info_title);
		info_price = (TextView) findViewById(R.id.info_price);
		info_type = (TextView) findViewById(R.id.info_type);
		info_number = (TextView) findViewById(R.id.info_number);
		info_number_left = (Button) findViewById(R.id.info_number_left);
		info_number_right = (Button) findViewById(R.id.info_number_right);
		info_content = (TextView) findViewById(R.id.info_content);

		kf_button = (Button) findViewById(R.id.kf_button);
		shop_btn = (Button) findViewById(R.id.shop_btn);
		add_btn = (Button) findViewById(R.id.add_btn);
	}

	private void initData() {
		// 获取购物车数字
		cartNumber();

		// 开始请求数据
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "toolsshop.php?action=info&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);
	}

	private void initEvent() {

		// 返回主界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopToolsInfoActivity.this, UserCenterActivity.class);
				startActivity(intent);
				sendBroadcast(new Intent("finish"));
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 减少数字
		info_number_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int number = Integer.parseInt(info_number.getText().toString());
				if (number > 1) {
					info_number.setText(String.valueOf(number - 1));
				}
			}
		});

		// 增加数字
		info_number_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int number = Integer.parseInt(info_number.getText().toString());
				info_number.setText(String.valueOf(number + 1));
			}
		});

		// 客服
		kf_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:400888888"));
				startActivity(intent);
			}
		});

		// 立即购买
		shop_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 添加到购物车
				addCart();

				// 跳转到购物车
				Intent intent = new Intent(ShopToolsInfoActivity.this, ShopCartActivity.class);
				intent.putExtra("action", "com.my3w.farm.activity.shop.ShopToolsActivity");
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 添加到购物车
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 添加到购物车
				addCart();

				Toast.makeText(ShopToolsInfoActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();

				// 获取购物车数字
				cartNumber();
			}
		});

		// 查看购物车
		title_cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopToolsInfoActivity.this, ShopCartActivity.class);
				intent.putExtra("action", "com.my3w.farm.activity.shop.ShopToolsActivity");
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});
	}

	// 添加到购物车
	private void addCart() {
		DBManager sqlite = new DBManager();
		sqlite.open(ShopToolsInfoActivity.this);

		ContentValues contentValues = new ContentValues();
		contentValues.put("id", Integer.parseInt(id));
		contentValues.put("arg", "0");
		contentValues.put("dal", "0");
		contentValues.put("content", content);
		contentValues.put("no", "");
		contentValues.put("title", title);
		contentValues.put("number", Integer.parseInt(info_number.getText().toString()));
		contentValues.put("price", price);
		contentValues.put("pic", pic);
		contentValues.put("unit", unit);
		contentValues.put("use_unit", use_unit);
		contentValues.put("use_number", use_number);
		contentValues.put("wheres", "tools");
		contentValues.put("level", DataCache.get(ShopToolsInfoActivity.this).getAsString("user_level"));

		try {
			Cursor cursors = sqlite.findById("ToolsCart", "id", Integer.parseInt(id), null);
			if (cursors != null) {
				if (cursors.getCount() <= 0) {
					sqlite.insert("ToolsCart", null, contentValues);
				} else {
					ContentValues contentValuesUpdate = new ContentValues();
					contentValuesUpdate.put("number", Integer.parseInt(info_number.getText().toString()));
					sqlite.udpate("ToolsCart", new String[] { "id" }, new String[] { id }, contentValuesUpdate);
				}
				cursors.close();
			} else {
				sqlite.insert("ToolsCart", null, contentValues);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		sqlite.close();
		sqlite = null;
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
							String thumb = jsonObject.optString("thumb");
							ImageMagent.getInstance().displayImage(thumb, info_image);
							String title = jsonObject.optString("title");
							info_title.setText(title);
							String price = jsonObject.optString("price");
							info_price.setText(price);
							String where = jsonObject.optString("where");
							info_type.setText(where);
							info_number.setText("1");
							String content = jsonObject.optString("content");
							info_content.setText(content);
							String unit = jsonObject.optString("unit");
							String use_unit = jsonObject.optString("use_unit");
							String use_number = jsonObject.optString("use_number");

							ShopToolsInfoActivity.this.content = where;
							ShopToolsInfoActivity.this.title = title;
							ShopToolsInfoActivity.this.price = price;
							ShopToolsInfoActivity.this.pic = thumb;
							ShopToolsInfoActivity.this.unit = unit;
							ShopToolsInfoActivity.this.use_unit = use_unit;
							ShopToolsInfoActivity.this.use_number = use_number;
						} else if (result == 1) {
							Toast.makeText(ShopToolsInfoActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopToolsInfoActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(ShopToolsInfoActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(ShopToolsInfoActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	// 获取购物车数字
	private void cartNumber() {
		title_cart_number.setText("(" + GetCartNumber.getInstance(this).get() + ")");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
