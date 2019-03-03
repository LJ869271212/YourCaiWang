package com.my3w.farm.activity.shop.view;

import android.content.Context;
import android.widget.TextView;

public class TabTextView extends TextView {
	public TabTextView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);
	}

}
