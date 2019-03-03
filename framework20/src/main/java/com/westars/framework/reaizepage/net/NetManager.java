package com.westars.framework.reaizepage.net;

import java.util.HashMap;

import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.reaizepage.net.http.HttpRequest.onProgressListener;

import android.content.Context;
import android.os.Handler;

/**
 * 网络请求管理类
 * 
 * @author Aports
 * 
 */
public class NetManager {

	private static NetManager INSTANCE;

	private static Context CONTEXT;

	public static NetManager sharedInstance(Context context) {
		CONTEXT = context;
		if (INSTANCE == null) {
			INSTANCE = new NetManager();
		}
		return INSTANCE;
	}

	/**
	 * http 请求 GET方法
	 * 
	 * @param handler
	 */
	public void httpGetConnect(String url, Handler handler) {
		new HttpRequest(CONTEXT, handler).execute(url);
	}

	/**
	 * http 请求POST方法
	 * 
	 * @param handler
	 * @param headerList
	 * @param encoded
	 * @param url
	 */
	public void httpPostConnect(String url, Handler handler, HashMap<String, ?> headerList) {
		new HttpRequest(CONTEXT, headerList, handler).execute(url);
	}

	/**
	 * http 请求POST上传请求
	 * 
	 * @param handler
	 * @param headerList
	 * @param encoded
	 * @param url
	 */
	public void httpPostConnect(String url, Handler handler, HashMap<String, ?> headerList, onProgressListener listener) {
		new HttpRequest(CONTEXT, headerList, handler, listener).execute(url);
	}

}
