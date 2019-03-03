package com.my3w.farm.activity.plant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hichip.callback.ICameraIOSessionCallback;
import com.hichip.callback.ICameraPlayStateCallback;
import com.hichip.content.HiChipDefines;
import com.hichip.control.HiCamera;
import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.circle.CircleActivity;
import com.my3w.farm.activity.game.borrow.BorrowActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.plant.PlantDialog.OnSuccessListener;
import com.my3w.farm.activity.plant.adapter.PlantDailogAdapter;
import com.my3w.farm.activity.shop.ShopLandActivity;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.comment.camhi.base.MyLiveViewGLMonitor;
import com.my3w.farm.comment.camhi.bean.HiDataValue;
import com.my3w.farm.comment.camhi.bean.MyCamera;
import com.my3w.farm.comment.image.ImageMagent;

import com.hichip.tools.HiSearchSDK;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.system.Device;
import com.westars.framework.util.toast.Toasts;
import com.westars.framework.view.image.RoundImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 种植
 *
 * @author Administrator
 */
public class PlantActivity extends baseActivity {

    private static final int CAMERA_IO_SESSION_HANDLER = 2;

    private static final int CAMERA_PLAY_STATE = 3;


    public static final int FILL_REQUEST_TIME = 120;

    private int IndexLandID;

    private String IndexLand;

    private String[] LandList;

    private String[] CameraList;

    private String[] CameraUid;

    private String[] CameraUserName;

    private String[] CameraPassword;

    private LinearLayout usercenter;

    private TextView user_nick;

    private RoundImageView user_pic;

    private LinearLayout plant_button;
    private LinearLayout youxi_button;
    private LinearLayout youcaiquan_button;

    private LinearLayout activity_title;
    private LinearLayout landlist;
    private TextView landtext;
    private TextView add_uid;
    private ImageView open_light;

    private ImageView mature;
    private TextView matureDay;
    private TextView select_sends;

    private String[] SeedList;
    private String[] SeedListDid;
    private String IndexSeedName;
    private String IndexSeedDid;

    private AlertDialog parkUidDialog;
    private View bottomView;
    private ListView cameraList;
    private PlantDailogAdapter plantDailogAdapter;
    private ArrayList<String> uidList = new ArrayList<>();

    private RelativeLayout video_root;
    private TextView full_btn;
    private MyLiveViewGLMonitor mVideoView;
    private ProgressBar video_loading_progress;
    private TextView video_loading_text;

    private MyCamera mCamera;

