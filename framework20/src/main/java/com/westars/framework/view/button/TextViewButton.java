package com.westars.framework.view.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.westars.framework.view.core.CoreTextView;

/**
 * 自定义的文字型按钮，通过改变透明度来区分按钮的各种状态。
 * 
 * @author Aports
 * 
 */
public class TextViewButton extends CoreTextView {

	private final int mOpacityNormal = 255;
	private final int mOpacityPressed = (int) (255 * 0.6);
	private final int mOpacityDisabled = (int) (255 * 0.3);

	public TextViewButton(Context context) {
		this(context, null);
	}

	public TextViewButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TextViewButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		setClickable(true);
		setOnTouchListener(onTouchChangeOpacityListener);
		if (!isEnabled()) {
			setCustomAlpha(mOpacityDisabled);
		}
	}

	private OnTouchListener onTouchChangeOpacityListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (isEnabled()) {
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					setCustomAlpha(mOpacityPressed);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					setCustomAlpha(mOpacityNormal);
				}
			}
			return false;
		}

	};

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			setCustomAlpha(mOpacityNormal);
		} else {
			setCustomAlpha(mOpacityDisabled);
		}
		super.setEnabled(enabled);
	}

	private void setCustomAlpha(int value) {
		if (null != getBackground()) {
			getBackground().setAlpha(value);
		}
		setTextColor(getTextColors().withAlpha(value));
	}
}
