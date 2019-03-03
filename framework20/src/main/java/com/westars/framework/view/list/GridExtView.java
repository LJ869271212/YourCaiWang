package com.westars.framework.view.list;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.westars.framework.R;
import com.westars.framework.view.core.CoreGridView;
import com.westars.framework.view.core.CoreTextView;

/**
 * GridView下载刷新，上拉加载
 * 
 * @author Aports
 * 
 */
public class GridExtView extends CoreGridView implements OnScrollListener {

	// headerView
	private LinearLayout headerView;
	private ImageView loading_header;
	private CoreTextView loading_text;

	private AnimationDrawable animationDrawable;
	private int headerContentHeight;
	private boolean showHeaderView = true; // 记录是否显示下拉刷新

	private final int HEADERVIEW_DONE = 1; // 初始化状态
	private final int HEADERVIEW_RELEASE = 2; // 下拉中状态
	private final int HEADERVIEW_RELEASE_TO_DONE = 3; // 处于回归的状态
	private final int HEADERVIEW_LOADING = 4; // 正在刷新中状态
	private int HEADERVIEW_FLAG = HEADERVIEW_DONE;

	private final int HEADERVIEW_TO_RELEASE = 1; // 未到达刷新状态
	private final int HEADERVIEW_TO_LOADING = 2; // 已经到达刷新中状态

	private int HEADERVIEW_SUCCESS_FLAG = HEADERVIEW_TO_RELEASE;

	private final int DOWN_RATIO = 3; // 下拉比例

	private int HEADERVIEW_STARTY;
	private int HEADERVIEW_TEMPY;

	// footerView;
	private LinearLayout footerView;
	private CoreTextView footerview_more;
	private CoreTextView footerview_date;
	private int footerContentHeight;
	private boolean showFooterView = true; // 记录是否上拉加载

	private final static int FOOTERVIEW_NONE = 0; // 未加载
	private final static int FOOTERVIEW_REFRESHING = 1; // 加载中
	private int FOOTERVIEW_STATE = FOOTERVIEW_NONE; // 默认为未加载

	private onGridViewListener listener;

	// 滚动状态
	private boolean isRefreshable;
	private boolean isLoadhable;

	public GridExtView(Context context) {
		super(context);
		init(context);
	}

	public GridExtView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public GridExtView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		addHeaderView(context);
		addFooterView(context);

