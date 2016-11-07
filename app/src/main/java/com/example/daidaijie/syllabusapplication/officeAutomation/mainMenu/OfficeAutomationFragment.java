package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.OAItemAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OASearchBean;
import com.example.daidaijie.syllabusapplication.event.OAClearEvent;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAModelComponent;
import com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail.OADetailActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficeAutomationFragment extends BaseFragment implements OAContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.oARecyclerView)
    RecyclerView mOARecyclerView;
    @BindView(R.id.refreshOALayout)
    SwipeRefreshLayout mRefreshOALayout;

    @Inject
    OAPresenter mOAPresenter;

    int mPosition;

    private static final String EXTRA_POS = OfficeAutomationFragment.class.getCanonicalName()
            + ".Position";

    private OAItemAdapter mOAItemAdapter;

    public static OfficeAutomationFragment newInstance(int position) {
        OfficeAutomationFragment fragment = new OfficeAutomationFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POS, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle args = getArguments();
        mPosition = args.getInt(EXTRA_POS, 0);
        DaggerOAComponent.builder()
                .oAModelComponent(OAModelComponent.newInstance(UserComponent.buildInstance(mAppComponent), new OASearchBean()))
                .oAModule(new OAModule(this, mPosition))
                .build().inject(this);
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        mOAItemAdapter = new OAItemAdapter(getActivity(), null);
        mOARecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOARecyclerView.setAdapter(mOAItemAdapter);

        setupSwipeRefreshLayout(mRefreshOALayout);
        mRefreshOALayout.setOnRefreshListener(this);

        mRefreshOALayout.post(new Runnable() {
            @Override
            public void run() {
                mOAPresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_office_automation;
    }

    @Override
    public void onRefresh() {
        mOAPresenter.loadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clear(OAClearEvent oaClearEvent) {
        mOAItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mRefreshOALayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.LongSnackbar(
                mRefreshOALayout, msg, SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOAPresenter.loadData();
            }
        }).show();
    }

    @Override
    public void showData(List<OABean> oaBeen) {
        mOAItemAdapter.setOABeen(oaBeen);
        mOAItemAdapter.notifyDataSetChanged();
        mOAItemAdapter.setOnOAReadListener(new OAItemAdapter.OnOAReadListener() {
            @Override
            public void onOARead(OABean oaBean, int position) {
                Intent intent = OADetailActivity.getIntent(mActivity, mPosition, position);
                startActivity(intent);
                mOAPresenter.setRead(oaBean, true);
                mOAItemAdapter.notifyItemChanged(position);
            }
        });
    }
}
