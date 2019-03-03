/** 
 * TuSdkDemo
 * AlbumComponentSimple.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午1:39:10 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package com.my3w.farm.comment.tusdk.examples.component;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.TuSdkGeeV1;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.TuAlbumComponent;
import org.lasque.tusdk.modules.components.TuSdkComponent.TuSdkComponentDelegate;

import com.my3w.farm.R;
import com.my3w.farm.comment.tusdk.SampleBase;
import com.my3w.farm.comment.tusdk.SampleGroup.GroupType;
import com.westars.framework.cache.DataCache;
import com.westars.framework.reaizepage.net.http.HttpRequest;
import com.westars.framework.reaizepage.net.http.HttpRequest.onProgressListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * 相册组件范例
 * 
 * @author Clear
 */
public class AlbumComponentTopic extends SampleBase implements TuSdkComponentDelegate {

	public interface onComponentListener {
		public void component(String path, String file);

		public void error(int val);
	}

	private onComponentListener listener;
	private Context context;

	/** 相册组件范例 */
	public AlbumComponentTopic(Context context, onComponentListener listener) {
		super(GroupType.ComponentSample, R.string.sample_AlbumComponent);

		this.context = context;
		this.listener = listener;
	}

	/** 显示范例 */
	// @see-http://tusdk.com/docs/android/api/org/lasque/tusdk/impl/components/TuAlbumComponent.html
	@Override
	public void showSample(Activity activity) {
		if (activity == null)
			return;

		TuAlbumComponent comp = TuSdkGeeV1.albumCommponent(activity, this);
		//不自动跳转到系统相册
		comp.componentOption().albumListOption().setDisableAutoSkipToPhotoList(true);
		// 在组件执行完成后自动关闭组件
		comp.setAutoDismissWhenCompleted(true)
				// 显示组件
				.showComponent();
		
	}
	
	@Override
	public void onComponentFinished(TuSdkResult result, Error error, TuFragment lastFragment) {
		// 上传到服务器
		String path = result.imageSqlInfo.path;

		HashMap<String, String> headerList = new HashMap<String, String>();
		headerList.put("File#files", path);
		headerList.put("exit", "jpg");
		HttpRequest http = new HttpRequest(context, headerList, handler, new onProgressListener() {

			@Override
			public void prgress(int val) {
				// TODO Auto-generated method stub

			}
		});
		http.execute(context.getResources().getString(R.string.config_host_url) + "topic.php?action=upload&&token="
				+ DataCache.get(context).getAsString("token"));

		Toast.makeText(context, "图片上传中，请稍候……", Toast.LENGTH_LONG).show();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 200) {
				String json = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(json);
					int result = jsonObject.optInt("errCode");
					if (result == 0) {
						JSONObject errMsg = jsonObject.optJSONObject("errMsg");
						if (listener != null)
							listener.component(errMsg.optString("path"), errMsg.optString("file"));
					} else if (result == 1) {
						if (listener != null)
							listener.error(-1);
					}
				} catch (JSONException e) {
					if (listener != null)
						listener.error(1);
				}
			} else {
				listener.error(2);
			}
		};
	};
}