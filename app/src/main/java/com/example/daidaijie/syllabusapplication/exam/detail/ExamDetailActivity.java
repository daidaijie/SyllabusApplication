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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.transition.Explode;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.widget.CustomMarqueeTextView;
import com.example.daidaijie.syllabusapplication.widget.LessonDetailLayout;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;

import java.util.Timer;
import java.util.TimerTask;

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


    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    private DateTime mExamTime;

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


       /* mAppBar.setBackgroundColor(getResources().getColor(
                R.color.colorPrimary));
        mToolbarLayout.setContentScrimColor(getResources().getColor(
                R.color.colorPrimary));
        mToolbar.setBackgroundColor(getResources().getColor(
                R.color.colorPrimary));*/

        mExamNameTextView.setText(mExam.getTrueName());
        mTitleTextView.setText(mExam.getTrueName());
        mLessonNumberLayout.setTitleText(mExam.getExam_class_number());
        mLessonTimeLayout.setTitleText(mExam.getPrettyTime());
        mExamRoomLayout.setTitleText(mExam.getExam_location());
        mLessonTeacherLayout.setTitleText(mExam.getExam_main_teacher());
        mPositionLayout.setTitleText(mExam.getExam_stu_position());
        mStudentSumLayout.setTitleText(mExam.getExam_stu_numbers());

        if (mExam.getExam_comment().trim().isEmpty()) {
            mExamTipLayout.setTitleText("无");
            mExamTipLayout.setTitleTextColor(getResources().getColor(R.color.defaultShowColor));
        } else {
            mExamTipLayout.setTitleText(mExam.getExam_comment());
            mExamTipLayout.setTitleTextColor(ThemeModel.getInstance().colorPrimary);
        }

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

        mExamTime = mExam.getExamTime();
        setupState();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mExamStateLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        setupState();
                    }
                });
            }
        }, 0, 60 * 1000);

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

    private void setupState() {
        DateTime now = DateTime.now();

        if (DateTimeComparator.getInstance().compare(mExamTime, now) > 0) {
            Period period = new Period(now, mExamTime);
            StringBuilder sb = new StringBuilder("离开考还有\n");
            int days = period.getWeeks() * 7 + period.getDays();
            if (period.getYears() != 0) {
                sb.append(period.getYears() + "年");
                sb.append(period.getMonths() + "月");
                sb.append(days + "天");
            } else {
                if (period.getMonths() != 0) {
                    sb.append(period.getMonths() + "月");
                    sb.append(days + "天");
                } else {
                    if (days != 0) {
                        sb.append(days + "天");
                    }
                }
            }
            sb.append(String.format("%d小时%d分钟",
                    period.getHours(), period.getMinutes()));

            SpannableStringBuilder style = new SpannableStringBuilder(sb);
            style.setSpan(new ForegroundColorSpan(
                            getResources().getColor(R.color.defaultTextColor)),
                    0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            style.setSpan(new ForegroundColorSpan(
                            ThemeModel.getInstance().colorPrimary),
                    6, sb.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            mExamStateLayout.setTitleText(style);
        } else {
            mExamStateLayout.setTitleTextColor(
                    getResources().getColor(R.color.defaultShowColor));
            mExamStateLayout.setTitleText("已结束");
        }
    }
}
