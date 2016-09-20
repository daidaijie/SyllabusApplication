package com.example.daidaijie.syllabusapplication.presenter;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.view.ISyllabusMainView;

import org.joda.time.LocalDate;

/**
 * Created by daidaijie on 2016/7/25.
 */
public abstract class ISyllabusMainPresenter extends BasePresenter<ISyllabusMainView> {

    //获取用户信息
    public abstract void setUserInfo();

    //加载背景
    public abstract void loadWallpaper(Context context);

    //更改背景
    public abstract void setWallpaper(Context context);

    //选择周数
    public abstract void  settingWeek(LocalDate date, int week);
}
