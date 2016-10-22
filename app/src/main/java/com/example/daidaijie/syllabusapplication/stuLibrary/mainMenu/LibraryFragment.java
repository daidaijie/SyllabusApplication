package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibItemAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;
import com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail.BookDetailActivity;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends BaseFragment implements LibraryContract.view, SwipeRefreshLayout.OnRefreshListener, LibItemAdapter.OnLibItemSelectCallBack {

    @BindView(R.id.libRecyclerView)
    RecyclerView mLibRecyclerView;
    @BindView(R.id.refreshLibLayout)
    SwipeRefreshLayout mRefreshLibLayout;

    private LibItemAdapter mLibItemAdapter;

    private static final String CLASS_NAME = LibraryFragment.class.getCanonicalName();

    private static final String EXTRA_POS = CLASS_NAME + ".mPosition";

    private int mPosition;

    @Inject
    LibraryPresenter mLibraryPresenter;

    public static LibraryFragment newInstance(int position) {
        LibraryFragment fragment = new LibraryFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POS, position);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        mPosition = args.getInt(EXTRA_POS, 0);
        DaggerLibraryComponent.builder()
                .libModelComponent(LibModelComponent.getInstance())
                .libraryModule(new LibraryModule(this, mPosition))
                .build().inject(this);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mLibItemAdapter = new LibItemAdapter(mActivity, null);
        mLibRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mLibRecyclerView.setAdapter(mLibItemAdapter);
        mLibItemAdapter.setOnLibItemSelectCallBack(this);

        setupSwipeRefreshLayout(mRefreshLibLayout);
        mRefreshLibLayout.setOnRefreshListener(this);

        mRefreshLibLayout.post(new Runnable() {
            @Override
            public void run() {
                mLibraryPresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_library;
    }

    @Override
    public void onRefresh() {
        mLibraryPresenter.loadData();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mRefreshLibLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mLibRecyclerView, "获取失败", SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showData(List<LibraryBean> libraryBeen) {
        mLibItemAdapter.setLibraryBeen(libraryBeen);
        mLibItemAdapter.notifyDataSetChanged();
        if (libraryBeen.size() == 0) {
            showFailMessage("NULL");
        }
    }

    @Override
    public void onLibSelect(int position) {
        Intent intent = BookDetailActivity.getIntent(mActivity, mPosition, position);
        startActivity(intent);
    }
}
