package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;

import com.example.daidaijie.syllabusapplication.R;

public class SplashActivity extends BaseActivity {

    private static final long SHOW_TIME_MIN = 1500;// 最小显示时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}
