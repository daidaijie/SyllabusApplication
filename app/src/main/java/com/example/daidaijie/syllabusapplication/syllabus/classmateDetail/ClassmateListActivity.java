package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StudentInfoAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class ClassmateListActivity extends BaseActivity implements ClassmateContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.classmateRecyclerView)
    RecyclerView mClassmateRecyclerView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.classmateRootLayout)
    LinearLayout mClassmateRootLayout;
    @BindView(R.id.findNumberTextView)
    TextView mFindNumberTextView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.headerView)
    RecyclerViewHeader mHeaderView;
    SearchView mSearchView;

    StudentInfoAdapter mStudentInfoAdapter;

    @Inject
    ClassmatePresenter mClassmatePresenter;

    private static final String EXTRA_LESSON_ID
            = ClassmateListActivity.class.getCanonicalName() + ".LessonID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mStudentInfoAdapter = new StudentInfoAdapter(this, null);
        mClassmateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mClassmateRecyclerView.setAdapter(mStudentInfoAdapter);
        mHeaderView.attachTo(mClassmateRecyclerView);

        setupTitleBar(mToolbar);
        mTitleTextView.setText("查看同学");

        DaggerClassmateComponent.builder()
                .syllabusComponent(SyllabusComponent.getINSTANCE())
                .classmateModule(new ClassmateModule(this, getIntent().getLongExtra(EXTRA_LESSON_ID, 0)))
                .build().inject(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mClassmatePresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_classmate_list;
    }

    public static Intent getIntent(Context packageContext, long lessonID) {
        Intent intent = new Intent(packageContext, ClassmateListActivity.class);
        intent.putExtra(EXTRA_LESSON_ID, lessonID);
        return intent;
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
//                queryClassmateList(newText);
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
//                mFindNumberTextView.setVisibility(View.GONE);
//                mStudentInfoAdapter.setStudentInfos(mStudentInfos);
//                mStudentInfoAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void queryClassmateList(final String quertText) {
/*
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
*/
    }


    @Override
    public void setTitleBg(int color) {
        mToolbar.setBackgroundColor(getResources().getColor(color));
    }

    @Override
    public void showData(List<StudentInfo> mStudentInfos) {
        mFindNumberTextView.setText("共查找到" + mStudentInfos.size() + "位同学");
        mStudentInfoAdapter.setStudentInfos(mStudentInfos);
        mStudentInfoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        mFindNumberTextView.setText("共查找到" + 0 + "位同学");
        SnackbarUtil.ShortSnackbar(mClassmateRecyclerView, msg, SnackbarUtil.Alert).show();
    }

    @Override
    public void onRefresh() {
        mClassmatePresenter.loadData();
    }
}