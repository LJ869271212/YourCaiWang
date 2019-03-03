package com.my3w.farm.comment.image;

import com.my3w.farm.R;
import com.my3w.farm.activity.baseActivity;
import com.westars.framework.view.core.CoreImageView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class ImageActivity extends baseActivity {

	private CoreImageView image;
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_images);

		path = getIntent().getStringExtra("path");

		initView();
		initData();
		initEvent();

	}

	private void initView() {
		image = (CoreImageView) findViewById(R.id.image);
	}

	private void initData() {
		ImageMagent.getInstance().displayImageBrower(path, image);
	}

	private void initEvent() {
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
