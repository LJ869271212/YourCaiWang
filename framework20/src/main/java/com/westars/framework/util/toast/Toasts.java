package com.westars.framework.util.toast;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.widget.Toast;

public class Toasts {

	private static Toast toast;

	/**
	 * 显示提示信息
	 * 
	 * @author TopRookie
	 * @version 1.0
	 * @date 2013-03-19
	 * @param text
	 *            提示内容
	 */
	public static void showToast(Context context, int text) {
		try {
			if (toast == null) {
				toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			} else {
				toast.setText(text);
			}
			// toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, DisplayUtil.dip2px(Application.context, 150));
			toast.show();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示提示信息
	 * 
	 * @author TopRookie
	 * @version 1.0
	 * @date 2013-03-19
	 * @param text
	 *            提示内容
	 */
	public static void showToast(Context context, String text) {
		try {
			if (toast == null) {
				toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			} else {
				toast.setText(text);
			}
			// toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, DisplayUtil.dip2px(Application.context, 150));
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 显示提示信息(时间较长)
	 * 
	 * @author TopRookie
	 * @version 1.0
	 * @date 2013-04-07
	 * @param text
	 *            提示内容
	 */
	public static void showLongToast(Context context, String text) {
		try {
			if (toast == null) {
				toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
			} else {
				toast.setText(text);
			}
			// toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, DisplayUtil.dip2px(Application.context, 150));
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
