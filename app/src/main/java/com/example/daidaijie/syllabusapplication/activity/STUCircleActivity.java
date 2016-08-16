package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StuCirclePagerAdapter;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;

import org.greenrobot.eventbus.EventBus;

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

    private long touchSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);

        touchSecond = 0;
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.currentThreadTimeMillis() - touchSecond < 200) {
                    EventBus.getDefault().post(new ToTopEvent(false,false));
                } else {
                    Toast.makeText(STUCircleActivity.this, "再次点击回到顶部", Toast.LENGTH_SHORT).show();
                }
                touchSecond = SystemClock.currentThreadTimeMillis();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPagerAdapter = new StuCirclePagerAdapter(getSupportFragmentManager());

        mContainerViewPager.setAdapter(mPagerAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_stucircle;
    }


}
