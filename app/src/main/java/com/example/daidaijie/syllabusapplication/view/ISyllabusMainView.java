package com.example.daidaijie.syllabusapplication.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by daidaijie on 2016/7/25.
 */
public interface ISyllabusMainView extends MvpView, IUserInfoView {

    //设置标题
    void setToolBarTitle(String title);

    //设置背景
    void setBackground(Bitmap bitmap);

    //设置ViewPager滑动
    void setViewPagerEnable(boolean enable);

    // TODO: 2016/7/25 这个记得在后面删掉啊，哭死了
    Resources getActivityResources();


}
