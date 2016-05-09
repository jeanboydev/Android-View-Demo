package com.jeanboy.demo.ui.view.pulltorefresh;

import android.graphics.PointF;

/**
 * Created by yule on 2016/5/6.
 */
public class RefreshIndicator {

    private float mResistance = 1.7f;
    private PointF ptLastMove = new PointF();
    private float offsetX;
    private float offsetY;
    private int headerHeight;
    private int currentPosY = 0;
    private int LastPosY = 0;
    private int pressedPos = 0;
    private boolean isUnderTouch = false;//是否触摸

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public int getCurrentPosY() {
        return currentPosY;
    }

    public void setCurrentPosY(int currentPosY) {
        LastPosY = currentPosY;
        currentPosY = currentPosY;
    }

    public int getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
    }

    public boolean isUnderTouch() {
        return isUnderTouch;
    }

    public void onRelease() {
        isUnderTouch = false;
    }

    public void onPressDown(float x, float y) {
        isUnderTouch = true;
        pressedPos = currentPosY;
        ptLastMove.set(x, y);
    }

    public final void onMove(float x, float y) {
        float offsetX = x - ptLastMove.x;
        float offsetY = y - ptLastMove.y;
        processOnMove(x, y, offsetX, offsetY);
        ptLastMove.set(x, y);
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY / mResistance);
    }

    protected void setOffset(float x, float y) {
        offsetX = x;
        offsetY = y;
    }
}
