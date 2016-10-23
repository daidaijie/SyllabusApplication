package com.example.daidaijie.syllabusapplication.other.update;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateApi;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/23.
 */

public class UpdateModel implements IUpdateModel {

    UpdateApi mUpdateApi;

    public UpdateModel(UpdateApi updateApi) {
        mUpdateApi = updateApi;
    }

    @Override
    public Observable<UpdateInfoBean> getUpdateInfo() {
        return mUpdateApi.getUpdateInfo()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<UpdateInfoBean>, Observable<UpdateInfoBean>>() {
                    @Override
                    public Observable<UpdateInfoBean> call(HttpResult<UpdateInfoBean> updateInfoBeanHttpResult) {
                        if (RetrofitUtil.isSuccessful(updateInfoBeanHttpResult)) {
                            return Observable.just(updateInfoBeanHttpResult.getData());
                        }
                        return Observable.error(new Throwable(updateInfoBeanHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}
