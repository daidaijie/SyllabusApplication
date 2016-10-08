package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SyllabusCollectionPagerAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;

import butterknife.BindView;

public class SyllabusCollectionActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.containerViewPager)
    ViewPager mContainerViewPager;

    SyllabusCollectionPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPagerAdapter = new SyllabusCollectionPagerAdapter(getSupportFragmentManager());

        mContainerViewPager.setAdapter(mPagerAdapter);

        //      设置tabLayout关联containerViewPager
        mContainerViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mContainerViewPager);
//      修改两个Tab的文字
        mTabLayout.getTabAt(0).setText("收集课表");
        mTabLayout.getTabAt(1).setText("发送课表");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_syllabus_collection;
    }
}
