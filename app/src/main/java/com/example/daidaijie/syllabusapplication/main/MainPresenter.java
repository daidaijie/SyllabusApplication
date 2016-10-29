package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.BannerBeen;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UpdateInfoBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.other.update.IUpdateModel;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class MainPresenter implements MainContract.presenter {

    IUpdateModel mIUpdateModel;

    IBannerModel mBannerModel;

    IUserModel mUserModel;

    ILoginModel mILoginModel;

    MainContract.view mView;

    @Inject
    @PerActivity
    public MainPresenter(MainContract.view view,
                         IBannerModel bannerModel,
                         @LoginUser IUserModel userModel,
                         ILoginModel ILoginModel,
                         IUpdateModel updateModel) {
        mView = view;
        mBannerModel = bannerModel;
        mUserModel = userModel;
        mILoginModel = ILoginModel;
        mIUpdateModel = updateModel;
    }

    @Override
    public void start() {

        showUserInfo();

        mView.showSemester(mILoginModel.getCurrentSemester());

        //先使用缓存的
        mBannerModel.getBannerNormal(new IBaseModel.OnGetSuccessCallBack<BannerBeen>() {
            @Override
            public void onGetSuccess(BannerBeen bannerBeen) {
                mView.setBannerPage(bannerBeen.getBanners());
            }
        });

        //再获取网络的
        mBannerModel.getBannerFromNet()
                .subscribe(new Action1<BannerBeen>() {
                    @Override
                    public void call(BannerBeen bannerBeen) {
                        mView.setBannerPage(bannerBeen.getBanners());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

        mIUpdateModel.getUpdateInfo()
                .subscribe(new Action1<UpdateInfoBean>() {
                    @Override
                    public void call(UpdateInfoBean updateInfoBean) {
                        if (updateInfoBean.getIntVersionCode() > App.versionCode) {
                            mView.showInfoMessage("需要更新啦");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    @Override
    public void showUserInfo() {
        mView.showUserInfo(mUserModel.getUserInfoNormal());
    }

    @Override
    public void setCurrentSemester(Semester semester) {
        mILoginModel.setCurrentSemester(semester);
        mView.showSemester(semester);
        mView.showInfoMessage("已切换到" + semester.getYearString() + " " + semester.getSeasonString());
    }

    @Override
    public void showSemesterSelect() {
        mView.setCurrentSemester(mILoginModel.getCurrentSemester());
    }
}
