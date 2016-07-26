package com.example.daidaijie.syllabusapplication.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.R;
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



}
