package com.jeanboy.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeanboy.demo.R;

/**
 * Created by yule on 2016/4/29.
 */
public class ToolBarUtil {

    private static View mStatusBarView;

    /**
     * 简单型状态栏(ToolBar)
     *
     * @param activity
     */
    public static void setOrdinaryToolBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            setKKStatusBar(activity, R.color.colorPrimaryDark);
        }
    }

    /**
     * 图片全屏透明状态栏（图片位于状态栏下面）
     *
     * @param activity
     */
    public static void setImageTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    /**
     * 图片全屏半透明状态栏（图片位于状态栏下面）
     *
     * @param activity
     */
    public static void setImageTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        } else {
            setKKStatusBar(activity, R.color.colorPrimary);
        }
    }

    /**
     * ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     */
    public static void setToolbarTabLayout(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }

    /**
     * DrawerLayout+ToolBar+TabLayout状态栏(ToolBar可伸缩)
     *
     * @param activity
     * @param coordinatorLayout
     */
    public static void setDrawerToolbarTabLayout(Activity activity, CoordinatorLayout coordinatorLayout) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            coordinatorLayout.setFitsSystemWindows(true);
            setKKStatusBar(activity, R.color.colorPrimaryDark);
        }
    }

    /**
     * CollapsingToolbarLayout状态栏(可折叠图片)
     *
     * @param activity
     * @param coordinatorLayout
     * @param appBarLayout
     * @param imageView
     * @param toolbar
     */
    public static void setCollapsingToolbar(Activity activity, CoordinatorLayout coordinatorLayout,
                                            AppBarLayout appBarLayout, ImageView imageView, Toolbar toolbar) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            coordinatorLayout.setFitsSystemWindows(false);
            appBarLayout.setFitsSystemWindows(false);
            imageView.setFitsSystemWindows(false);
            toolbar.setFitsSystemWindows(true);
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = (int) (getStatusBarHeight(activity) +
                    activity.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
            toolbar.setLayoutParams(lp);
            setKKStatusBar(activity, R.color.colorPrimaryDark);
            setCollapsingToolbarStatus(appBarLayout);
        }
    }

    /**
     * DrawerLayout+ToolBar型状态栏
     *
     * @param activity
     */
    public static void setDrawerToolbar(Activity activity) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) activity.findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
            setKKStatusBar(activity, R.color.colorPrimaryDark);
        }
    }

    /**
     * Android4.4上CollapsingToolbar折叠时statusBar显示和隐藏
     *
     * @param appBarLayout
     */
    private static void setCollapsingToolbarStatus(AppBarLayout appBarLayout) {
        ViewCompat.setAlpha(mStatusBarView, 1);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mStatusBarView, percentage);
            }
        });
    }

    private static void setKKStatusBar(Activity activity, int statusBarColor) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        mStatusBarView = contentView.getChildAt(0);
        //改变颜色时避免重复添加statusBarView
        if (mStatusBarView != null && mStatusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
            mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
            return;
        }
        mStatusBarView = new View(activity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        mStatusBarView.setBackgroundColor(ContextCompat.getColor(activity, statusBarColor));
        contentView.addView(mStatusBarView, lp);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}
