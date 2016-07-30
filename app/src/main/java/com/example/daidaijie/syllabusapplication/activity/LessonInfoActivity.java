package com.example.daidaijie.syllabusapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.lessonNameTextView)
    TextView mLessonNameTextView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.showClassMateButton)
    Button mShowClassMateButton;

    private Lesson lesson;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lesson = (Lesson) getIntent().getSerializableExtra("LESSON");

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setResult(201, null);

        GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.bg_show_classmate);
        shape.setColor(getResources().getColor(lesson.getBgColor()));
        mShowClassMateButton.setBackgroundDrawable(shape);

        mLessonDetailRootLayout.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        mAppBar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbarLayout.setContentScrimColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));

//        mToolbar.setTitle(lesson.getTrueName());
        mLessonNameTextView.setText(lesson.getName());
        mTitleTextView.setText(lesson.getTrueName());
        mLessonNameLayout.setTitleText(lesson.getTrueName());
        mLessonNumberLayout.setTitleText(lesson.getId());
        mLessonRoomLayout.setTitleText(lesson.getRoom());
        mLessonTeacherLayout.setTitleText(lesson.getTeacher());
        mLessonTimeLayout.setTitleText(lesson.getTimeGridListString("\n"));

        mTitleTextView.setVisibility(View.GONE);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        mTitleTextView.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        mTitleTextView.setVisibility(View.VISIBLE);
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        mTitleTextView.setVisibility(View.GONE);
//                        if (state == CollapsingToolbarLayoutState.COLLAPSED) {}
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

        mFab.setScaleX(0.0f);
        mFab.setScaleY(0.0f);
        mContentScrollView.setTranslationY(1920.0f);
        mDetailContentLayout.setAlpha(0.0f);
        mShowClassMateButton.setAlpha(0.0f);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(
                mFab, "scaleX", 0.0f, 1.0f
        );
        animatorX.setDuration(200);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(
                mFab, "scaleY", 0.0f, 1.0f
        );
        animatorY.setDuration(200);
        ObjectAnimator animatorH = ObjectAnimator.ofFloat(
                mContentScrollView, "translationY", devideHeight, 0.0f
        );
        animatorH.setDuration(300);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(
                mDetailContentLayout, "alpha", 0.0f, 1.0f
        );
        animatorA.setDuration(300);
        ObjectAnimator animatorX2 = ObjectAnimator.ofFloat(
                mShowClassMateButton,"alpha",0.0f,1.0f
        );
        animatorX2.setDuration(300);
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY).with(animatorX2).after(animatorH).with(animatorA);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        }, 400);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
