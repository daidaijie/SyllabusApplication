package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StudentInfoAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.util.StringUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ClassmateListActivity extends BaseActivity {

    public static final String EXTRA_STUDENT_LIST =
            "com.example.daidaijie.syllabusapplication.student_list";

    public static final String EXTRA_BG_COLOR =
            "com.example.daidaijie.syllabusapplication.bg_color";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.classmateRecyclerView)
    RecyclerViewEmptySupport mClassmateRecyclerView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.classmateRootLayout)
    LinearLayout mClassmateRootLayout;
    SearchView mSearchView;
    @BindView(R.id.tv_empty_view)
    TextView mTvEmptyView;
    @BindView(R.id.findNumberTextView)
    TextView mFindNumberTextView;

    private List<StudentInfo> mStudentInfos;
    private StudentInfoAdapter mStudentInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

        mStudentInfos = (List<StudentInfo>) getIntent().getSerializableExtra(EXTRA_STUDENT_LIST);
        mStudentInfoAdapter = new StudentInfoAdapter(this, mStudentInfos);
        mClassmateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mClassmateRecyclerView.setEmptyView(mTvEmptyView);
        mClassmateRecyclerView.setAdapter(mStudentInfoAdapter);

        int bgColor = getResources()
                .getColor(getIntent().getIntExtra(EXTRA_BG_COLOR, ThemeUtil.getInstance().colorPrimary));
        mToolbar.setBackgroundColor(bgColor);
        mToolbar.setTitle("");

        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitleTextView.setText("查看同学(" + mStudentInfos.size() + ")");
        mFindNumberTextView.setVisibility(View.GONE);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_classmate_list;
    }

    public static Intent getIntent(Context packageContext, List<StudentInfo> studentInfos, int bgColor) {
        Intent intent = new Intent(packageContext, ClassmateListActivity.class);
        intent.putExtra(EXTRA_STUDENT_LIST, (Serializable) studentInfos);
        intent.putExtra(EXTRA_BG_COLOR, bgColor);
        return intent;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_classmate_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setQueryHint("搜索姓名\\学号\\专业\\性别");
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        textView.setHintTextColor(getResources().getColor(R.color.material_grey_400));
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryClassmateList(newText);
                return true;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {//设置打开关闭动作监听
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mFindNumberTextView.setVisibility(View.GONE);
                mStudentInfoAdapter.setStudentInfos(mStudentInfos);
                mStudentInfoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void queryClassmateList(final String quertText) {
        Observable.from(mStudentInfos)
                .subscribeOn(Schedulers.computation())
                .filter(new Func1<StudentInfo, Boolean>() {
                    @Override
                    public Boolean call(StudentInfo studentInfo) {
                        if (quertText.length() == 0) {
                            return true;
                        }
                        //判断为学号
                        if (StringUtil.isNumberic(quertText)) {
                            if (studentInfo.getNumber().contains(quertText)) {
                                return true;
                            }
                        }
                        //判断性别
                        if (quertText.length() == 1) {
                            if (studentInfo.getGender().equals(quertText)) {
                                return true;
                            }
                        }
                        if (studentInfo.getName().contains(quertText) ||
                                studentInfo.getMajor().contains(quertText)) {
                            return true;
                        }

                        return false;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StudentInfo>() {

                    List<StudentInfo> mQueryInfo;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mQueryInfo = new ArrayList<StudentInfo>();
                    }

                    @Override
                    public void onCompleted() {
                        mStudentInfoAdapter.setStudentInfos(mQueryInfo);
                        mStudentInfoAdapter.notifyDataSetChanged();
                        mFindNumberTextView.setVisibility(View.VISIBLE);
                        mFindNumberTextView.setText("查找到" + mQueryInfo.size() + "位同学");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(StudentInfo studentInfo) {
                        mQueryInfo.add(studentInfo);
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*public void getStudentList() {
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
    }*/
}