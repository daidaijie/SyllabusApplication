package com.example.daidaijie.syllabusapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.bean.CircleBean;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;
import com.example.daidaijie.syllabusapplication.model.PostListModel;
import com.example.daidaijie.syllabusapplication.service.CirclesService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.postContentButton)
    FloatingActionButton mPostContentButton;

    private CirclesAdapter mCirclesAdapter;

    int lowID;

    private PostListModel mPostListModel;

    public static final String TAG = "StuCircleFragment";

    private static final String SAVED_POST_LIST_BEEN = "SavedPostListBeen";

    private int[] pullAnimSrcs = new int[]{R.drawable.mt_pull, R.drawable.mt_pull01, R.drawable.mt_pull02, R.drawable.mt_pull03, R.drawable.mt_pull04, R.drawable.mt_pull05};
    private int[] refreshAnimSrcs = new int[]{R.drawable.mt_refreshing01, R.drawable.mt_refreshing02, R.drawable.mt_refreshing03, R.drawable.mt_refreshing04, R.drawable.mt_refreshing05, R.drawable.mt_refreshing06};
    private int[] loadingAnimSrcs = new int[]{R.drawable.mt_loading01, R.drawable.mt_loading02};

    public static StuCircleFragment newInstance() {
        StuCircleFragment fragment = new StuCircleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPostListModel = PostListModel.getInstance();
        if (savedInstanceState != null) {
            mPostListModel.mPostListBeen = (List<PostListBean>) savedInstanceState
                    .getSerializable(SAVED_POST_LIST_BEEN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stu_circle, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);

        mSpringView.setType(SpringView.Type.FOLLOW);
        mSpringView.setListener(this);
        mSpringView.setHeader(new MeituanHeader(getActivity(), pullAnimSrcs, refreshAnimSrcs));
        mSpringView.setFooter(new MeituanFooter(getActivity(), loadingAnimSrcs));

        if (mPostListModel.mPostListBeen == null) {
            mPostListModel.mPostListBeen = new ArrayList<>();
        }

        mCirclesAdapter = new CirclesAdapter(getActivity(), mPostListModel.mPostListBeen);
        //以后一定要记住这句话
        mCircleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mCircleRecyclerView.setAdapter(mCirclesAdapter);

        lowID = Integer.MAX_VALUE;

        if (savedInstanceState == null) {
            mSpringView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpringView.callFresh();
//                getCircles();
                }
            }, 0);
        }

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
        CirclesService circlesService = RetrofitUtil.getDefault().create(CirclesService.class);
        circlesService.getCircles(10, lowID)
                .subscribeOn(Schedulers.io())
                .map(new Func1<CircleBean, List<PostListBean>>() {
                    @Override
                    public List<PostListBean> call(CircleBean circleBean) {
                        return circleBean.getPost_list();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PostListBean>>() {

                    List<PostListBean> tmpPostListBeen;

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: " + tmpPostListBeen.size());
                        if (tmpPostListBeen != null) {
                            if (lowID != Integer.MAX_VALUE) {
                                mPostListModel.mPostListBeen.addAll(tmpPostListBeen);
                                mCirclesAdapter.setPostListBeen(mPostListModel.mPostListBeen);
                            } else {
                                mPostListModel.mPostListBeen = tmpPostListBeen;
                                mCirclesAdapter.setPostListBeen(mPostListModel.mPostListBeen);
                            }
                            lowID = tmpPostListBeen.get(tmpPostListBeen.size() - 1).getId();
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
                        tmpPostListBeen = postListBeen;
                    }
                });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toTop(ToTopEvent toTopEvent) {
        mCircleRecyclerView.smoothScrollToPosition(0);
        if (toTopEvent.isRefresh) {
            mSpringView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSpringView.callFresh();
                }
            }, 50);
        }
        if (toTopEvent.isShowSuccuess) {
            SnackbarUtil.ShortSnackbar(mCircleRecyclerView, "发送成功", SnackbarUtil.Confirm).show();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVED_POST_LIST_BEEN, (Serializable) mPostListModel.mPostListBeen);
    }

    @OnClick(R.id.postContentButton)
    public void onClick() {
        Intent intent = PostContentActivity.getIntent(getActivity());
        getActivity().startActivity(intent);
    }
}
