package com.jeanboy.demo.ui.widget.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;
import android.widget.TextView;

import com.jeanboy.demo.R;

/**
 * Created by yule on 2016/5/5.
 */
public class RefreshLayout extends ViewGroup {

    private static final String TAG = "RefreshLayout";
    public static final int STATE_NONE = 0;//空闲状态
    public static final int STATE_RESET = 1;//释放
    public static final int STATE_PULL_TO_REFRESH = 2;//下拉
    public static final int STATE_RELEASE_TO_REFRESH = 3;//达到可以刷新的距离
    public static final int STATE_REFRESHING = 4;//刷新数据

    public static final int STATE_LOADING = 5;//加载数据
    public static final int STATE_NO_MORE_DATA = 6;//没有更多

    public static int currentState = STATE_NONE;//初始化状态
    public static int lastState = STATE_NONE;//上次状态

    private static final int SCROLL_DURATION = 150;//回滚的时间
    private static final float OFFSET_RADIO = 1.7f;//阻尼系数
    private int mDownMotionY = -1;
    private int mLastMotionY = -1;//上一次移动的点

    private View mHeaderView;
    private TextView mStateView;
    private View mContentView;
    private View mFooterView;

    private int mHeaderHeight;
    private int mScreenHeight;

    private boolean isEnabled = true;
    private int mTouchSlop;//最小滑动距离
    private Scroller mScroller;

    private RefreshHandler mRefreshHandler;

    private boolean disallowIntercept = true;//默认不允许拦截（即，往子view传递事件），该属性只有在interceptAllMoveEvents为false的时候才有效

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout, 0, 0);
        if (array != null) {
            LayoutInflater mInflater = LayoutInflater.from(getContext());
            int mHeaderResId = array.getResourceId(R.styleable.RefreshLayout_refresh_header, -1);

            if (mHeaderResId > -1) {
                mHeaderView = mInflater.inflate(mHeaderResId, this, false);
            }
            array.recycle();
        }
        mScroller = new Scroller(context);
        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mTouchSlop = conf.getScaledTouchSlop();
        //        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        mHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());//默认高度

    }


    @Override
    protected void onFinishInflate() {//当View中所有的子控件均被映射成xml后触发

        mContentView = getChildAt(0);
        LayoutParams params = mHeaderView.getLayoutParams();
        params.height = mHeaderHeight;
        addView(mHeaderView, 0, params);

        mStateView = (TextView) mHeaderView.findViewById(R.id.tv_state);

        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int headerMeasureSpec = MeasureSpec.makeMeasureSpec(mHeaderHeight, MeasureSpec.EXACTLY);
        mHeaderView.measure(widthMeasureSpec, headerMeasureSpec);

        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);//父类期望的宽度
        int height = MeasureSpec.getSize(heightMeasureSpec);//父类期望的高度
        setMeasuredDimension(width, height);//设置自己的宽度和高度
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int headerWidth = mHeaderView.getMeasuredWidth();
        int headerHeight = mHeaderView.getMeasuredHeight();

        mHeaderView.layout(0, -headerHeight, headerWidth, 0);

        int contentWidth = mContentView.getMeasuredWidth();
        int contentHeight = mContentView.getMeasuredHeight();

        mContentView.layout(0, 0, contentWidth, contentHeight);


    }

    /**
     * 事件分发处理
     *
     * @param ev
     * @return 处理是否分发事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private float mDownX, mDownY;

    /**
     * 事件拦截处理
     *
     * @param ev
     * @return 是否拦截children touch事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveX = ev.getX();
                float mMoveY = ev.getY();

                if (Math.abs(mMoveX - mDownX) > Math.abs(mMoveY - mDownY) || canScrollUp()) {// 水平方向移动
                    return super.onInterceptTouchEvent(ev);
                }
                break;
        }

        return isEnabled;
    }

    /**
     * touch事件处理
     *
     * @param event
     * @return 是否消费touch事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                float mMoveY = event.getY();
                int scrollY = getScrollY();
                int mDiffY = (int) (mDownY - mMoveY);
                Log.d(TAG, "=======mDiffY====" + mDiffY + "==scrollY==" + scrollY);

                if (currentState != STATE_REFRESHING && scrollY < 0) {
                    if (Math.abs(scrollY) >= mHeaderHeight) {
                        currentState = STATE_RELEASE_TO_REFRESH;
                        mStateView.setText(getResources().getString(R.string.msg_release_to_refresh));
                    } else {
                        currentState = STATE_PULL_TO_REFRESH;
                        mStateView.setText(getResources().getString(R.string.msg_pull_down_to_refresh));
                    }
                    lastState = currentState;
                }


                if (currentState == STATE_REFRESHING && mDiffY > 0) {//上拉处理
                    return true;
                }

                scrollBy(0, mDiffY);
                mDownX = event.getX();
                mDownY = mMoveY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                release();
                break;
        }

        return true;
    }


    public boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return mContentView.getScrollY() > 0;
            }
        } else {
            return mContentView.canScrollVertically(-1);
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 释放
     */
    private void release() {
        Log.d(TAG, "=======currentState====" + currentState);
        int currentY = getScrollY();
        int dy = -currentY;
        if (currentState == STATE_RELEASE_TO_REFRESH || currentState == STATE_REFRESHING) {
            dy = -(currentY + mHeaderHeight);
            currentState = STATE_REFRESHING;
            mStateView.setText(getResources().getString(R.string.msg_refreshing));
            if (mRefreshHandler != null) {
                mRefreshHandler.onRefreshBegin();
            }

        }
        mScroller.startScroll(0, currentY, 0, dy);
        invalidate();
    }

    public void refreshComplete() {
        currentState = STATE_NONE;
        int currentY = getScrollY();
        int dy = -currentY;
        mScroller.startScroll(0, currentY, 0, dy);
        invalidate();
    }

    public void setHandler(RefreshHandler mRefreshHandler) {
        this.mRefreshHandler = mRefreshHandler;
    }
}
