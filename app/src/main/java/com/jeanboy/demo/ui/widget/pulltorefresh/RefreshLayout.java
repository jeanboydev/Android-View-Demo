package com.jeanboy.demo.ui.widget.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
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

    private View mHeaderLayout;
    private View mContentLayout;
    private View mFooterLayout;

    private int mHeaderHeight;
    private int mScreenHeight;

    private boolean mPullRefreshEnabled = true;
    private int mTouchSlop;//最小滑动距离
    private Scroller mScroller;


    private int mHeaderResId;

    private boolean isFirst = true;

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
            mHeaderResId = array.getResourceId(R.styleable.RefreshLayout_refresh_header, -1);

            if (mHeaderResId > -1) {
                mHeaderLayout = mInflater.inflate(mHeaderResId, this, false);
                addView(mHeaderLayout, 0, mHeaderLayout.getLayoutParams());
            }
            array.recycle();
        }
        mScroller = new Scroller(context);
        mHeaderHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130,
                getResources().getDisplayMetrics());
        ViewConfiguration conf = ViewConfiguration.get(getContext());
        mTouchSlop = conf.getScaledTouchSlop();
        mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
        mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }


    @Override
    protected void onFinishInflate() {//当View中所有的子控件均被映射成xml后触发
        final int childCount = getChildCount();
        if (childCount > 2) {//只能包含header和content
            throw new IllegalStateException("RefreshView only can host 2 elements");
        } else if (childCount == 2) {//header和content都有
            if (mHeaderResId > -1 && mHeaderLayout == null) {
                mHeaderLayout = findViewById(mHeaderResId);
            }
            // not specify header or content
            if (mContentLayout == null || mHeaderLayout == null) {
                View child1 = getChildAt(0);
                View child2 = getChildAt(1);
                // both are not specified
                if (mContentLayout == null && mHeaderLayout == null) {
                    mHeaderLayout = child1;
                    mContentLayout = child2;
                }
                // only one is specified
                else {
                    if (mHeaderLayout == null) {
                        mHeaderLayout = mContentLayout == child1 ? child2 : child1;
                    } else {
                        mContentLayout = mHeaderLayout == child1 ? child2 : child1;
                    }
                }
            }
//            addView(mHeaderLayout, 0, mHeaderLayout.getLayoutParams());
//            addView(mContentLayout, 1, mHeaderLayout.getLayoutParams());
        } else if (childCount == 1) {//默认设置的为content
            mContentLayout = getChildAt(0);
        } else {//没有内容显示error信息
            TextView errorView = new TextView(getContext());
            errorView.setClickable(true);
            errorView.setTextColor(0xffff6600);
            errorView.setGravity(Gravity.CENTER);
            errorView.setTextSize(20);
            errorView.setText("The content view in RefreshLayout is empty. Do you forget to specify its id in xml layout file?");
            mContentLayout = errorView;
            addView(mContentLayout);
        }


        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChild(mHeaderLayout, widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));


        measureChild(mContentLayout, widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeaderLayout.layout(0, 0, mHeaderLayout.getMeasuredWidth(), mHeaderLayout.getMeasuredHeight());
        mContentLayout.layout(0, mHeaderLayout.getMeasuredHeight(), mContentLayout.getMeasuredWidth(),
                mHeaderLayout.getMeasuredHeight() + mContentLayout.getMeasuredHeight());


        if (isFirst) {
            LayoutParams lp = mHeaderLayout.getLayoutParams();
            lp.height = mHeaderHeight;
            mHeaderLayout.setLayoutParams(lp);

            scrollTo(0, mHeaderLayout.getMeasuredHeight());
        }
        isFirst = false;
    }

    public final static int TOUCH_STATE_DEFAULT = 0; //默认
    public final static int TOUCH_STATE_SCROLLING = 1;//当前在滑动状态
    public final static int TOUCH_STATE_FLING = 2;//当前fling状态

    private int mTouchStatus = TOUCH_STATE_DEFAULT;

    private int mPointerId = 0;//多点触控

    private int mTotalLength = 0;
    private int mMaximumVelocity = 0;
    private VelocityTracker mVelocityTracker;


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
                if (xDiff > mTouchSlop) {// 超过了最小滑动距离
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
                if (currentState == STATE_RELEASE_TO_REFRESH) {
                    currentState = STATE_REFRESHING;
                } else {
                    currentState = STATE_NONE;
                }

                mVelocityTracker.computeCurrentVelocity(1000);
                if (Math.abs(mVelocityTracker.getYVelocity()) > mMaximumVelocity && !checkIsBroad()) {
                    mScroller.fling(getScrollX(), getScrollY(), 0, -(int) mVelocityTracker.getYVelocity(), 0, 0, 0, mTotalLength - getHeight());
                } else {
                    actionUP(); //回弹效果
                }

                mTouchStatus = TOUCH_STATE_DEFAULT;


                break;
            case MotionEvent.ACTION_POINTER_UP://添加多点触控的支持

                if (currentState == STATE_RELEASE_TO_REFRESH) {
                    currentState = STATE_REFRESHING;
                } else {
                    currentState = STATE_NONE;
                }


                if (ev.getPointerId(touchIndex) == mPointerId) {
                    int newIndex = touchIndex == 0 ? 1 : 0;
                    mPointerId = ev.getPointerId(newIndex);
                    mDownMotionY = (int) (ev.getY(newIndex) + 0.5f);
                    mLastMotionY = mDownMotionY;
                }


                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchStatus = TOUCH_STATE_DEFAULT;
                break;
            case MotionEvent.ACTION_DOWN:
                mPointerId = ev.getPointerId(0);
                mDownMotionY = (int) ev.getY();//记录按下的点
                mLastMotionY = mDownMotionY;

                break;
            case MotionEvent.ACTION_POINTER_DOWN: //添加多点触控的处理
                mPointerId = ev.getPointerId(touchIndex);
                mDownMotionY = (int) (ev.getY(touchIndex) + 0.5f); //记录按下的点
                mLastMotionY = mDownMotionY;

                break;
            case MotionEvent.ACTION_MOVE:
                touchIndex = ev.findPointerIndex(mPointerId);
                if (touchIndex < 0) //当前index小于0就返false继续接受下一次事件
                    return false;
                int scrollY = (int) (mLastMotionY - ev.getY(touchIndex)); //计算滑动的距离
                scrollBy(0, scrollY); //调用滑动函数
                int mTempMotionY = (int) ev.getY(touchIndex); //记录上一次按下的点


                int mScrollY = mTempMotionY - mDownMotionY;

                Log.d("====mScrollY=======", "===" + mScrollY + "====scrollY==" + scrollY + "====mDownMotionY==" + mDownMotionY + "====mTempMotionY==" + mTempMotionY);

                if (mScrollY >= mTouchSlop) {
                    if (currentState == STATE_NONE) {
                        currentState = STATE_PULL_TO_REFRESH;
                    }

                    if (currentState == STATE_PULL_TO_REFRESH) {
                        if (mScrollY > (mHeaderHeight * 2 / 3)) {
                            currentState = STATE_RELEASE_TO_REFRESH;
                        }
                    }
                    int scaleHeight = (int) (mScrollY * 0.8f);// 去了滑动距离的80%，减小灵敏度而已
                    Log.d("====mScrollY=======", "===" + mScrollY + "====scaleHeight==" + scaleHeight);
                    if (scaleHeight <= mScreenHeight / 4) {
                        adjustHeaderPadding(scaleHeight);
                    }
                }


                mLastMotionY = mTempMotionY;

                break;
        }
        return true;
    }

    private void actionUP() {
        if (getScrollY() < 0 || getHeight() > mTotalLength) {//顶部回弹
            if (currentState == STATE_REFRESHING) {
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY()); //开启回弹效果
            } else {
                mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() - mHeaderLayout.getHeight())); //开启回弹效果
            }

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
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        // 判断当前视图是否超过了顶部或者顶部就让它滑动的距离为1/3这样就有越拉越拉不动的效果
        if (getScrollY() < 0 || getScrollY() + getHeight() > mTotalLength) {
            super.scrollBy(x, y / 3);
        } else {
            super.scrollBy(x, y);
        }
    }

    /**
     * 调整header view的top padding
     *
     * @param topPadding
     */
    private void adjustHeaderPadding(int topPadding) {
        mHeaderLayout.setPadding(mHeaderLayout.getPaddingLeft(), topPadding,
                mHeaderLayout.getPaddingRight(), 0);
    }


}
