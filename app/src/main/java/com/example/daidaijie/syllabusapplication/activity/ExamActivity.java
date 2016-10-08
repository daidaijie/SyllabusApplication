package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.ExamAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.ExamInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.examListRecycleList)
    RecyclerView mExamListRecycleList;

    @BindView(R.id.refreshExamLayout)
    SwipeRefreshLayout mRefreshExamLayout;

    public static final String TAG = "ExamActivity";

    private ExamAdapter mExamAdapter;

    private static final String SAVED_EXAMS = "savedExams";

    private List<Exam> mExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRefreshExamLayout.setOnRefreshListener(this);
        mRefreshExamLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mExamListRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mExamAdapter = new ExamAdapter(this, null);
        mExamListRecycleList.setAdapter(mExamAdapter);

        if (savedInstanceState == null) {
            mRefreshExamLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshExamLayout.setRefreshing(true);
                    getExamList();
                }
            }, 50);
        } else {
            mExams = (List<Exam>) savedInstanceState.getSerializable(SAVED_EXAMS);
        }
        mExamAdapter.setExams(mExams);
        mExamAdapter.notifyDataSetChanged();

        Timer timer = new Timer();
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

    public void getExamList() {

        Semester semester = User.getInstance().getCurrentSemester();
        ExamInfoService examInfoService = RetrofitUtil.getDefault().create(ExamInfoService.class);
        examInfoService.getExamInfo(
                User.getInstance().getAccount(),
                User.getInstance().getPassword(),
                semester.getYearString(),
                semester.getSeason() + ""
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExamInfo>() {
                    @Override
                    public void onCompleted() {
                        mRefreshExamLayout.setRefreshing(false);
                        if (mExams == null) {
                            showFailBannner();
                            return;
                        }
                        mExamAdapter.setExams(mExams);
                        mExamAdapter.notifyDataSetChanged();
                        showSuccessBanner();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshExamLayout.setRefreshing(false);
                        Log.d(TAG, "onError: " + e.getMessage());
                        showFailBannner();
                    }

                    @Override
                    public void onNext(ExamInfo examInfo) {
                        mExams = examInfo.getEXAMS();
                    }
                });
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        getExamList();
    }

    public void showSuccessBanner() {
        SnackbarUtil.ShortSnackbar(
                mExamListRecycleList,
                "更新考试成功",
                SnackbarUtil.Confirm
        ).show();
    }

    public void showFailBannner() {
        SnackbarUtil.LongSnackbar(
                mExamListRecycleList,
                "更新考试失败",
                SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshExamLayout.setRefreshing(true);
                getExamList();
            }
        }).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVED_EXAMS, (Serializable) mExams);
    }
}
