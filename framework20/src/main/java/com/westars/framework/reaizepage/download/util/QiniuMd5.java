package com.westars.framework.reaizepage.download.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 获取七牛MD5
 * 
 * @author Aports
 * 
 */
public class QiniuMd5 {

	private String url;

	public QiniuMd5(String url) {
		if (url.indexOf("?") <= -1) {
			this.url = url + "?hash/md5";
		} else {
			this.url = url + "/hash/md5";
		}
	}

	/**
	 * 返回md5
	 * 
	 * @return
	 */
	public String getMd5() {
		String md5 = "";
		try {
			String json = HttpGet(url);
			if (json != null) {
				JSONObject jsonObj = new JSONObject(json);
				md5 = jsonObj.getString("md5");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return md5;
	}

	// 数据请求
	public String HttpGet(String urls) {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			url = new URL(urls);
			connection = (HttpURLConnection) url.openConnection();
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}
}
