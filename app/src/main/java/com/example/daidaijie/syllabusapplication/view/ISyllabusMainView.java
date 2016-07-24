package com.example.daidaijie.syllabusapplication.view;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by daidaijie on 2016/7/25.
 */
public interface ISyllabusMainView extends MvpView{

    //设置头像
    void setHeadImageView(Uri uri);

    //设置标题
    void setTitle(String title);

    //设置背景
    void setBackground(Bitmap bitmap);

    //设置成功获取数据显示后的Bannner
    void showSuccessBanner();

    //设置不成功获取数据显示后的Bannner
    void showFailBannner();

    //显示Swipe加载
    void showLoading();

    //隐藏Swipe加载
    void hideLoading();


}
