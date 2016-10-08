package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.GradeListAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.GradeInfo;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.GradeService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

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
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;
    @BindView(R.id.sumNumTextView)
    TextView mSumNumTextView;
    @BindView(R.id.sumCreditTextView)
    TextView mSumCreditTextView;
    @BindView(R.id.sumGpaTextView)
    TextView mSumGpaTextView;

    private GradeInfo mGradeInfo;

    private GradeListAdapter mGradeListAdapter;

    private MenuItem mExpandMenuItem;

    private boolean isAllExpand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGradeListRecycleList.setEmptyView(mEmptyTextView);

        isAllExpand = false;

        mGradeInfo = null;
        mGradeListAdapter = new GradeListAdapter(this, mGradeInfo);
        mGradeListRecycleList.setLayoutManager(new LinearLayoutManager(this));
        mGradeListRecycleList.setAdapter(mGradeListAdapter);

        mHeader.attachTo(mGradeListRecycleList);
        setHeader();

        mRefreshGradeLayout.setOnRefreshListener(this);
        mRefreshGradeLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

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
        gradeService.getGrade(User.getInstance().getAccount(), User.getInstance().getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GradeInfo>() {
                    @Override
                    public void onCompleted() {
                        showSuccessBanner();
                        mRefreshGradeLayout.setRefreshing(false);
                        mGradeInfo.trimList();
                        setExpandMenu(isAllExpand);
                        mGradeInfo.setIsExpands(isAllExpand);
                        mGradeListAdapter.setGradeInfo(mGradeInfo);
                        mGradeListAdapter.notifyDataSetChanged();
                        setHeader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showFailBannner();
                        mRefreshGradeLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(GradeInfo gradeInfo) {
                        mGradeInfo = gradeInfo;
                    }
                });
    }

    private void setHeader() {
        if (mGradeInfo != null) {
            mSumCreditTextView.setText(String.format("%.1f", mGradeInfo.getAllCredit()));
            mSumNumTextView.setText(mGradeInfo.getAllSize() + "");
            mSumGpaTextView.setText(String.format("%.2f", mGradeInfo.getAllGpa()));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grade, menu);
        mExpandMenuItem = menu.findItem(R.id.action_expand);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mGradeInfo == null) {
            return true;
        }

        isAllExpand = !isAllExpand;
        setExpandMenu(isAllExpand);
        mGradeInfo.setIsExpands(isAllExpand);
        mGradeListAdapter.notifyDataSetChanged();

        return super.onOptionsItemSelected(item);
    }

    private void setExpandMenu(boolean flag) {
        mExpandMenuItem.setTitle(!flag ? "展开" : "收起");
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
