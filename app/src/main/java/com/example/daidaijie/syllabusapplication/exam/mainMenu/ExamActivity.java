package com.example.daidaijie.syllabusapplication.exam.mainMenu;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.ExamAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.exam.ExamModelComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;

public class ExamActivity extends BaseActivity implements ExamContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.examListRecycleList)
    RecyclerView mExamListRecycleList;
    @BindView(R.id.refreshExamLayout)
    SwipeRefreshLayout mRefreshExamLayout;

    private ExamAdapter mExamAdapter;
    Timer timer;

    @Inject
    ExamPresenter mExamPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRefreshExamLayout.setOnRefreshListener(this);
        setupSwipeRefreshLayout(mRefreshExamLayout);

        mExamListRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mExamAdapter = new ExamAdapter(this, null);
        mExamListRecycleList.setAdapter(mExamAdapter);

        ExamModelComponent.newInstance();
        DaggerExamComponent.builder()
                .examModelComponent(ExamModelComponent.getINSTANCE())
                .examModule(new ExamModule(this))
                .build().inject(this);

        mRefreshExamLayout.post(new Runnable() {
            @Override
            public void run() {
                mExamPresenter.start();
            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mExamListRecycleList.post(new Runnable() {
                    @Override
                    public void run() {
                        mExamAdapter.notifyDataSetChanged();
                    }
                });
            }
        }, 0, 1000 * 30);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam;
    }

    @Override
    public void onRefresh() {
        mExamPresenter.loadData();
    }

    @Override
    public void showData(List<Exam> exams) {
        mExamAdapter.setExams(exams);
        mExamAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mExamListRecycleList,
                msg,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mExamListRecycleList,
                msg,
                SnackbarUtil.Info
        ).show();
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.LongSnackbar(
                mExamListRecycleList, msg, SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExamPresenter.loadData();
            }
        }).show();
    }

    @Override
    public void showLoading(boolean isShow) {
        mRefreshExamLayout.setRefreshing(isShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        ExamModelComponent.destroy();
    }
}
