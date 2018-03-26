package com.example.junyoung.dropchecker.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.junyoung.dropchecker.fragments.ProductImageViewFragment;
import com.example.junyoung.dropchecker.R;

public class DropListFixedTabsPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;

    public DropListFixedTabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductImageViewFragment.newInstance(getPageTitle(position).toString());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.supreme);
            case 1:
                return context.getString(R.string.kith);
            case 2:
                return context.getString(R.string.bape);
            case 3:
                return context.getString(R.string.palace);
            default:
                return null;
        }
    }
}
