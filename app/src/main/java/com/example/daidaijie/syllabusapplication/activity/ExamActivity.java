package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import butterknife.BindView;

public class ExamActivity extends BaseActivity {


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam;
    }


}
