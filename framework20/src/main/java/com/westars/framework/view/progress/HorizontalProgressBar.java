package com.westars.framework.view.progress;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.westars.framework.R;

/**
 * 横向进度条，自定义颜色
 * 
 * @author Aports
 * 
 */
public class HorizontalProgressBar extends ProgressBar {

	public HorizontalProgressBar(Context context) {
		super(context, null, android.R.attr.progressBarStyleHorizontal);
	}

	public HorizontalProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.progressBarStyleHorizontal);
	}

	public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setProgressDrawable(Drawable d) {
		super.setProgressDrawable(getResources().getDrawable(R.drawable.horizontal_progressbar));
	}
}
