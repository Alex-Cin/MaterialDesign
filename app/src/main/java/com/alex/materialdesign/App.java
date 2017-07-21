package com.alex.materialdesign;

import android.app.Application;

import org.alex.util.BaseUtil;

/**
 * 作者：Alex
 * 时间：2017/2/24 16:15
 * 简述：
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtil.getInstance().init(this);
    }
}
