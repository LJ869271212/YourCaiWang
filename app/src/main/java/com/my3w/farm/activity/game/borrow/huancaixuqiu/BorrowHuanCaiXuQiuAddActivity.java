package com.my3w.farm.activity.game.borrow.huancaixuqiu;

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
import com.my3w.farm.comment.image.ImageMagent;
import com.my3w.farm.comment.tusdk.examples.component.AlbumComponentBorrow;
import com.my3w.farm.comment.tusdk.examples.suite.CameraComponentBorrow;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 换菜需求
 * 
 * @author Administrator
 *
 */
public class BorrowHuanCaiXuQiuAddActivity extends baseActivity {

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private TextView land_name;
	private EditText borrow_name;
	private EditText description;
	private EditText borrow_num;
	private TextView borrow_unit;
	private EditText borrow_price;
	private TextView br_date1;
	private EditText return_name;
	private EditText phone;
	private EditText qq;
	private EditText email;
	private EditText address;
	private EditText cade;

	private Button upload;
	private Button submit_btn;

	private String[] LandNoList;

	private String[] unitList = new String[] { "斤", "棵", "克", "千克" };

	private String[] addList = { "相册选择", "相机拍摄" };

	private String title = "我要换菜";
	private String file = "";
	private String pid = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_1_borrow_add_4);

		initView();
		initData();
		initEvent();
	}

	private void initView() {

		title_back = (ImageView) this.findViewById(R.id.title_back);
		title_back.setImageResource(R.drawable.r);
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("换菜需求");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(BorrowHuanCaiXuQiuAddActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(BorrowHuanCaiXuQiuAddActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		land_name = (TextView) this.findViewById(R.id.land_name);
		borrow_name = (EditText) this.findViewById(R.id.borrow_name);
		description = (EditText) this.findViewById(R.id.description);
		borrow_num = (EditText) this.findViewById(R.id.borrow_num);
		borrow_unit = (TextView) this.findViewById(R.id.borrow_unit);
		borrow_price = (EditText) this.findViewById(R.id.borrow_price);
		br_date1 = (TextView) this.findViewById(R.id.br_date1);
		return_name = (EditText) this.findViewById(R.id.return_name);
		phone = (EditText) this.findViewById(R.id.phone);
		qq = (EditText) this.findViewById(R.id.qq);
		email = (EditText) this.findViewById(R.id.email);
		address = (EditText) this.findViewById(R.id.address);
		cade = (EditText) this.findViewById(R.id.cade);

		upload = (Button) this.findViewById(R.id.upload);
		submit_btn = (Button) this.findViewById(R.id.submit_btn);

	}

	private void initData() {
		HttpRequest http = new HttpRequest(BorrowHuanCaiXuQiuAddActivity.this, SelectLandhandler);
		http.execute(getResources().getString(R.string.config_host_url) + "tools.php?action=getland&token="
				+ DataCache.get(BorrowHuanCaiXuQiuAddActivity.this).getAsString("token"));
	}

	private void initEvent() {
		// 选择土地
		land_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (LandNoList != null && LandNoList.length > 0) {
					new AlertDialog.Builder(BorrowHuanCaiXuQiuAddActivity.this).setTitle("选择土地编号")
							.setItems(LandNoList, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							land_name.setText(LandNoList[which]);
						}
					}).show();
				} else {
					Toasts.showToast(BorrowHuanCaiXuQiuAddActivity.this, "土地获取中，请稍后……");
				}
			}
		});

		// 选择单位
		borrow_unit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(BorrowHuanCaiXuQiuAddActivity.this).setTitle("选择单位")
						.setItems(unitList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						borrow_unit.setText(unitList[which]);
					}
				}).show();
			}
		});

		// 选择日期
		br_date1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int year = 0;
				int month = 0;
				int day = 0;
				if (br_date1.getText().toString().equals("")) {
					Calendar calendar = Calendar.getInstance(Locale.CHINA);
					Date date = new Date();
					calendar.setTime(date);
					year = calendar.get(Calendar.YEAR);
					month = calendar.get(Calendar.MONTH);
					day = calendar.get(Calendar.DAY_OF_MONTH);
				} else {
					String getDate = br_date1.getText().toString();
					String[] DateList = getDate.split("-");
					year = Integer.parseInt(DateList[0]);
					month = Integer.parseInt(DateList[1]) - 1;
					day = Integer.parseInt(DateList[2]);
				}
				new DatePickerDialog(BorrowHuanCaiXuQiuAddActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						br_date1.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1) + "-" + String.valueOf(dayOfMonth));
					}
				}, year, month, day).show();
			}
		});

		// 上传图片
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(BorrowHuanCaiXuQiuAddActivity.this).setTitle("选择图片上传方式")
						.setItems(addList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						if (which == 0) { // 相册选择
							new AlbumComponentBorrow(BorrowHuanCaiXuQiuAddActivity.this, new AlbumComponentBorrow.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(BorrowHuanCaiXuQiuAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "相册出了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void component(String path, String file) {
									Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();
									BorrowHuanCaiXuQiuAddActivity.this.file = path + file;
								}
							}).showSample(BorrowHuanCaiXuQiuAddActivity.this);
						} else if (which == 1) { // 拍照
							new CameraComponentBorrow(BorrowHuanCaiXuQiuAddActivity.this, new CameraComponentBorrow.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(BorrowHuanCaiXuQiuAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void component(String path, String file) {
									Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();
									BorrowHuanCaiXuQiuAddActivity.this.file = path + file;
								}
							}).showSample(BorrowHuanCaiXuQiuAddActivity.this);
						}
					}
				}).show();
			}
		});

		// 发布信息
		submit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String sland_name = land_name.getText().toString();
				String sborrow_name = borrow_name.getText().toString();
				String sdescription = description.getText().toString();
				String sborrow_num = borrow_num.getText().toString();
				String sborrow_unit = borrow_unit.getText().toString();
				String sborrow_price = borrow_price.getText().toString();
				String sbr_date1 = br_date1.getText().toString();
				String sreturn_name = return_name.getText().toString();
				String sphone = phone.getText().toString();
				String sqq = qq.getText().toString();
				String semail = email.getText().toString();
				String saddress = address.getText().toString();
				String scade = cade.getText().toString();
				String stitle = title;
				String sfile = file;

				if (checkPost(sfile, "封面图不存在，请上传图片"))
					return;
				else if (checkPost(sland_name, "请选择土地"))
					return;
				else if (checkPost(sborrow_name, "请选择蔬菜名称"))
					return;
				else if (checkPost(sborrow_num, "请输入数量"))
					return;
				else if (checkPost(sborrow_price, "请输入单价"))
					return;
				else if (checkPost(sbr_date1, "交换菜日期不能为空"))
					return;
				else if (checkPost(sreturn_name, "交换菜名称不能为空"))
					return;
				else if (checkPost(sdescription, "交换菜描述不能为空"))
					return;
				else if (checkPost(sphone, "手机号不能为空"))
					return;
				else {
					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("pid", pid);
					headerList.put("address", saddress);
					headerList.put("cade", scade);
					headerList.put("phone", sphone);
					headerList.put("land_name", sland_name);
					headerList.put("qq", sqq);
					headerList.put("email", semail);
					headerList.put("borrow_name", sborrow_name);
					headerList.put("return_name", sreturn_name);
					headerList.put("title", stitle);
					headerList.put("description", sdescription);
					headerList.put("borrow_num", sborrow_num);
					headerList.put("br_date", sbr_date1);
					headerList.put("files", sfile);
					headerList.put("borrow_unit", sborrow_unit);
					headerList.put("borrow_price", sborrow_price);
					HttpRequest httpseed = new HttpRequest(BorrowHuanCaiXuQiuAddActivity.this, headerList, SubmitHeandler);
					httpseed.execute(BorrowHuanCaiXuQiuAddActivity.this.getResources().getString(R.string.config_host_url)
							+ "borrow.php?action=add&token=" + DataCache.get(BorrowHuanCaiXuQiuAddActivity.this).getAsString("token"));
				}
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
							LandNoList = new String[jsonArray.length()];
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObj = jsonArray.optJSONObject(i);
								LandNoList[i] = jsonObj.optString("no");
							}
						} else {
							Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "还没有土地，快去买一块吧", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(BorrowHuanCaiXuQiuAddActivity.this, ShopLandActivity.class);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} else if (result == 1) {
						Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BorrowHuanCaiXuQiuAddActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	// 发布借菜
	@SuppressLint("HandlerLeak")
	private Handler SubmitHeandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "借菜发布成功", Toast.LENGTH_SHORT).show();
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					} else if (result == 1) {
						Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(BorrowHuanCaiXuQiuAddActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(BorrowHuanCaiXuQiuAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

	// 判断表单写入是否正确
	private boolean checkPost(String val, String msg) {
		if (val.equals("")) {
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
}
