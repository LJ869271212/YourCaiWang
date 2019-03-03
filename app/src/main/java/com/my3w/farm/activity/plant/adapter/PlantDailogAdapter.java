package com.my3w.farm.activity.plant.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.my3w.farm.R;

import java.util.ArrayList;

public class PlantDailogAdapter extends BaseAdapter {


    private ArrayList<String> uidList;

    private LayoutInflater mInflater;


    public PlantDailogAdapter(Context context, ArrayList<String> uidList) {
        this.mInflater = LayoutInflater.from(context);
        this.uidList = uidList;
    }

    @Override
    public int getCount() {
        return uidList.size();
    }

    @Override
    public Object getItem(int position) {
        return uidList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String uid = uidList.get(position);

        if (uid != null){
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.activity_plant_camera_dialog_item, null);
                holder.plant_uid = (TextView) convertView.findViewById(R.id.plant_uid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.plant_uid.setText(uid);
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView plant_uid;
    }
}