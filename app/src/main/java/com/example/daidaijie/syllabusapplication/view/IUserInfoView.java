package com.example.daidaijie.syllabusapplication.view;

import android.net.Uri;

/**
 * Created by daidaijie on 2016/7/26.
 */
public interface IUserInfoView {
    //设置头像
    void setHeadImageView(Uri uri);

    //设置昵称
    void setNickName(String nickName);
}
