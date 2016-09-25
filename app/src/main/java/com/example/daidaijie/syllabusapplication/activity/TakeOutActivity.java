package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.TakeOutMenuAdapter;
import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.model.BmobModel;
import com.example.daidaijie.syllabusapplication.service.TakeOutInfoService;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TakeOutActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.menuListRecyclerView)
    RecyclerView mMenuListRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    TakeOutMenuAdapter mTakeOutMenuAdapter;

    List<TakeOutInfoBean> mTakeOutInfoBeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTakeOutMenuAdapter = new TakeOutMenuAdapter(this, mTakeOutInfoBeen);
        mMenuListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMenuListRecyclerView.setAdapter(mTakeOutMenuAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                getTakeOutInfo();
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out;
    }

    private void getTakeOutInfo() {
        TakeOutInfoService service = BmobModel.getInstance().mRetrofit.create(TakeOutInfoService.class);
        service.getTokenResult("name,long_number,short_number,condition")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BmobResult<TakeOutInfoBean>>() {
                    @Override
                    public void onCompleted() {
                        mTakeOutMenuAdapter.setTakeOutInfoBeen(mTakeOutInfoBeen);
                        mTakeOutMenuAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(TakeOutActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        e.printStackTrace();
                        Logger.t("takeout").e(e.getMessage());
                        Toast.makeText(TakeOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BmobResult<TakeOutInfoBean> bmobResult) {
                        mTakeOutInfoBeen = bmobResult.getResults();
                    }
                });
    }

    @Override
    public void onRefresh() {
        getTakeOutInfo();
    }
}
