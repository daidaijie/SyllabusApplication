package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle;

import com.example.daidaijie.syllabusapplication.bean.CircleBean;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpsBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.CirclesApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class SchoolCircleModel implements ISchoolCircleModel {

    List<PostListBean> mPostListBeen;

    CirclesApi mCirclesApi;

    IUserModel mIUserModel;

    private int lowID;

    public SchoolCircleModel(CirclesApi circlesApi, IUserModel IUserModel) {
        mCirclesApi = circlesApi;
        mIUserModel = IUserModel;
        lowID = Integer.MAX_VALUE;
        mPostListBeen = new ArrayList<>();
    }


    @Override
    public Observable<List<PostListBean>> getCircleListFromNet() {
        return mCirclesApi.getCircles(10, lowID)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<CircleBean>, Observable<CircleBean>>() {
                    @Override
                    public Observable<CircleBean> call(HttpResult<CircleBean> circleBeanHttpResult) {
                        if (RetrofitUtil.isSuccessful(circleBeanHttpResult)) {
                            return Observable.just(circleBeanHttpResult.getData());
                        } else {
                            return Observable.error(new Throwable(circleBeanHttpResult.getMessage()));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<CircleBean, Observable<List<PostListBean>>>() {
                    @Override
                    public Observable<List<PostListBean>> call(CircleBean circleBean) {
                        List<PostListBean> post_list = circleBean.getPost_list();
                        for (PostListBean bean : post_list) {
                            for (ThumbUpsBean thumbUpsBean : bean.getThumb_ups()) {
                                if (thumbUpsBean.getUid() == mIUserModel.getUserBaseBean().getId()) {
                                    bean.isMyLove = true;
                                    break;
                                }
                            }
                        }
                        if (lowID != Integer.MAX_VALUE) {
                            mPostListBeen.addAll(post_list);
                        } else {
                            mPostListBeen = post_list;
                        }
                        lowID = post_list.get(post_list.size() - 1).getId();

                        return Observable.just(mPostListBeen);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<PostListBean>> loadCircleListFromNet() {
        lowID = Integer.MAX_VALUE;
        return getCircleListFromNet();
    }
}
