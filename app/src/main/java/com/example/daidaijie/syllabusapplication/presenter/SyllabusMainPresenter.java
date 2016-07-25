package com.example.daidaijie.syllabusapplication.presenter;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.SyllabusActivity;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SharedPreferencesUtil;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/7/25.
 */
public class SyllabusMainPresenter extends ISyllabusMainPresenter {

    public static final String TAG = "SyllabusMainPresenter";

    private UserInfo mUserInfo;

    private Syllabus mSyllabus;

    @Override
    public void setUserInfo() {
        mUserInfo = User.getInstance().getUserInfo();

        // TODO: 2016/7/25 一般到这里都有mUserInfo的了，但是现在还没写好。。。。所以只能加一步判断
        if (mUserInfo != null) {
            mView.setHeadImageView(Uri.parse(mUserInfo.getAvatar()));
            mView.setNickName(mUserInfo.getNickname());
        }
    }

    @Override
    public void loadWallpaper() {
        // TODO: 2016/7/25 这里暂时没获取壁纸，先假装有壁纸

        Bitmap wallPaperBitmap = BitmapFactory.decodeResource(mView.getActivityResources()
                , R.drawable.background);
        mView.setBackground(wallPaperBitmap);
    }


    @Override
    public void getSyllabus() {

        UserInfoService service = RetrofitUtil.getDefault().create(UserInfoService.class);
        // TODO: 2016/7/25 目前没写登录界面所以只能这么坑的获取
        service.getUserInfo(
                "13yjli3",
                "O3o",
                "query",
                "2013-2014"
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
                        //刷新用户信息
                        setUserInfo();
                        mView.showSuccessBanner();
                        mView.hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        mView.showFailBannner();
                    }

                    @Override
                    public void onNext(Lesson lesson) {
                        Log.d(TAG, "onNext: " + lesson.getName());

                        //将lesson的时间格式化
                        lesson.convertDays();
                        lesson.setBgColor(Syllabus.bgColors[colorIndex++ % Syllabus.bgColors.length]);

                        //获取该课程上的节点上的时间列表
                        List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();
                        if (timeGirds.size() != 0) {
                            Log.d(TAG, "onNext: " + timeGirds.get(0).getTimeList());
                        }

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
    }
}
