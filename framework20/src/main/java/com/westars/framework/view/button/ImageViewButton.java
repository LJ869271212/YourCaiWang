package com.westars.framework.view.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.westars.framework.view.core.CoreImageView;

/**
 * 自定义的图标型按钮，通过改变图标透明度来区分按钮的各种状态。
 * 
 * @author Aports
 * 
 */
public class ImageViewButton extends CoreImageView {

	private final int mOpacityNormal = 255;
	private final int mOpacityPressed = (int) (255 * 0.6);
	private final int mOpacityDisabled = (int) (255 * 0.3);

	public ImageViewButton(Context context) {
		this(context, null);
	}

	public ImageViewButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageViewButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		setScaleType(ScaleType.CENTER_INSIDE);
		setClickable(true);
		setOnTouchListener(onTouchChangeOpacityListener);
		if (!isEnabled()) {
			setAlpha(mOpacityDisabled);
		}
	}

	private OnTouchListener onTouchChangeOpacityListener = new OnTouchListener() {

		@SuppressWarnings("deprecation")
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isEnabled()) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					setAlpha(mOpacityPressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					setAlpha(mOpacityNormal);
				}
			}
			return false;
		}

	};

	/**
	 * 设置单个icon做按钮的情况，不设背景，按下态与禁用态用透明度做
	 * 
	 * @param resId
	 *            the resource identifier of the drawable
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		// setBackgroundColor(0x00000000);
		if (!isEnabled()) {
			setAlpha(mOpacityDisabled);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			setAlpha(mOpacityNormal);
		} else {
			setAlpha(mOpacityDisabled);
		}
		super.setEnabled(enabled);
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		// 页面跳转后再回来，需要重设按钮透明度
		if (hasWindowFocus) {
			setEnabled(isEnabled());
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		setEnabled(isEnabled());
	}
}