package com.my3w.farm.comment.application;

import org.lasque.tusdk.core.TuSdkApplication;

import com.my3w.farm.comment.camhi.bean.HiDataValue;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import android.content.Context;
import android.util.Log;

public class FarmApplication extends TuSdkApplication {

    private static FarmApplication instance;

    public final static String SLEEP_INTENT = "org.videolan.vlc.SleepIntent";
    public final static String INCOMING_CALL_INTENT = "org.videolan.vlc.IncomingCallIntent";
    public final static String CALL_ENDED_INTENT = "org.videolan.vlc.CallEndedIntent";

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initImageLoader();
        initTuSdk();
        initXg();
    }

    public static Context getAppContext() {
        return instance;
    }

    private void initImageLoader() {
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    private void initTuSdk() {
        this.setEnableLog(true);
        this.initPreLoader(this.getApplicationContext(), "79db5c1f858aeea5-00-rn59q1");
    }

    private void initXg() {
        //开启信鸽日志输出

        XGPushConfig.enableDebug(this, true);

        //信鸽注册代码
        XGPushManager.registerPush(this, new XGIOperateCallback() {

            @Override
            public void onSuccess(Object data, int flag) {
                HiDataValue.XGToken = String.valueOf(data);
                Log.d("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
    }

}
