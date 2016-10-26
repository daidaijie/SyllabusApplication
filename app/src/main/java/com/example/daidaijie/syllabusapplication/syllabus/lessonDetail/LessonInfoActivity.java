package com.example.daidaijie.syllabusapplication.syllabus.lessonDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;
import com.example.daidaijie.syllabusapplication.syllabus.classmateDetail.ClassmateListActivity;
import com.example.daidaijie.syllabusapplication.widget.LessonDetailLayout;

import javax.inject.Inject;

import butterknife.BindView;


public class LessonInfoActivity extends BaseActivity implements LessonInfoContract.view {

    @BindView(R.id.lessonNumberLayout)
    LessonDetailLayout mLessonNumberLayout;
    @BindView(R.id.lessonRoomLayout)
    LessonDetailLayout mLessonRoomLayout;
    @BindView(R.id.lessonTeacherLayout)
    LessonDetailLayout mLessonTeacherLayout;
    @BindView(R.id.lessonTimeLayout)
    LessonDetailLayout mLessonTimeLayout;
    @BindView(R.id.detailContentLayout)
    LinearLayout mDetailContentLayout;
    @BindView(R.id.lessonDetailRootLayout)
    CoordinatorLayout mLessonDetailRootLayout;
    @BindView(R.id.contentScrollView)
    NestedScrollView mContentScrollView;
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
    @BindView(R.id.lessonCreditLayout)
    LessonDetailLayout mLessonCreditLayout;

    private static final String EXTRA_LESSON_ID
            = LessonInfoActivity.class.getCanonicalName() + ".LessonID";

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    @Inject
    LessonInfoPresenter mLessonInfoPresenter;

    private CollapsingToolbarLayoutState state;

    long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        mID = getIntent().getLongExtra(EXTRA_LESSON_ID, 0);


        DaggerLessonInfoComponent.builder()
                .syllabusComponent(SyllabusComponent.getINSTANCE())
                .lessonInfoModule(new LessonInfoModule(mID, this))
                .build().inject(this);
        mLessonInfoPresenter.start();

        mShowClassMateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ClassmateListActivity.getIntent(LessonInfoActivity.this, mID);
                startActivity(intent);
            }
        });

        setResult(RESULT_OK);
    }

    @Override
    public void showData(Lesson lesson) {
        GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.bg_show_classmate);
        shape.setColor(getResources().getColor(lesson.getBgColor()));
        mShowClassMateButton.setBackgroundDrawable(shape);

        mAppBar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbarLayout.setContentScrimColor(getResources().getColor(
                lesson.getBgColor()));
        mToolbar.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));

        mLessonNameTextView.setText(lesson.getName());
        mTitleTextView.setText(lesson.getTrueName());
        mLessonNumberLayout.setTitleText(lesson.getId());
        mLessonCreditLayout.setTitleText(lesson.getCredit());
        mLessonRoomLayout.setTitleText(lesson.getRoom());
        mLessonTeacherLayout.setTitleText(lesson.getTeacher());
        mLessonTimeLayout.setTitleText(lesson.getTimeGridListString("\n"));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lesson_info;
    }

    public static Intent getIntent(Context packageContext, long id) {
        Intent intent = new Intent(packageContext, LessonInfoActivity.class);
        intent.putExtra(EXTRA_LESSON_ID, id);
        return intent;
    }
}
