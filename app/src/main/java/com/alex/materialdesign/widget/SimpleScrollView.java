package com.alex.materialdesign.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import org.alex.util.LogTrack;

/**
 * 作者：Alex
 * 时间：2017/3/6 23:57
 * 简述：
 */
public class SimpleScrollView extends ScrollView {
    public SimpleScrollView(Context context) {
        super(context);
    }

    public SimpleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogTrack.i(super.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }
}
