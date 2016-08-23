package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.OAItemAdapter;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.model.OAmodel;
import com.example.daidaijie.syllabusapplication.service.OAService;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.RecyclerViewEmptySupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficeAutomationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.oARecyclerView)
    RecyclerViewEmptySupport mOARecyclerView;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshOALayout)
    SwipeRefreshLayout mRefreshOALayout;

    private int position;

    private static final String POS = "com.example.daidaijie.syllabusapplication.activity" +
            ".OfficeAutomationFragment.Position";

    public static final String TAG = "OfficeAutomationFragment";

    private List<OABean> mOABeen;

    private OAItemAdapter mOAItemAdapter;

    public static OfficeAutomationFragment newInstance(int position) {
        OfficeAutomationFragment fragment = new OfficeAutomationFragment();
        Bundle args = new Bundle();
        args.putInt(POS, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        position = args.getInt(POS, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_office_automation, container, false);
        ButterKnife.bind(this, view);

        mOARecyclerView.setEmptyView(mEmptyTextView);
        mOAItemAdapter = new OAItemAdapter(getActivity(), mOABeen);
        mOARecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOARecyclerView.setAdapter(mOAItemAdapter);

        mRefreshOALayout.setOnRefreshListener(this);
        mRefreshOALayout.setRefreshing(true);
        getOAinfo();

        return view;
    }

    @Override
    public void onRefresh() {
        getOAinfo();
    }

    private void getOAinfo() {
        OAService oaService = OAmodel.getInstance().mRetrofit.create(OAService.class);
        oaService.getOAInfo(
                "undefined", 0, "", position * 10, (position + 1) * 10
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OABean>>() {
                    @Override
                    public void onCompleted() {
                        mRefreshOALayout.setRefreshing(false);
                        SnackbarUtil.ShortSnackbar(
                                mOARecyclerView, "获取成功", SnackbarUtil.Confirm
                        );
                        mOAItemAdapter.setOABeen(mOABeen);
                        mOAItemAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshOALayout.setRefreshing(false);
                        Log.d(TAG, "onError: " + e.getMessage());
                        SnackbarUtil.LongSnackbar(
                                mOARecyclerView, "获取失败", SnackbarUtil.Alert
                        );
                    }

                    @Override
                    public void onNext(List<OABean> oaBeen) {
                        mOABeen = oaBeen;
                    }
                });

    }
}
