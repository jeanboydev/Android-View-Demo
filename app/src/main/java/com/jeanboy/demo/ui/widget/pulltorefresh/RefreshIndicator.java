package com.jeanboy.demo.ui.widget.pulltorefresh;

import android.widget.TextView;

/**
 * Created by yule on 2016/5/6.
 */
public class RefreshIndicator {

    private TextView mStateView;

    public void setStateView(TextView mStateView) {
        this.mStateView = mStateView;
    }


    public void updateStateText(String msg) {
        if (mStateView != null) {
            mStateView.setText(msg);
        }
    }

}
