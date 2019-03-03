package com.westars.framework.view.image;

import android.content.Context;
import android.util.AttributeSet;

import com.westars.framework.view.core.CoreImageView;

/**
 * 正方形ImageView
 * 
 * @author Aports
 * 
 */
public class SquareImageView extends CoreImageView {
	public SquareImageView(Context context) {
		super(context);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

}
