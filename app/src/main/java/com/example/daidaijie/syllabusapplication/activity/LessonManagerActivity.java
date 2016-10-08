package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LessonManagerPagerAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;

import butterknife.BindView;

public class LessonManagerActivity extends BaseActivity {


    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lessonManagerViewPager)
    ViewPager mLessonManagerViewPager;

    private LessonManagerPagerAdapter mLessonManagerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLessonManagerPagerAdapter = new LessonManagerPagerAdapter(getSupportFragmentManager());
        mLessonManagerViewPager.setAdapter(mLessonManagerPagerAdapter);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lesson_manager;
    }
}