    private boolean isFullScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plant);

        initView();
        initData();
        initEvent();
    }

    private void initView() {

        usercenter = (LinearLayout) findViewById(R.id.usercenter);

        user_nick = (TextView) findViewById(R.id.user_nick);
        String user_nickString = DataCache.get(PlantActivity.this).getAsString("user_nick");
        if (user_nickString != null && !user_nickString.equals(""))
            user_nick.setText(user_nickString);
        else
            user_nick.setText(user_nickString);

        user_pic = (RoundImageView) findViewById(R.id.user_pic);
        String user_picString = DataCache.get(PlantActivity.this).getAsString("user_pic");
        if (user_picString != null && !user_picString.equals(""))
            ImageMagent.getInstance().displayImage(user_picString, user_pic);

        activity_title = (LinearLayout) findViewById(R.id.activity_title);
        plant_button = (LinearLayout) findViewById(R.id.plant_button);
        landlist = (LinearLayout) findViewById(R.id.landlist);
        landtext = (TextView) findViewById(R.id.landtext);
        add_uid = (TextView) findViewById(R.id.add_uid);
        open_light = (ImageView) findViewById(R.id.open_light);

        youxi_button = (LinearLayout) findViewById(R.id.youxi_button);
        youcaiquan_button = (LinearLayout) findViewById(R.id.youcaiquan_button);

        mature = (ImageView) findViewById(R.id.mature);
        matureDay = (TextView) findViewById(R.id.matureDay);
        select_sends = (TextView) findViewById(R.id.select_sends);

        video_root = (RelativeLayout) findViewById(R.id.video_root);
        activity_title.setVisibility(View.VISIBLE);
        int width = Device.getScreenWidth(PlantActivity.this);
        int height = (int) (width / 3 * 1.7);
        video_root.setLayoutParams(new LinearLayout.LayoutParams(width, height));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        video_loading_progress = (ProgressBar) findViewById(R.id.video_loading_progress);
        video_loading_text = (TextView) findViewById(R.id.video_loading_text);

        full_btn = (TextView) findViewById(R.id.full_btn);

        mVideoView = (MyLiveViewGLMonitor) findViewById(R.id.video);

        bottomView = View.inflate(this, R.layout.activity_plant_camera_dialog, null);//填充ListView布局
        cameraList = (ListView) bottomView.findViewById(R.id.list_view);//初始化ListView控件
        plantDailogAdapter = new PlantDailogAdapter(this, uidList);
        cameraList.setAdapter(plantDailogAdapter);//ListView设置适配器

    }

    private void initData() {
        HiSearchSDK hiSearchSDK = new HiSearchSDK(new HiSearchSDK.OnSearchResult() {
            @Override
            public void searchResult(List<HiSearchSDK.HiSearchResult> list) {
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        HiSearchSDK.HiSearchResult hiSearchResult = list.get(i);
                        uidList.add(hiSearchResult.uid);
                    }
                    plantDailogAdapter.notifyDataSetChanged();
                }
            }
        });
        hiSearchSDK.search2();
        hiSearchSDK.search();

        //获取种子信息
        getLand();
    }

    private void initEvent() {

        // 个人中心
        usercenter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantActivity.this, UserCenterActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 选择土地
        landlist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (LandList != null && LandList.length > 0) {
                    new AlertDialog.Builder(PlantActivity.this).setTitle("点击选择土地").setItems(LandList, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            IndexLandID = which;
                            IndexLand = LandList[which];
                            landtext.setText(IndexLand);

                            createCamera(CameraUid[which], CameraUserName[which], CameraPassword[which], IndexLand);
                        }
                    }).show();
                } else {
                    Toasts.showToast(PlantActivity.this, "土地获取中，请稍后……");
                }
            }
        });

        // 开灯（补光）
        open_light.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HttpRequest http = new HttpRequest(PlantActivity.this, heatinghandler);
                http.execute(getResources().getString(R.string.config_host_url) + "control.php?action=wendu&token=" + DataCache.get(PlantActivity.this).getAsString("token") + "&keynum=" + IndexLand + "&add_wendu=" + FILL_REQUEST_TIME);
            }
        });

        // 选择做的事
        plant_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new PlantDialog(PlantActivity.this, new OnSuccessListener() {

                    @Override
                    public void success(int what) {
                        if (what == PlantDialog.WATERING_FLAG) {
                            Toasts.showToast(PlantActivity.this, "浇水成功");
                        } else if (what == PlantDialog.FILL_FLAG) {
                            Toasts.showToast(PlantActivity.this, "补光成功");
                        } else if (what == PlantDialog.APPLY_FLAG) {
                            Toasts.showToast(PlantActivity.this, "施肥申请成功，请等待工人施工");
                        }
                    }

                    @Override
                    public void error(int what) {
                        if (what == PlantDialog.WATERING_FLAG) {
                            Toasts.showToast(PlantActivity.this, "浇水失败，请重试");
                        } else if (what == PlantDialog.FILL_FLAG) {
                            Toasts.showToast(PlantActivity.this, "补光失败，请重试");
                        } else if (what == PlantDialog.APPLY_FLAG) {
                            Toasts.showToast(PlantActivity.this, "施肥失败，请重试");
                        }
                    }
                }, IndexLand).show();
            }
        });

        // 选择种子
        select_sends.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SeedList != null && SeedList.length > 0) {
                    new AlertDialog.Builder(PlantActivity.this).setTitle("点击选择种子").setItems(SeedList, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            IndexSeedName = SeedList[which];
                            IndexSeedDid = SeedListDid[which];
                            select_sends.setText(SeedList[which]);

                            PlantActivity.this.getSendsSuccess(IndexSeedDid);
                        }
                    }).show();
                } else {
                    Toast.makeText(PlantActivity.this, "土地还没有种任何种子，快去种点吧", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 游戏
        youxi_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantActivity.this, BorrowActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 友菜圈
        youcaiquan_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlantActivity.this, CircleActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 是否全屏
        full_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFullScreen) {
                    activity_title.setVisibility(View.VISIBLE);
                    int width = Device.getScreenHeight(PlantActivity.this);
                    int height = (int) (width / 3 * 1.7);

                    LinearLayout.LayoutParams fullScreenLayoutParams = (LinearLayout.LayoutParams) video_root.getLayoutParams();
                    fullScreenLayoutParams.width = width;
                    fullScreenLayoutParams.height = height;
                    video_root.setLayoutParams(fullScreenLayoutParams);

                    RelativeLayout.LayoutParams fullScreenVideoLyaoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                    fullScreenVideoLyaoutParams.width = width;
                    fullScreenVideoLyaoutParams.height = height;
                    mVideoView.setLayoutParams(fullScreenVideoLyaoutParams);

                    full_btn.setText("进入全屏");

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    isFullScreen = false;
                } else {
                    activity_title.setVisibility(View.GONE);
                    int width = Device.getScreenHeight(PlantActivity.this) + getStatusBarHeight(PlantActivity.this) / 3;
                    int height = Device.getScreenWidth(PlantActivity.this) - getStatusBarHeight(PlantActivity.this);

                    LinearLayout.LayoutParams fullScreenLayoutParams = (LinearLayout.LayoutParams) video_root.getLayoutParams();
                    fullScreenLayoutParams.width = width;
                    fullScreenLayoutParams.height = height;
                    video_root.setLayoutParams(fullScreenLayoutParams);

                    RelativeLayout.LayoutParams fullScreenVideoLayoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
                    fullScreenVideoLayoutParams.width = width;
                    fullScreenVideoLayoutParams.height = height;
                    mVideoView.setLayoutParams(fullScreenVideoLayoutParams);

                    full_btn.setText("退出全屏");

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isFullScreen = true;
                }
            }
        });

        //手动添加UID播放
        add_uid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parkUidDialog == null) {
                    parkUidDialog = new AlertDialog.Builder(PlantActivity.this)
                            .setTitle("选择UID").setView(bottomView)
                            .setPositiveButton("返回到土地", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createCamera(CameraUid[IndexLandID], CameraUserName[IndexLandID], CameraPassword[IndexLandID], LandList[IndexLandID]);
                                    parkUidDialog.dismiss();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    parkUidDialog.dismiss();
                                }
                            }).create();
                }
                parkUidDialog.show();
            }
        });

        //选择UID
        cameraList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = uidList.get(position);
                createCamera(uid, "admin", "admin");
                if (parkUidDialog != null)
                    parkUidDialog.dismiss();
            }
        });
    }

    //获取种子信息
    private void getLand() {
        HttpRequest http = new HttpRequest(PlantActivity.this, PlantActivity.this.SelectLandhandler);
        http.execute(getResources().getString(R.string.config_host_url) + "control.php?action=getland&token=" + DataCache.get(PlantActivity.this).getAsString("token"));
    }

    // 获取种子
    private void getSends(String keyon) {
        Log.e("xxx", PlantActivity.this.getResources().getString(R.string.config_host_url) + "control.php?action=getseed&token=" + DataCache.get(PlantActivity.this).getAsString("token") + "&keynum=" + keyon);
        HttpRequest httpseed = new HttpRequest(PlantActivity.this, seedApiHandler);
        httpseed.execute(PlantActivity.this.getResources().getString(R.string.config_host_url) + "control.php?action=getseed&token=" + DataCache.get(PlantActivity.this).getAsString("token") + "&keynum=" + keyon);
    }

    // 获取种子成熟期
    private void getSendsSuccess(String seedsId) {
        HttpRequest httpseed = new HttpRequest(PlantActivity.this, seedSuccessApiHandler);
        httpseed.execute(PlantActivity.this.getResources().getString(R.string.config_host_url) + "control.php?action=mature&token=" + DataCache.get(PlantActivity.this).getAsString("token") + "&seedsId=" + seedsId);
    }

    // 开灯handler
    @SuppressLint("HandlerLeak")
    private Handler heatinghandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                open_light.setImageResource(R.drawable.closelight);
            } else if (msg.what == 200) {
                String json = (String) msg.obj;
                if (json != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(json);
                        int result = jsonObject.optInt("errCode");
                        if (result == 0) {
                            open_light.setImageResource(R.drawable.openlight);

                            // 开始记时关闭
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(FILL_REQUEST_TIME * 1000);
                                        heatinghandler.sendEmptyMessage(1);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        } else if (result == 1) {
                            Toast.makeText(PlantActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PlantActivity.this, LoginActivity.class);
                            startActivity(intent);
                            sendBroadcast(new Intent("finish"));
                            overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                        }
                    } catch (JSONException e) {

                    }
                }
                Log.e("my3w", json);
            } else {
                Toast.makeText(PlantActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    // 选择土地
    @SuppressLint("HandlerLeak")
    public Handler SelectLandhandler = new Handler() {
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
                            LandList = new String[jsonArray.length()];
                            CameraList = new String[jsonArray.length()];
                            CameraUid = new String[jsonArray.length()];
                            CameraUserName = new String[jsonArray.length()];
                            CameraPassword = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.optJSONObject(i);
                                LandList[i] = jsonObj.optString("no");
                                CameraList[i] = jsonObj.optString("camera_ip");
                                CameraUid[i] = jsonObj.optString("camera_uid");
                                CameraUserName[i] = jsonObj.optString("camera_username");
                                CameraPassword[i] = jsonObj.optString("camera_password");
                            }

                            // 设置默认土地信息
                            IndexLandID = 0;
                            IndexLand = LandList[0];
                            landtext.setText(IndexLand);

                            // 获取土地种植种子信息
                            PlantActivity.this.getSends(IndexLand);

                            Log.e("hichip", "cameralist" + CameraList[0] + ", " + CameraUid[0] + ":" + CameraUserName[0] + "--" + CameraPassword[0]);
                            createCamera(CameraUid[0], CameraUserName[0], CameraPassword[0], CameraList[0]);
                        } else {
                            Toast.makeText(PlantActivity.this, "还没有土地，快去买一块吧", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PlantActivity.this, ShopLandActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                        }
                    } else if (result == 1) {
                        Toast.makeText(PlantActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlantActivity.this, LoginActivity.class);
                        startActivity(intent);
                        sendBroadcast(new Intent("finish"));
                        overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(PlantActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
            }
        }
    };

    // 种子成熟期
    @SuppressLint("HandlerLeak")
    private Handler seedSuccessApiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200) {
                String json = (String) msg.obj;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(json);
                    int result = jsonObject.optInt("errCode");
                    if (result == 0) {
                        String jsonImage = jsonObject.optString("errMsg");
                        ImageMagent.getInstance().displayImage(jsonImage, mature);
                        String jsonDay = jsonObject.optString("errDay");
                        matureDay.setText(jsonDay);
                    } else if (result == 1) {
                        Toast.makeText(PlantActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlantActivity.this, LoginActivity.class);
                        startActivity(intent);
                        sendBroadcast(new Intent("finish"));
                        overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(PlantActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlantActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
            }
        }
    };

    // 获取种子列表
    @SuppressLint("HandlerLeak")
    private Handler seedApiHandler = new Handler() {
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
                            if (jsonArray != null && jsonArray.length() > 0) {
                                SeedList = new String[jsonArray.length()];
                                SeedListDid = new String[jsonArray.length()];
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObj = jsonArray.optJSONObject(i);
                                    SeedList[i] = jsonObj.optString("title");
                                    SeedListDid[i] = jsonObj.optString("did");
                                }
                                IndexSeedName = SeedList[0];
                                IndexSeedDid = SeedListDid[0];
                                select_sends.setText(IndexSeedName);

                                PlantActivity.this.getSendsSuccess(IndexSeedDid);
                            } else {
                                select_sends.setText("未种植种子");
                            }
                        } else if (result == 1) {
                            Toast.makeText(PlantActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PlantActivity.this, LoginActivity.class);
                            startActivity(intent);
                            sendBroadcast(new Intent("finish"));
                            overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                        } else {
                            select_sends.setText("未种植种子");
                        }
                    } catch (JSONException e) {
                        select_sends.setText("未种植种子");
                    }
                }
                Log.e("my3w", json);
            } else {
                Toast.makeText(PlantActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler cameraHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PlantActivity.CAMERA_IO_SESSION_HANDLER) {
                switch (msg.arg1) {
                    case HiCamera.CAMERA_CONNECTION_STATE_DISCONNECTED:
                        video_loading_progress.setVisibility(View.GONE);
                        video_loading_text.setVisibility(View.VISIBLE);
                        video_loading_text.setText("摄像头断开连接");
                        break;
                    case HiCamera.CAMERA_CONNECTION_STATE_CONNECTING:
                        video_loading_progress.setVisibility(View.VISIBLE);
                        video_loading_text.setVisibility(View.VISIBLE);
                        video_loading_text.setText("摄像头连接中…");
                        break;
                    case HiCamera.CAMERA_CONNECTION_STATE_WRONG_PASSWORD:
                        video_loading_progress.setVisibility(View.GONE);
                        video_loading_text.setVisibility(View.VISIBLE);
                        video_loading_text.setText("摄像头账号密码错误");
                        break;
                    case HiCamera.CAMERA_CONNECTION_STATE_LOGIN:
                        if (mCamera != null)
                            mCamera.startLiveShow(mCamera.getVideoQuality(), mVideoView);
                        break;
                }
            } else if (msg.what == PlantActivity.CAMERA_PLAY_STATE) {
                switch (msg.arg1) {
                    case ICameraPlayStateCallback.PLAY_STATE_START:
                        video_loading_progress.setVisibility(View.INVISIBLE);
                        video_loading_text.setVisibility(View.INVISIBLE);
                        break;
                    case ICameraPlayStateCallback.PLAY_STATE_EDN:
                        break;
                    case ICameraPlayStateCallback.PLAY_STATE_POS:
                        break;
                    case ICameraPlayStateCallback.PLAY_STATE_RECORDING_END:
                        break;
                    case ICameraPlayStateCallback.PLAY_STATE_RECORDING_START:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    private void createCamera(String uid, String username, String password, String keyon) {
        if (mCamera == null) {
            mCamera = new MyCamera(PlantActivity.this, "Camera", uid, username, password);
            mCamera.registerIOSessionListener(new ICameraIOSessionCallback() {
                @Override
                public void receiveSessionState(HiCamera hiCamera, int i) {
                    Message msg = cameraHandler.obtainMessage();
                    msg.what = PlantActivity.CAMERA_IO_SESSION_HANDLER;
                    msg.arg1 = i;
                    cameraHandler.sendMessage(msg);
                }

                @Override
                public void receiveIOCtrlData(HiCamera hiCamera, int i, byte[] bytes, int i1) {

                }
            });
            mCamera.registerPlayStateListener(new ICameraPlayStateCallback() {
                @Override
                public void callbackState(HiCamera hiCamera, int i, int i1, int i2) {
                    Message msg = cameraHandler.obtainMessage();
                    msg.what = PlantActivity.CAMERA_PLAY_STATE;
                    msg.arg1 = i;
                    cameraHandler.sendMessage(msg);
                }

                @Override
                public void callbackPlayUTC(HiCamera hiCamera, int i) {

                }
            });
            mCamera.connect();
            mVideoView.setCamera(mCamera);
        } else {
            mCamera.disconnect();
            mCamera.setUid(uid);
            mCamera.setUsername(username);
            mCamera.setPassword(password);
            mCamera.connect();
        }

        // 获取种子
        getSends(keyon);
    }

    private void createCamera(String uid, String username, String password) {
        if (mCamera == null) {
            mCamera = new MyCamera(PlantActivity.this, "Camera", uid, username, password);
            mCamera.registerIOSessionListener(new ICameraIOSessionCallback() {
                @Override
                public void receiveSessionState(HiCamera hiCamera, int i) {
                    Message msg = cameraHandler.obtainMessage();
                    msg.what = PlantActivity.CAMERA_IO_SESSION_HANDLER;
                    msg.arg1 = i;
                    cameraHandler.sendMessage(msg);
                }

                @Override
                public void receiveIOCtrlData(HiCamera hiCamera, int i, byte[] bytes, int i1) {

                }
            });
            mCamera.registerPlayStateListener(new ICameraPlayStateCallback() {
                @Override
                public void callbackState(HiCamera hiCamera, int i, int i1, int i2) {
                    Message msg = cameraHandler.obtainMessage();
                    msg.what = PlantActivity.CAMERA_PLAY_STATE;
                    msg.arg1 = i;
                    cameraHandler.sendMessage(msg);
                }

                @Override
                public void callbackPlayUTC(HiCamera hiCamera, int i) {

                }
            });
            mCamera.connect();
            mVideoView.setCamera(mCamera);
        } else {
            mCamera.disconnect();
            mCamera.setUid(uid);
            mCamera.setUsername(username);
            mCamera.setPassword(password);
            mCamera.connect();
        }
    }

    //停止播放视频
    private void stopVideo() {
        if (mCamera != null) {
            if (mCamera.getConnectState() == 1) {
                mCamera.stopLiveShow();
                mCamera.stopListening();
                mCamera.stopTalk();
                mCamera.disconnect();
            } else {
                mCamera.disconnect();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PlantActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
        if (mCamera != null)
            mCamera = null;
    }

    public int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

}
