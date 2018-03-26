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
import com.example.junyoung.dropchecker.adapter.FixedTabsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostFragment extends Fragment {
    @BindView(R.id.viewpager_post) ViewPager viewPager;
    @BindView(R.id.tab_post) TabLayout tabLayout;

    public static PostFragment newInstance() {
        return new PostFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(new FixedTabsPagerAdapter(getFragmentManager(), getContext()));
        viewPager.setBackgroundResource(R.color.grey_900);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
