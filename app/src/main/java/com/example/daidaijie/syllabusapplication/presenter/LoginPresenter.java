package com.example.daidaijie.syllabusapplication.presenter;

import android.util.Log;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.retrofitApi.GetUserBaseApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.joda.time.DateTime;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/8/24.
 */
public class LoginPresenter extends ILoginPresenter {

    private static final String TAG = "LoginPresenter";
    Semester mCurrentSemester;

    private boolean isSuccessLogin;

    /**
     * 登陆逻辑
     *
     * @param username 用户名
     * @param password 密码
     * @param isLogin  是否需要登陆，如果是，就必须进行请求，不是的话，就查看是否有缓存
     */
    @Override
    public void login(final String username, final String password, final boolean isLogin) {


        if (!isLogin) {
            /**
             * 当前储存的不为空
             */
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                mView.showLoginFail("");
                return;
            }

            /**
             * 信息都不为空
             */
            if (User.getInstance().getUserBaseBean() != null && User.getInstance().getUserInfo() != null) {
                if (User.getInstance().getUserInfo().getToken() != null
                        && !User.getInstance().getUserInfo().getToken().isEmpty()) {
                    mView.showLoginSuccess();
                    return;
                }
            }
        }

        mView.showLoadingDialog();

        final UserInfoService userInfoService = RetrofitUtil.getDefault().create(UserInfoService.class);
        final GetUserBaseApi userBaseService = RetrofitUtil.getDefault().create(GetUserBaseApi.class);


        mCurrentSemester = User.getInstance().getCurrentSemester();

        if (mCurrentSemester == null) {
            int queryYear;
            int querySem;
            DateTime dateTime = DateTime.now();
            int year = dateTime.getYear();
            int month = dateTime.getMonthOfYear();
            if (month > 1 && month < 8) {
                queryYear = year - 1;
                querySem = 2;
            } else if (month == 8) {
                queryYear = year;
                querySem = 3;
            } else {
                if (month > 8) {
                    queryYear = year;
                } else {
                    queryYear = year - 1;
                }
                querySem = 1;
            }

            mCurrentSemester = new Semester(queryYear, querySem);
            User.getInstance().setCurrentSemester(mCurrentSemester);
        }

        isSuccessLogin = true;

        userInfoService.getUserInfo(
                username,
                password,
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
                            mView.showLoginFail(userInfoHttpResult.getMessage());
                            isSuccessLogin = false;
                            return false;
                        }
                    }
                })
                .flatMap(new Func1<HttpResult<UserInfo>, Observable<HttpResult<UserBaseBean>>>() {
                    @Override
                    public Observable<HttpResult<UserBaseBean>> call(HttpResult<UserInfo> result) {
                        UserInfo userInfo = result.getData();
                        User.getInstance().setCurrentAccount(username);
                        User.getInstance().setUserInfo(userInfo);

                        User.getInstance().setAccount(username);
                        User.getInstance().setPassword(password);
                        LessonModel.getInstance().setCurrentLessonModel(username);

                        Syllabus mSyllabus;
                        if (User.getInstance().getSyllabus(mCurrentSemester) == null) {
                            mSyllabus = new Syllabus();
                        } else {
                            mSyllabus = User.getInstance().getSyllabus(mCurrentSemester);
                        }
                        mSyllabus.convertSyllabus(userInfo.getClasses(), mCurrentSemester);

                        User.getInstance().setSyllabus(User.getInstance().getCurrentSemester(), mSyllabus);

                        return userBaseService.get_user(username);
                    }
                }).filter(new Func1<HttpResult<UserBaseBean>, Boolean>() {
            @Override
            public Boolean call(HttpResult<UserBaseBean> result) {
                Log.e(TAG, "call: " + result.getCode());
                if (RetrofitUtil.isSuccessful(result)) {
                    return true;
                } else {
                    isSuccessLogin = false;
                    mView.showLoginFail(result.getMessage());
                    return false;
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<UserBaseBean>>() {
                    @Override
                    public void onCompleted() {
                        if (isSuccessLogin) {
                            mView.showLoginSuccess();
                        }
                        mView.dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.dismissLoadingDialog();
                        Log.e(TAG, "onError: " + e.getMessage());
                        mView.showLoginFail("登陆失败");
                    }

                    @Override
                    public void onNext(HttpResult<UserBaseBean> result) {
                        User.getInstance().setUserBaseBean(result.getData());
                        isSuccessLogin = true;
                    }
                });
    }
}
