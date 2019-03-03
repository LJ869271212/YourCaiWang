package com.my3w.farm.activity.login.sqlite;

import java.util.ArrayList;
import java.util.List;

import com.my3w.farm.activity.login.entity.UserEntity;
import com.my3w.farm.comment.utils.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserSqlite {

	private static int Version = 3;

	private static UserSqlite userSqlite;

	public DatabaseHelper dbHelper;
	public SQLiteDatabase sqliteDatabase;

	public static UserSqlite get(Context context) {
		if (userSqlite == null)
			userSqlite = new UserSqlite();
		userSqlite.dbHelper = new DatabaseHelper(context, "yourcaiwang", Version);
		userSqlite.sqliteDatabase = userSqlite.dbHelper.getReadableDatabase();
		return userSqlite;
	}

	public boolean insert(String username, String userpass) {
		ContentValues values = new ContentValues();
		values.put("name", username);
		values.put("pass", userpass);
		if (sqliteDatabase.insert("user", null, values) >= 1) {
			return true;
		}
		return false;
	}

	public int getCount(String username, String userpass) {
		Cursor cursor = sqliteDatabase.query("user", new String[] { "name", "pass" }, "name=? and pass=?",
				new String[] { username, userpass }, null, null, null);
		return cursor.getCount();
	}

	public List<UserEntity> getList() {
		List<UserEntity> rows = new ArrayList<UserEntity>();
		Cursor cursor = sqliteDatabase.query("user", null, "", null, null, null, null);
		while (cursor.moveToNext()) {
			UserEntity data = new UserEntity();
			data.setUser(cursor.getString(cursor.getColumnIndex("name")));
			data.setPass(cursor.getString(cursor.getColumnIndex("pass")));
			rows.add(data);
		}
		return rows;
	}
}
