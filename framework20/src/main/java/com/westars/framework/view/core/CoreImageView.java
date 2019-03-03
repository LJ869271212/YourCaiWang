package com.westars.framework.view.core;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;

public class CoreImageView extends ImageView {
	public CoreImageView(Context context) {
		super(context);
	}

	public CoreImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CoreImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setImageGif(String uri) {
		try {
			File file = DiskCacheUtils.findInCache(uri, ImageLoader.getInstance().getDiskCache());
			if (file != null) {
				if (getDrawable() != null) {
					this.setImageDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
				}
				GifDrawable dm = new GifDrawable(file);
				setImageDrawable(dm);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setImageGif(File file) {
		try {
			if (file != null) {
				if (getDrawable() != null) {
					this.setImageDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
				}
				GifDrawable dm = new GifDrawable(file);
				setImageDrawable(dm);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
