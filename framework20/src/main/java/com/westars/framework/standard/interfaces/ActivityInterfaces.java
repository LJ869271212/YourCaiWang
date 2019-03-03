package com.westars.framework.standard.interfaces;

/**
 * Activity接口内容和视图过程
 * 
 * @author Aports
 * 
 */
public interface ActivityInterfaces {

	/**
	 * 初始化视图
	 */
	public void initView();

	/**
	 * 初始化数据
	 */
	public void initData();

	/**
	 * 初始化事件
	 */
	public void initEvent();

	/**
	 * 反初始化
	 */
	public void uninit();

}