package com.example.daidaijie.syllabusapplication.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
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

    private boolean isSuccess;

    public SyllabusFragmentPresenter() {
        reloadSyllabus();
    }

    @Override
    public void showSyllabus() {
        mView.showSyllabus(mSyllabus);
    }

    @Override
    public void updateSyllabus() {
        isSuccess = false;
        mView.setViewPagerEnable(false);
        final UserInfoService service = RetrofitUtil.getDefault().create(UserInfoService.class);
        GetUserBaseService userBaseService = RetrofitUtil.getDefault().create(GetUserBaseService.class);

        final Semester semester = User.getInstance().getCurrentSemester();

        userBaseService.get_user(User.getInstance().getAccount())
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HttpResult<UserBaseBean>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<UserBaseBean> result) {
                        if (RetrofitUtil.isSuccessful(result)) {
                            return true;
                        } else {
                            mView.showFailBanner(result.getMessage());
                            return false;
                        }
                    }
                })
                .flatMap(new Func1<HttpResult<UserBaseBean>, Observable<HttpResult<UserInfo>>>() {
                    @Override
                    public Observable<HttpResult<UserInfo>> call(HttpResult<UserBaseBean> result) {
                        UserBaseBean userBaseBean = result.getData();
                        User.getInstance().setUserBaseBean(userBaseBean);
                        return service.getUserInfo(
                                User.getInstance().getAccount(),
                                User.getInstance().getPassword(),
                                "query",
                                semester.getYearString()
                                , semester.getSeason() + ""
                        );
                    }
                })
                .filter(new Func1<HttpResult<UserInfo>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<UserInfo> userInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(userInfoHttpResult)) {
                            return true;
                        } else {
                            mView.showFailBanner(userInfoHttpResult.getMessage());
                            return false;
                        }
                    }
                })
                .flatMap(new Func1<HttpResult<UserInfo>, Observable<Lesson>>() {
                    @Override
                    public Observable<Lesson> call(HttpResult<UserInfo> result) {
                        UserInfo userInfo = result.getData();
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
                        if (isSuccess) {
                            User.getInstance().setSyllabus(User.getInstance().getCurrentSemester(), mSyllabus);
                            LessonModel.getInstance().save();
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
                        Log.d(TAG, "onError: " + e.getMessage());
                        e.printStackTrace();
                        mView.hideLoading();
                        mView.showFailBanner("同步失败");
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
                        isSuccess = true;

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
