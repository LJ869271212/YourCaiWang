package com.my3w.farm.comment.image;

import com.my3w.farm.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class ImageMagent {

	private DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.empty) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.empty) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();
	
	private DisplayImageOptions optionsImage = new DisplayImageOptions.Builder().showStubImage(R.drawable.emptyimage) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.emptyimage) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.emptyimage) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.build();

	public static ImageMagent imageMagent;

	public static ImageMagent getInstance() {
		if (imageMagent == null)
			imageMagent = new ImageMagent();
		return imageMagent;
	}
	
	public void displayImageBrower(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView, optionsImage);
	}

	public void displayImage(String uri, ImageView imageView) {
		ImageLoader.getInstance().displayImage(uri, imageView, options);
	}
	
	public Drawable getDrawable(Resources res, String uri){
		return new BitmapDrawable(res, ImageLoader.getInstance().loadImageSync(uri, options));
	}

}
