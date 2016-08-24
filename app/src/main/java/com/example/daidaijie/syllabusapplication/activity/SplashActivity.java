package com.example.daidaijie.syllabusapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.daidaijie.syllabusapplication.R;

public class SplashActivity extends BaseActivity {

    private static final long SHOW_TIME_MIN = 1500;// 最小显示时间

    private long mStartTime;// 开始时间


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartTime = System.currentTimeMillis();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, SHOW_TIME_MIN - mStartTime);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }


}
