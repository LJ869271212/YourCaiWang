package com.my3w.farm.activity.shop;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.shop.adapter.LandAdapter;
import com.my3w.farm.activity.shop.adapter.LandAdapterOn;
import com.my3w.farm.activity.shop.adapter.TabContentAdapter;
import com.my3w.farm.activity.shop.entity.SelectListEntity;
import com.my3w.farm.activity.shop.entity.SelectListIner;
import com.my3w.farm.activity.shop.entity.SelectListLand;
import com.my3w.farm.activity.shop.entity.SelectListLandOn;
import com.my3w.farm.activity.shop.entity.SelectValueClassLevelEntity;
import com.my3w.farm.activity.shop.entity.SelectValueEntity;
import com.my3w.farm.activity.shop.sqlite.DBManager;
import com.my3w.farm.activity.shop.view.TabGridView;
import com.my3w.farm.activity.shop.view.TabTextView;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 买地
 * 
 * @author Administrator
 *
 */
public class ShopLandActivity extends baseActivity {

	private LoadingDialog dialog;

	// View
	private ImageView title_back;
	private LinearLayout title_icon_box;
	private RoundImageView title_icon;

	// Select View
	private LinearLayout land_select_pinzhong;
	private TextView land_select_pinzhong_txt;
	private LinearLayout land_select_diqu;
	private TextView land_select_diqu_txt;
	private LinearLayout land_select_zhongzhiqu;
	private TextView land_select_zhongzhiqu_txt;
	private LinearLayout land_select_dapengtexing;
	private TextView land_select_dapengtexing_txt;
	private LinearLayout land_select_dapengleixing;
	private TextView land_select_dapengleixing_txt;

	// Select List
	private SelectListEntity listData;

	// Select Data
	private int pingzhong_id;
	private String pingzhong_name;
	private String pingzhong_key;
	private int diqu_id;
	private String diqu_name;
	private String diqu_key;
	private int zhongzhiqu_id;
	private String zhongzhiqu_name;
	private String zhongzhiqu_key;
	private int dapengtexing_id;
	private String dapengtexing_name;
	private String dapengtexing_key;
	private int dapengleixing_id;
	private String dapengleixing_name;
	private String dapengleixing_key;

	// A3 Tab View
	private LinearLayout quyu_tab;
	private LinearLayout quyu_content;

	// Land View
	private TextView land_number;
	private TextView land_ctype;
	private TabGridView tudi;

	// Land Data
	private ArrayList<SelectListLand> landList = new ArrayList<SelectListLand>();
	private LandAdapter tudiAdapter;
	private String land_ctype_contet = "";

	// select land view
	private TabGridView tudi_select;

	// select land Data
	private ArrayList<SelectListLandOn> selectlandList = new ArrayList<SelectListLandOn>();
	private LandAdapterOn tudiselectAdapter;

