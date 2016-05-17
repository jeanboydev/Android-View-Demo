package com.jeanboy.demo.ui.widget.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeanboy.demo.R;

/**
 * Created by yule on 2016/5/5.
 */
public class RefreshLayout extends ViewGroup {

    public static final int STATE_NONE = 0;//默认状态
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
    private float mLastMotionY = -1;//上一次移动的点

    private View mHeaderLayout;
    private View mContentLayout;
    private View mFooterLayout;
    private int mHeaderHeight;
    private int mFooterHeight;

    private boolean mPullRefreshEnabled = true;
    private int mTouchSlop;//最小滑动距离


    private int mHeaderResId;

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
//        setOrientation(LinearLayout.VERTICAL);

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

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        return true;
    }


}
