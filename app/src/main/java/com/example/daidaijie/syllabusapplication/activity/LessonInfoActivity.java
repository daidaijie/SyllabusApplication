package com.example.daidaijie.syllabusapplication.activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.LessonDetailInfo;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.retrofitApi.LessonDetailService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.widget.LessonDetailLayout;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

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

    private AlertDialog mLoadingDialog;

    public static final String TAG = "LessonInfoActivity";
    public static final String EXTRA_LESSON_ID
            = "example.daidaijie.syllabusapplication.LessonInfoActivity.LessonID";
    private Lesson lesson;

    List<StudentInfo> mStudentInfos;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }

    private CollapsingToolbarLayoutState state;

    private boolean singleLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

        lesson = LessonModel.getInstance().getLesson(getIntent().getLongExtra(EXTRA_LESSON_ID, 0));

        Log.d(TAG, "onCreate: " + lesson.getName());

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

        mLessonNameTextView.setText(lesson.getName());
        mTitleTextView.setText(lesson.getTrueName());
        mLessonNumberLayout.setTitleText(lesson.getId());
        mLessonCreditLayout.setTitleText(lesson.getCredit());
        mLessonRoomLayout.setTitleText(lesson.getRoom());
        mLessonTeacherLayout.setTitleText(lesson.getTeacher());
        mLessonTimeLayout.setTitleText(lesson.getTimeGridListString("\n"));

        mLoadingDialog = LoadingDialogBuiler
                .getLoadingDialog(this, getResources().getColor(lesson.getBgColor()));


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

        mShowClassMateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleLock) return;
                singleLock = true;
                mLoadingDialog.show();
                //要在show之后加，不然没效果
                WindowManager.LayoutParams params = mLoadingDialog.getWindow().getAttributes();
                params.width = deviceWidth * 7 / 8;
                mLoadingDialog.getWindow().setAttributes(params);
                getStudentList();
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
                        mLoadingDialog.dismiss();
                        Intent intent = ClassmateListActivity.getIntent(
                                LessonInfoActivity.this, mStudentInfos, lesson.getBgColor()
                        );

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LessonInfoActivity.this,
                                    mToolbarLayout, "tool_bar");
                            startActivity(intent, options.toBundle());
                        } else {
                            startActivity(intent);
                        }
                        singleLock = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismiss();
                        singleLock = false;
                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {
                        mStudentInfos.add(studentInfo);
                    }
                });
    }

    public static Intent getIntent(Context packageContext, long id) {
        Intent intent = new Intent(packageContext, LessonInfoActivity.class);
        intent.putExtra(EXTRA_LESSON_ID, id);
        return intent;
    }

    @Override
    public void onBackPressed() {
        mAppBar.removeAllViews();
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //让它死吧
        this.finish();
    }
}
