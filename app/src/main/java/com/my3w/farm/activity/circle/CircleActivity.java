package com.my3w.farm.activity.circle;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.circle.adapter.CircleAdapter;
import com.my3w.farm.activity.circle.adapter.CircleAdapter.onclickItemListener;
import com.my3w.farm.activity.circle.entity.CircleListCommentListEntity;
import com.my3w.farm.activity.circle.entity.CircleListEntity;
import com.my3w.farm.activity.circle.entity.CircleListImageListEntity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.comment.image.ImageMagent;
import com.my3w.farm.comment.tusdk.examples.component.AlbumComponentTopic;
import com.my3w.farm.comment.tusdk.examples.suite.CameraComponentTopic;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.system.Device;
import com.westars.framework.view.image.RoundImageView;
import com.westars.framework.view.list.ListExtView;
import com.westars.framework.view.list.ListExtView.onListViewListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 友菜圈
 * 
 * @author Administrator
 *
 */
public class CircleActivity extends baseActivity {

	private ImageView title_back;
	private TextView title_name;
	private ImageView title_other;

	// hander
	private ImageView back;
	private RoundImageView user_icon;
	private TextView user_name;

	private ArrayList<CircleListEntity> list = new ArrayList<CircleListEntity>();
	private CircleAdapter adapter;
	private ListExtView listview;
	private LinearLayout bottom1;
	private EditText repay;
	private Button add_btn;
	private Button clear_btn;

	private String page = "1";

	private String pid = "0";
	private String tid = "0";
	
	private String[] addList = {"相册选择", "相机拍摄"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle);

