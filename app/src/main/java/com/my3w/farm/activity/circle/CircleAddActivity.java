package com.my3w.farm.activity.circle;

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
import com.my3w.farm.comment.tusdk.examples.component.AlbumComponentTopic;
import com.my3w.farm.comment.tusdk.examples.suite.CameraComponentTopic;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.system.Network;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CircleAddActivity extends baseActivity {

	private TextView back_btn;
	private TextView add_btn;
	private TextView add_images;
	private EditText comment;
	private NoScrollGridView imageList;

	private CircleAdapterNoScrollGridAdapter adapter;
	private ArrayList<CircleListImageListEntity> data = new ArrayList<CircleListImageListEntity>();
	private ArrayList<CircleListImageListEntity> dataPost = new ArrayList<CircleListImageListEntity>();

	private String path;
	private String file;

	private String[] addList = { "相册选择", "相机拍摄" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_add);

		path = this.getIntent().getStringExtra("path");
		file = this.getIntent().getStringExtra("file");

		initView();
		initData();
		initEvent();
	}

	private void initView() {
		back_btn = (TextView) this.findViewById(R.id.back_btn);
		add_btn = (TextView) this.findViewById(R.id.add_btn);
		add_images = (TextView) this.findViewById(R.id.add_images);
		comment = (EditText) this.findViewById(R.id.comment);
		imageList = (NoScrollGridView) this.findViewById(R.id.imageList);
	}

	private void initData() {
		adapter = new CircleAdapterNoScrollGridAdapter(this, data);
		imageList.setAdapter(adapter);

		CircleListImageListEntity listData = new CircleListImageListEntity();
		listData.setImg_180(getResources().getString(R.string.config_image_host_url) + path + "180_" + file);
		listData.setImg_550(getResources().getString(R.string.config_image_host_url) + path + "550_" + file);
		listData.setImg_un(getResources().getString(R.string.config_image_host_url) + path + file);
		data.add(listData);

		CircleListImageListEntity listDataPost = new CircleListImageListEntity();
		listDataPost.setImg_180(path + "180_" + file);
		listDataPost.setImg_550(path + "550_" + file);
		listDataPost.setImg_un(path + file);
		dataPost.add(listDataPost);
	}

	private void initEvent() {
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CircleAddActivity.this, CircleActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 发布
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment_string = comment.getText().toString();

				if (!comment_string.equals("")) {
					String imageList = "";
					if (dataPost != null || dataPost.size() > 0) {
						imageList += "[";
						for (int i = 0; i < dataPost.size(); i++) {
							CircleListImageListEntity post = (CircleListImageListEntity) dataPost.get(i);
							imageList += "{\"_raw\":\"" + post.getImg_550() + "," + post.getImg_180() + "," + post.getImg_un() + "\"}";
							if (i + 1 != data.size()) {
								imageList += ",";
							}
						}
						imageList += "]";
					}

					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("images", imageList);
					headerList.put("content", comment.getText().toString());
					headerList.put("ip", Network.getIPAddress(true));
					HttpRequest http = new HttpRequest(CircleAddActivity.this, headerList, Submithandler);
					http.execute(getResources().getString(R.string.config_host_url) + "topic.php?action=add&token="
							+ DataCache.get(CircleAddActivity.this).getAsString("token"));
				} else {
					Toast.makeText(CircleAddActivity.this, "请说点什么！", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 增加照片
		add_images.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(CircleAddActivity.this).setTitle("选择加相片方式")
						.setItems(addList, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) { // 相册选择
							new AlbumComponentTopic(CircleAddActivity.this, new AlbumComponentTopic.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CircleAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(CircleAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CircleAddActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
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
										Toast.makeText(CircleAddActivity.this, "一次只能拍六张照片哦", Toast.LENGTH_SHORT).show();
									}
								}
							}).showSample(CircleAddActivity.this);
						} else if (which == 1) { // 拍照
							new CameraComponentTopic(CircleAddActivity.this, new CameraComponentTopic.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CircleAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(CircleAddActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CircleAddActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
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
										Toast.makeText(CircleAddActivity.this, "一次只能拍六张照片哦", Toast.LENGTH_SHORT).show();
									}
								}
							}).showSample(CircleAddActivity.this);
						}
					}
				}).show();
			}
		});
	}

	// 提交回复
	@SuppressLint("HandlerLeak")
	private Handler Submithandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						Intent intent = new Intent(CircleAddActivity.this, CircleActivity.class);
						intent.putExtra("path", path);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					} else if (result == 1) {
						Toast.makeText(CircleAddActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CircleAddActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(CircleAddActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(CircleAddActivity.this, CircleActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
