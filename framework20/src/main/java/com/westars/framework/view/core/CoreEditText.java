package com.westars.framework.view.core;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class CoreEditText extends EditText {

	public CoreEditText(Context context) {
		super(context);
	}

	public CoreEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CoreEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public void setTypeface(Typeface tf, int style) {
	}
}
