package com.example.junyoung.dropchecker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.fragments.BuyAndSellPostFragment;

public class FixedTabsPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {
    private Context context;
    private String pageTitle;

    public FixedTabsPagerAdapter(android.support.v4.app.FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                pageTitle = context.getString(R.string.want_to_buy_post_page_title);
                return BuyAndSellPostFragment.newInstance(pageTitle);
            case 1:
                pageTitle = context.getString(R.string.want_to_sell_post_page_title);
                return BuyAndSellPostFragment.newInstance(pageTitle);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.want_to_buy_post_page_title);
            case 1:
                return context.getString(R.string.want_to_sell_post_page_title);
            default:
                return null;
        }
    }
}
