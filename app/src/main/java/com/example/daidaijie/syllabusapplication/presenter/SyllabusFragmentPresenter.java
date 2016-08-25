package com.example.daidaijie.syllabusapplication.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.event.SyllabusEvent;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.GetUserBaseService;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SharedPreferencesUtil;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/7/26.
 */
public class SyllabusFragmentPresenter extends ISyllabusFragmentPresenter {

    private static final String TAG = "SyllabusFragment";

    private Syllabus mSyllabus;

    private int mWeek;

    public SyllabusFragmentPresenter() {
        // TODO: 2016/7/26 目前暂时不保存UserInfo和User
        reloadSyllabus();
    }

    @Override
    public void showSyllabus() {
        mView.showSyllabus(mSyllabus);
    }

    @Override
    public void updateSyllabus() {
        mView.setViewPagerEnable(false);
        UserInfoService service = RetrofitUtil.getDefault().create(UserInfoService.class);
        // TODO: 2016/7/25 目前没写登录界面所以只能这么坑的获取
        Log.d(TAG, "updateSyllabus: " + User.getInstance().mAccount);
        Log.d(TAG, "updateSyllabus: " + User.getInstance().mPassword);

        service.getUserInfo(
                User.getInstance().mAccount,
                User.getInstance().mPassword,
                "query",
                "2014-2015"
                , "1"
        ).subscribeOn(Schedulers.io())
                .flatMap(new Func1<UserInfo, Observable<Lesson>>() {
                    @Override
                    public Observable<Lesson> call(UserInfo userInfo) {
                        User.getInstance().setUserInfo(userInfo);
                        return Observable.from(userInfo.getClasses());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Lesson>() {

                    private int colorIndex = 0;

                    @Override
                    public void onStart() {
                        super.onStart();
                        mSyllabus = new Syllabus();
                    }

                    @Override
                    public void onCompleted() {
                        Log.d("", "onCompleted: ");
                        SharedPreferencesUtil.putString(SharedPreferencesUtil.SYLLABUS_GSON
                                , GsonUtil.getDefault().toJson(mSyllabus));
                        User.getInstance().mSyllabus = mSyllabus;
                        showSyllabus();
                        updateUserInfo();
                        mView.showSuccessBanner();
                        mView.hideLoading();
                        mView.rippleSyllabus();
                        mView.setViewPagerEnable(true);
                        EventBus.getDefault().post(new SyllabusEvent(mWeek));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        mView.hideLoading();
                        mView.showFailBannner();
                        mView.setViewPagerEnable(true);
                    }

                    @Override
                    public void onNext(Lesson lesson) {
                        Log.d(TAG, "onNext: " + lesson.getName());

                        //将lesson的时间格式化
                        lesson.convertDays();
                        lesson.setBgColor(Syllabus.bgColors[colorIndex++ % Syllabus.bgColors.length]);

                        //获取该课程上的节点上的时间列表
                        List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();
                        /*if (timeGirds.size() != 0) {
                            Log.d(TAG, "onNext: " + timeGirds.get(0).getTimeList());
                        }*/
                        for (int i = 0; i < timeGirds.size(); i++) {
                            Lesson.TimeGird timeGrid = timeGirds.get(i);
                            for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                                char x = timeGrid.getTimeList().charAt(j);
                                int time = Syllabus.chat2time(x);

                                SyllabusGrid syllabusGrid = mSyllabus.getSyllabusGrids()
                                        .get(timeGrid.getWeekDate())
                                        .get(time);

                                //将该课程添加到时间节点上去
                                syllabusGrid.getLessons().add(lesson);
                            }
                        }
                    }
                });

        GetUserBaseService userBaseService = RetrofitUtil.getDefault().create(GetUserBaseService.class);
        userBaseService.get_user("13yjli3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserBaseBean>() {
                    @Override
                    public void call(UserBaseBean userBaseBean) {
                        User.getInstance().setUserBaseBean(userBaseBean);
                    }
                });
    }

    @Override
    public void updateUserInfo() {
        UserInfo mUserInfo = User.getInstance().getUserInfo();
        if (mUserInfo != null) {
            mView.setHeadImageView(Uri.parse(mUserInfo.getAvatar()));
            mView.setNickName(mUserInfo.getNickname());
        }
    }

    @Override
    public void reloadSyllabus() {
        String syllabusJsonString = SharedPreferencesUtil
                .getString(SharedPreferencesUtil.SYLLABUS_GSON);
        mSyllabus = GsonUtil.getDefault().fromJson(syllabusJsonString, Syllabus.class);
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }
}
