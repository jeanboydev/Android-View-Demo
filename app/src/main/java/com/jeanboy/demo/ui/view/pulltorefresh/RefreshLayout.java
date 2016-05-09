package com.jeanboy.demo.ui.view.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;

import com.jeanboy.demo.R;

/**
 * Created by yule on 2016/5/5.
 */
public class RefreshLayout extends ViewGroup {
    // status enum

    public final static int TOUCH_STATE_DEFAULT = 0; //默认
    public final static int TOUCH_STATE_SCROLLING = 1;//当前在滑动状态
    public final static int TOUCH_STATE_FLING = 2;//当前fling状态

    public final static byte STATUS_INIT = 3;
    public final static byte STATUS_PREPARE = 4;//准备加载
    public final static byte STATUS_LOADING = 5;//加载中
    public final static byte STATUS_COMPLETE = 6;//加载完成


    private int mTouchStatus = TOUCH_STATE_DEFAULT;
    private int mPagingTouchSlop;//滑动最小距离

    private int mLastMotionY;//记录上次y的位置

    private byte mStatus = STATUS_INIT;//当前状态

    private boolean isHorizontalMove = false;//是否为水平滚动
    private boolean mDisableWhenHorizontalMove = false;//是否禁止水平滚动

    private int mDurationToClose = 200;
    private int mDurationToCloseHeader = 1000;


    //    private RefreshIndicator refreshIndicator;
    private Scroller mScroller;


    private LayoutInflater mInflater;
    protected View mContent;
    private int mHeaderResId = 0;
    private View mHeaderView;
    //    private int mFooterId = 0;
//    private View mFooterView;
    private int mHeaderHeight;

    private int mLoadingMinTime = 500;
    private long mLoadingStartTime = 0;


    private int mPointerId = 0;//多点触控

    private int mTotalLength = 0;
    private int mMaximumVelocity = 0;
    private VelocityTracker mVelocityTracker;


    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        refreshIndicator = new RefreshIndicator();
        mScroller = new Scroller(context);