		initView();
		initData();
		initEvent();
	}

	@SuppressLint("InflateParams")
	private void initView() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_name = (TextView) findViewById(R.id.title_name);
		title_name.setText("友菜圈");
		title_other = (ImageView) findViewById(R.id.title_other);
		title_other.setVisibility(View.VISIBLE);
		title_other.setImageResource(R.drawable.k);

		bottom1 = (LinearLayout) findViewById(R.id.bottom1);
		repay = (EditText) findViewById(R.id.repay);
		add_btn = (Button) findViewById(R.id.add_btn);
		clear_btn = (Button) findViewById(R.id.clear_btn);

		listview = (ListExtView) findViewById(R.id.listview);
		listview.setShowHeaderView(true);
		listview.setShowFooterView(true);

		adapter = new CircleAdapter(this, list);
		listview.setAdapter(adapter);

		View view = LayoutInflater.from(this).inflate(R.layout.activity_circle_list_head_view, null);
		back = (ImageView) view.findViewById(R.id.back);
		RelativeLayout.LayoutParams viewParams = (RelativeLayout.LayoutParams) back.getLayoutParams();
		viewParams.width = Device.getScreenWidth(this);
		viewParams.height = Device.getScreenWidth(this) / 10 * 6;
		back.setLayoutParams(viewParams);
		user_icon = (RoundImageView) view.findViewById(R.id.user_icon);
		String user_picString = DataCache.get(CircleActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, user_icon);

		user_name = (TextView) view.findViewById(R.id.user_name);
		String user_nickString = DataCache.get(CircleActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			user_name.setText(user_nickString);
		else
			user_name.setText("");

		listview.addHeaderView(view);
	}

	private void initData() {
		HttpRequest http = new HttpRequest(this, handler);
		http.execute(getResources().getString(R.string.config_host_url) + "topic.php?action=list&token="
				+ DataCache.get(this).getAsString("token"));
	}

	private void getToolsIndex() {
		String url = getResources().getString(R.string.config_host_url) + "topic.php?action=list&token="
				+ DataCache.get(this).getAsString("token") + "&page=" + page;
		Log.e("xxxx", url);
		HttpRequest http = new HttpRequest(this, dataHandler);
		http.execute(url);
	}

	private void initEvent() {

		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});

		// 发布
		title_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(CircleActivity.this).setTitle("选择发布方式")
						.setItems(addList, new DialogInterface.OnClickListener() {
		
					public void onClick(DialogInterface dialog, int which) {
						if(which == 0){ //相册选择
							new AlbumComponentTopic(CircleActivity.this, new AlbumComponentTopic.onComponentListener() {
								
								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CircleActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(CircleActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CircleActivity.this, "相册出了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}
								
								@Override
								public void component(String path, String file) {
									Intent intent = new Intent(CircleActivity.this, CircleAddActivity.class);
									intent.putExtra("path", path);
									intent.putExtra("file", file);
									startActivity(intent);
									finish();
								}
							}).showSample(CircleActivity.this);
						} else if(which == 1){ //拍照
							new CameraComponentTopic(CircleActivity.this, new CameraComponentTopic.onComponentListener() {

								@Override
								public void error(int val) {
									if (val == -1) {
										Toast.makeText(CircleActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(CircleActivity.this, LoginActivity.class);
										startActivity(intent);
										sendBroadcast(new Intent("finish"));
										overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
									} else {
										Toast.makeText(CircleActivity.this, "拍照除了点小问题，请重试", Toast.LENGTH_LONG).show();
									}
								}

								@Override
								public void component(String path, String file) {
									Intent intent = new Intent(CircleActivity.this, CircleAddActivity.class);
									intent.putExtra("path", path);
									intent.putExtra("file", file);
									startActivity(intent);
									finish();
								}
							}).showSample(CircleActivity.this);
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

		// 显示回复
		adapter.setOnClickItemListener(new onclickItemListener() {

			@Override
			public void clickItem(int position, int index) {
				bottom1.setVisibility(View.VISIBLE);
				repay.setText("");
				tid = String.valueOf(list.get(position).getId());
				pid = String.valueOf(list.get(position).getCommentlist().get(index).getId());

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(repay, InputMethodManager.SHOW_FORCED);
			}

			@Override
			public void clickUser(int uid) {
				// TODO 显示用户信息
				Toast.makeText(CircleActivity.this, "用户ID" + uid, Toast.LENGTH_LONG).show();
			}

			@Override
			public void clickDItem(int position) {
				bottom1.setVisibility(View.VISIBLE);
				repay.setText("");
				tid = String.valueOf(list.get(position).getId());
				pid = "0";

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(repay, InputMethodManager.SHOW_FORCED);
			}
		});

		// 隐藏
		clear_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				bottom1.setVisibility(View.GONE);

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(repay.getWindowToken(), 0);
			}
		});

		// 回复
		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!repay.getText().toString().equals("")) {
					HashMap<String, String> headerList = new HashMap<String, String>();
					headerList.put("tid", tid);
					headerList.put("pid", pid);
					headerList.put("content", repay.getText().toString());
					HttpRequest http = new HttpRequest(CircleActivity.this, headerList, Submithandler);
					http.execute(getResources().getString(R.string.config_host_url) + "topic.php?action=repay&token="
							+ DataCache.get(CircleActivity.this).getAsString("token"));
				} else {
					bottom1.setVisibility(View.GONE);
				}
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

						// 刷新数据
						page = "1";
						getToolsIndex();

						bottom1.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(repay.getWindowToken(), 0);
					} else if (result == 1) {
						Toast.makeText(CircleActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CircleActivity.this, LoginActivity.class);
						startActivity(intent);
						sendBroadcast(new Intent("finish"));
						overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(CircleActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
			}
		};
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
								CircleListEntity data = new CircleListEntity();
								data.setId(jsonObjects.optInt("id"));
								data.setComcount(jsonObjects.optLong("comcount"));
								data.setContent(jsonObjects.optString("content"));
								data.setUid(jsonObjects.optInt("uid"));
								data.setUsericon(jsonObjects.optString("usericon"));
								data.setUsername(jsonObjects.optString("username"));

								// 图片列表
								ArrayList<CircleListImageListEntity> imageList = new ArrayList<CircleListImageListEntity>();
								JSONArray jsonImageList = jsonObjects.optJSONArray("imagelist");
								if (jsonImageList != null) {
									for (int j = 0; j < jsonImageList.length(); j++) {
										JSONObject jsonImageObjects = jsonImageList.optJSONObject(j);
										CircleListImageListEntity imageData = new CircleListImageListEntity();
										imageData.setImg_180(jsonImageObjects.optString("img_180"));
										imageData.setImg_550(jsonImageObjects.optString("img_550"));
										imageData.setImg_un(jsonImageObjects.optString("img_un"));
										imageList.add(imageData);
									}
									data.setImagelist(imageList);
								}

								// 评论列表
								ArrayList<CircleListCommentListEntity> commentList = new ArrayList<CircleListCommentListEntity>();
								JSONArray jsonCommentList = jsonObjects.optJSONArray("commentlist");
								if (jsonCommentList != null) {
									for (int j = 0; j < jsonCommentList.length(); j++) {
										JSONObject jsonCommentObjects = jsonCommentList.optJSONObject(j);
										CircleListCommentListEntity commentData = new CircleListCommentListEntity();
										commentData.setId(jsonCommentObjects.optInt("id"));
										commentData.setContent(jsonCommentObjects.optString("content"));
										commentData.setUser(jsonCommentObjects.optString("user"));
										commentData.setUserid(jsonCommentObjects.optInt("userid"));
										commentData.setUserto(jsonCommentObjects.optString("userto"));
										commentData.setUsertoid(jsonCommentObjects.optInt("usertoid"));
										commentList.add(commentData);
									}
									data.setCommentlist(commentList);
								}

								list.add(data);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CircleActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CircleActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CircleActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CircleActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
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
							if (list != null)
								list.clear();

						jsonObject = new JSONObject(json);
						int result = jsonObject.optInt("errCode");
						if (result == 0) {

							JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObjects = jsonArray.optJSONObject(i);
								CircleListEntity data = new CircleListEntity();
								data.setId(jsonObjects.optInt("id"));
								data.setComcount(jsonObjects.optLong("comcount"));
								data.setContent(jsonObjects.optString("content"));
								data.setUid(jsonObjects.optInt("uid"));
								data.setUsericon(jsonObjects.optString("usericon"));
								data.setUsername(jsonObjects.optString("username"));

								// 图片列表
								ArrayList<CircleListImageListEntity> imageList = new ArrayList<CircleListImageListEntity>();
								JSONArray jsonImageList = jsonObjects.optJSONArray("imagelist");
								if (jsonImageList != null) {
									for (int j = 0; j < jsonImageList.length(); j++) {
										JSONObject jsonImageObjects = jsonImageList.optJSONObject(j);
										CircleListImageListEntity imageData = new CircleListImageListEntity();
										imageData.setImg_180(jsonImageObjects.optString("img_180"));
										imageData.setImg_550(jsonImageObjects.optString("img_550"));
										imageData.setImg_un(jsonImageObjects.optString("img_un"));
										imageList.add(imageData);
									}
									data.setImagelist(imageList);
								}

								// 评论列表
								ArrayList<CircleListCommentListEntity> commentList = new ArrayList<CircleListCommentListEntity>();
								JSONArray jsonCommentList = jsonObjects.optJSONArray("commentlist");
								if (jsonCommentList != null) {
									for (int j = 0; j < jsonCommentList.length(); j++) {
										JSONObject jsonCommentObjects = jsonCommentList.optJSONObject(j);
										CircleListCommentListEntity commentData = new CircleListCommentListEntity();
										commentData.setId(jsonCommentObjects.optInt("id"));
										commentData.setContent(jsonCommentObjects.optString("content"));
										commentData.setUser(jsonCommentObjects.optString("user"));
										commentData.setUserid(jsonCommentObjects.optInt("userid"));
										commentData.setUserto(jsonCommentObjects.optString("userto"));
										commentData.setUsertoid(jsonCommentObjects.optInt("usertoid"));
										commentList.add(commentData);
									}
									data.setCommentlist(commentList);
								}

								list.add(data);
							}

							if (adapter != null)
								adapter.notifyDataSetChanged();

						} else if (result == 1) {
							Toast.makeText(CircleActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(CircleActivity.this, LoginActivity.class);
							startActivity(intent);
							sendBroadcast(new Intent("finish"));
							overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
						}
					} catch (JSONException e) {
						Toast.makeText(CircleActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
					}
				}
			} else {
				Toast.makeText(CircleActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
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
			Intent intent = new Intent(CircleActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}