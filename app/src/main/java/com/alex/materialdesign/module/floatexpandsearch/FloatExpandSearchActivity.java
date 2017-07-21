package com.alex.materialdesign.module.floatexpandsearch;

import android.os.Bundle;
import android.view.View;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDActivity;
import com.alex.materialdesign.widget.SearchLayout;

/**
 * 作者：Alex
 * 时间：2017/2/27 19:30
 * 简述：
 * 主页：{@link com.alex.materialdesign.module.MainActivity}
 */
public class FloatExpandSearchActivity extends MDActivity {
    private SearchLayout searchLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_float_expand_search;
    }

    @Override
    public void onCreateData(Bundle bundle) {
        searchLayout = findView(R.id.sl);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (R.id.iv_portrait == id) {
            searchLayout.startFolding();
        }
    }
}
