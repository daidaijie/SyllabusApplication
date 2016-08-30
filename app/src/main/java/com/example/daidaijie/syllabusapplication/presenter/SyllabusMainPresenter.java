package com.example.daidaijie.syllabusapplication.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.ExamActivity;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.ExamInfoService;
import com.example.daidaijie.syllabusapplication.service.UserInfoService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SharedPreferencesUtil;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import id.zelory.compressor.Compressor;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
    public void loadWallpaper(Context context) {
        // TODO: 2016/7/25 这里暂时没获取壁纸，先假装有壁纸

        Bitmap wallPaperBitmap = BitmapFactory.decodeResource(context.getResources()
                , R.drawable.background);
        mView.setBackground(wallPaperBitmap);
    }

    @Override
    public void setWallpaper(final Context context) {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropHeight(mView.getDevideHeight())
                .setCropWidth(mView.getDeviceWidth())
                .setEnablePreview(false)
                .setCropReplaceSource(false)//配置裁剪图片时是否替换原始图片，默认不替换
                .setForceCrop(true)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .build();

        GalleryFinal.openGallerySingle(200, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                Observable.just(resultList.get(0).getPhotoPath())
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<String, Observable<File>>() {
                            @Override
                            public Observable<File> call(String s) {
                                Compressor compressor = new Compressor.Builder(context)
                                        .setQuality(80)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .build();
                                return compressor.compressToFileAsObservable(new File(s));
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                mView.setBackground(BitmapFactory.decodeFile(file.toString()));
                            }
                        });
                        /*.map(new Func1<String, Bitmap>() {
                            @Override
                            public Bitmap call(String photoPath) {
                                return BitmapFactory.decodeFile(photoPath);
                            }
                        }).observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Action1<Bitmap>() {
                            @Override
                            public void call(Bitmap bitmap) {
                                mView.setBackground(bitmap);
                            }
                        });*/
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

}