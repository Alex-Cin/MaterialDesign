package com.alex.materialdesign.module.recyclerview.adapter;

import com.alex.materialdesign.R;
import com.chad.adapter.Holder;
import com.chad.adapter.SingleRecyclerAdapter;

import org.alex.util.LogTrack;

/**
 * 作者：Alex
 * 时间：2017/2/26 08:23
 * 简述：
 */
public class RecyclerAdapter extends SingleRecyclerAdapter<String>{
    @Override
    public int getLayoutResId() {
        return R.layout.item_recycler_view;
    }

    @Override
    public int getItemCount() {
        LogTrack.e(super.getItemCount()+" --- ");
        return super.getItemCount();
    }

    @Override
    protected void onConvert(Holder holder, String result, int position) {

        LogTrack.e("---");
    }
}
