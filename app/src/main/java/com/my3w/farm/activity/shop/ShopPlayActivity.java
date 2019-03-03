package com.my3w.farm.activity.shop;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.plant.PlantActivity;
import com.my3w.farm.activity.shop.alipay.OrderInfoUtil2_0;
import com.my3w.farm.activity.shop.alipay.PayResult;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.plug.pay.OrderInfo;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShopPlayActivity extends baseActivity {

	private LoadingDialog dialog;

	/**
	 * 支付宝
	 */
	public static final String APPID = "2015120900946117";
	public static final String RSA_PRIVATE = "" + "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMlZfv+g5PJaAoZq"
			+ "0XsYOEc0RgpoQnJCJ46vLkKyyhjayz60G0oZFxR8RL7fYZMoflGFFEzaM7b1g/V9"
			+ "ZroCEN/iLTjt0Eyh9dTjxSeGVRcyD+3r3FaEy9oGZWxzpxjWRE6f1JtuOUj0q56i"
			+ "eizwF9cGWhwxdIbsm/ZtHKQ3PRwfAgMBAAECgYBSEUBKQPIQNc/BqdgoK/8ERrIl"
			+ "m8k5eO1WouBgHy9wYf1lWVOmKAPYPa5nffTvXhnecDeN+4IImJlofexY9G6bmAec"
			+ "0SfVxcv6m4bUFc8zfh7rsmIkecFMtytf1CcADds/ad/SwN+8plYIGBEbQFRXO4g6"
			+ "LqE29Z819qrISlXGQQJBAP2MdqdJtgH+7HUTf/sxS+tNerUWtu5xanaY4oSSgYRs"
			+ "QCq+IlFqKnYLF2ES/Il9aCchfqZaXCjzvpkAq79MxAsCQQDLS9bQyxCNZMWNbLtu"
			+ "5NX1ixO/awLCsIzr9H5I3zBOSRf981Bk7myQFQEtr/5SXcqlkhxdrmSB9/8wpHPb"
			+ "jSC9AkBZkvMkPJ1JbTc6UU5Ifuz0TyYHreOor3bOOUrlTSDmmJu+vfc/zgL8OwTi"
			+ "baO8KwrNsD6fJJMkgKAYS61hYwxrAkAxYxYnmP1HbDC137FVClCUGxpMCUIda+iA"
			+ "NeAr8dR5YDZs4hwouOc0xXWI1NVyywg82tb7Ry9xh36+IWVn88v9AkBBMhmG36Pd"
			+ "bkTMZXIxjIAWXos/MZ7NgqTzkwX9pdXC6RWHf0/6Mk2VVzjPYAc2lWX1uciHLwAT" + "zv/bb1324ekF";
	private static final int SDK_PAY_FLAG = 1;

	private ImageView title_back;
	private TextView title_name;
	private RoundImageView title_icon;

	private ImageButton alipay;
	private ImageButton wxpay;
	private TextView price;

	private String order_id;
	private String order_sn;
	private String order_name;
	private String order_content;
	private String order_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_play);

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("支付方式");
		title_icon = (RoundImageView) findViewById(R.id.title_icon);

		String user_picString = DataCache.get(ShopPlayActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		alipay = (ImageButton) findViewById(R.id.alipay);
		wxpay = (ImageButton) findViewById(R.id.wxpay);
		price = (TextView) findViewById(R.id.price);
	}

	private void initData() {
		order_id = getIntent().getStringExtra("order_id");
		order_sn = getIntent().getStringExtra("order_sn");
		order_name = getIntent().getStringExtra("order_name");
		order_content = getIntent().getStringExtra("order_content");
		order_price = getIntent().getStringExtra("order_price");

		price.setText("￥" + order_price);
	}

	private void initEvent() {

		// 返回主界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopPlayActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 支付宝支付
		alipay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderInfo info = new OrderInfo();
				info.setOrder_id(order_id);
				info.setOrder_sn(order_sn);
				info.setOrder_title(order_name);
				info.setOrder_body(order_content);
				info.setOrder_free(order_price);

				Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, info);
				String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
				String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
				final String orderInfo = orderParam + "&" + sign;
				Log.e("xxxxxxx", orderInfo);
				Runnable authRunnable = new Runnable() {

					@Override
					public void run() {
						// 构造AuthTask 对象
						PayTask alipay = new PayTask(ShopPlayActivity.this);
						// 调用授权接口，获取授权结果
						Map<String, String> result = alipay.payV2(orderInfo, true);

						Message msg = new Message();
						msg.what = SDK_PAY_FLAG;
						msg.obj = result;
						mAlipayHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread authThread = new Thread(authRunnable);
				authThread.start();
			}
		});

		// 微信支付
		wxpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OrderInfo info = new OrderInfo();
				info.setOrder_id(order_id);
				info.setOrder_sn(order_sn);
				info.setOrder_title(order_name);
				info.setOrder_body(order_content);
				info.setOrder_free(order_price);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ShopPlayActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("HandlerLeak")
	private Handler mAlipayHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				Log.e("xxxxxxx", resultInfo);
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					try {
						JSONObject jsonObject = new JSONObject(resultInfo);

						String sign = jsonObject.getString("sign");
						String sign_type = jsonObject.getString("sign_type");

						JSONObject alipay_trade_app_pay_response = jsonObject.getJSONObject("alipay_trade_app_pay_response");
						String code = alipay_trade_app_pay_response.getString("code");
						String msgs = alipay_trade_app_pay_response.getString("msg");
						String app_id = alipay_trade_app_pay_response.getString("app_id");
						String out_trade_no = alipay_trade_app_pay_response.getString("out_trade_no");
						String trade_no = alipay_trade_app_pay_response.getString("trade_no");
						String total_amount = alipay_trade_app_pay_response.getString("total_amount");
						String seller_id = alipay_trade_app_pay_response.getString("seller_id");
						String charset = alipay_trade_app_pay_response.getString("charset");

						if (APPID.equals(app_id)) {
							// 请求loading
							if (dialog == null)
								dialog = new LoadingDialog(ShopPlayActivity.this);
							dialog.show("支付成功，更新订单中，请稍候…");

							// 提交订单
							HashMap<String, String> headerList = new HashMap<String, String>();
							headerList.put("sign", sign);
							headerList.put("sign_type", sign_type);
							headerList.put("out_trade_no", out_trade_no);
							headerList.put("trade_no", trade_no);
							headerList.put("total_amount", total_amount);
							headerList.put("seller_id", seller_id);
							HttpRequest http = new HttpRequest(ShopPlayActivity.this, headerList, alipaySuccessHandler);
							http.execute(getResources().getString(R.string.config_host_url) + "cart.php?action=alipayRetrun&token="
									+ DataCache.get(ShopPlayActivity.this).getAsString("token"));
						} else {
							PayError("非法请求导致支付失败，您的损失我们不会进行负责！");
						}
					} catch (JSONException e) {
						PaySuccess("您的订单还在处理中，请留意订单状态");
					}
				} else if (TextUtils.equals(resultStatus, "8000")) {
					PaySuccess("您的订单还在处理中，请留意查看订单状态");
				} else {
					PayError("支付失败，您还是重新购买吧");
				}
				break;
			}
			default:
				break;
			}
		};
	};

	// 支付宝支付成功后的操作
	@SuppressLint("HandlerLeak")
	private Handler alipaySuccessHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				if (json != null) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 1) {
							Toast.makeText(ShopPlayActivity.this, "登录超时或已经被别人登录，请别担心，我们会自动处理您的订单，如果有问题请联系我们", Toast.LENGTH_SHORT).show();
							finish();
							Intent intent = new Intent(ShopPlayActivity.this, LoginActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else if (result == 2) {
							PayError("非法请求导致支付失败，您的损失我们不会进行负责！");
						} else {
							if (dialog != null)
								dialog.cancel();

							PaySuccess();
						}
					} catch (JSONException e) {

						if (dialog != null)
							dialog.cancel();

						PaySuccess("数据出现异常，请别担心，我们会自动处理您的订单，如果有问题请联系我们");
					}
				}
			} else {

				if (dialog != null)
					dialog.cancel();

				PaySuccess("网络出现了点问题，请别担心，我们会自动处理您的订单，如果有问题请联系我们");
			}
		}
	};

	private void PaySuccess() {
		Intent intent = new Intent(ShopPlayActivity.this, PlantActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
	}

	private void PaySuccess(String msg) {
		Toast.makeText(ShopPlayActivity.this, msg, Toast.LENGTH_SHORT).show();
		PaySuccess();
	}

	private void PayError() {

	}

	private void PayError(String msg) {
		Toast.makeText(ShopPlayActivity.this, msg, Toast.LENGTH_SHORT).show();
		PayError();
	}
}
