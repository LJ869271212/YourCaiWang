package com.my3w.farm.activity.icamera;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hichip.tools.HiSearchSDK;
import com.westars.framework.view.image.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.icamera.adapter.CameraAdapter;
import com.my3w.farm.activity.icamera.entity.CameraEntity;
import com.my3w.farm.activity.icamera.sqlite.CameraSqlite;
import com.my3w.farm.comment.camhi.bean.HiDataValue;

public class iCameraListActivity extends baseActivity {

    // View
    private ImageView title_back;
    private TextView title_name;
    private TextView title_username;
    private RoundImageView title_icon;

    private ListView listView;
    private CameraAdapter adapter;

    private ArrayList<CameraEntity> listSearchData = new ArrayList<>();

    private ArrayList<CameraEntity> listData = new ArrayList<>();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_camera_list);

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_back.setImageResource(R.drawable.r);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("家庭摄像");
        title_username = (TextView) findViewById(R.id.title_username);
        title_username.setText("搜索摄像头");
        title_icon = (RoundImageView) findViewById(R.id.title_icon);
        title_icon.setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.listview);
        adapter = new CameraAdapter(this, listData);
        listView.setAdapter(adapter);


        dialog = new ProgressDialog(iCameraListActivity.this);
        dialog.setMessage("家庭摄像头搜索中，请稍候……");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

    }

    private void initData() {
        List<CameraEntity> listSqlite = CameraSqlite.get(this).getList();
        if (listSqlite.size() > 0) {
            listData.addAll(listSqlite);
            adapter.notifyDataSetChanged();
        }
    }

    private void initEvent() {

        title_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(iCameraListActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
            }
        });

        //重新搜索
        title_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraSqlite.get(iCameraListActivity.this).clear();

                if (listData.size() > 0) {
                    listData.removeAll(listData);
                }
                adapter.notifyDataSetChanged();

                //开启提示
                if (dialog != null)
                    dialog.show();

                HiSearchSDK hiSearchSDK = new HiSearchSDK(new HiSearchSDK.OnSearchResult() {
                    @Override
                    public void searchResult(List<HiSearchSDK.HiSearchResult> list) {
                        if (list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                HiSearchSDK.HiSearchResult hiSearchResult = list.get(i);
                                CameraEntity cameraEntity = new CameraEntity();
                                cameraEntity.setName(hiSearchResult.name);
                                cameraEntity.setUid(hiSearchResult.uid);
                                listSearchData.add(cameraEntity);
                            }

                            for (int i = 0; i < listSearchData.size(); i++) {
                                CameraEntity cameraEntityData = listSearchData.get(i);
                                CameraSqlite.get(iCameraListActivity.this).insert(cameraEntityData.getName(), cameraEntityData.getUid());
                            }

                            //关闭提示
                            if (dialog != null)
                                dialog.hide();

                            List<CameraEntity> listSqlite = CameraSqlite.get(iCameraListActivity.this).getList();
                            listData.addAll(listSqlite);
                            adapter.notifyDataSetChanged();
                        } else {

                            //关闭提示
                            if (dialog != null)
                                dialog.hide();

                            Toast.makeText(iCameraListActivity.this, "没有找到摄像头", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                hiSearchSDK.search2();
                hiSearchSDK.search();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(iCameraListActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
        }
        return super.onKeyDown(keyCode, event);
    }

}
