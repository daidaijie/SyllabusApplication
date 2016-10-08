package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.TakeOutMenuAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TakeOutActivity extends BaseActivity implements TakeOutContract.TakeOutView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.menuListRecyclerView)
    RecyclerView mMenuListRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    TakeOutMenuAdapter mTakeOutMenuAdapter;

    /**
     * 必须使用实体类,否则Dagger2无法找到构造函数
     */
    @Inject
    @PerActivity
    TakeOutPresenter mTakeOutPresenter;

    private static final String RESULT_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".TakeOutActivity.resultPosition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerTakeOutComponent.builder()
                .appComponent(mAppComponent)
                .takeOutModule(new TakeOutModule(this))
                .build().inject(this);


        mTakeOutMenuAdapter = new TakeOutMenuAdapter(this, null);
        mMenuListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMenuListRecyclerView.setAdapter(mTakeOutMenuAdapter);


        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mTakeOutPresenter.start();
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out;
    }

    @Override
    public void onRefresh() {
        mTakeOutPresenter.loadData();
    }

    public static Intent getIntent(int position) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_POSITION, position);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 206 & resultCode == RESULT_OK) {
            int resultPos = data.getIntExtra(RESULT_POSITION, -1);
            if (resultPos != -1) {
                mTakeOutMenuAdapter.notifyItemChanged(resultPos);
            } else {
                mTakeOutMenuAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mMenuListRecyclerView, msg, SnackbarUtil.Alert)
                .show();
    }

    @Override
    public void showData(List<TakeOutInfoBean> mTakeOutInfoBeen) {
        mTakeOutMenuAdapter.setTakeOutInfoBeen(mTakeOutInfoBeen);
        mTakeOutMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }
}
