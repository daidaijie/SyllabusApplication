package com.example.daidaijie.syllabusapplication.login.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.MainActivity;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.login.login.LoginActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashContract.view {

    private static final long SHOW_TIME_MIN = 1500;// 最小显示时间

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
        long nowTime = System.currentTimeMillis();
        final Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        if (nowTime - mStartTime >= SHOW_TIME_MIN) {
            startActivity(intent);
            SplashActivity.this.finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, nowTime - mStartTime);
        }

    }

    @Override
    public void toMainView() {
        UserComponent.buildInstance(mAppComponent);
        long nowTime = System.currentTimeMillis();
        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (nowTime - mStartTime >= SHOW_TIME_MIN) {
            startActivity(intent);
            SplashActivity.this.finish();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, nowTime - mStartTime);
        }
    }
}
