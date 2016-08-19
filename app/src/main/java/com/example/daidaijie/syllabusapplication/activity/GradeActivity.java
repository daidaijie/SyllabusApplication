package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.GradeInfo;
import com.example.daidaijie.syllabusapplication.service.GradeService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GradeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "GradeActivity";

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.gradeListRecycleList)
    RecyclerViewEmptySupport mGradeListRecycleList;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshGradeLayout)
    SwipeRefreshLayout mRefreshGradeLayout;

    private GradeInfo mGradeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRefreshGradeLayout.setOnRefreshListener(this);
        mRefreshGradeLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshGradeLayout.setRefreshing(true);
                getGrade();
            }
        }, 50);
    }

    private void getGrade() {
        GradeService gradeService = RetrofitUtil.getDefault().create(GradeService.class);
        gradeService.getGrade("13yjli3", "O3o")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GradeInfo>() {
                    @Override
                    public void onCompleted() {
                        showSuccessBanner();
                        Log.d(TAG, "onCompleted: " + mGradeInfo.getGPA());
                        for (List<GradeInfo.GradeBean> gradeBeanList : mGradeInfo.getGRADES()) {
                            for (GradeInfo.GradeBean gradeBean : gradeBeanList) {
                                Log.d(TAG, "onCompleted: " + gradeBean.getClass_name());
                            }
                            Log.d(TAG, "onCompleted: ");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailBannner();
                    }

                    @Override
                    public void onNext(GradeInfo gradeInfo) {
                        mGradeInfo = gradeInfo;
                    }
                });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_grade;
    }

    @Override
    public void onRefresh() {
        getGrade();
    }

    public void showSuccessBanner() {
        SnackbarUtil.ShortSnackbar(
                mGradeListRecycleList,
                "获取成绩成功",
                SnackbarUtil.Confirm
        ).show();
    }

    public void showFailBannner() {
        SnackbarUtil.LongSnackbar(
                mGradeListRecycleList,
                "获取成绩失败",
                SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRefreshGradeLayout.setRefreshing(true);
                getGrade();
            }
        }).show();
    }
}
