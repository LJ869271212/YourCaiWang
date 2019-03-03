package com.westars.framework.standard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.westars.framework.standard.interfaces.ActivityInterfaces;

/**
 * Westars Activity
 * 
 * @author Aports
 * 
 * @see android.app.Activity
 * @see com.westars.ActivityInterfaces.standard.impl.ActivityImpl
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public abstract class BaseActivity extends Activity implements ActivityInterfaces {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
