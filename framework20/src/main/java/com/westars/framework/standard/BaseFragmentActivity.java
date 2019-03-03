package com.westars.framework.standard;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.westars.framework.standard.interfaces.FragmentActivityInterfaces;

/**
 * Westars Fragment Activity
 * 
 * @author Aports
 * 
 * @see android.support.v4.app.FragmentActivity
 * @see com.westars.FragmentActivityInterfaces.standard.impl.FragmentActivityImpl
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public abstract class BaseFragmentActivity extends FragmentActivity implements FragmentActivityInterfaces {

}
