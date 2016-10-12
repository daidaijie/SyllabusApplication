package com.example.daidaijie.syllabusapplication.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.event.SettingWeekEvent;
import com.example.daidaijie.syllabusapplication.event.SyllabusEvent;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.retrofitApi.GetUserBaseApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.BitmapSaveUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

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
        final GetUserBaseApi userBaseService = RetrofitUtil.getDefault().create(GetUserBaseApi.class);

        final Semester mCurrentSemester = User.getInstance().getCurrentSemester();

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

                if (User.getInstance().getSyllabus(mCurrentSemester) == null) {
                    mSyllabus = new Syllabus();
                } else {
                    mSyllabus = User.getInstance().getSyllabus(mCurrentSemester);
                }
                mSyllabus.convertSyllabus(userInfo.getClasses(), mCurrentSemester);

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
                            if (User.getInstance().getCurrentSemester().getStartWeekTime() == 0) {
                                EventBus.getDefault().post(new SettingWeekEvent());
                            }
                        }
                        mView.hideLoading();
                        mView.setViewPagerEnable(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
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

    @Override
    public void saveSyllabus(Bitmap syllabusBitmap, Bitmap timeBitmap, Bitmap dayBitmap) {
        String wallPaperName = User.getInstance().getWallPaperFileName();
        Bitmap wallPaperBitmap;

        if (!wallPaperName.isEmpty() && new File(wallPaperName).exists()) {
            wallPaperBitmap = BitmapFactory.decodeFile(wallPaperName);
        } else {
            wallPaperBitmap = BitmapFactory.decodeResource(App.getContext().getResources()
                    , R.drawable.background);
        }

        Matrix matrix = new Matrix();
        float scale;
        float scaleHeight = (dayBitmap.getHeight() + syllabusBitmap.getHeight()) * 1.0f
                / wallPaperBitmap.getHeight();
        float scaleWidht = (timeBitmap.getWidth() + syllabusBitmap.getWidth()) * 1.0f
                / wallPaperBitmap.getWidth();
        scale = Math.max(scaleHeight, scaleWidht);

        matrix.postScale(scale, scale);

        Bitmap result = Bitmap.createBitmap(timeBitmap.getWidth() + syllabusBitmap.getWidth(),
                syllabusBitmap.getHeight() + dayBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);

        Bitmap resizeBmp = Bitmap.createBitmap(wallPaperBitmap, 0, 0, wallPaperBitmap.getWidth(),
                wallPaperBitmap.getHeight(), matrix, true);
        canvas.drawBitmap(resizeBmp, 0, 0, null);

        canvas.drawBitmap(dayBitmap, 0, 0, null);
        canvas.drawBitmap(timeBitmap, 0, dayBitmap.getHeight(), null);
        canvas.drawBitmap(syllabusBitmap, timeBitmap.getWidth(), dayBitmap.getHeight(), null);


        BitmapSaveUtil.saveFile(
                result, "Syllabus" + System.currentTimeMillis() + ".jpg", "STUOA", 100);

        wallPaperBitmap.recycle();
        syllabusBitmap.recycle();
        dayBitmap.recycle();
        timeBitmap.recycle();
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }
}
