package com.my3w.farm.activity.icamera.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.my3w.farm.comment.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import com.my3w.farm.activity.icamera.entity.CameraEntity;

public class CameraSqlite {

    private static int Version = 3;

    private static CameraSqlite cameraSqlite;

    public DatabaseHelper dbHelper;
    public SQLiteDatabase sqliteDatabase;


    public static CameraSqlite get(Context context) {
        if (cameraSqlite == null)
            cameraSqlite = new CameraSqlite();
        cameraSqlite.dbHelper = new DatabaseHelper(context, "yourcaiwang", Version);
        cameraSqlite.sqliteDatabase = cameraSqlite.dbHelper.getReadableDatabase();
        return cameraSqlite;
    }


    public boolean insert(String name, String uid) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("uid", uid);
        if (sqliteDatabase.insert("camera", null, values) >= 1) {
            return true;
        }
        return false;
    }

    public boolean update(String uid, String username, String userpass) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("userpass", userpass);

        String[] args = {uid};

        if (sqliteDatabase.update("camera", values, "uid=?", args) >= 1) {
            return true;
        }
        return false;
    }

    public boolean clear() {
        sqliteDatabase.execSQL("DELETE FROM camera");
        return true;
    }

    public CameraEntity find(String uid) {
        CameraEntity data = new CameraEntity();
        String[] args = {uid};
        Cursor cursor = sqliteDatabase.query("camera", null, "uid=?", args, null, null, null);
        while (cursor.moveToNext()) {
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setUid(cursor.getString(cursor.getColumnIndex("uid")));
            data.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            data.setUserpass(cursor.getString(cursor.getColumnIndex("userpass")));
        }
        return data;
    }

    public List<CameraEntity> getList() {
        List<CameraEntity> rows = new ArrayList<CameraEntity>();
        Cursor cursor = sqliteDatabase.query("camera", null, "", null, null, null, null);
        while (cursor.moveToNext()) {
            CameraEntity data = new CameraEntity();
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setUid(cursor.getString(cursor.getColumnIndex("uid")));
            data.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            data.setUserpass(cursor.getString(cursor.getColumnIndex("userpass")));
            rows.add(data);
        }
        return rows;
    }
}
