package com.my3w.farm.activity.icamera;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hichip.content.HiChipDefines;
import com.hichip.system.HiDefaultData;

import org.w3c.dom.Text;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.activity.home.HomeActivity;
import com.my3w.farm.activity.icamera.entity.CameraEntity;
import com.my3w.farm.activity.icamera.sqlite.CameraSqlite;
import com.my3w.farm.activity.login.LoginActivity;
import com.my3w.farm.activity.login.RegistActivity;
import com.my3w.farm.comment.camhi.bean.HiDataValue;
import com.my3w.farm.comment.camhi.bean.MyCamera;

public class ICameraListEditActivity extends baseActivity {

    private String uid_data;

    private ImageView title_back;
    private TextView title_name;
    private ImageView title_other;

    private TextView uid;
    private EditText username;
    private EditText userpass;
    private Button submit;

    private String oldname;
    private String olduid;
    private String oldusername;
    private String olduserpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icamera_list_edit);

        Intent intent = getIntent();
        uid_data = intent.getStringExtra("uid");

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        title_back = (ImageView) findViewById(R.id.title_back);
        title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("摄像头修改");
        title_other = (ImageView) findViewById(R.id.title_other);
        title_other.setVisibility(View.GONE);

        uid = (TextView) findViewById(R.id.uid);
        username = (EditText) findViewById(R.id.username);
        userpass = (EditText) findViewById(R.id.userpass);
        submit = (Button) findViewById(R.id.submit);
    }

    private void initData() {
        CameraEntity data = CameraSqlite.get(this).find(uid_data);
        uid.setText(data.getUid());
        username.setText(data.getUsername());
        userpass.setText(data.getUserpass());

        olduid = data.getUid();
        oldname = data.getName();
        oldusername = data.getUsername() == null ? "" : data.getUsername();
        olduserpass = data.getUserpass() == null ? "" : data.getUserpass();
    }

    private void initEvent() {

        // 返回到注册界面
        title_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ICameraListEditActivity.this, iCameraListActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameData = username.getText().toString();
                String userpassData = userpass.getText().toString();
                if (CameraSqlite.get(ICameraListEditActivity.this).update(uid_data, usernameData, userpassData)) {

                    MyCamera myCamera = new MyCamera(ICameraListEditActivity.this, oldname, olduid, oldusername, olduserpass);
                    byte[] old_auth = HiChipDefines.HI_P2P_S_AUTH.parseContent(0, oldusername, olduserpass);
                    byte[] new_auth = HiChipDefines.HI_P2P_S_AUTH.parseContent(0, usernameData, userpassData);
                    myCamera.sendIOCtrl(HiChipDefines.HI_P2P_SET_USER_PARAM, HiChipDefines.HI_P2P_SET_AUTH.parseContent(new_auth, old_auth));

                    Intent intent = new Intent(ICameraListEditActivity.this, iCameraListActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ICameraListEditActivity.this, iCameraListActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
        }
        return super.onKeyDown(keyCode, event);
    }
}
