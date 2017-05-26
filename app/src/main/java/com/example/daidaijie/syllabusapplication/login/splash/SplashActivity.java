package com.example.daidaijie.syllabusapplication.login.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.login.login.LoginActivity;
import com.example.daidaijie.syllabusapplication.main.MainActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashContract.view {

    private long mStartTime;// 开始时间

    @Inject
    SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStartTime = System.currentTimeMillis();

        DaggerSplashComponent.builder()
                .appComponent(mAppComponent)
                .splashModule(new SplashModule(this))
                .build().inject(this);
        mSplashPresenter.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void toLoginView() {
        final Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        SplashActivity.this.finish();

    }

    @Override
    public void toMainView() {
        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
