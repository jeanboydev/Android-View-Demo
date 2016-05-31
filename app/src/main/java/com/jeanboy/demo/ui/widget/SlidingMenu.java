package com.jeanboy.demo.ui.widget;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by yule on 2016/5/25.
 */
public class SlidingMenu extends ViewGroup {

    private View mLeftView;
    private View mContentView;

    private int mLeftViewWidth = 0;
    private Scroller mScroller;
    private int mTouchTapSlop;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchTapSlop = ViewConfiguration.get(context).getScaledDoubleTapSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLeftView = getChildAt(0);
        mContentView = getChildAt(1);

        LayoutParams params = mLeftView.getLayoutParams();
        mLeftViewWidth = params.width;
    }

    /**
     * @param widthMeasureSpec  父view传过来的期望值
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int leftWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mLeftViewWidth, MeasureSpec.EXACTLY);
        mLeftView.measure(leftWidthMeasureSpec, heightMeasureSpec);

        mContentView.measure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);//设置自己的尺寸
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftView.layout(-mLeftView.getMeasuredWidth(), 0, 0, mLeftView.getMeasuredHeight());

        mContentView.layout(0, 0, mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
    }

    float downX, downY, moveX;

    boolean isOpen = false;

    /**
     * 处理是否分发事件
     *
     * @param ev
     * @return true不分发-->执行父类的onTouchEvent，没有父类touch结束
     * false分发执行自己的-->onInterceptTouchEvent
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截children touch事件
     *
     * @param ev
     * @return true 拦截children touch事件-->执行自己的onTouchEvent
     * false 不拦截-->执行children的dispatchTouchEvent
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = ev.getX();
                float moveY = ev.getY();
                if (Math.abs(moveX - downX) >= Math.abs(moveY - downY)) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * touch事件处理，是否消费touch事件
     *
     * @param event
     * @return true消费事件实现触摸逻辑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                int diffX = (int) (downX - moveX + 0.5f);//四舍五入

                int scrollX = getScrollX() + diffX;
                if (scrollX < 0 && scrollX < -mLeftView.getMeasuredWidth()) {
                    scrollTo(-mLeftView.getMeasuredWidth(), 0);
                } else if (scrollX > 0) {
                    scrollTo(0, 0);
                } else {
                    scrollBy(diffX, 0);
                }

                downX = moveX;
                downX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                int width = mLeftView.getMeasuredWidth();
                int currentX = getScrollX();
                float middle = -width / 2f;
                if (currentX > middle) {
                    isOpen = false;
//                    scrollTo(0, 0);

                    mScroller.startScroll(currentX, 0, -currentX, 0);

                } else {
                    isOpen = true;
//                    scrollTo(-width, 0);
                    mScroller.startScroll(currentX, 0, -width - currentX, 0);
                }

                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    //移动手机屏幕
    //scrollTo标点移动 scrollBy增量移动
    //scroller模拟数据变换

//    ViewDragHelper

    class MyCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    }

}
