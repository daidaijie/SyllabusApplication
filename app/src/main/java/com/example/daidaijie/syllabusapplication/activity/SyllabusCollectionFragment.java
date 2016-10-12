package com.example.daidaijie.syllabusapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CollectionItemAdapter;
import com.example.daidaijie.syllabusapplication.bean.CollectionInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.event.CollectionStateChangeEvent;
import com.example.daidaijie.syllabusapplication.model.SyllabusCollectionModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.retrofitApi.SyllabusCollectionService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusCollectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refreshCollectionLayout)
    SwipeRefreshLayout mRefreshCollectionLayout;
    @BindView(R.id.collectionRecyclerView)
    RecyclerView mCollectionRecyclerView;
    @BindView(R.id.addCollectionButton)
    FloatingActionButton mAddCollectionButton;

    SyllabusCollectionModel mSyllabusCollectionModel;
    CollectionItemAdapter mCollectionItemAdapter;

    public static final String TAG = "SyllabusCollectionFragment";

    public static SyllabusCollectionFragment newInstance() {
        SyllabusCollectionFragment fragment = new SyllabusCollectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_syllabus_collection, container, false);
        ButterKnife.bind(this, view);

        mSyllabusCollectionModel = SyllabusCollectionModel.getInstance();
        mCollectionItemAdapter = new CollectionItemAdapter(getActivity(), mSyllabusCollectionModel.getCollectionInfo());
        mCollectionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCollectionRecyclerView.setAdapter(mCollectionItemAdapter);

        mRefreshCollectionLayout.setOnRefreshListener(this);

        mRefreshCollectionLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshCollectionLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshCollectionLayout.setRefreshing(true);
                getCollections();
            }
        });

        mAddCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCollectionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getCollections() {
        SyllabusCollectionService collectionService =
                RetrofitUtil.getDefault().create(SyllabusCollectionService.class);
        collectionService.getCollectionInfo(User.getInstance().getAccount(),
                User.getInstance().getUserInfo().getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<CollectionInfo>>() {
                    @Override
                    public void onCompleted() {
                        mRefreshCollectionLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshCollectionLayout.setRefreshing(false);
                        showFailBanner("获取失败");
                    }

                    @Override
                    public void onNext(HttpResult<CollectionInfo> collectionInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(collectionInfoHttpResult)) {
                            mSyllabusCollectionModel.setCollectionInfo(collectionInfoHttpResult.getData());
                            mCollectionItemAdapter.setCollectionInfo(mSyllabusCollectionModel.getCollectionInfo());
                            mCollectionItemAdapter.notifyDataSetChanged();
                        } else {
                            showFailBanner(collectionInfoHttpResult.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        getCollections();
    }

    public void showFailBanner(String msg) {
        SnackbarUtil.LongSnackbar(
                mAddCollectionButton,
                msg.toUpperCase(),
                SnackbarUtil.Alert
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateList(CollectionStateChangeEvent event) {
        mCollectionItemAdapter.setCollectionInfo(mSyllabusCollectionModel.getCollectionInfo());
        mCollectionItemAdapter.notifyDataSetChanged();
    }
}
