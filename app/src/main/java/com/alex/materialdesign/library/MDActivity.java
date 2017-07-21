package com.alex.materialdesign.library;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import org.alex.baseui.AbsActivity;

/**
 * 作者：Alex
 * 时间：2017/2/24 16:15
 * 简述：
 */
public abstract class MDActivity extends AbsActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }
}
