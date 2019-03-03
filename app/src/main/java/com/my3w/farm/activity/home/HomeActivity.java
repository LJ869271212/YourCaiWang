package com.my3w.farm.activity.home;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hichip.sdk.HiChipSDK;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.campaign.CampaignDynamicActivity;
import com.my3w.farm.activity.circle.CircleActivity;
import com.my3w.farm.activity.game.borrow.BorrowActivity;
import com.my3w.farm.activity.icamera.ICameraActivity;
import com.my3w.farm.activity.icamera.iCameraListActivity;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.plant.PlantActivity;
import com.my3w.farm.activity.shop.ShopLandActivity;
import com.my3w.farm.activity.shop.ShopSeedActivity;
import com.my3w.farm.activity.shop.ShopToolsActivity;
import com.my3w.farm.activity.user.UserCenterActivity;
import com.my3w.farm.activity.user.UserMessageActivity;
import com.my3w.farm.comment.image.ImageMagent;

import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.util.system.Device;
import com.westars.framework.view.image.ImageCycleView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 首页
 *
 * @author Administrator
 */
public class HomeActivity extends baseActivity {

    private ImageCycleView imageCycleView;

    private ArrayList<View> views;

    private ViewPager viewPager;

    private LinearLayout dots;

    private TextView maidi;
    private TextView zhongzi;
    private TextView zhongzhi;
    private TextView icamera;
    private TextView youcaiquan;
    private TextView youxi;

    private TextView huodong;
    private TextView gongju;

    private LinearLayout usermessage;
    private LinearLayout usercenter;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
        initData();
        initEvent();
    }

    @SuppressLint("InflateParams")
    private void initView() {

        imageCycleView = (ImageCycleView) findViewById(R.id.cycleView);

        LayoutParams imageCycleViewParams = (LayoutParams) imageCycleView.getLayoutParams();
        imageCycleViewParams.width = Device.getScreenWidth(this);
        imageCycleViewParams.height = Device.getScreenWidth(this) / 10 * 6;
        imageCycleView.setLayoutParams(imageCycleViewParams);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        View view1 = LayoutInflater.from(this).inflate(R.layout.activity_home_a, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.activity_home_b, null);

        views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        MYViewPagerAdapter adapter = new MYViewPagerAdapter();
        adapter.setViews(views);
        viewPager.setAdapter(adapter);

        dots = (LinearLayout) findViewById(R.id.dots);
        for (int i = 0; i < views.size(); i++) {
            ImageView dos = new ImageView(this);
            dos.setScaleType(ScaleType.CENTER_CROP);
            if (i == 0) {
                dos.setImageResource(R.drawable.dos_on);
            } else {
                dos.setImageResource(R.drawable.dos_off);
            }
            dos.setPadding(5, 5, 5, 5);
            dots.addView(dos, i);
        }

        maidi = (TextView) view1.findViewById(R.id.maidi);
        zhongzi = (TextView) view1.findViewById(R.id.zhongzi);
        zhongzhi = (TextView) view1.findViewById(R.id.zhongzhi);
        icamera = (TextView) view1.findViewById(R.id.icamera);
        youxi = (TextView) view1.findViewById(R.id.youxi);
        huodong = (TextView) view1.findViewById(R.id.huodong);

        youcaiquan = (TextView) view2.findViewById(R.id.youcaiquan);
        gongju = (TextView) view2.findViewById(R.id.gongju);

        usermessage = (LinearLayout) findViewById(R.id.usermessage);
        usercenter = (LinearLayout) findViewById(R.id.usercenter);

        HiChipSDK.init(new HiChipSDK.HiChipInitCallback() {
            @Override
            public void onSuccess(int i, int i1) {

            }

            @Override
            public void onFali(int i, int i1) {

            }
        });
    }

    private void initData() {
        HttpRequest http = new HttpRequest(this, handler);
        http.execute(getResources().getString(R.string.config_host_url) + "comment.php?action=banner&token="
                + DataCache.get(this).getAsString("token"));
    }

    @SuppressWarnings("deprecation")
    private void initEvent() {

        // 功能列表切换
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < dots.getChildCount(); i++) {
                    ImageView dos = (ImageView) dots.getChildAt(i);
                    dos.setImageResource(R.drawable.dos_off);
                }

                ImageView dosOf = (ImageView) dots.getChildAt(arg0);
                dosOf.setImageResource(R.drawable.dos_on);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        // 买地
        maidi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShopLandActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 种子
        zhongzi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShopSeedActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 种植
        zhongzhi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PlantActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);

            }
        });

        //家庭摄像
        icamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, iCameraListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 有菜圈
        youcaiquan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CircleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 游戏
        youxi.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BorrowActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 活动
        huodong.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CampaignDynamicActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 工具
        gongju.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ShopToolsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 消息
        usermessage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserMessageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

        // 个人中心
        usercenter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserCenterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
            }
        });

    }

    // 获取区域handler
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
                        JSONArray jsonArray = jsonObject.optJSONArray("errMsg");
                        if (result == 0) {
                            if (jsonArray.length() > 0) {
                                ArrayList<String> imageDescList = new ArrayList<String>();
                                ArrayList<String> urlList = new ArrayList<String>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject addressObject = jsonArray.getJSONObject(i);
                                    imageDescList.add(addressObject.optString("img_title"));
                                    urlList.add(addressObject.optString("pic_url"));
                                }
                                ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
                                    @Override
                                    public void onImageClick(int position, View imageView) {
                                        // TODO 实现点击事件
                                    }

                                    @Override
                                    public void displayImage(String imageURL, ImageView imageView) {
                                        ImageMagent.getInstance().displayImage(imageURL, imageView);
                                    }
                                };
                                imageCycleView.setImageResources(imageDescList, urlList, mAdCycleViewListener);
                                imageCycleView.startImageCycle();
                            }
                        } else if (result == 1) {
                            Toast.makeText(HomeActivity.this, "登录超时或已经被别人登录，请重新登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            sendBroadcast(new Intent("finish"));
                            overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(HomeActivity.this, "数据出现异常，请重试", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(HomeActivity.this, "网络出现了点问题，请查看网络连接是否正常后再重试", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public class MYViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public void setViews(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {

            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HiChipSDK.uninit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
