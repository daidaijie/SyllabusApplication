package com.example.daidaijie.syllabusapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.daidaijie.syllabusapplication.R;

public class LessonInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        this.setResult(201,null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lesson_info;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
