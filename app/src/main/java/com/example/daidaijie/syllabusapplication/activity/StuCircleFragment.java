package com.example.daidaijie.syllabusapplication.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.bean.CircleBean;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.service.CirclesService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuCircleFragment extends Fragment implements SpringView.OnFreshListener {


    @BindView(R.id.circleRecyclerView)
    RecyclerView mCircleRecyclerView;
    @BindView(R.id.springView)
    SpringView mSpringView;

    CirclesAdapter mCirclesAdapter;

    int lowID;

    public static final String TAG = "StuCircleFragment";

    private int[] pullAnimSrcs = new int[]{R.drawable.mt_pull, R.drawable.mt_pull01, R.drawable.mt_pull02, R.drawable.mt_pull03, R.drawable.mt_pull04, R.drawable.mt_pull05};
    private int[] refreshAnimSrcs = new int[]{R.drawable.mt_refreshing01, R.drawable.mt_refreshing02, R.drawable.mt_refreshing03, R.drawable.mt_refreshing04, R.drawable.mt_refreshing05, R.drawable.mt_refreshing06};
    private int[] loadingAnimSrcs = new int[]{R.drawable.mt_loading01, R.drawable.mt_loading02};

    public static StuCircleFragment newInstance() {
        StuCircleFragment fragment = new StuCircleFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stu_circle, container, false);
        ButterKnife.bind(this, view);

        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new MeituanHeader(getActivity(), pullAnimSrcs, refreshAnimSrcs));
        mSpringView.setFooter(new MeituanFooter(getActivity(), loadingAnimSrcs));


        mCirclesAdapter = new CirclesAdapter(getActivity(), new ArrayList<PostListBean>());
        //以后一定要记住这句话
        mCircleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCircleRecyclerView.setAdapter(mCirclesAdapter);

        lowID = Integer.MAX_VALUE;
        getCircles();

        return view;
    }

    @Override
    public void onRefresh() {
        lowID = Integer.MAX_VALUE;
        getCircles();
    }

    @Override
    public void onLoadmore() {
        getCircles();
    }

    private void getCircles() {
        Log.d(TAG, "getCircles: ");
        Retrofit retrofit = RetrofitUtil.getDefault();
        CirclesService circlesService = retrofit.create(CirclesService.class);
        circlesService.getCircles(10, lowID)
                .subscribeOn(Schedulers.io())
                .map(new Func1<CircleBean, List<PostListBean>>() {
                    @Override
                    public List<PostListBean> call(CircleBean circleBean) {
                        return circleBean.getPost_list();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PostListBean>>() {

                    List<PostListBean> mPostListBeen;

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: " + mPostListBeen.size());
                        if (mPostListBeen != null) {
                            if (lowID != Integer.MAX_VALUE) {
                                mCirclesAdapter.getPostListBeen().addAll(mPostListBeen);
                            } else {
                                mCirclesAdapter.setPostListBeen(mPostListBeen);
                            }
                            lowID = mPostListBeen.get(mPostListBeen.size() - 1).getId();
                            mCirclesAdapter.notifyDataSetChanged();
                        }
                        mSpringView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mSpringView.onFinishFreshAndLoad();
                    }

                    @Override
                    public void onNext(List<PostListBean> postListBeen) {
                        mPostListBeen = postListBeen;
                    }
                });

    }
}