        mInflater = LayoutInflater.from(getContext());
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout, 0, 0);
        if (array != null) {
            mHeaderResId = array.getResourceId(R.styleable.RefreshLayout_refresh_header, -1);
//            mFooterId = array.getResourceId(R.styleable.RefreshLayout_refresh_footer, mFooterId);

            if (mHeaderResId > -1) {
                mHeaderView = mInflater.inflate(mHeaderResId, this, false);
                addView(mHeaderView, mHeaderView.getLayoutParams());
            }

            array.recycle();
        }

        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mPagingTouchSlop = conf.getScaledTouchSlop() * 2;
        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        mHeaderHeight = 500;// FIXME: 2016/5/6
    }

    @Override
    protected void onFinishInflate() {//当View中所有的子控件均被映射成xml后触发
        final int childCount = getChildCount();
        if (childCount > 2) {//只能包含header和content
            throw new IllegalStateException("RefreshView only can host 2 elements");
        } else if (childCount == 2) {//header和content都有
            if (mHeaderResId != 0 && mHeaderView == null) {
                mHeaderView = findViewById(mHeaderResId);
            }
            // not specify header or content
            if (mContent == null || mHeaderView == null) {
                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
//                if (child1 instanceof PtrUIHandler) {
//                    mHeaderView = child1;
//                    mContent = child2;
//                } else if (child2 instanceof PtrUIHandler) {
//                    mHeaderView = child2;
//                    mContent = child1;
//                } else {
                // both are not specified
                if (mContent == null && mHeaderView == null) {
                    mHeaderView = child1;
                    mContent = child2;
                }
                // only one is specified
                else {
                    if (mHeaderView == null) {
                        mHeaderView = mContent == child1 ? child2 : child1;
                    } else {
                        mContent = mHeaderView == child1 ? child2 : child1;
                    }
                }
//                }
            }

        } else if (childCount == 1) {//默认设置的为content
            mContent = getChildAt(0);
        } else {//没有内容显示error信息
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in RefreshLayout is empty. Do you forget to specify its id in xml layout file?");
            mContent = errorView;
            addView(mContent);
        }
        if (mHeaderView != null) {
            mHeaderView.bringToFront();
        }
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
//            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);//测量header中的子view
//            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
//            mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
//            refreshIndicator.setHeaderHeight(mHeaderHeight);
        }
        if (mContent != null) {
            measureContentView(mContent, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureContentView(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                getPaddingTop() + getPaddingBottom(), lp.height);

//        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//                getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
//        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
//                getPaddingTop() + getPaddingBottom() + lp.topMargin, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int offsetY = refreshIndicator.getCurrentPosY();
        int offsetY = 0;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        if (mHeaderView != null) {
//            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
//            int left = paddingLeft + lp.leftMargin;
//            int top = paddingTop + lp.topMargin + offsetY - mHeaderHeight;


            int left = paddingLeft;
            int top = paddingTop + offsetY - mHeaderHeight;
            int right = left + mHeaderView.getMeasuredWidth();
            int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }

        if (mContent != null) {

            int left = paddingLeft;
            int top = paddingTop + offsetY;

//            MarginLayoutParams lp = (MarginLayoutParams) mContent.getLayoutParams();
//            int left = paddingLeft + lp.leftMargin;
//            int top = paddingTop + lp.topMargin + offsetY;
            int right = left + mContent.getMeasuredWidth();
            int bottom = top + mContent.getMeasuredHeight();
            mContent.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mTouchStatus != TOUCH_STATE_DEFAULT)) {
            return true;
        }
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                int xDiff = Math.abs(mLastMotionY - y);
                if (xDiff > mPagingTouchSlop) {// 超过了最小滑动距离
                    mTouchStatus = TOUCH_STATE_SCROLLING;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointerId = ev.getPointerId(ev.getActionIndex()); //记录当前pointId
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                if (!mScroller.isFinished()) {//当动画还没有结束的时候强制结束
                    mScroller.abortAnimation();
                    mScroller.forceFinished(true);
                }
                mTouchStatus = TOUCH_STATE_DEFAULT;
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchStatus = TOUCH_STATE_DEFAULT;
                break;
        }

        return mTouchStatus != TOUCH_STATE_DEFAULT;
    }

    public boolean dispatchTouchEventSupper(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (!isEnabled() || mContent == null || mHeaderView == null) {
//            return dispatchTouchEventSupper(ev);
//        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        int touchIndex = ev.getActionIndex();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                if (Math.abs(mVelocityTracker.getYVelocity()) > mMaximumVelocity && !checkIsBroad()) {
                    mScroller.fling(getScrollX(), getScrollY(), 0, -(int) mVelocityTracker.getYVelocity(), 0, 0, 0, mTotalLength - getHeight());
                } else {
                    actionUP(); //回弹效果
                }

                mTouchStatus = TOUCH_STATE_DEFAULT;
                break;
            case MotionEvent.ACTION_POINTER_UP://添加多点触控的支持
                if (ev.getPointerId(touchIndex) == mPointerId) {
                    int newIndex = touchIndex == 0 ? 1 : 0;
                    mPointerId = ev.getPointerId(newIndex);
                    mLastMotionY = (int) (ev.getY(newIndex) + 0.5f);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchStatus = TOUCH_STATE_DEFAULT;
                break;
            case MotionEvent.ACTION_DOWN:
                mPointerId = ev.getPointerId(0);
                mLastMotionY = (int) ev.getY();//记录按下的点
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //添加多点触控的处理
                mPointerId = ev.getPointerId(touchIndex);
                mLastMotionY = (int) (ev.getY(touchIndex) + 0.5f); //记录按下的点
                break;
            case MotionEvent.ACTION_MOVE:
                touchIndex = ev.findPointerIndex(mPointerId);
                if (touchIndex < 0) //当前index小于0就返false继续接受下一次事件
                    return false;
                int scrollY = (int) (mLastMotionY - ev.getY(touchIndex)); //计算滑动的距离
                scrollBy(0, scrollY); //调用滑动函数
                mLastMotionY = (int) ev.getY(touchIndex); //记录上一次按下的点
                break;
        }
        return true;
    }

    private void actionUP() {
        if (getScrollY() < 0 || getHeight() > mTotalLength) {//顶部回弹
            mScroller.startScroll(0, getScrollY(), 0, -getScrollY()); //开启回弹效果
            invalidate();
        } else if (getScrollY() + getHeight() > mTotalLength) {//底部回弹
            //开启底部回弹
            mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + getHeight() - mTotalLength));
            invalidate();
        }
    }

    private boolean checkIsBroad() {
        return getScrollY() < 0 || getScrollY() + getHeight() > mTotalLength;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void changeScrollY(int distance) {
        int currentY = getScrollY();
        scrollBy(0, -distance);
        if (currentY > 0 && currentY > mPagingTouchSlop) {
            mStatus = STATUS_PREPARE;
        }
    }

}
