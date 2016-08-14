package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.widget.CustomMarqueeTextView;
import com.example.daidaijie.syllabusapplication.widget.LessonDetaiLayout;

import butterknife.BindView;

public class ExamDetailActivity extends BaseActivity {


    public static final String EXTRA_EXAM = "com.example.daidaijie.syllabusapplication.activity" +
            ".ExamDetailActivity.mExam";
    Exam mExam;
    @BindView(R.id.examNameTextView)
    TextView mExamNameTextView;
    @BindView(R.id.titleTextView)
    CustomMarqueeTextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.lessonNumberLayout)
    LessonDetaiLayout mLessonNumberLayout;
    @BindView(R.id.lessonTimeLayout)
    LessonDetaiLayout mLessonTimeLayout;
    @BindView(R.id.lessonTeacherLayout)
    LessonDetaiLayout mLessonTeacherLayout;
    @BindView(R.id.examRoomLayout)
    LessonDetaiLayout mExamRoomLayout;
    @BindView(R.id.positionLayout)
    LessonDetaiLayout mPositionLayout;
    @BindView(R.id.studentSumLayout)
    LessonDetaiLayout mStudentSumLayout;
    @BindView(R.id.detailContentLayout)
    LinearLayout mDetailContentLayout;
    @BindView(R.id.contentScrollView)
    NestedScrollView mContentScrollView;
    @BindView(R.id.examDetailRootLayout)
    CoordinatorLayout mExamDetailRootLayout;


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

        mExam = (Exam) getIntent().getSerializableExtra(EXTRA_EXAM);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAppBar.setBackgroundColor(getResources().getColor(
                R.color.colorPrimary));
        mToolbarLayout.setContentScrimColor(getResources().getColor(
                R.color.colorPrimary));
        mToolbar.setBackgroundColor(getResources().getColor(
                R.color.colorPrimary));

        mExamNameTextView.setText(mExam.getTrueName());
        mTitleTextView.setText(mExam.getTrueName());
        mLessonNumberLayout.setTitleText(mExam.getExam_class_number());
        mLessonTimeLayout.setTitleText(mExam.getPrettyTime());
        mExamRoomLayout.setTitleText(mExam.getExam_location());
        mLessonTeacherLayout.setTitleText(mExam.getExam_main_teacher());
        mPositionLayout.setTitleText(mExam.getExam_stu_position());
        mStudentSumLayout.setTitleText(mExam.getExam_stu_numbers());



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
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam_detail;
    }

    public static Intent getIntent(Context context, Exam exam) {
        Intent intent = new Intent(context, ExamDetailActivity.class);
        intent.putExtra(EXTRA_EXAM, exam);
        return intent;
    }
}
