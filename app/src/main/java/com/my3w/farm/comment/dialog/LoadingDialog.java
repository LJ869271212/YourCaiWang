package com.my3w.farm.comment.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {

	private ProgressDialog loading;

	public LoadingDialog(Context context) {
		if (loading == null)
			loading = new ProgressDialog(context);
	}

	public void show(String msg) {
		if (loading != null) {
			loading.setCancelable(false);
			loading.setMessage(msg);
			loading.show();
		}
	}

	public void cancel() {
		if (loading != null)
			loading.cancel();
	}

}
