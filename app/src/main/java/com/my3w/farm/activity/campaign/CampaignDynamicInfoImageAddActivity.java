package com.my3w.farm.activity.campaign;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.circle.adapter.CircleAdapterNoScrollGridAdapter;
import com.my3w.farm.activity.circle.entity.CircleListImageListEntity;
import com.my3w.farm.activity.circle.view.NoScrollGridView;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.tusdk.examples.component.AlbumComponentDynamic;
import com.my3w.farm.comment.tusdk.examples.suite.CameraComponentDynamic;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CampaignDynamicInfoImageAddActivity extends baseActivity {

	private LoadingDialog dialog;

	private TextView back_btn;
	private TextView add_btn;
	private TextView add_images;
	private EditText namicTitle;
	private EditText comment;
	private NoScrollGridView imageList;

	private CircleAdapterNoScrollGridAdapter adapter;
	private ArrayList<CircleListImageListEntity> data = new ArrayList<CircleListImageListEntity>();
	private ArrayList<CircleListImageListEntity> dataPost = new ArrayList<CircleListImageListEntity>();

	private String[] addList = { "相册选择", "相机拍摄" };

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic_info_image_add);

		id = getIntent().getStringExtra("id");

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		back_btn = (TextView) this.findViewById(R.id.back_btn);
		add_btn = (TextView) this.findViewById(R.id.add_btn);
		add_images = (TextView) this.findViewById(R.id.add_images);
		namicTitle = (EditText) this.findViewById(R.id.namicTitle);
		comment = (EditText) this.findViewById(R.id.comment);
		imageList = (NoScrollGridView) this.findViewById(R.id.imageList);
	}

	private void initData() {
		adapter = new CircleAdapterNoScrollGridAdapter(this, data);
		imageList.setAdapter(adapter);
	}

	private void initEvent() {
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 发布
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String namicTitleString = namicTitle.getText().toString();
				String commentString = comment.getText().toString();

				if (dataPost == null || dataPost.size() == 0 || "".equals(namicTitleString) || "".equals(commentString)) {
					Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "标题、内容或图片不能为空", Toast.LENGTH_LONG).show();
				} else {
					String imageList = "";
					if (dataPost != null || dataPost.size() > 0) {
						imageList += "[";
						for (int i = 0; i < dataPost.size(); i++) {
							CircleListImageListEntity post = (CircleListImageListEntity) dataPost.get(i);
							imageList += "\"" + post.getImg_un() + "\"";
							if (i + 1 != data.size()) {
								imageList += ",";
							}
						}
						imageList += "]";
					}

					if (dialog == null)
						dialog = new LoadingDialog(CampaignDynamicInfoImageAddActivity.this);
					dialog.show("残所作品提交中，请稍候…");

					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("imageJson", imageList);
					headerList.put("title", namicTitleString);
					headerList.put("content", commentString);
					headerList.put("pid", id);
					HttpRequest http = new HttpRequest(CampaignDynamicInfoImageAddActivity.this, headerList, Posthandler);
					http.execute(getResources().getString(R.string.config_host_url) + "stretch.php?action=addImage&token="
							+ DataCache.get(CampaignDynamicInfoImageAddActivity.this).getAsString("token"));
				}
			}
		});

		// 增加照片
		add_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(CampaignDynamicInfoImageAddActivity.this).setTitle("选择加相片方式")
						.setItems(addList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) { // 相册选择
							new AlbumComponentDynamic(CampaignDynamicInfoImageAddActivity.this,
									new AlbumComponentDynamic.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(CampaignDynamicInfoImageAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void component(String path, String file) {
									if (data != null && data.size() < 6) {
										CircleListImageListEntity listData = new CircleListImageListEntity();
										listData.setImg_180(
												getResources().getString(R.string.config_image_host_url) + path + "180_" + file);
										listData.setImg_550(
												getResources().getString(R.string.config_image_host_url) + path + "550_" + file);
										listData.setImg_un(getResources().getString(R.string.config_image_host_url) + path + file);
										data.add(listData);

										CircleListImageListEntity listDataPost = new CircleListImageListEntity();
										listDataPost.setImg_180(path + "180_" + file);
										listDataPost.setImg_550(path + "550_" + file);
										listDataPost.setImg_un(path + file);
										dataPost.add(listDataPost);

										if (adapter != null)
											adapter.notifyDataSetChanged();
									} else {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "一次只能拍六张照片哦", Toast.LENGTH_SHORT).show();
									}
								}
							}).showSample(CampaignDynamicInfoImageAddActivity.this);
						} else if (which == 1) { // 拍照
							new CameraComponentDynamic(CampaignDynamicInfoImageAddActivity.this,
									new CameraComponentDynamic.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(CampaignDynamicInfoImageAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void component(String path, String file) {
									if (data != null && data.size() < 6) {
										CircleListImageListEntity listData = new CircleListImageListEntity();
										listData.setImg_180(
												getResources().getString(R.string.config_image_host_url) + path + "180_" + file);
										listData.setImg_550(
												getResources().getString(R.string.config_image_host_url) + path + "550_" + file);
										listData.setImg_un(getResources().getString(R.string.config_image_host_url) + path + file);
										data.add(listData);

										CircleListImageListEntity listDataPost = new CircleListImageListEntity();
										listDataPost.setImg_180(path + "180_" + file);
										listDataPost.setImg_550(path + "550_" + file);
										listDataPost.setImg_un(path + file);
										dataPost.add(listDataPost);

										if (adapter != null)
											adapter.notifyDataSetChanged();
									} else {
										Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "一次只能拍六张照片哦", Toast.LENGTH_SHORT).show();
									}
								}
							}).showSample(CampaignDynamicInfoImageAddActivity.this);
						}
					}
				}).show();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler Posthandler = new Handler() {
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
							Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "参赛作品发布成功！", Toast.LENGTH_SHORT).show();
							finish();
						} else if (result == 1) {
							Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CampaignDynamicInfoImageAddActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						} else {
							String error = jsonObject.optString("errMsg");
							Toast.makeText(CampaignDynamicInfoImageAddActivity.this, error, Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CampaignDynamicInfoImageAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
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
