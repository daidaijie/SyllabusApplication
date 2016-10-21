package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.TakeOutMenuAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;
import com.example.daidaijie.syllabusapplication.takeout.detailMenu.TakeOutDetailMenuActivity;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TakeOutActivity extends BaseActivity implements TakeOutContract.view, SwipeRefreshLayout.OnRefreshListener, TakeOutMenuAdapter.OnItemClickListener {

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


    private static final String CLASS_NAME = TakeOutActivity.class.getCanonicalName();

    private static final String RESULT_POSITION = CLASS_NAME + ".resultPosition";

    private final int REQUEST_DETAIL_MENU = 206;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        DaggerTakeOutComponent.builder()
                .takeOutModelComponent(TakeOutModelComponent.getInstance(mAppComponent))
                .takeOutModule(new TakeOutModule(this))
                .build().inject(this);

        mMenuListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTakeOutMenuAdapter = new TakeOutMenuAdapter(this, null);
        mMenuListRecyclerView.setAdapter(mTakeOutMenuAdapter);
        mTakeOutMenuAdapter.setOnItemClickListener(this);

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
        mTakeOutPresenter.loadDataFromNet();
    }

    public static Intent getIntent(int position) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_POSITION, position);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DETAIL_MENU & resultCode == RESULT_OK) {
            mTakeOutMenuAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mMenuListRecyclerView, msg, SnackbarUtil.Alert)
                .show();
    }

    @Override
    public void showData(List<TakeOutInfoBean> takeOutInfoBeen) {
        mTakeOutMenuAdapter.setTakeOutInfoBeen(takeOutInfoBeen);
        mTakeOutMenuAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void onItemClick(String objectID) {
        Intent intent = TakeOutDetailMenuActivity.getIntent(this, objectID);
        startActivityForResult(intent, REQUEST_DETAIL_MENU);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TakeOutModelComponent.destroy();
    }
}
