/** 
 * TuSdkDemo
 * DemoListCell.java
 *
 * @author 		Clear
 * @Date 		2015-4-21 下午2:12:20 
 * @Copyright 	(c) 2015 tusdk.com. All rights reserved.
 * 
 */
package com.my3w.farm.comment.tusdk.view;

import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;

import com.my3w.farm.R;
import com.my3w.farm.comment.tusdk.SampleBase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 范例列表行视图
 * 
 * @author Clear
 */
public class DemoListCell extends TuSdkCellRelativeLayout<SampleBase> {
	/** 布局ID */
	public static int getLayoutId() {
		return R.layout.demo_view_list_cell;
	}

	public DemoListCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DemoListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DemoListCell(Context context) {
		super(context);
	}

	/** 标题视图 */
	private TextView mTitleView;

	@Override
	public void loadView() {
		super.loadView();
		// 标题视图
		mTitleView = this.getViewById(R.id.lsq_titleLabel);
	}

	@Override
	protected void bindModel() {
		if (this.getModel() == null || this.getModel().titleResId == 0)
			return;
		mTitleView.setText(this.getModel().titleResId);
	}
}