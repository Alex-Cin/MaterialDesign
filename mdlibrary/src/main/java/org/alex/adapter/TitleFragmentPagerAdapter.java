package org.alex.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.alex.util.ObjUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Alex
 * 时间：2017/2/26 08:02
 * 简述：
 */

@SuppressWarnings("all")
public class TitleFragmentPagerAdapter extends FragmentPagerAdapter {
    protected List<Fragment> listFm;
    protected List<String> listTitle;

    public TitleFragmentPagerAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public TitleFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFm) {
        this(fm, listFm, null);
    }

    public TitleFragmentPagerAdapter(FragmentManager fm, List<Fragment> listFm, List<String> listTitle) {
        super(fm);
        if (ObjUtil.isEmpty(listFm)) {
            listFm = new ArrayList<>();
        }
        if (ObjUtil.isEmpty(listTitle)) {
            listTitle = new ArrayList<>();
        }
        this.listFm = listFm;
        this.listTitle = listTitle;
        notifyDataSetChanged();
    }

    public void addTitle(List<String> listTitle) {
        if (listTitle == null || listTitle.size() <= 0 || this.listTitle == null) {
            return;
        }
        this.listTitle.addAll(listTitle);
        notifyDataSetChanged();
    }

    public void addTitle(String... title) {
        List<String> listTitle = new ArrayList<>();
        for (int i = 0; (title != null) && (i < title.length); i++) {
            listTitle.add(title[i]);
        }
        addTitle(listTitle);
    }

    public void addFm(Fragment fragment) {
        if (fragment == null || listFm == null) {
            return;
        }
        listFm.add(fragment);
        notifyDataSetChanged();
    }

    public void addFm(Fragment... fragment) {
        List<Fragment> listFragment = new ArrayList<>();
        for (int i = 0; (fragment != null) && (i < fragment.length); i++) {
            listFragment.add(fragment[i]);
        }
        addFm(listFragment);
    }

    public void addFm(List<Fragment> list) {
        if (list == null || list.size() <= 0 || this.listFm == null) {
            return;
        }
        this.listFm.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return ((listFm == null) ? null : listFm.get(position));
    }

    @Override
    public int getCount() {
        return ((listFm == null) ? 0 : listFm.size());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (listTitle == null) ? "" : listTitle.get(position);
    }
}


