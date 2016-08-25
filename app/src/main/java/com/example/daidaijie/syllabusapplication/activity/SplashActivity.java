package com.example.daidaijie.syllabusapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.presenter.LoginPresenter;
import com.example.daidaijie.syllabusapplication.view.ILoginView;
import com.squareup.haha.perflib.Main;

public class SplashActivity extends BaseActivity implements ILoginView {

    private static final long SHOW_TIME_MIN = 1500;// 最小显示时间

    private long mStartTime;// 开始时间

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mStartTime = System.currentTimeMillis();
        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attach(this);

        mLoginPresenter.login(
                User.getInstance().getAccount(),
                User.getInstance().getPassword()
        );


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detach();
    }

    @Override
    public void showLoginSuccess() {
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

    @Override
    public void showLoginFail() {
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
    public void showLoadingDialog() {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
