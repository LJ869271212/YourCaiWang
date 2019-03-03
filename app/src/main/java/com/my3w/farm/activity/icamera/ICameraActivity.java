package com.my3w.farm.activity.icamera;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hichip.callback.ICameraIOSessionCallback;
import com.hichip.callback.ICameraPlayStateCallback;
import com.hichip.control.HiCamera;
import com.hichip.tools.HiSearchSDK;
import com.westars.framework.util.system.Device;

import java.util.List;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.plant.PlantActivity;
import com.my3w.farm.comment.camhi.base.MyLiveViewGLMonitor;
import com.my3w.farm.comment.camhi.bean.MyCamera;

public class ICameraActivity extends baseActivity {

    private static final int CAMERA_IO_SESSION_HANDLER = 2;

    private static final int CAMERA_PLAY_STATE = 3;

    private TextView back;

    private String name;
    private String uid;
    private String username;
    private String userpass;

    private RelativeLayout video_root;
    private MyLiveViewGLMonitor mVideoView;
    private ProgressBar video_loading_progress;
    private TextView video_loading_text;


    private MyCamera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icamera);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        uid = intent.getStringExtra("uid");
        username = intent.getStringExtra("username");
        userpass = intent.getStringExtra("userpass");

        initView();
        initData();
        initEvent();
    }

    private void initView() {

        back = (TextView) findViewById(R.id.back);
        video_root = (RelativeLayout) findViewById(R.id.video_root);
        mVideoView = (MyLiveViewGLMonitor) findViewById(R.id.video);
        video_loading_progress = (ProgressBar) findViewById(R.id.video_loading_progress);
        video_loading_text = (TextView) findViewById(R.id.video_loading_text);

        int width = Device.getScreenHeight(ICameraActivity.this) + getStatusBarHeight(ICameraActivity.this) / 3;
        int height = Device.getScreenWidth(ICameraActivity.this) - getStatusBarHeight(ICameraActivity.this);

        LinearLayout.LayoutParams fullScreenLayoutParams = (LinearLayout.LayoutParams) video_root.getLayoutParams();
        fullScreenLayoutParams.width = width;
        fullScreenLayoutParams.height = height;
        video_root.setLayoutParams(fullScreenLayoutParams);

        RelativeLayout.LayoutParams fullScreenVideoLayoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        fullScreenVideoLayoutParams.width = width;
        fullScreenVideoLayoutParams.height = height;
        mVideoView.setLayoutParams(fullScreenVideoLayoutParams);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }

    private void initData() {

        mCamera = new MyCamera(this, name, uid, username, userpass);
        mCamera.registerIOSessionListener(new ICameraIOSessionCallback() {
            @Override
            public void receiveSessionState(HiCamera hiCamera, int i) {
                Message msg = cameraHandler.obtainMessage();
                msg.what = ICameraActivity.CAMERA_IO_SESSION_HANDLER;
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
                msg.what = ICameraActivity.CAMERA_PLAY_STATE;
                msg.arg1 = i;
                cameraHandler.sendMessage(msg);
            }

            @Override
            public void callbackPlayUTC(HiCamera hiCamera, int i) {

            }
        });
        mCamera.connect();
        mVideoView.setCamera(mCamera);
    }

    private void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler cameraHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ICameraActivity.CAMERA_IO_SESSION_HANDLER) {
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
            } else if (msg.what == ICameraActivity.CAMERA_PLAY_STATE) {
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


    public int getStatusBarHeight(Activity activity) {
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
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
    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
        if (mCamera != null)
            mCamera = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
