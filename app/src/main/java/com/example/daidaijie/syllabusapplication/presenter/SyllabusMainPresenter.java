package com.example.daidaijie.syllabusapplication.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;

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

import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
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
            mView.setHeadImageView(Uri.parse(mUserInfo.getAvatar()));
            mView.setNickName(mUserInfo.getNickname());
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
    public void setWallpaper() {
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
                        .map(new Func1<String, Bitmap>() {
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
                        });
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    @Override
    public void getExamList(final Context context) {
        mView.showLoadingDialog();
        Retrofit retrofit = RetrofitUtil.getDefault();
        ExamInfoService examInfoService = retrofit.create(ExamInfoService.class);
        examInfoService.getExamInfo(
                "13yjli3", "O3o", "2015-2016", "0"
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExamInfo>() {
                    Intent mIntent = null;

                    @Override
                    public void onCompleted() {
                        mView.dismissLoadingDialog();
                        context.startActivity(mIntent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoadingDialog();
                        mView.showFailSnackbar("获取考试列表失败", "再次获取", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getExamList(context);
                            }
                        });
                    }

                    @Override
                    public void onNext(ExamInfo examInfo) {
                        mIntent = ExamActivity.getIntent(context, examInfo.getEXAMS());
                    }
                });
    }
}