		// 滚动事件监听
		setOnScrollListener(this);
	}

	@SuppressLint("InflateParams")
	private void addHeaderView(Context context) {
		if (!isInEditMode()) {
			LayoutInflater inflater = LayoutInflater.from(context);
			headerView = (LinearLayout) inflater.inflate(R.layout.com_westars_frame_view_general_westarsgeneralview_headerview, null);
			headerView.setBackgroundColor(Color.parseColor("#ffeeeeee"));
			loading_header = (ImageView) headerView.findViewById(R.id.loading_header);
			loading_text = (CoreTextView) headerView.findViewById(R.id.loading_text);
			loading_text.setText(R.string.list_pull);

			loading_header.setImageResource(R.anim.anim_list);
			animationDrawable = (AnimationDrawable) loading_header.getDrawable();

			measureView(headerView);
			headerContentHeight = headerView.getMeasuredHeight();
			headerView.setPadding(0, -1 * headerContentHeight, 0, 0);

			addHeaderView(headerView, null, false);
		}
	}

	@SuppressLint("InflateParams")
	private void addFooterView(Context context) {
		if (!isInEditMode()) {
			LayoutInflater inflater = LayoutInflater.from(context);
			footerView = (LinearLayout) inflater.inflate(R.layout.com_westars_frame_view_general_westarsgeneralview_footerview, null);
			footerView.setBackgroundColor(Color.parseColor("#ffeeeeee"));
			footerview_more = (CoreTextView) footerView.findViewById(R.id.footerview_more);
			footerview_date = (CoreTextView) footerView.findViewById(R.id.footerview_date);

			measureView(footerView);

			footerContentHeight = footerView.getMeasuredHeight();

			footerView.setPadding(0, -1 * footerContentHeight, 0, 0);

			addFooterView(footerView, null, false);
		}
	}

	/**
	 * 计算View高度
	 * 
	 * @param child
	 */
	@SuppressWarnings("deprecation")
	private void measureView(View child) {
		if (!isInEditMode()) {
			ViewGroup.LayoutParams params = child.getLayoutParams();
			if (params == null) {
				params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
			int lpHeight = params.height;
			int childHeightSpec;
			if (lpHeight > 0) {
				childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
			} else {
				childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
			}
			child.measure(childWidthSpec, childHeightSpec);
		}
	}

	@SuppressLint({ "ClickableViewAccessibility", "SimpleDateFormat" })
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isRefreshable) {
			if (showHeaderView) {
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					HEADERVIEW_STARTY = (int) ev.getY();
					if (HEADERVIEW_FLAG == HEADERVIEW_DONE || HEADERVIEW_FLAG == HEADERVIEW_RELEASE) {
						HEADERVIEW_FLAG = HEADERVIEW_DONE;
					}
					break;
				case MotionEvent.ACTION_UP:
					// 放手操作的时候，刷新状态不再加载状态
					if (HEADERVIEW_FLAG != HEADERVIEW_LOADING) {
						if (HEADERVIEW_SUCCESS_FLAG == HEADERVIEW_TO_LOADING) {
							// 放手的时候，如果到刷新状态，就开始进行刷新操作
							HEADERVIEW_FLAG = HEADERVIEW_LOADING;
						} else {
							// 其他状态恢复到默认状态
							if (HEADERVIEW_FLAG == HEADERVIEW_RELEASE) {
								// 用户进入回归状态
								HEADERVIEW_FLAG = HEADERVIEW_RELEASE_TO_DONE;
							} else {
								// 用户进入默认状态
								HEADERVIEW_FLAG = HEADERVIEW_DONE;
							}
						}
						changeHeaderViewByState();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					HEADERVIEW_TEMPY = (int) ev.getY();

					if (HEADERVIEW_FLAG == HEADERVIEW_DONE || HEADERVIEW_FLAG == HEADERVIEW_RELEASE) {
						// 开始下拉
						if (HEADERVIEW_TEMPY - HEADERVIEW_STARTY > 0) {
							HEADERVIEW_FLAG = HEADERVIEW_RELEASE;
						}

						if ((HEADERVIEW_TEMPY - HEADERVIEW_STARTY) / DOWN_RATIO < headerContentHeight) {
							// 未到达
							HEADERVIEW_SUCCESS_FLAG = HEADERVIEW_TO_RELEASE;
						} else {
							// 已到达
							HEADERVIEW_SUCCESS_FLAG = HEADERVIEW_TO_LOADING;
						}

						changeHeaderViewByState();
					}
					break;
				default:
					break;
				}
			}
		} else if (isLoadhable) {
			if (showFooterView) {
				switch (ev.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					if (FOOTERVIEW_STATE == FOOTERVIEW_NONE) {
						FOOTERVIEW_STATE = FOOTERVIEW_REFRESHING;
						if (listener != null)
							listener.Load();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (footerView != null) {
						if (footerview_more != null)
							footerview_more.setText(R.string.list_loadmore);
						if (footerview_date != null) {
							Date now = new Date();
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
							String date = dateFormat.format(now);
							footerview_date.setText(date);
						}
						footerView.setPadding(0, 0, 0, 0);
						RefreshView();
					}
					break;
				default:
					break;
				}
			}
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 完成刷新
	 */
	public void onCompleteRefresh() {
		AnimationHeaderView(0, -headerContentHeight);
		HEADERVIEW_FLAG = HEADERVIEW_DONE;
		HEADERVIEW_SUCCESS_FLAG = HEADERVIEW_TO_RELEASE;
	}

	/**
	 * 取消下拉刷新
	 */
	public void onCancelRefresh() {
		headerView.setPadding(0, -headerContentHeight, 0, 0);
		RefreshView();
		HEADERVIEW_FLAG = HEADERVIEW_DONE;
		HEADERVIEW_SUCCESS_FLAG = HEADERVIEW_TO_RELEASE;
	}

	/**
	 * 是否使用下拉刷新
	 * 
	 * @param val
	 */
	public void setShowHeaderView(boolean val) {
		this.showHeaderView = val;
	}

	/**
	 * 是否使用上拉加载
	 * 
	 * @param val
	 */
	public void setShowFooterView(boolean val) {
		this.showFooterView = val;
	}

	// 状态后的操作
	private void changeHeaderViewByState() {

		Log.i("xxz", "HEADERVIEW_FLAG:" + HEADERVIEW_FLAG);

		// 用户进入下拉状态
		if (HEADERVIEW_FLAG == HEADERVIEW_RELEASE) {
			if (headerView != null) {

				// 动画操作
				if (!animationDrawable.isRunning()) {
					animationDrawable.stop();
					animationDrawable.start();
				}

				// 视图操作
				headerView.setPadding(0, (HEADERVIEW_TEMPY - HEADERVIEW_STARTY) / DOWN_RATIO - headerContentHeight, 0, 0);
				RefreshView();

				if (HEADERVIEW_SUCCESS_FLAG == HEADERVIEW_TO_RELEASE) {
					// 未到达
					loading_text.setText(R.string.list_pull);
				} else {
					// 已到达
					loading_text.setText(R.string.list_release);
				}
			}
		}

		// 用户进入到刷新状态
		if (HEADERVIEW_FLAG == HEADERVIEW_LOADING) {
			if (headerView != null) {
				AnimationHeaderView((HEADERVIEW_TEMPY - HEADERVIEW_STARTY) / DOWN_RATIO - headerContentHeight, 0);
				loading_text.setText(R.string.list_refreshing);

				if (listener != null)
					listener.Refresh();
			}
		}

		// 用户处于回归状态
		if (HEADERVIEW_FLAG == HEADERVIEW_RELEASE_TO_DONE) {
			AnimationHeaderView((HEADERVIEW_TEMPY - HEADERVIEW_STARTY) / DOWN_RATIO - headerContentHeight, -headerContentHeight);
			HEADERVIEW_FLAG = HEADERVIEW_DONE;
		}

	}

	// headerView回归动画
	private void AnimationHeaderView(float start, float end) {
		ValueAnimator animation = ValueAnimator.ofFloat(start, end);
		animation.setDuration(500);
		animation.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				Float fPos = (Float) animation.getAnimatedValue();
				headerView.setPadding(0, fPos.intValue(), 0, 0); // 下拉刷新
				RefreshView();
			}
		});
		animation.start();
	}

	/**
	 * 完成上拉加载
	 */
	public void onCompleteLoad() {
		footerView.setPadding(0, -footerContentHeight, 0, 0);
		FOOTERVIEW_STATE = FOOTERVIEW_NONE;
	}

	/**
	 * 事件回调
	 * 
	 * @author Aports
	 * 
	 */
	public interface onGridViewListener {
		public void Refresh();

		public void Load();
	}

	/**
	 * 设置回调事件
	 * 
	 * @param listener
	 */
	public void setOnGridViewListener(onGridViewListener listener) {
		this.listener = listener;
	}

	// 刷新headerview视图
	private void RefreshView() {
		ListAdapter mAdapter = getAdapter();
		if (mAdapter != null) {
			((HeaderViewGridAdapter) mAdapter).notifyDataSetChanged();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (firstVisibleItem == 0) {
			isRefreshable = true;
			isLoadhable = false;
		} else if ((firstVisibleItem + visibleItemCount) >= totalItemCount) {
			isRefreshable = false;
			isLoadhable = true;
		} else {
			isRefreshable = false;
			isLoadhable = false;
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
