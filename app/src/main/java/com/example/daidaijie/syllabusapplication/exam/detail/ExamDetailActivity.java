package com.example.daidaijie.syllabusapplication.exam.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.transition.Explode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.exam.ExamModelComponent;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.widget.CustomMarqueeTextView;
import com.example.daidaijie.syllabusapplication.widget.LessonDetailLayout;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

public class ExamDetailActivity extends BaseActivity implements ExamDetailContract.view {

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
    LessonDetailLayout mLessonNumberLayout;
    @BindView(R.id.lessonTimeLayout)
    LessonDetailLayout mLessonTimeLayout;
    @BindView(R.id.lessonTeacherLayout)
    LessonDetailLayout mLessonTeacherLayout;
    @BindView(R.id.examRoomLayout)
    LessonDetailLayout mExamRoomLayout;
    @BindView(R.id.positionLayout)
    LessonDetailLayout mPositionLayout;
    @BindView(R.id.studentSumLayout)
    LessonDetailLayout mStudentSumLayout;
    @BindView(R.id.detailContentLayout)
    LinearLayout mDetailContentLayout;
    @BindView(R.id.contentScrollView)
    NestedScrollView mContentScrollView;
    @BindView(R.id.examDetailRootLayout)
    CoordinatorLayout mExamDetailRootLayout;
    @BindView(R.id.examTipLayout)
    LessonDetailLayout mExamTipLayout;
    @BindView(R.id.examStateLayout)
    LessonDetailLayout mExamStateLayout;

    @Inject
    ExamDetailPresenter mExamDetailPresenter;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    private Timer mTimer;

    private static String EXTRA_EXAM_POS = ExamDetailActivity.class.getCanonicalName() + ".mPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mExamStateLayout.setTitleTextColor(getResources().getColor(R.color.defaultShowColor));
        mExamStateLayout.setTitleText("已结束");

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

        DaggerExamDetailComponent.builder()
                .examModelComponent(ExamModelComponent.newInstance(mAppComponent))
                .examDetailModule(
                        new ExamDetailModule(this, getIntent().getIntExtra(EXTRA_EXAM_POS, 0)))
                .build().inject(this);


        mExamStateLayout.post(new Runnable() {
            @Override
            public void run() {
                mExamDetailPresenter.start();
                mExamDetailPresenter.setUpstate();
            }
        });

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mExamStateLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mExamDetailPresenter.setUpstate();
                    }
                });
            }
        }, 0, 3000);

    }

    @Override
    public void showData(Exam exam) {
        mExamNameTextView.setText(exam.getTrueName());
        mTitleTextView.setText(exam.getTrueName());
        mLessonNumberLayout.setTitleText(exam.getExam_class_number());
        mLessonTimeLayout.setTitleText(exam.getPrettyTime());
        mExamRoomLayout.setTitleText(exam.getExam_location());
        mLessonTeacherLayout.setTitleText(exam.getExam_main_teacher());
        mPositionLayout.setTitleText(exam.getExam_stu_position());
        mStudentSumLayout.setTitleText(exam.getExam_stu_numbers());

        if (exam.getExam_comment().trim().isEmpty()) {
            mExamTipLayout.setTitleText("无");
            mExamTipLayout.setTitleTextColor(getResources().getColor(R.color.defaultShowColor));
        } else {
            mExamTipLayout.setTitleText(exam.getExam_comment());
            mExamTipLayout.setTitleTextColor(ThemeUtil.getInstance().colorPrimary);
        }

    }

    @Override
    public void showState(SpannableStringBuilder state) {
        mExamStateLayout.setTitleText(state);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam_detail;
    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, ExamDetailActivity.class);
        intent.putExtra(EXTRA_EXAM_POS, position);
        return intent;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}
