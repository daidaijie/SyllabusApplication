package com.example.daidaijie.syllabusapplication.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.OfficeAutomationActivity;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.event.SyllabusEvent;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.GetUserBaseService;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/7/26.
 */
public class SyllabusFragmentPresenter extends ISyllabusFragmentPresenter {

    private static final String TAG = "SyllabusFragment";

    private Syllabus mSyllabus;

    private int mWeek;

    private boolean isSuccessLogin;

    public SyllabusFragmentPresenter() {
        reloadSyllabus();
    }

    @Override
    public void showSyllabus() {
        mView.showSyllabus(mSyllabus);
    }

    @Override
    public void updateSyllabus() {
        isSuccessLogin = true;
        mView.setViewPagerEnable(false);
        final UserInfoService userInfoService = RetrofitUtil.getDefault().create(UserInfoService.class);
        final GetUserBaseService userBaseService = RetrofitUtil.getDefault().create(GetUserBaseService.class);

        Semester mCurrentSemester = User.getInstance().getCurrentSemester();

        userInfoService.getUserInfo(
                User.getInstance().getAccount(),
                User.getInstance().getPassword(),
                "query",
                mCurrentSemester.getYearString()
                , mCurrentSemester.getSeason() + ""
        ).subscribeOn(Schedulers.io())
                .filter(new Func1<HttpResult<UserInfo>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<UserInfo> userInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(userInfoHttpResult)) {
                            return true;
                        } else {
                            isSuccessLogin = false;
                            mView.showFailBanner(userInfoHttpResult.getMessage());
                            return false;
                        }
                    }
                }).flatMap(new Func1<HttpResult<UserInfo>, Observable<HttpResult<UserBaseBean>>>() {
            @Override
            public Observable<HttpResult<UserBaseBean>> call(HttpResult<UserInfo> result) {
                UserInfo userInfo = result.getData();
                User.getInstance().setUserInfo(userInfo);

                mSyllabus = new Syllabus();
                mSyllabus.convertSyllabus(userInfo.getClasses());
                User.getInstance().setSyllabus(User.getInstance().getCurrentSemester(), mSyllabus);

                return userBaseService.get_user(User.getInstance().getAccount());
            }
        }).filter(new Func1<HttpResult<UserBaseBean>, Boolean>() {
            @Override
            public Boolean call(HttpResult<UserBaseBean> result) {
                Log.e(TAG, "call: " + result.getCode());
                if (RetrofitUtil.isSuccessful(result)) {
                    return true;
                } else {
                    mView.showFailBanner(result.getMessage());
                    isSuccessLogin = false;
                    return false;
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<UserBaseBean>>() {
                    @Override
                    public void onCompleted() {
                        if (mView == null) {
                            return;
                        }
                        if (isSuccessLogin) {
                            showSyllabus();
                            updateUserInfo();
                            EventBus.getDefault().post(new SyllabusEvent(mWeek));
                            mView.showSuccessBanner();
                        }
                        mView.hideLoading();
                        mView.setViewPagerEnable(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView == null) {
                            return;
                        }
                        mView.hideLoading();
                        mView.showFailBanner("同步失败");
                        mView.setViewPagerEnable(true);
                    }

                    @Override
                    public void onNext(HttpResult<UserBaseBean> result) {
                        User.getInstance().setUserBaseBean(result.getData());
                    }
                });
    }

    @Override
    public void updateUserInfo() {
        UserInfo mUserInfo = User.getInstance().getUserInfo();
        if (mUserInfo != null) {
            if (mUserInfo.getAvatar() != null) {
                mView.setHeadImageView(Uri.parse(mUserInfo.getAvatar()));
            } else {
                mView.setHeadImageView(Uri.parse("res://" + App.getContext().getPackageName()
                        + "/" + R.drawable.ic_syllabus_icon));
            }
            if (mUserInfo.getNickname() != null) {
                mView.setNickName(mUserInfo.getNickname());
            } else {
                mView.setNickName("未登陆用户");
            }
        }
    }

    @Override
    public void reloadSyllabus() {
        mSyllabus = User.getInstance().getSyllabus(User.getInstance().getCurrentSemester());
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }
}
