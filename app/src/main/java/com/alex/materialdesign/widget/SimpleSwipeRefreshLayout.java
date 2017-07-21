package com.alex.materialdesign.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.alex.util.LogTrack;

/**
 * 作者：Alex
 * 时间：2017/3/6 23:58
 * 简述：
 */
public class SimpleSwipeRefreshLayout extends SwipeRefreshLayout {

    public SimpleSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SimpleSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogTrack.i(super.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }
}
