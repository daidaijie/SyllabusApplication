package com.example.daidaijie.syllabusapplication.login.splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.login.login.LoginActivity;
import com.example.daidaijie.syllabusapplication.main.MainActivity;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity implements SplashContract.view {

    private long mStartTime;// 开始时间

    @Inject
    SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash);

        mStartTime = System.currentTimeMillis();

        DaggerSplashComponent.builder()
                .appComponent(((App) getApplication()).getAppComponent())
                .splashModule(new SplashModule(this))
                .build().inject(this);
        mSplashPresenter.start();
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        SplashActivity.this.finish();
    }
}
