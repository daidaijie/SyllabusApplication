package com.example.daidaijie.syllabusapplication.other.update;

import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/23.
 */

public class UpdatePresenter implements UpdateContract.presenter {

    IUpdateModel mUpdateModel;

    UpdateContract.view mView;

    @Inject
    @PerActivity
    public UpdatePresenter(UpdateContract.view view, IUpdateModel updateModel) {
        mView = view;
        mUpdateModel = updateModel;
    }

    @Override
    public void start() {
        mView.showLoading(true);
        mUpdateModel.getUpdateInfo()
                .subscribe(new Subscriber<UpdateInfoBean>() {
                    @Override
                    public void onCompleted() {
                        mView.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage()==null){
                            mView.showFailMessage("获取失败");
                        }else{
                            mView.showFailMessage(e.getMessage());
                        }
                        mView.showLoading(false);
                    }

                    @Override
                    public void onNext(UpdateInfoBean updateInfoBean) {
                        mView.showInfo(updateInfoBean);
                    }
                });
    }
}
