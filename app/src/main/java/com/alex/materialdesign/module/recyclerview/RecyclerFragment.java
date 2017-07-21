package com.alex.materialdesign.module.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.alex.materialdesign.R;
import com.alex.materialdesign.library.MDFragment;
import com.alex.materialdesign.module.recyclerview.adapter.RecyclerAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.alex.helper.recyclerview.RecyclerViewHelper;
import org.alex.helper.recyclerview.SimpleItemDecoration;
import org.alex.helper.recyclerview.library.LayoutType;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Alex
 * 时间：2017/2/26 07:47
 * 简述：
 */
public class RecyclerFragment extends MDFragment {
    private XRecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    public void onCreateData(Bundle bundle) {
        recyclerView = findView(R.id.rv);
        recyclerView.setLoadingListener(new MyLoadingListener());
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallBeat);
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        SimpleItemDecoration.Builder.getInstance().backgroundColor("#DDDDDD").color("#DDDDDD").dividerSize(2.0F).build().attachToRecyclerView(recyclerView);
        RecyclerViewHelper.Builder.getInstance().layoutManager(LayoutType.VLinearLayout).build().attachToRecyclerView(recyclerView);
        adapter.refreshItem(getDataList(true));
    }

    private List getDataList(boolean isRefresh) {
        List list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            list.add(isRefresh ? "最新数据 " + i : "更多数据 " + i);
        }
        return list;
    }

    private final class MyLoadingListener implements XRecyclerView.LoadingListener {

        @Override
        public void onRefresh() {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.refreshItem(getDataList(true));
                    recyclerView.loadMoreComplete();
                    recyclerView.refreshComplete();
                }
            }, 1500);

        }

        @Override
        public void onLoadMore() {

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    adapter.addItem(getDataList(false));
                    recyclerView.loadMoreComplete();
                    recyclerView.refreshComplete();
                }
            }, 1500);

        }
    }
}
