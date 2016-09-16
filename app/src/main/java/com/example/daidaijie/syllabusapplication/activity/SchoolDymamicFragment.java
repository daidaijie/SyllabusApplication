package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SchoolDymamicAdapter;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.SchoolDynamic;
import com.example.daidaijie.syllabusapplication.service.SchoolDynamicService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolDymamicFragment extends Fragment implements SpringView.OnFreshListener {

    public static final String TAG = "SchoolDymamicFragment";

    //已经加载的页数
    private int loadPage;

    private SchoolDymamicAdapter mSchoolDymamicAdapter;

    private List<SchoolDynamic> mSchoolDynamics;

    private int[] pullAnimSrcs = new int[]{R.drawable.mt_pull, R.drawable.mt_pull01, R.drawable.mt_pull02, R.drawable.mt_pull03, R.drawable.mt_pull04, R.drawable.mt_pull05};
    private int[] refreshAnimSrcs = new int[]{R.drawable.mt_refreshing01, R.drawable.mt_refreshing02, R.drawable.mt_refreshing03, R.drawable.mt_refreshing04, R.drawable.mt_refreshing05, R.drawable.mt_refreshing06};
    private int[] loadingAnimSrcs = new int[]{R.drawable.mt_loading01, R.drawable.mt_loading02};

    @BindView(R.id.dymamicRecyclerView)
    RecyclerView mDymamicRecyclerView;
    @BindView(R.id.springView)
    SpringView mSpringView;
    @BindView(R.id.postContentButton)
    FloatingActionButton mPostContentButton;


    public static SchoolDymamicFragment newInstance() {
        SchoolDymamicFragment fragment = new SchoolDymamicFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school_dymamic, container, false);
        ButterKnife.bind(this, view);

        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new MeituanHeader(getActivity(), pullAnimSrcs, refreshAnimSrcs));
        mSpringView.setFooter(new MeituanFooter(getActivity(), loadingAnimSrcs));

        mSchoolDynamics = new ArrayList<>();
        mSchoolDymamicAdapter = new SchoolDymamicAdapter(getActivity(), mSchoolDynamics);
        mDymamicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDymamicRecyclerView.setAdapter(mSchoolDymamicAdapter);

        loadPage = 0;

        mSpringView.post(new Runnable() {
            @Override
            public void run() {
                mSpringView.callFresh();
            }
        });

        return view;
    }

    private void loadPage() {
        SchoolDynamicService schoolDynamicService = RetrofitUtil.getDefault().create(SchoolDynamicService.class);
        schoolDynamicService.getSchoolDynamic(1, loadPage + 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<List<SchoolDynamic>>>() {
                    @Override
                    public void onCompleted() {
                        mSchoolDymamicAdapter.setSchoolDynamics(mSchoolDynamics);
                        mSchoolDymamicAdapter.notifyDataSetChanged();
                        mSpringView.onFinishFreshAndLoad();
                        loadPage++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        mSpringView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onNext(HttpResult<List<SchoolDynamic>> listHttpResult) {
                        if (!RetrofitUtil.isSuccessful(listHttpResult)) {
                            showFailBanner(listHttpResult.getMessage());
                        } else {
                            mSchoolDynamics.addAll(listHttpResult.getData());
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        loadPage = 0;
        mSchoolDynamics.clear();
        loadPage();
    }

    @Override
    public void onLoadmore() {
        loadPage();
    }

    public void showFailBanner(String msg) {
        SnackbarUtil.LongSnackbar(
                mPostContentButton,
                msg.toUpperCase(),
                SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPage();
            }
        }).show();
    }
}
