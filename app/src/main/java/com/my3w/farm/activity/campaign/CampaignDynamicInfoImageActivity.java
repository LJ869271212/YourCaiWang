package com.my3w.farm.activity.campaign;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.campaign.adapter.CampaignDynamicImageAdapter;
import com.my3w.farm.activity.campaign.entity.ImageInfoListEntity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.ListExtView;
import com.westars.framework.view.list.ListExtView.onListViewListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CampaignDynamicInfoImageActivity extends baseActivity {

	private LoadingDialog dialog;

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private TextView namicTitle;
	private ListExtView listview;

	private CampaignDynamicImageAdapter adapter;
	private ArrayList<ImageInfoListEntity> data = new ArrayList<ImageInfoListEntity>();

	private String id;
	private String page = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic_info_image);

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

		title_back = (ImageView) this.findViewById(R.id.title_back);
		title_back.setImageResource(R.drawable.r);
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("参赛作品");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(CampaignDynamicInfoImageActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(CampaignDynamicInfoImageActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		namicTitle = (TextView) this.findViewById(R.id.namicTitle);
		listview = (ListExtView) this.findViewById(R.id.listview);
		listview.setShowHeaderView(true);
		listview.setShowFooterView(true);
		adapter = new CampaignDynamicImageAdapter(this, data);
		listview.setAdapter(adapter);
	}

	private void initData() {
		// 开始请求数据
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=info&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);

		// 开始请求列表
		HttpRequest httpList = new HttpRequest(this, Listhandler);
		httpList.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=imageList&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id);
	}

	private void getToolsIndex() {
		// 开始请求列表
		HttpRequest httpList = new HttpRequest(this, ListDataHandler);
		httpList.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=imageList&token="
				+ DataCache.get(this).getAsString("token") + "&id=" + id + "&page=" + page);
	}

	private void initEvent() {

		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
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
				Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, CampaignDynamicInfoImageInfoActivity.class);
				intent.putExtra("id", String.valueOf(data.get(position - 1).getId()));
				startActivity(intent);
				overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
			}
		});
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
							namicTitle.setText(jsonObject.optString("title"));
						} else if (result == 1) {
							Toast.makeText(CampaignDynamicInfoImageActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicInfoImageActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicInfoImageActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler Listhandler = new Handler() {
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
								ImageInfoListEntity dataEntity = new ImageInfoListEntity();
								dataEntity.setId(jsonObjects.optString("id"));
								dataEntity.setAll_pic(jsonObjects.optString("all_pic"));
								dataEntity.setUsername(jsonObjects.optString("username"));
								dataEntity.setIsRecom(jsonObjects.optString("isRecom"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setToucount(jsonObjects.optString("toucount"));
								dataEntity.setComcount(jsonObjects.optString("comcount"));

								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CampaignDynamicInfoImageActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicInfoImageActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicInfoImageActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		}
	};

	@SuppressLint("HandlerLeak")
	private Handler ListDataHandler = new Handler() {
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
								ImageInfoListEntity dataEntity = new ImageInfoListEntity();
								dataEntity.setId(jsonObjects.optString("id"));
								dataEntity.setAll_pic(jsonObjects.optString("all_pic"));
								dataEntity.setUsername(jsonObjects.optString("username"));
								dataEntity.setIsRecom(jsonObjects.optString("isRecom"));
								dataEntity.setTitle(jsonObjects.optString("title"));
								dataEntity.setToucount(jsonObjects.optString("toucount"));
								dataEntity.setComcount(jsonObjects.optString("comcount"));

								data.add(dataEntity);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CampaignDynamicInfoImageActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicInfoImageActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicInfoImageActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CampaignDynamicInfoImageActivity.this, HomeActivity.class);
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
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
