package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.mainmenu;


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
import com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.postContent.PostContentActivity;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.event.CircleStateChangeEvent;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;
import com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.StuCircleModelComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.MyItemAnimator;
import com.github.ybq.endless.Endless;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuCircleFragment extends BaseFragment implements StuCircleContract.view, SwipeRefreshLayout.OnRefreshListener, Endless.LoadMoreListener {


    @BindView(R.id.circleRecyclerView)
    RecyclerView mCircleRecyclerView;
    @BindView(R.id.refreshStuCircleLayout)
    SwipeRefreshLayout mRefreshStuCircleLayout;
    @BindView(R.id.postContentButton)
    FloatingActionButton mPostContentButton;

    View mLoadMoreView;

    Endless endless;

    private CirclesAdapter mCirclesAdapter;

    @Inject
    StuCirclePresenter mStuCirclePresenter;

    public static StuCircleFragment newInstance() {
        StuCircleFragment fragment = new StuCircleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerStuCircleComponent.builder()
                .stuCircleModelComponent(StuCircleModelComponent.getInstance())
                .stuCircleModule(new StuCircleModule(this))
                .build().inject(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        mCirclesAdapter = new CirclesAdapter(getActivity(), null);
        //以后一定要记住这句话
        mCircleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCircleRecyclerView.setAdapter(mCirclesAdapter);
        mCirclesAdapter.setOnLikeCallBack(mStuCirclePresenter);
        mCircleRecyclerView.setItemAnimator(new MyItemAnimator());


        mRefreshStuCircleLayout.setOnRefreshListener(this);
        setupSwipeRefreshLayout(mRefreshStuCircleLayout);

        mLoadMoreView = mActivity.getLayoutInflater().inflate(R.layout.bottom_load_more, null);
        mLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        endless = Endless.applyTo(mCircleRecyclerView, mLoadMoreView);
        endless.setLoadMoreListener(this);

        mRefreshStuCircleLayout.post(new Runnable() {
            @Override
            public void run() {
                mStuCirclePresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_stu_circle;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        StuCircleModelComponent.destory();
    }

    @OnClick(R.id.postContentButton)
    public void onClick() {
        Intent intent = PostContentActivity.getIntent(mActivity);
        mActivity.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toTop(ToTopEvent toTopEvent) {
        mCircleRecyclerView.smoothScrollToPosition(0);
        if (toTopEvent.isRefresh) {
            mCircleRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStuCirclePresenter.refresh();
                }
            }, 100);
        }
        if (toTopEvent.isShowSuccuess) {
            SnackbarUtil.ShortSnackbar(mCircleRecyclerView, "发送成功", SnackbarUtil.Confirm).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void circleStateChange(CircleStateChangeEvent event) {
        mCirclesAdapter.notifyItemChanged(event.position);
    }

    @Override
    public void showRefresh(boolean isShow) {
        mRefreshStuCircleLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mPostContentButton, msg, SnackbarUtil.Alert).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mPostContentButton, msg, SnackbarUtil.Confirm).show();
    }

    @Override
    public void loadMoreFinish() {
        endless.loadMoreComplete();
    }

    @Override
    public void showData(List<PostListBean> postListBeen) {
        mCirclesAdapter.setPostListBeen(postListBeen);
        mCirclesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mStuCirclePresenter.refresh();
    }

    @Override
    public void onLoadMore(int i) {
        mStuCirclePresenter.loadData();
    }

}
