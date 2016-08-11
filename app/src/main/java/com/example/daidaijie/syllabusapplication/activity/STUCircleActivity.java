package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StuCirclePagerAdapter;

import butterknife.BindView;

public class STUCircleActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.containerViewPager)
    ViewPager mContainerViewPager;

    StuCirclePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPagerAdapter = new StuCirclePagerAdapter(getSupportFragmentManager());

        mContainerViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_stucircle;
    }


}
