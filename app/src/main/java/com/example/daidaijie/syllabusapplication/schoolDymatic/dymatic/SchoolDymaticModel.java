package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.bean.ThumbUp;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpsBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.DeletePostApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.SchoolDymaticApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.ThumbUpApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class SchoolDymaticModel implements ISchoolDymaticModel {

    //已经加载的页数
    private int loadPage;

    List<SchoolDymatic> mSchoolDymatics;

    SchoolDymaticApi mSchoolDymaticApi;

    IUserModel mIUserModel;

    ThumbUpApi mThumbUpApi;

    DeletePostApi mDeletePostApi;

    public SchoolDymaticModel(SchoolDymaticApi schoolDymaticApi, IUserModel IUserModel,
                              ThumbUpApi thumbUpApi, DeletePostApi deletePostApi) {
        mSchoolDymaticApi = schoolDymaticApi;
        mThumbUpApi = thumbUpApi;
        mIUserModel = IUserModel;
        mDeletePostApi = deletePostApi;
        loadPage = 0;
    }

    @Override
    public Observable<List<SchoolDymatic>> loadSchoolDynamicListFromNet() {
        loadPage = 0;
        return getSchoolDynamicListFromNet();
    }

    @Override
    public void getDymaticByPosition(int position, IBaseModel.OnGetSuccessCallBack<SchoolDymatic> onGetSuccessCallBack) {
        onGetSuccessCallBack.onGetSuccess(mSchoolDymatics.get(position));
    }

    @Override
    public Observable<ThumbUpReturn> like(int position) {
        final SchoolDymatic schoolDymatic = mSchoolDymatics.get(position);
        final ThumbUp thumbUp = new ThumbUp(
                schoolDymatic.getId(),
                mIUserModel.getUserInfoNormal().getUser_id(),
                mIUserModel.getUserInfoNormal().getToken());
        return mThumbUpApi.like(thumbUp)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<ThumbUpReturn>, Observable<ThumbUpReturn>>() {
                    @Override
                    public Observable<ThumbUpReturn> call(HttpResult<ThumbUpReturn> thumbUpReturnHttpResult) {
                        if (thumbUpReturnHttpResult.getCode() == 201) {
                            ThumbUpReturn thumbUpReturn = thumbUpReturnHttpResult.getData();
                            ThumbUpsBean bean = new ThumbUpsBean();
                            bean.setUid(thumbUp.getUid());
                            bean.setId(thumbUpReturn.getId());
                            schoolDymatic.getThumb_ups().add(bean);
                            schoolDymatic.isMyLove = true;
                            return Observable.just(thumbUpReturn);
                        } else {
                            return Observable.error(new Throwable(thumbUpReturnHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> unlike(int position) {
        final SchoolDymatic schoolDymatic = mSchoolDymatics.get(position);
        ThumbUpsBean myThumbUpsBean = null;
        for (ThumbUpsBean bean : schoolDymatic.getThumb_ups()) {
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
                        if (RetrofitUtil.isSuccessful(voidHttpResult)) {
                            schoolDymatic.getThumb_ups().remove(finalMyThumbUpsBean);
                            schoolDymatic.isMyLove = false;
                            return Observable.just(voidHttpResult.getData());
                        } else {
                            return Observable.error(new Throwable(voidHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<SchoolDymatic>> deletePost(final int position) {
        final SchoolDymatic schoolDymatic = mSchoolDymatics.get(position);
        UserInfo userInfo = mIUserModel.getUserInfoNormal();

        return mDeletePostApi.deletePost(schoolDymatic.getId(),
                userInfo.getUser_id(), userInfo.getToken())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<List<SchoolDymatic>>>() {
                    @Override
                    public Observable<List<SchoolDymatic>> call(HttpResult<Void> voidHttpResult) {
                        if (RetrofitUtil.isSuccessful(voidHttpResult)) {
                            mSchoolDymatics.remove(position);
                            return Observable.just(mSchoolDymatics);
                        } else {
                            return Observable.error(new Throwable(voidHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<List<SchoolDymatic>> getSchoolDynamicListFromNet() {
        return mSchoolDymaticApi.getSchoolDymatic(1, loadPage + 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<HttpResult<List<SchoolDymatic>>, Observable<List<SchoolDymatic>>>() {
                    @Override
                    public Observable<List<SchoolDymatic>> call(HttpResult<List<SchoolDymatic>> listHttpResult) {
                        if (RetrofitUtil.isSuccessful(listHttpResult)) {
                            loadPage++;
                            List<SchoolDymatic> schoolDymatics = listHttpResult.getData();
                            for (SchoolDymatic dynamic : schoolDymatics) {
                                for (ThumbUpsBean thumbUpsBean : dynamic.getThumb_ups()) {
                                    if (thumbUpsBean.getUid() == mIUserModel.getUserBaseBeanNormal().getId()) {
                                        dynamic.isMyLove = true;
                                        break;
                                    }
                                }
                            }
                            if (loadPage == 1) {
                                mSchoolDymatics = schoolDymatics;
                            } else {
                                mSchoolDymatics.addAll(schoolDymatics);
                            }
                            return Observable.just(mSchoolDymatics);
                        }
                        return Observable.error(new Throwable(listHttpResult.getMessage()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
