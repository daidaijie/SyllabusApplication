package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.OAItemAdapter;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OARead;
import com.example.daidaijie.syllabusapplication.event.OAClearEvent;
import com.example.daidaijie.syllabusapplication.model.OAModel;
import com.example.daidaijie.syllabusapplication.retrofitApi.OAService;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfficeAutomationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.oARecyclerView)
    RecyclerView mOARecyclerView;
    @BindView(R.id.emptyTextView)
    TextView mEmptyTextView;
    @BindView(R.id.refreshOALayout)
    SwipeRefreshLayout mRefreshOALayout;

    private int position;

    private static final String EXTRA_POS = "com.example.daidaijie.syllabusapplication.activity" +
            ".OfficeAutomationFragment.Position";

    public static final String TAG = "OfficeAutomationFragment";

    private List<OABean> mOABeen;

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
        position = args.getInt(EXTRA_POS, 0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_office_automation, container, false);
        ButterKnife.bind(this, view);

        mOAItemAdapter = new OAItemAdapter(getActivity(), mOABeen);
        mOARecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOARecyclerView.setAdapter(mOAItemAdapter);

        mRefreshOALayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefreshOALayout.setOnRefreshListener(this);
        mRefreshOALayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshOALayout.setRefreshing(true);
                getOAinfo();
            }
        });


        return view;
    }

    @Override
    public void onRefresh() {
        getOAinfo();
    }

    private void getOAinfo() {
        mEmptyTextView.setText("");
        OAService oaService = OAModel.getInstance().mRetrofit.create(OAService.class);
        oaService.getOAInfo(
                "undefined", OAModel.getInstance().subID, OAModel.getInstance().keyword,
                position * 10, (position + 1) * 10
        ).subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<OABean>, Observable<OABean>>() {
                    @Override
                    public Observable<OABean> call(List<OABean> oaBeen) {
                        return Observable.from(oaBeen);
                    }
                })
                .observeOn(Schedulers.io())
                .filter(new Func1<OABean, Boolean>() {
                    @Override
                    public Boolean call(OABean oaBean) {
                        boolean isNotNULL = (oaBean.getDOCVALIDDATE() != null
                                && oaBean.getDOCVALIDTIME() != null);
                        if (isNotNULL) {
                            oaBean.setRead(OARead.hasRead(oaBean));
                        }
                        return isNotNULL;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<OABean>() {

                    List<OABean> tmpOABeen = new ArrayList<>();

                    @Override
                    public void onCompleted() {
                        mRefreshOALayout.setRefreshing(false);
                        mOABeen = tmpOABeen;
                        mOAItemAdapter.setOABeen(mOABeen);
                        mOAItemAdapter.notifyDataSetChanged();
                        if (mOABeen.size() == 0) {
                            mEmptyTextView.setText("暂无办公自动化信息");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mEmptyTextView.setText("暂无办公自动化信息");
                        mRefreshOALayout.setRefreshing(false);
                        SnackbarUtil.LongSnackbar(
                                mRefreshOALayout, "获取失败", SnackbarUtil.Alert
                        ).show();

                    }

                    @Override
                    public void onNext(OABean oaBean) {
                        tmpOABeen.add(oaBean);
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clear(OAClearEvent oaClearEvent) {
        for (OABean oaBean : mOABeen) {
            oaBean.setRead(false);
        }
        mOAItemAdapter.notifyDataSetChanged();
    }
}
