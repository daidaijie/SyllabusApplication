package com.example.daidaijie.syllabusapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.widget.LessonDetaiLayout;

import butterknife.BindView;


public class LessonInfoActivity extends BaseActivity {

    public static final String TAG = "LessonInfoActivity";
    @BindView(R.id.lessonNameLayout)
    LessonDetaiLayout mLessonNameLayout;
    @BindView(R.id.lessonNumberLayout)
    LessonDetaiLayout mLessonNumberLayout;
    @BindView(R.id.lessonRoomLayout)
    LessonDetaiLayout mLessonRoomLayout;
    @BindView(R.id.lessonTeacherLayout)
    LessonDetaiLayout mLessonTeacherLayout;
    @BindView(R.id.lessonTimeLayout)
    LessonDetaiLayout mLessonTimeLayout;
    @BindView(R.id.detailContentLayout)
    LinearLayout mDetailContentLayout;
    @BindView(R.id.lessonDetailRootLayout)
    CoordinatorLayout mLessonDetailRootLayout;
    @BindView(R.id.contentScrollView)
    NestedScrollView mContentScrollView;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;

    private Lesson lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lesson = (Lesson) getIntent().getSerializableExtra("LESSON");

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        this.setResult(201, null);

        mLessonDetailRootLayout.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        mAppBar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbarLayout.setContentScrimColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));

        mLessonNameLayout.setTitleText(lesson.getTrueName());
        mLessonNumberLayout.setTitleText(lesson.getId());
        mLessonRoomLayout.setTitleText(lesson.getRoom());
        mLessonTeacherLayout.setTitleText(lesson.getTeacher());
        mLessonTimeLayout.setTitleText(lesson.getTimeGridListString("\n"));

        mFab.setScaleX(0.0f);
        mFab.setScaleY(0.0f);
        mContentScrollView.setTranslationY(1920.0f);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(
                mFab, "scaleX", 0.0f, 1.0f
        );
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(
                mFab, "scaleY", 0.0f, 1.0f
        );

        ObjectAnimator animatorH = ObjectAnimator.ofFloat(
                mContentScrollView, "translationY", 1920.0f, 0.0f
        );
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY).after(animatorH);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.setDuration(618);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        },200);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lesson_info;
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
