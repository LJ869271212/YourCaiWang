package com.my3w.farm.activity.campaign;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.my3w.farm.comment.dialog.LoadingDialog;
import com.my3w.farm.comment.image.ImageMagent;
import com.westars.framework.cache.DataCache;
import com.westars.framework.view.image.RoundImageView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignDynamicInfoImageInfoActivity extends baseActivity {

	private LoadingDialog dialog;

	private ImageView title_back;
	private TextView title_name;
	private TextView title_username;
	private RoundImageView title_icon;

	private WebView webview;

	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campaign_dynamic_info_image_info);

		id = getIntent().getStringExtra("id");

		initView();
		initData();
		initEvent();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		// 请求loading
		if (dialog == null)
			dialog = new LoadingDialog(this);
		dialog.show("数据加载中，请稍候…");

		title_back = (ImageView) this.findViewById(R.id.title_back);
		title_back.setImageResource(R.drawable.r);
		title_name = (TextView) this.findViewById(R.id.title_name);
		title_name.setText("参赛作品详情");
		title_icon = (RoundImageView) this.findViewById(R.id.title_icon);
		String user_picString = DataCache.get(CampaignDynamicInfoImageInfoActivity.this).getAsString("user_pic");
		if (user_picString != null && !user_picString.equals(""))
			ImageMagent.getInstance().displayImage(user_picString, title_icon);

		title_username = (TextView) this.findViewById(R.id.title_username);
		String user_nickString = DataCache.get(CampaignDynamicInfoImageInfoActivity.this).getAsString("user_nick");
		if (user_nickString != null && !user_nickString.equals(""))
			title_username.setText(user_nickString);
		else
			title_username.setText("");

		webview = (WebView) findViewById(R.id.webview);
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);

	}

	private void initData() {
		webview.loadUrl("http://www.yourcaiwang.com/Party/phDetailHtml/id/" + id +"?token=" + DataCache.get(this).getAsString("token"));
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					if (dialog != null)
						dialog.cancel();

				}
			}
		});
	}

	private void initEvent() {

		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
			}
		});
	}
	


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_activity_close_left, R.anim.anim_activity_close_right);
		}
		return super.onKeyDown(keyCode, event);
	}

}
