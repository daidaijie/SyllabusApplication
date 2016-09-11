package com.example.daidaijie.syllabusapplication.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
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
        final UserInfoService service = RetrofitUtil.getDefault().create(UserInfoService.class);
        GetUserBaseService userBaseService = RetrofitUtil.getDefault().create(GetUserBaseService.class);

        userBaseService.get_user(User.getInstance().getAccount())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<UserBaseBean, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(UserBaseBean userBaseBean) {
                        User.getInstance().setUserBaseBean(userBaseBean);
                        return service.getUserInfo(
                                User.getInstance().getAccount(),
                                User.getInstance().getPassword(),
                                "query",
                                "2016-2017"
                                , "1"
                        );
                    }
                })
                .flatMap(new Func1<UserInfo, Observable<Lesson>>() {
                    @Override
                    public Observable<Lesson> call(UserInfo userInfo) {
                        if (userInfo.getToken() != null && !userInfo.getToken().isEmpty()) {
                            User.getInstance().setUserInfo(userInfo);
                        }
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
                        User.getInstance().setSyllabus(mSyllabus);
                        LessonModel.getInstance().save();
                        showSyllabus();
                        updateUserInfo();
                        mView.showSuccessBanner();
                        mView.hideLoading();
                        mView.setViewPagerEnable(true);
                        EventBus.getDefault().post(new SyllabusEvent(mWeek));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showFailBannner();
                        mView.setViewPagerEnable(true);
                    }

                    @Override
                    public void onNext(Lesson lesson) {

                        //将lesson的时间格式化
                        lesson.convertDays();
                        lesson.setBgColor(Syllabus.bgColors[colorIndex++ % Syllabus.bgColors.length]);

                        //获取该课程上的节点上的时间列表
                        List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();
                        /*if (timeGirds.size() != 0) {
                            Log.d(TAG, "onNext: " + timeGirds.get(0).getTimeList());
                        }*/
                        //把该课程添加到课程管理去
                        LessonModel.getInstance().addLesson(lesson);

                        Log.d(TAG, "onNext: " + lesson.getName());
                        for (int i = 0; i < timeGirds.size(); i++) {
                            Lesson.TimeGird timeGrid = timeGirds.get(i);
                            for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                                char x = timeGrid.getTimeList().charAt(j);
                                int time = Syllabus.chat2time(x);

                                SyllabusGrid syllabusGrid = mSyllabus.getSyllabusGrids()
                                        .get(timeGrid.getWeekDate())
                                        .get(time);

                                //将该课程添加到时间节点上去
                                syllabusGrid.getLessons().add(lesson.getIntID());
                            }
                        }

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
        mSyllabus = User.getInstance().getSyllabus();
    }

    public int getWeek() {
        return mWeek;
    }

    public void setWeek(int week) {
        mWeek = week;
    }
}
