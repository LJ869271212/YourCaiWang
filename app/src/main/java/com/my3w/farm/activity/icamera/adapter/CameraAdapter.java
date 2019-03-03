package com.my3w.farm.activity.icamera.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import com.my3w.farm.R;
import com.my3w.farm.activity.icamera.ICameraActivity;
import com.my3w.farm.activity.icamera.ICameraListEditActivity;
import com.my3w.farm.activity.icamera.entity.CameraEntity;

public class CameraAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater mInflater;

    private ArrayList<CameraEntity> data;

    public CameraAdapter(Context context, ArrayList<CameraEntity> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final CameraEntity getData = data.get(position);

        if (getData != null) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_i_camera_list_view, null);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.uid = (TextView) convertView.findViewById(R.id.uid);
                holder.edit = (Button) convertView.findViewById(R.id.edit);
                holder.start = (Button) convertView.findViewById(R.id.start);
                holder.username = (TextView) convertView.findViewById(R.id.username);
                holder.userpass = (TextView) convertView.findViewById(R.id.userpass);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(getData.getName());
            holder.uid.setText(getData.getUid());
            holder.username.setText(getData.getUsername() == null ? "未设置" : getData.getUsername());
            holder.userpass.setText(getData.getUserpass() == null ? "未设置" : getData.getUserpass());

            //编辑
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ICameraListEditActivity.class);
                    intent.putExtra("uid", getData.getUid());
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.anim_activity_open_left, R.anim.anim_activity_open_right);
                }
            });

            //播放
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ICameraActivity.class);
                    intent.putExtra("name", getData.getName());
                    intent.putExtra("uid", getData.getUid());
                    intent.putExtra("username", getData.getUsername());
                    intent.putExtra("userpass", getData.getUserpass());
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }


    public class ViewHolder {
        private TextView name;
        private TextView uid;
        private Button edit;
        private Button start;
        private TextView username;
        private TextView userpass;
    }
}
