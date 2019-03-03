package com.my3w.farm.comment.camhi.base;

import android.app.Application;

public class CrashApplication extends Application{
	private static  CrashApplication app;
	@Override
	public void onCreate() {
		super.onCreate();
		app=this;
		CrashHandler.getInstance().init(this);
	}
	
	public static synchronized CrashApplication getInstance(){
		return app;
	}
}









