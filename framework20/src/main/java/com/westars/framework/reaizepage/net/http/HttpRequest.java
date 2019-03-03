package com.westars.framework.reaizepage.net.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.westars.framework.reaizepage.net.http.constant.ServiceCode;
import com.westars.framework.reaizepage.net.http.entity.ProgressHttpEntity;
import com.westars.framework.reaizepage.net.http.entity.ProgressHttpEntity.ProgressListener;

/**
 * Http请求，可POST和GET两种方式获取数据
 * 
 * @author Aports
 * 
 * @see org.apache.http.HttpResponse
 */
@SuppressWarnings("deprecation")
public class HttpRequest extends AsyncTask<String, Integer, HttpResponse> {

	public interface onProgressListener {
		public void prgress(int val);
	}

	public onProgressListener listener;

	private String LOG_TAG = "Http";

	private String _method = "GET";

	private HashMap<String, ?> _headerList = null;

	private String _encoded = "UTF-8";

	private Handler _handler = null;

	private boolean _file = false;

	private String __url;

	private long totalSize;

	/**
	 * GET请求
	 * 
	 * @param context
	 * @param handler
	 */
	public HttpRequest(Context context, Handler handler) {
		this._method = "GET";
		this._handler = handler;
	}

	/**
	 * POST请求
	 * 
	 * @param context
	 * @param headerList
	 * @param encoded
	 * @param handler
	 */
	public HttpRequest(Context context, HashMap<String, ?> headerList, Handler handler) {
		this._method = "POST";
		this._headerList = headerList;
		this._handler = handler;
		this._file = false;
	}

	/**
	 * POST上传请求
	 * 
	 * @param context
	 * @param headerList
	 * @param encoded
	 * @param handler
	 * @param listener
	 */
	public HttpRequest(Context context, HashMap<String, ?> headerList, Handler handler, onProgressListener listener) {
		this._method = "POST";
		this._headerList = headerList;
		this._handler = handler;
		this._file = true;
		this.listener = listener;
	}

	@Override
	protected HttpResponse doInBackground(String... utls) {

		this.__url = utls[0];

		@SuppressWarnings("resource")
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse response = null;
		try {
			// 从连接池中取连接的超时时间
			ConnManagerParams.setTimeout(httpClient.getParams(), 4000);
			// 连接超时
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 6000);
			// 请求超时
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10000);

