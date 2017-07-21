package com.alex.materialdesign.module.scrollportrait;

import android.os.Bundle;
import android.view.View;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDActivity;

import org.alex.util.LogTrack;

/**
 * 作者：Alex
 * 时间：2017/2/26 18:48
 * 简述：
 */
public class ScrollPortraitActivity extends MDActivity {
    @Override
    public int getLayoutResId() {
        return R.layout.activity_scroll_portrait;
    }

    @Override
    public void onCreateData(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(R.id.iv_portrait == v.getId()){
            LogTrack.e(" -- ");
        }

    }
}
