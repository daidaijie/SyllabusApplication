package com.example.daidaijie.syllabusapplication.activity;

import android.animation.Animator;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.util.CircularAnimUtil;

public class LessonInfoActivity extends BaseActivity {

    public static final String TAG = "LessonInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Lesson lesson = (Lesson) getIntent().getSerializableExtra("LESSON");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setContentScrimColor(getResources().getColor(
                lesson.getBgColor()));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        setSupportActionBar(toolbar);
        this.setResult(201, null);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            fab.post(new Runnable() {
                @Override
                public void run() {
                    Animator animator = ViewAnimationUtils.createCircularReveal(
                            fab,
                            fab.getWidth() / 2,
                            fab.getHeight() / 2,
                            0,
                            Math.max(fab.getHeight(), fab.getWidth()));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(500);
                    animator.start();

                }
            });
        }
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
