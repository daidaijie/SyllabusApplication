package com.example.daidaijie.syllabusapplication.schoolDymatic.circle;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.CircleBean;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.ThumbUp;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpsBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.CirclesApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.DeletePostApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.ThumbUpApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.orhanobut.logger.Logger;

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

    ThumbUpApi mThumbUpApi;

    DeletePostApi mDeletePostApi;

    private int lowID;

    public SchoolCircleModel(CirclesApi circlesApi, IUserModel IUserModel,
                             ThumbUpApi thumbUpApi, DeletePostApi deletePostApi) {
        mCirclesApi = circlesApi;
        mThumbUpApi = thumbUpApi;
        mIUserModel = IUserModel;
        mDeletePostApi = deletePostApi;
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
                                if (thumbUpsBean.getUid() == mIUserModel.getUserBaseBeanNormal().getId()) {
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

    @Override
    public void getCircleByPosition(int position, IBaseModel.OnGetSuccessCallBack<PostListBean> onGetSuccessCallBack) {
        onGetSuccessCallBack.onGetSuccess(mPostListBeen.get(position));
    }

    @Override
    public Observable<ThumbUpReturn> like(int position) {
        final PostListBean postListBean = mPostListBeen.get(position);
        final ThumbUp thumbUp = new ThumbUp(
                postListBean.getId(),
                mIUserModel.getUserInfoNormal().getUser_id(),
                mIUserModel.getUserInfoNormal().getToken());
        return mThumbUpApi.like(thumbUp)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<ThumbUpReturn>, Observable<ThumbUpReturn>>() {
                    @Override
                    public Observable<ThumbUpReturn> call(HttpResult<ThumbUpReturn> thumbUpReturnHttpResult) {
                        Logger.t("thumpUp like").json(GsonUtil.getDefault().toJson(thumbUpReturnHttpResult));
                        if (thumbUpReturnHttpResult.getCode() == 201) {
                            ThumbUpReturn thumbUpReturn = thumbUpReturnHttpResult.getData();
                            ThumbUpsBean bean = new ThumbUpsBean();
                            bean.setUid(thumbUp.getUid());
                            bean.setId(thumbUpReturn.getId());
                            postListBean.getThumb_ups().add(bean);
                            postListBean.isMyLove = true;
                            return Observable.just(thumbUpReturn);
                        } else {
                            return Observable.error(new Throwable(thumbUpReturnHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> unlike(int position) {
        final PostListBean postListBean = mPostListBeen.get(position);
        ThumbUpsBean myThumbUpsBean = null;
        for (ThumbUpsBean bean : postListBean.getThumb_ups()) {
            if (bean.getUid() == mIUserModel.getUserInfoNormal().getUser_id()) {
                myThumbUpsBean = bean;
                break;
            }
        }
        if (myThumbUpsBean == null)
            return Observable.error(new Throwable("you haven't thumbUp this!"));
        final ThumbUpsBean finalMyThumbUpsBean = myThumbUpsBean;
        return mThumbUpApi.unlike(
                myThumbUpsBean.getId(),
                mIUserModel.getUserInfoNormal().getUser_id(),
                mIUserModel.getUserInfoNormal().getToken())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(HttpResult<Void> voidHttpResult) {
                        Logger.t("thumpUp unlike").json(GsonUtil.getDefault().toJson(voidHttpResult));
                        if (RetrofitUtil.isSuccessful(voidHttpResult)) {
                            postListBean.getThumb_ups().remove(finalMyThumbUpsBean);
                            postListBean.isMyLove = false;
                            return Observable.just(voidHttpResult.getData());
                        } else {
                            return Observable.error(new Throwable(voidHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<PostListBean>> deletePost(final int position) {
        final PostListBean postListBean = mPostListBeen.get(position);
        UserInfo userInfo = mIUserModel.getUserInfoNormal();

        return mDeletePostApi.deletePost(postListBean.getId(),
                userInfo.getUser_id(), userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<List<PostListBean>>>() {
                    @Override
                    public Observable<List<PostListBean>> call(HttpResult<Void> voidHttpResult) {
                        if (RetrofitUtil.isSuccessful(voidHttpResult)) {
                            mPostListBeen.remove(position);
                            return Observable.just(mPostListBeen);
                        } else {
                            return Observable.error(new Throwable(voidHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

}
