package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.OAPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfficeAutomationActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    OAPagerAdapter mOAPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOAPagerAdapter = new OAPagerAdapter(getSupportFragmentManager());
        mContentViewPager.setAdapter(mOAPagerAdapter);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_office_automation;
    }
}