	// Button view
	private Button add_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_land);

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
		title_icon_box = (LinearLayout) findViewById(R.id.title_icon_box);
		title_icon = (RoundImageView) findViewById(R.id.title_icon);

		String user_picString = DataCache.get(ShopLandActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		land_select_pinzhong = (LinearLayout) findViewById(R.id.land_select_pinzhong);
		land_select_pinzhong_txt = (TextView) findViewById(R.id.land_select_pinzhong_txt);
		land_select_diqu = (LinearLayout) findViewById(R.id.land_select_diqu);
		land_select_diqu_txt = (TextView) findViewById(R.id.land_select_diqu_txt);
		land_select_zhongzhiqu = (LinearLayout) findViewById(R.id.land_select_zhongzhiqu);
		land_select_zhongzhiqu_txt = (TextView) findViewById(R.id.land_select_zhongzhiqu_txt);
		land_select_dapengtexing = (LinearLayout) findViewById(R.id.land_select_dapengtexing);
		land_select_dapengtexing_txt = (TextView) findViewById(R.id.land_select_dapengtexing_txt);
		land_select_dapengleixing = (LinearLayout) findViewById(R.id.land_select_dapengleixing);
		land_select_dapengleixing_txt = (TextView) findViewById(R.id.land_select_dapengleixing_txt);

		quyu_tab = (LinearLayout) findViewById(R.id.quyu_tab);
		quyu_content = (LinearLayout) findViewById(R.id.quyu_content);

		land_number = (TextView) findViewById(R.id.land_number);
		land_ctype = (TextView) findViewById(R.id.land_ctype);
		tudi = (TabGridView) findViewById(R.id.tudi);
		tudi.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tudi.setNumColumns(6);
		tudiAdapter = new LandAdapter(this, landList);
		tudi.setAdapter(tudiAdapter);

		tudi_select = (TabGridView) findViewById(R.id.tudi_select);
		tudi_select.setSelector(new ColorDrawable(Color.TRANSPARENT));
		tudi_select.setNumColumns(6);
		tudiselectAdapter = new LandAdapterOn(this, selectlandList);
		tudi_select.setAdapter(tudiselectAdapter);

		add_btn = (Button) findViewById(R.id.add_btn);
	}

	private void initData() {
		// 开始请求数据
		HttpRequest http = new HttpRequest(this, screenHandler);
		http.execute(getResources().getString(R.string.config_host_url) + "landshop.php?action=screen&token="
				+ DataCache.get(this).getAsString("token"));
	}

	private void initEvent() {

		// 返回主界面
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopLandActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});
		
		//个人中心
		title_icon_box.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopLandActivity.this, UserCenterActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});

		// 选择地区
		land_select_diqu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SelectValueEntity[] address = listData.getAddress();
				final String[] diqu = new String[address.length];
				for (int i = 0; i < address.length; i++) {
					diqu[i] = address[i].getName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(ShopLandActivity.this);
				builder.setTitle("请选择地区");
				builder.setItems(diqu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						diqu_id = which;
						diqu_name = address[which].getName();
						diqu_key = address[which].getKey();

						land_select_diqu_txt.setText(diqu_name);

						// 开始请求区域
						getLandIner();
					}
				});
				builder.show();
			}
		});

		// 选择品种
		land_select_pinzhong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SelectValueEntity[] varieties = listData.getVarieties();
				final String[] pingzhong = new String[varieties.length];
				for (int i = 0; i < varieties.length; i++) {
					pingzhong[i] = varieties[i].getName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(ShopLandActivity.this);
				builder.setTitle("请选择品种");
				builder.setItems(pingzhong, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pingzhong_id = which;
						pingzhong_name = varieties[which].getName();
						pingzhong_key = varieties[which].getKey();

						land_select_pinzhong_txt.setText(pingzhong_name);

						// 开始请求区域
						getLandIner();
					}
				});
				builder.show();
			}
		});

		// 选择种植区
		land_select_zhongzhiqu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SelectValueEntity[] growing = listData.getGrowing();
				final String[] zhongzhiqu = new String[growing.length];
				for (int i = 0; i < growing.length; i++) {
					zhongzhiqu[i] = growing[i].getName();
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(ShopLandActivity.this);
				builder.setTitle("请选择种植区");
				builder.setItems(zhongzhiqu, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						zhongzhiqu_id = which;
						zhongzhiqu_name = growing[which].getName();
						zhongzhiqu_key = growing[which].getKey();

						land_select_zhongzhiqu_txt.setText(zhongzhiqu_name);

						SelectValueClassLevelEntity[] classs = growing[zhongzhiqu_id].getClasss();
						if (classs != null) {
							land_select_dapengtexing.setVisibility(View.VISIBLE);
						} else {
							land_select_dapengtexing.setVisibility(View.GONE);
						}

						final SelectValueClassLevelEntity[] level = growing[zhongzhiqu_id].getLevel();
						if (level != null) {
							land_select_dapengleixing.setVisibility(View.VISIBLE);
						} else {
							land_select_dapengleixing.setVisibility(View.GONE);
						}

						// 开始请求区域
						getLandIner();
					}
				});
				builder.show();
			}
		});

		// 大棚特性
		land_select_dapengtexing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectValueEntity[] growing = listData.getGrowing();
				final SelectValueClassLevelEntity[] classs = growing[zhongzhiqu_id].getClasss();
				if (classs != null) {
					final String[] texing = new String[classs.length];
					for (int i = 0; i < classs.length; i++) {
						texing[i] = classs[i].getName();
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(ShopLandActivity.this);
					builder.setTitle("请选择大棚特性");
					builder.setItems(texing, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dapengtexing_id = which;
							dapengtexing_name = classs[which].getName();
							dapengtexing_key = classs[which].getKey();

							land_select_dapengtexing_txt.setText(dapengtexing_name);

							// 开始请求区域
							getLandIner();
						}
					});
					builder.show();
				}
			}
		});

		// 大棚类型
		land_select_dapengleixing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SelectValueEntity[] growing = listData.getGrowing();
				final SelectValueClassLevelEntity[] level = growing[zhongzhiqu_id].getLevel();
				if (level != null) {
					final String[] leixing = new String[level.length];
					for (int i = 0; i < level.length; i++) {
						leixing[i] = level[i].getName();
					}

					AlertDialog.Builder builder = new AlertDialog.Builder(ShopLandActivity.this);
					builder.setTitle("请选择大棚特性");
					builder.setItems(leixing, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dapengleixing_id = which;
							dapengleixing_name = level[which].getName();
							dapengleixing_key = level[which].getKey();

							land_select_dapengleixing_txt.setText(dapengleixing_name);

							// 开始请求区域
							getLandIner();
						}
					});
					builder.show();
				}
			}
		});

		// 土地选择
		tudi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (landList.get(position).getSell_status().equals("hire")) {
					Toast.makeText(ShopLandActivity.this, "当前土地已经出售", Toast.LENGTH_SHORT).show();
				} else {
					landList.get(position).setBackgroud(R.drawable.select_3);
					tudiAdapter.notifyDataSetChanged();

					if (isPostionAdd(position)) {
						SelectListLand getData = landList.get(position);
						SelectListLandOn data = new SelectListLandOn();
						data.setAcreage(getData.getAcreage());
						data.setBackgroud(R.drawable.select_3);
						data.setDigital(getData.getDigital());
						data.setId(getData.getId());
						data.setNo(getData.getNo());
						data.setPostion(position);
						data.setSell_status(getData.getSell_status());
						data.setPrice(getData.getPrice());
						selectlandList.add(data);
						tudiselectAdapter.notifyDataSetChanged();
					}
				}
			}
		});

		// 删除土地选择
		tudi_select.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				landList.get(selectlandList.get(position).getPostion()).setBackgroud(R.drawable.select_2);
				selectlandList.remove(position);
				tudiAdapter.notifyDataSetChanged();
				tudiselectAdapter.notifyDataSetChanged();
			}
		});

		// 添加到购物车
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (selectlandList.size() > 0) {
					DBManager sqlite = new DBManager();
					sqlite.open(ShopLandActivity.this);
					for (int i = 0; i < selectlandList.size(); i++) {
						ContentValues contentValues = new ContentValues();
						contentValues.put("id", Integer.parseInt(selectlandList.get(i).getId()));
						contentValues.put("arg", selectlandList.get(i).getAcreage());
						contentValues.put("dal", selectlandList.get(i).getDigital());
						contentValues.put("content", land_ctype_contet);
						contentValues.put("no", selectlandList.get(i).getNo());
						contentValues.put("title", selectlandList.get(i).getNo());
						contentValues.put("number", 1);
						contentValues.put("price", selectlandList.get(i).getPrice());
						contentValues.put("pic", "");
						contentValues.put("unit", "1");
						contentValues.put("use_unit", "1");
						contentValues.put("use_number", "1");
						contentValues.put("wheres", "lands");
						contentValues.put("level", DataCache.get(ShopLandActivity.this).getAsString("user_level"));

						try {
							Cursor cursors = sqlite.findById("LandCart", "id", Integer.parseInt(selectlandList.get(i).getId()), null);
							if (cursors != null) {
								if (cursors.getCount() <= 0) {
									sqlite.insert("LandCart", null, contentValues);
								}
								cursors.close();
							} else {
								sqlite.insert("LandCart", null, contentValues);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					sqlite.close();
					sqlite = null;

					Toast.makeText(ShopLandActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(ShopLandActivity.this, ShopCartActivity.class);
					intent.putExtra("action", "com.my3w.farm.activity.shop.ShopLandActivity");
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
				}
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
							JSONArray jsonArrayAddress = jsonObject.optJSONArray("address");
							SelectValueEntity[] address = new SelectValueEntity[jsonArrayAddress.length()];
							for (int i = 0; i < jsonArrayAddress.length(); i++) {
								JSONObject jsonOpt = jsonArrayAddress.optJSONObject(i);
								address[i] = new SelectValueEntity();
								address[i].setKey(jsonOpt.optString("key"));
								address[i].setName(jsonOpt.optString("name"));
								address[i].setValue(jsonOpt.optString("value"));
							}
							listData.setAddress(address);
							
							JSONArray jsonArrayVarieties = jsonObject.optJSONArray("varieties");
							SelectValueEntity[] varieties = new SelectValueEntity[jsonArrayVarieties.length()];
							for (int i = 0; i < jsonArrayVarieties.length(); i++) {
								JSONObject jsonOpt = jsonArrayVarieties.optJSONObject(i);
								varieties[i] = new SelectValueEntity();
								varieties[i].setKey(jsonOpt.optString("key"));
								varieties[i].setName(jsonOpt.optString("name"));
								varieties[i].setValue(jsonOpt.optString("value"));
							}
							listData.setVarieties(varieties);

							JSONArray jsonArrayGrowing = jsonObject.optJSONArray("growing");
							SelectValueEntity[] growing = new SelectValueEntity[jsonArrayGrowing.length()];
							for (int i = 0; i < jsonArrayGrowing.length(); i++) {
								JSONObject jsonOpt = jsonArrayGrowing.optJSONObject(i);
								growing[i] = new SelectValueEntity();
								growing[i].setKey(jsonOpt.optString("key"));
								growing[i].setName(jsonOpt.optString("name"));
								growing[i].setValue(jsonOpt.optString("value"));

								// class
								JSONArray classArray = jsonOpt.optJSONArray("class");
								if (classArray != null) {
									SelectValueClassLevelEntity[] classs = new SelectValueClassLevelEntity[classArray.length()];
									for (int j = 0; j < classArray.length(); j++) {
										JSONObject classOpt = classArray.optJSONObject(j);
										classs[j] = new SelectValueClassLevelEntity();
										classs[j].setKey(classOpt.optString("key"));
										classs[j].setName(classOpt.optString("name"));
										classs[j].setValue(classOpt.optString("value"));
									}
									growing[i].setClasss(classs);
								}

								// level
								JSONArray levelArray = jsonOpt.optJSONArray("level");
								if (levelArray != null) {
									SelectValueClassLevelEntity[] level = new SelectValueClassLevelEntity[levelArray.length()];
									for (int j = 0; j < levelArray.length(); j++) {
										JSONObject levelOpt = levelArray.optJSONObject(j);
										level[j] = new SelectValueClassLevelEntity();
										level[j].setKey(levelOpt.optString("key"));
										level[j].setName(levelOpt.optString("name"));
										level[j].setValue(levelOpt.optString("value"));
									}
									growing[i].setLevel(level);
								}
							}
							listData.setGrowing(growing);

							// 默认数据
							diqu_id = 0;
							diqu_name = address[diqu_id].getName();
							diqu_key = address[diqu_id].getKey();
							land_select_diqu_txt.setText(diqu_name);
							
							pingzhong_id = 0;
							pingzhong_name = varieties[pingzhong_id].getName();
							pingzhong_key = varieties[pingzhong_id].getKey();
							land_select_pinzhong_txt.setText(pingzhong_name);

							zhongzhiqu_id = 0;
							zhongzhiqu_name = growing[zhongzhiqu_id].getName();
							zhongzhiqu_key = growing[zhongzhiqu_id].getKey();
							land_select_zhongzhiqu_txt.setText(zhongzhiqu_name);

							SelectValueClassLevelEntity[] classsArray = growing[zhongzhiqu_id].getClasss();
							if (classsArray != null) {
								dapengtexing_id = 0;
								dapengtexing_name = classsArray[dapengtexing_id].getName();
								dapengtexing_key = classsArray[dapengtexing_id].getKey();
								land_select_dapengtexing_txt.setText(dapengtexing_name);
							}

							SelectValueClassLevelEntity[] levelArray = growing[zhongzhiqu_id].getLevel();
							if (levelArray != null) {
								dapengleixing_id = 0;
								dapengleixing_name = levelArray[dapengleixing_id].getName();
								dapengleixing_key = levelArray[dapengleixing_id].getKey();
								land_select_dapengleixing_txt.setText(dapengleixing_name);
							}

							// 开始请求区域
							getLandIner();

						} else if (result == 1) {
							Toast.makeText(ShopLandActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopLandActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(ShopLandActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(ShopLandActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(ShopLandActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(ShopLandActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	// 获取区域
	private void getLandIner() {
		// 请求loading
		if (dialog == null)
			dialog = new LoadingDialog(this);
		dialog.show("数据加载中，请稍候…");

		// 清楚原先数据
		quyu_tab.removeAllViews();
		quyu_content.removeAllViews();

		// 清除土地数据
		if (landList != null)
			landList.clear();
		tudiAdapter.notifyDataSetChanged();

		// 清楚选择文字
		land_number.setText("");
		land_ctype.setText("");

		// 清楚选择的土地信息
		if (selectlandList != null)
			selectlandList.clear();
		tudiselectAdapter.notifyDataSetChanged();

		// 开始请求新数据
		HttpRequest http = new HttpRequest(this, landIner);
		http.execute(getResources().getString(R.string.config_host_url) + "landshop.php?action=landiner&token="
				+ DataCache.get(this).getAsString("token") + "&ctype=" + pingzhong_key + "&city=" + diqu_key + "&area=" + zhongzhiqu_key
				+ "&ptype=" + dapengtexing_key + "&plevel=" + dapengleixing_key);
	}

	// 获取区域handler
	@SuppressLint("HandlerLeak")
	private Handler landIner = new Handler() {
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
							JSONArray landinerArray = jsonObject.optJSONArray("landiner");
							if (landinerArray.length() <= 0) {
								quyu_content.addView(getNullTextView());
							} else {
								for (int i = 0; i < landinerArray.length(); i++) {
									JSONObject landinerObject = landinerArray.getJSONObject(i);
									String names = landinerObject.optString("letter");
									String textcolor = "#ff0a7e31";
									String background = "#ffffffff";
									if (i == 0) {
										textcolor = "#ffffffff";
										background = "#ff0a7e31";
									}
									TabTextView view = getLandInerTabView(names, textcolor, background);
									view.setTag(i);
									quyu_tab.addView(view, i);

									int Visblie = View.GONE;
									if (i == 0) {
										Visblie = View.VISIBLE;
									}
									JSONArray inerArray = landinerObject.optJSONArray("iner");
									TabGridView tabGridView = getLandInerTabGridView(inerArray, Visblie);
									tabGridView.setTag(i);
									quyu_content.addView(tabGridView, i);

									// Tab点击事件
									view.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											int index = (Integer) ((TabTextView) v).getTag();

											for (int j = 0; j < quyu_tab.getChildCount(); j++) {
												TabTextView views = (TabTextView) quyu_tab.getChildAt(j);
												views.setTextColor(Color.parseColor("#ff0a7e31"));
												views.setBackgroundColor(Color.parseColor("#ffffffff"));
											}
											((TabTextView) v).setTextColor(Color.parseColor("#ffffffff"));
											((TabTextView) v).setBackgroundColor(Color.parseColor("#ff0a7e31"));

											for (int j = 0; j < quyu_content.getChildCount(); j++) {
												TabGridView views = (TabGridView) quyu_content.getChildAt(j);
												views.setVisibility(View.GONE);
											}
											TabGridView gv = (TabGridView) quyu_content.getChildAt(index);
											if (gv != null)
												gv.setVisibility(View.VISIBLE);

										}
									});
								}
							}
						} else if (result == 1) {
							Toast.makeText(ShopLandActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ShopLandActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(ShopLandActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(ShopLandActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@SuppressWarnings("deprecation")
	private TextView getNullTextView() {
		TextView view = new TextView(this);
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(textViewParams);
		view.setPadding(0, 20, 0, 20);
		view.setText("当前筛选条件没有土地信息");
		view.setTextColor(Color.parseColor("#ffff0000"));
		view.setGravity(Gravity.CENTER);
		view.setBackgroundColor(Color.parseColor("#ffffffff"));
		return view;
	}

	// 生成区域Tab
	@SuppressWarnings("deprecation")
	private TabTextView getLandInerTabView(String txt, String textColor, String background) {
		TabTextView view = new TabTextView(this);
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		textViewParams.rightMargin = 5;
		view.setLayoutParams(textViewParams);
		view.setText(txt);
		view.setTextColor(Color.parseColor(textColor));
		view.setGravity(Gravity.CENTER);
		view.setBackgroundColor(Color.parseColor(background));
		return view;
	}

	@SuppressWarnings("deprecation")
	private TabGridView getLandInerTabGridView(JSONArray inerArray, int visibility) {
		TabGridView view = new TabGridView(this);
		view.setSelector(new ColorDrawable(Color.TRANSPARENT));
		view.setVisibility(visibility);
		view.setNumColumns(5);
		view.setHorizontalSpacing(0);
		view.setVerticalSpacing(0);
		LinearLayout.LayoutParams gridViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(gridViewParams);
		if (inerArray != null) {
			final ArrayList<SelectListIner> selectListIner = new ArrayList<SelectListIner>();
			for (int j = 0; j < inerArray.length(); j++) {
				JSONObject inerObject = inerArray.optJSONObject(j);
				SelectListIner data = new SelectListIner();
				data.setId(inerObject.optString("id"));
				data.setNo(inerObject.optString("no"));
				data.setCtype_en(inerObject.optString("ctype_en"));
				selectListIner.add(data);
			}
			TabContentAdapter tabAdapter = new TabContentAdapter(this, selectListIner);
			view.setAdapter(tabAdapter);
			view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if (selectlandList.size() <= 0) {
						// 请求loading
						if (dialog == null)
							dialog = new LoadingDialog(ShopLandActivity.this);
						dialog.show("数据加载中，请稍候…");

						// 请求数据
						HttpRequest http = new HttpRequest(ShopLandActivity.this, land);
						http.execute(getResources().getString(R.string.config_host_url) + "landshop.php?action=land&token="
								+ DataCache.get(ShopLandActivity.this).getAsString("token") + "&id="
								+ selectListIner.get(position).getId());

						// 选中后改变内容
						land_number.setText("编号：" + selectListIner.get(position).getNo());
						land_ctype.setText("适合种植：" + selectListIner.get(position).getCtype_en());

						// 选择后的内容
						land_ctype_contet = selectListIner.get(position).getCtype_en();
					} else {
						Toast.makeText(ShopLandActivity.this, "请添加当前土地到购物车后再选择其它区域土地购买", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		return view;
	}

	@SuppressLint("HandlerLeak")
	private Handler land = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (dialog != null)
				dialog.cancel();

			if (msg.what == 200) {
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						if (landList != null)
							landList.clear();

						JSONArray jsonArray = jsonObject.optJSONArray("landlist");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject landObject = jsonArray.optJSONObject(i);
							SelectListLand data = new SelectListLand();
							data.setId(landObject.optString("id"));
							data.setAcreage(landObject.optString("acreage"));
							data.setDigital(landObject.optString("digital"));
							data.setNo(landObject.optString("no"));
							data.setSell_status(landObject.optString("sell_status"));
							data.setPrice(Float.parseFloat(landObject.optString("price")));

							if (data.getSell_status().equals("hire")) {
								data.setBackgroud(R.drawable.select_1);
							} else {
								data.setBackgroud(R.drawable.select_2);
							}

							landList.add(data);
						}

						if (tudiAdapter != null)
							tudiAdapter.notifyDataSetChanged();
					} else if (result == 1) {
						Toast.makeText(ShopLandActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ShopLandActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					Toast.makeText(ShopLandActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(ShopLandActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 判断是否添加了当前的数据到已选数据中
	private boolean isPostionAdd(int postion) {
		for (int i = 0; i < selectlandList.size(); i++) {
			if (selectlandList.get(i).getPostion() == postion) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ShopLandActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}
}
