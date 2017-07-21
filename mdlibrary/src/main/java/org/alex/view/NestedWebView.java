package org.alex.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * 作者：Alex
 * 时间：2017/2/26 08:10
 * 简述：
 */
public class NestedWebView extends WebView {

    public OnScrollChangeListener listener;


    public NestedWebView(Context context) {
        super(context);
    }

    public NestedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {//t就是x轴,l是y轴

        super.onScrollChanged(l, t, oldl, oldt);

        float webcontent = getContentHeight() * getScale();// webview的高度
        float webnow = getHeight() + getScrollY();// 当前webview的高度
        //Log.i("TAG1", "webview.getScrollY()====>>" + getScrollY());
        if (Math.abs(webcontent - webnow) < 1) {
            // 已经处于底端
            Log.i("TAG1", "已经处于底端");
            listener.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            Log.i("TAG1", "已经处于顶端");
            listener.onPageTop(l, t, oldl, oldt);
        } else {
            listener.onScrollChanged(l, t, oldl, oldt);
        }

        //这是X轴
        if (t - oldt > 10) {
            Log.i("TAG1", "t - oldt > 0 .....");
            listener.onDown();
        } else {
            listener.onUp();
        }

    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {

        this.listener = listener;

    }

    public interface OnScrollChangeListener {

        void onPageEnd(int l, int t, int oldl, int oldt);

        void onPageTop(int l, int t, int oldl, int oldt);

        void onScrollChanged(int l, int t, int oldl, int oldt);

        void onUp();

        void onDown();

    }

}

