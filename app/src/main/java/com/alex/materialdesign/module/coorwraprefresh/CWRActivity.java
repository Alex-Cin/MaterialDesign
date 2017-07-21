package com.alex.materialdesign.module.coorwraprefresh;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDActivity;
import com.alex.materialdesign.module.recyclerview.RecyclerFragment;
import com.alex.materialdesign.module.scrollview.ScrollFragment;
import com.alex.materialdesign.module.webview.WebFragment;

import org.alex.adapter.TitleFragmentPagerAdapter;
import org.hellojp.tabsindicator.TabsIndicator;

/**
 * 作者：Alex
 * 时间：2017/2/24 16:21
 * 简述： 协调布局 包裹着 下拉刷新
 * -------- 主页：{@link com.alex.materialdesign.module.MainActivity}
 */
public class CWRActivity extends MDActivity {
    private LinearLayout headLayout;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_cwr_1;
    }

    @Override
    public void onCreateData(Bundle bundle) {
        AppBarLayoutOnOffsetChangedListener onOffsetChangedListener = new AppBarLayoutOnOffsetChangedListener();

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.ab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.vp);
        headLayout = (LinearLayout) findViewById(R.id.layout_head);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.co_to_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(this);
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        adapter.addFm(new ScrollFragment(), new RecyclerFragment(), new WebFragment());
        adapter.addTitle("ScrollView", "RecyclerView", "WebView");

        TabsIndicator tabsIndicator = (TabsIndicator) findViewById(R.id.ti);
        tabsIndicator.setViewPager(0, viewPager);
    }
    private final class AppBarLayoutOnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset <= -headLayout.getHeight() / 2) {
                collapsingToolbarLayout.setTitle("Alex");
            } else {
                collapsingToolbarLayout.setTitle("");
            }
        }
    }
}
