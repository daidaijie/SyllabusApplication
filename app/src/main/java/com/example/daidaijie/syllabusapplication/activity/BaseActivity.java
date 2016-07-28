package com.example.daidaijie.syllabusapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.daidaijie.syllabusapplication.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/7/25.
 */
public abstract class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(getContentView());

        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    protected abstract int getContentView();
}
