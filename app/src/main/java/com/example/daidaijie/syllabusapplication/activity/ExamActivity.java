package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.ExamAdapter;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.service.ExamInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ExamActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.examListRecycleList)
    RecyclerViewEmptySupport mExamListRecycleList;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
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

        mExamListRecycleList.setEmptyView(mEmptyTextView);
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
        }, 0, 1000);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam;
    }

    public void getExamList() {
        Retrofit retrofit = RetrofitUtil.getDefault();
        ExamInfoService examInfoService = retrofit.create(ExamInfoService.class);
        examInfoService.getExamInfo(
                "13yjli3", "O3o", "2013-2014", "1"
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExamInfo>() {
                    @Override
                    public void onCompleted() {
                        mRefreshExamLayout.setRefreshing(false);
                        mExamAdapter.setExams(mExams);
                        mExamAdapter.notifyDataSetChanged();
                        showSuccessBanner();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshExamLayout.setRefreshing(true);
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
