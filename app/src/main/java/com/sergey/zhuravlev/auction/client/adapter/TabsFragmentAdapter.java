package com.sergey.zhuravlev.auction.client.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sergey.zhuravlev.auction.client.fragment.AbstractTabFragment;
import com.sergey.zhuravlev.auction.client.fragment.CategoriesFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private CategoriesFragment categoriesFragment;
    private Context context;

    public TabsFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.tabs = new HashMap<>();
        //homeFragment = HomeFragment.getInstance(context, client);
        //CategoriesFragment categoriesFragment1 = CategoriesFragment.getInstance(context);
        categoriesFragment = CategoriesFragment.getInstance(context);
        tabs.put(0, categoriesFragment);
        //tabs.put(1, categoriesFragment2);
    }

    public CategoriesFragment getCategoriesFragment() {
        return categoriesFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

}