			if (this._method == "GET") {
				response = httpClient.execute(this.getGetMethod());
			} else {
				if (this._file) {
					response = httpClient.execute(this.getPostFileMethod());
				} else {
					response = httpClient.execute(this.getPostMethod());
				}
			}
		} catch (ClientProtocolException e) {
			ErrorHandler(ServiceCode.CLIENTPROTOCOL_EXCEPTION);
			Log.w(LOG_TAG, "ClientProtocolException:" + e.toString());
		} catch (IOException e) {
			ErrorHandler(ServiceCode.IO_EXCEPTION);
			Log.w(LOG_TAG, "IOException:" + e.toString());
		}
		return response;
	}

	protected void onPostExecute(HttpResponse result) {
		if (result != null) {
			new connectRunnable(result).start();
		}
	}

	// 线程执行结果
	public class connectRunnable extends Thread {
		public HttpResponse _result;

		public connectRunnable(HttpResponse result) {
			this._result = result;
		}

		@Override
		public void run() {
			Message message = new Message();
			message.what = this._result.getStatusLine().getStatusCode();

			if (message.what == 200) {
				HttpEntity res = this._result.getEntity();
				if (res != null) {
					try {
						BufferedInputStream in = new BufferedInputStream(res.getContent());
						message.obj = getData(in);

						Log.e("HttpReuqest", "data:" + (String) message.obj);
					} catch (ParseException e) {
						ErrorHandler(ServiceCode.PARSE_EXCEPTION);
						Log.w(LOG_TAG, "ParseException:" + e.toString());
					} catch (IllegalStateException e) {
						ErrorHandler(ServiceCode.ILLEGALSTATE_EXCEPTION);
						Log.w(LOG_TAG, "IllegalStateException:" + e.toString());
					} catch (IOException e) {
						ErrorHandler(ServiceCode.IO_EXCEPTION);
						Log.w(LOG_TAG, "IOException:" + e.toString());
					}
				} else {
					Log.w(LOG_TAG, "返回数据为空");
				}
			}
			if (_handler != null) {
				_handler.sendMessage(message);
			}
		}
	};

	// GET请求数据拼凑
	private HttpGet getGetMethod() {
		// 创建Head请求
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		if (this._headerList != null) {
			// 遍历所有参数
			Iterator<?> iter = this._headerList.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iter.next();
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				params.add(new BasicNameValuePair(key, val));
			}
		}

		// 抓去遍历
		String param = URLEncodedUtils.format(params, this._encoded);

		Log.e("HttpRequest", param);

		HttpGet Method = null;
		if (param.equals("")) {
			Method = new HttpGet(this.__url);
		} else {
			Method = new HttpGet(this.__url + "?" + param);
		}
		return Method;
	}

	// POST请求数据拼凑，无上传图片版
	private HttpPost getPostMethod() {
		// 创建Head请求
		List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();

		if (this._headerList != null) {
			// 遍历所有参数
			Iterator<?> iter = this._headerList.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) iter.next();
				String key = (String) entry.getKey();
				String val = (String) entry.getValue();
				params.add(new BasicNameValuePair(key, val));
			}
		}
		HttpPost postMethod = new HttpPost(this.__url);
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(params, this._encoded));
		} catch (UnsupportedEncodingException e) {
			ErrorHandler(ServiceCode.UNSUPPORTEDENCODING_EXCEPTION);
			Log.w(LOG_TAG, e.toString());
		}
		return postMethod;
	}

	// POST请求数据拼凑，上传图片版
	private HttpPost getPostFileMethod() {
		// 生成请求对象
		HttpPost postMethod = new HttpPost(this.__url);
		try {

			Class.forName("org.apache.http.entity.mime.MultipartEntity");

			// 请求参数配置
			org.apache.http.entity.mime.MultipartEntity mpEntity = new org.apache.http.entity.mime.MultipartEntity();
			if (this._headerList != null) {
				// 遍历所有参数
				Iterator<?> iter = this._headerList.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<?, ?> entry = (Entry<?, ?>) iter.next();
					String key = (String) entry.getKey();
					String val = (String) entry.getValue();
					if (key.indexOf("#") != -1) {
						File imageFile = new File(val);
						org.apache.http.entity.mime.content.FileBody file = new org.apache.http.entity.mime.content.FileBody(imageFile);
						String[] fileArr = key.split("#");
						mpEntity.addPart(fileArr[1], file);

						totalSize = mpEntity.getContentLength();
						ProgressHttpEntity progressHttpEntity = new ProgressHttpEntity(mpEntity, new ProgressListener() {
							@Override
							public void transferred(long transferedBytes) {
								publishProgress((int) (100 * transferedBytes / totalSize));
							}
						});
						postMethod.setEntity(progressHttpEntity);
					} else {
						org.apache.http.entity.mime.content.ContentBody body = new org.apache.http.entity.mime.content.StringBody(val);
						mpEntity.addPart(key, body);
						postMethod.setEntity(mpEntity);
					}
				}
			}

		} catch (ClassNotFoundException e) {
			Log.w(LOG_TAG, "无org.apache.http.entity.mime包，上传请求失败，请检查当前包中是否有httpmime-4.0.jar包，不存在，请下载！");
		} catch (UnsupportedEncodingException e) {
			ErrorHandler(ServiceCode.UNSUPPORTEDENCODING_EXCEPTION);
			Log.w(LOG_TAG, e.toString());
		}

		return postMethod;
	}

	// 转换为字符
	private String getData(InputStream in) {
		if (in == null) {
			return null;
		}

		String result = "";
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = "";
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
			ErrorHandler(ServiceCode.EXCEPTION);
			Log.w(LOG_TAG, e.toString());
		} finally {
			if (result != null) {
				try {
					br.close();
				} catch (IOException e) {
					ErrorHandler(ServiceCode.IO_EXCEPTION);
					Log.w(LOG_TAG, e.toString());
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 错误后直接请求 Handler
	 */
	private void ErrorHandler(int ExceptionType) {
		if (_handler != null) {
			Message message = new Message();
			message.what = ExceptionType;
			_handler.sendMessage(message);
		}
	}

	/**
	 * 上传中
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		if (listener != null) {
			listener.prgress(values[0]);
		}
	}

	/**
	 * 清除数据释放内存
	 */
	public void recycle() {
		this._handler = null;
		if (this._headerList != null)
			this._headerList.clear();
		this._headerList = null;
	}
}
