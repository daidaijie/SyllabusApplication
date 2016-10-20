package com.example.daidaijie.syllabusapplication.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.AppComponent;

import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/10/12.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    protected AppComponent mAppComponent;
    protected int deviceWidth;
    protected int deviceHeight;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
        mAppComponent = mActivity.mAppComponent;

        deviceWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        deviceHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);

        init(savedInstanceState);

        return view;
    }

    protected abstract void init(Bundle savedInstanceState);

    protected abstract int getContentView();


    protected void setupSwipeRefreshLayout(SwipeRefreshLayout mRefreshLayout) {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

}
