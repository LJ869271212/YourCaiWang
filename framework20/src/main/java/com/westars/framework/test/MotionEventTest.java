package com.westars.framework.test;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MotionEventTest {

	private View v;

	public void down(View v) {
		this.v = v;
		new Thread() {
			@Override
			public void run() {
				synchronized (this) {
					try {
						wait(2000); // 1秒
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				MessageHandler.sendEmptyMessage(1);
			}
		}.start();

	}

	@SuppressLint("HandlerLeak")
	private Handler MessageHandler = new Handler() {
		@SuppressLint("Recycle")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Log.e("xxz", "down");
				v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
						100, 100, 0));
				v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 100,
						100, 0));
				break;
			default:
				break;
			}

		}
	};

}
