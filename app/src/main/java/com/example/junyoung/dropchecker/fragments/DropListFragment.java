package com.example.junyoung.dropchecker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.junyoung.dropchecker.R;
import com.example.junyoung.dropchecker.adapter.DropListFixedTabsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DropListFragment extends Fragment {
    @BindView(R.id.viewpager_droplist) ViewPager viewPager;
    @BindView(R.id.tab_droplist) TabLayout tabLayout;

    public static DropListFragment newInstance() {
        return new DropListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_droplist, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new DropListFixedTabsPagerAdapter(getFragmentManager(), getContext()));
        viewPager.setBackgroundResource(R.color.grey_900);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
