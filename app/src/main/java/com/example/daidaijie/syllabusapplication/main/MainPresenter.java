package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.BannerBeen;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
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

    IBannerModel mBannerModel;

    IUserModel mUserModel;

    ILoginModel mILoginModel;

    MainContract.view mView;

    @Inject
    @PerActivity
    public MainPresenter(MainContract.view view, IBannerModel bannerModel, @LoginUser IUserModel userModel, ILoginModel ILoginModel) {
        mView = view;
        mBannerModel = bannerModel;
        mUserModel = userModel;
        mILoginModel = ILoginModel;
    }

    @Override
    public void start() {
        mUserModel.getUserInfo()
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        mView.showUserInfo(userInfo);
                    }
                });
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
