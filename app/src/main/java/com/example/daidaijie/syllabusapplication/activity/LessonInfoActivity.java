package com.example.daidaijie.syllabusapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.LessonDetailInfo;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.service.LessonDetailService;
import com.example.daidaijie.syllabusapplication.util.CircularAnimUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.widget.LessonDetaiLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class LessonInfoActivity extends BaseActivity {

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
    /*@BindView(R.id.fab)
    FloatingActionButton mFab;*/
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
    LessonDetaiLayout mLessonCreditLayout;

    public static final String TAG = "LessonInfoActivity";
    public static final String EXTRA_LESSON_INFO
            = "example.daidaijie.syllabusapplication.LessonInfoActivity.LessonInfo";
    private Lesson lesson;

    List<StudentInfo> mStudentInfos;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*mLessonDetailRootLayout.setBackgroundColor(getResources().getColor(
                lesson.getBgColor()));*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

//        ActivityTransition.with(getIntent()).to(mAppBar).start(savedInstanceState);
        lesson = (Lesson) getIntent().getSerializableExtra(EXTRA_LESSON_INFO);

        Log.d(TAG, "onCreate: "+lesson.getName());

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setResult(201, null);

        GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.bg_show_classmate);
        shape.setColor(getResources().getColor(lesson.getBgColor()));
        mShowClassMateButton.setBackgroundDrawable(shape);

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
        mLessonCreditLayout.setTitleText(lesson.getCredit());
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

        /*ObjectAnimator animatorX = ObjectAnimator.ofFloat(
                mFab, "scaleX", 0.0f, 1.0f
        );
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(
                mFab, "scaleY", 0.0f, 1.0f
        );
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(300);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.setScaleX(0.0f);
                mFab.setScaleY(0.0f);
                mFab.setVisibility(View.VISIBLE);
                animatorSet.start();
            }
        },400);*/


        mShowClassMateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentList();
               /* ObjectAnimator animator = ObjectAnimator.ofFloat(
                        mShowClassMateButton, "scaleX", 1.0f, 0.0f
                );
                ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.YELLOW), Gravity.LEFT, ClipDrawable.HORIZONTAL);
                mProgressBar.getIndeterminateDrawable().setColorFilter(
                        getResources().getColor(lesson.getBgColor()),
                        PorterDuff.Mode.SRC_IN);
                mProgressBar.setVisibility(View.VISIBLE);
                animator.setDuration(618);
                animator.setInterpolator(new AccelerateInterpolator());
                animator.start();*/
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lesson_info;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            this.onBackPressed();
//            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getStudentList() {
        Retrofit retrofit = RetrofitUtil.getDefault();
        LessonDetailService lessonDetailService = retrofit.create(LessonDetailService.class);
        lessonDetailService.getLessonDetail(lesson.getId())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<LessonDetailInfo, Observable<StudentInfo>>() {
                    @Override
                    public Observable<StudentInfo> call(LessonDetailInfo lessonDetailInfo) {
                        return Observable.from(lessonDetailInfo.getClass_info().getStudent());
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentInfo>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        mStudentInfos = new ArrayList<>();
                    }

                    @Override
                    public void onCompleted() {
                        Intent intent = ClassmateListActivity.getIntent(
                                LessonInfoActivity.this, mStudentInfos, lesson.getBgColor()
                        );

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LessonInfoActivity.this,
                                    mToolbarLayout, "tool_bar");
                            startActivity(intent,options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {
                        mStudentInfos.add(studentInfo);
                    }
                });
    }

    public static Intent getIntent(Context packageContext, Lesson lesson) {
        Intent intent = new Intent(packageContext, LessonInfoActivity.class);
        intent.putExtra(EXTRA_LESSON_INFO, lesson);
        return intent;
    }

    @Override
    public void onBackPressed() {
//        mFab.setVisibility(View.GONE);
        mAppBar.removeAllViews();
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.finish();
    }
}
