package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.event.DymaticStateChangeEvent;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic.PostDymaticActivity;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;
import com.github.ybq.endless.Endless;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolDymaticFragment extends BaseFragment implements SchoolDymaticContract.view, Endless.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.dymamicRecyclerView)
    RecyclerView mDymaticRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.postContentButton)
    FloatingActionButton mPostContentButton;

    View mLoadMoreView;

    private SchoolDymaticAdapter mSchoolDymaticAdapter;

    Endless mEndless;

    boolean isLoaded;

    boolean singleLock;

    @Inject
    SchoolDymaticPresenter mSchoolDymaticPresenter;

    public static SchoolDymaticFragment newInstance() {
        SchoolDymaticFragment fragment = new SchoolDymaticFragment();
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        DaggerSchoolDymaticComponent.builder()
                .schoolDymaticModelComponent(SchoolDymaticModelComponent.getINSTANCE(mAppComponent))
                .schoolDymaticModule(new SchoolDymaticModule(this))
                .build().inject(this);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        setupSwipeRefreshLayout(mSwipeRefreshLayout);

        mSchoolDymaticAdapter = new SchoolDymaticAdapter(getActivity(), null);

        mDymaticRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDymaticRecyclerView.setAdapter(mSchoolDymaticAdapter);
        mDymaticRecyclerView.setItemAnimator(new MyItemAnimator());
        mSchoolDymaticAdapter.setOnLikeCallBack(mSchoolDymaticPresenter);

        mLoadMoreView = mActivity.getLayoutInflater().inflate(R.layout.bottom_load_more, null);
        mLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSchoolDymaticPresenter.refresh();
            }
        });

        mEndless = Endless.applyTo(mDymaticRecyclerView, mLoadMoreView);
        mEndless.setLoadMoreListener(this);

        mPostContentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSchoolDymaticPresenter.handlerFAB();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_school_dymamic;
    }

    @Override
    public void showRefresh(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mPostContentButton,
                msg,
                SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mPostContentButton,
                msg,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mPostContentButton,
                msg,
                SnackbarUtil.Info
        ).show();
    }

    @Override
    public void loadMoreFinish() {
        singleLock = false;
        mEndless.loadMoreComplete();
    }

    @Override
    public void showData(List<SchoolDymatic> schoolDymatics) {
        mSchoolDymaticAdapter.setSchoolDymatics(schoolDymatics);
        mSchoolDymaticAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadEnd() {
        isLoaded = true;
        mLoadMoreView.setVisibility(View.GONE);
    }

    @Override
    public void loadStart() {
        singleLock = false;
        isLoaded = false;
        mLoadMoreView.setVisibility(View.VISIBLE);
    }

    @Override
    public void toPostDymatic() {
        Intent intent = new Intent(mActivity, PostDymaticActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoadMore(int i) {
        mDymaticRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!singleLock && !isLoaded) {
                    singleLock = true;
                    mSchoolDymaticPresenter.loadData();
                } else {
                    loadMoreFinish();
                }
            }
        }, 500);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        SchoolDymaticModelComponent.destroy();
    }

    @Override
    public void onRefresh() {
        mSchoolDymaticPresenter.refresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void circleStateChange(DymaticStateChangeEvent event) {
        mSchoolDymaticAdapter.notifyItemChanged(event.position);
    }
}
