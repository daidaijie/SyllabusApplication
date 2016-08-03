package com.example.daidaijie.syllabusapplication.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

/**
 * Created by daidaijie on 2016/7/25.
 */
public interface ISyllabusMainView extends MvpView, IUserInfoView ,ILoadingView{

    //设置标题
    void setToolBarTitle(String title);

    //设置背景
    void setBackground(Bitmap bitmap);

    //设置ViewPager滑动
    void setViewPagerEnable(boolean enable);

    //获取设备宽度
    int getDeviceWidth();

    //获取设备高度
    int getDevideHeight();

    //展示获取成功的Snackbar
    void showSuccessSnackbar(String info);

    //展示获取失败后的Snackbar
    void showFailSnackbar(String info, String again, View.OnClickListener listener);
}
