package com.example.daidaijie.syllabusapplication.other.update;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import javax.inject.Inject;

import butterknife.BindView;
import info.hoang8f.widget.FButton;

public class UpdateActivity extends BaseActivity implements UpdateContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nowVersionTextView)
    TextView mNowVersionTextView;
    @BindView(R.id.pusherTextView)
    TextView mPusherTextView;
    @BindView(R.id.updateInfoTextView)
    TextView mUpdateInfoTextView;
    @BindView(R.id.copyUpdateButton)
    FButton mCopyUpdateButton;
    @BindView(R.id.updateButton)
    FButton mUpdateButton;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    UpdatePresenter mUpdatePresenter;
    @BindView(R.id.newVersionTextView)
    TextView mNewVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mNowVersionTextView.setText(App.versionName);


        mPusherTextView.setText("13计算机李宇杰");

        mCopyUpdateButton.setVisibility(View.GONE);
        mUpdateButton.setVisibility(View.GONE);

        DaggerUpdateComponent.builder()
                .appComponent(mAppComponent)
                .updateModule(new UpdateModule(this))
                .build().inject(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mUpdatePresenter.start();
            }
        });
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_update;
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mTitleTextView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mTitleTextView, msg, SnackbarUtil.Info
        ).show();
    }

    @Override
    public void showLoading(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void showInfo(final UpdateInfoBean updateInfoBean) {
        int newVersionCode = Integer.parseInt(updateInfoBean.getVersionCode());
        if (newVersionCode > App.versionCode) {
            mUpdateButton.setVisibility(View.VISIBLE);
        }

        mCopyUpdateButton.setVisibility(View.VISIBLE);
        mCopyUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.copyToClipboard(updateInfoBean.getDownload_address());
                showInfoMessage("已经复制到粘贴板");
            }
        });
        mUpdateInfoTextView.setText(updateInfoBean.getVersionDescription().trim());
        mNewVersionTextView.setText(updateInfoBean.getVersionName());
    }

    @Override
    public void onRefresh() {
        mUpdatePresenter.start();
    }
}
