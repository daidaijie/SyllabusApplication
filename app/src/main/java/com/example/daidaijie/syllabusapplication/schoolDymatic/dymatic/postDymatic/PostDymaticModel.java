package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import android.support.annotation.Nullable;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.BmobPhoto;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.bean.PostActivityBean;
import com.example.daidaijie.syllabusapplication.bean.QiNiuImageInfo;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.PostActivityApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.ImageUploader;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.orhanobut.logger.Logger;

import org.joda.time.LocalDateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/31.
 */

public class PostDymaticModel implements IPostDymaticModel {

    private PostActivityApi mPostActivityApi;

    private List<String> mPhotoImgs;

    private IUserModel mIUserModel;

    LocalDateTime mStartTime;
    LocalDateTime mEndTime;

    public PostDymaticModel(PostActivityApi postActivityApi, IUserModel IUserModel) {
        mPostActivityApi = postActivityApi;
        mIUserModel = IUserModel;
        mPhotoImgs = new ArrayList<>();
        mStartTime = LocalDateTime.now();
        mEndTime = LocalDateTime.now();
    }

    @Override
    public LocalDateTime getStartTime() {
        return mStartTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        mStartTime = startTime;
    }

    @Override
    public String getStartTimeString() {
        return getTimeString(mStartTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return mEndTime;
    }

    @Override
    public String getEndTimeString() {
        return getTimeString(mEndTime);
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        mEndTime = endTime;
    }

    private String getTimeString(LocalDateTime localDateTime) {
        return String.format("%4d.%02d.%02d %02d:%02d", localDateTime.getYear(),
                localDateTime.getMonthOfYear(), localDateTime.getDayOfMonth(),
                localDateTime.getHourOfDay(), localDateTime.getMinuteOfHour());
    }

    @Override
    public List<String> getPhotoImgs() {
        return mPhotoImgs;
    }

    @Override
    public void postPhotoToBmob(final OnPostCallBack onPostCallBack) {
        if (mPhotoImgs.size() == 0) {
            onPostCallBack.onSuccess(null);
            return;
        }

        Observable.from(mPhotoImgs)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, File>() {
                    @Override
                    public File call(String s) {
                        return new File(s.substring("file://".length(), s.length()));
                    }
                })
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<File, Observable<File>>() {
                    @Override
                    public Observable<File> call(File file) {
                        return Compressor.getDefault(App.getContext())
                                .compressToFileAsObservable(file);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<File, Observable<QiNiuImageInfo>>() {
                    @Override
                    public Observable<QiNiuImageInfo> call(File file) {
                        return ImageUploader.getObservableAsQiNiu(file);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QiNiuImageInfo>() {
                    PhotoInfo photoInfo = new PhotoInfo();

                    @Override
                    public void onStart() {
                        super.onStart();
                        photoInfo.setPhoto_list(new ArrayList<PhotoInfo.PhotoListBean>());
                    }

                    @Override
                    public void onCompleted() {
                        String photoListJsonString = GsonUtil.getDefault()
                                .toJson(photoInfo);
                        onPostCallBack.onSuccess(photoListJsonString);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.printStack(e);
                        onPostCallBack.onFail("图片上传失败");
                    }

                    @Override
                    public void onNext(QiNiuImageInfo qiNiuImageInfo) {
                        PhotoInfo.PhotoListBean photoListBean = new PhotoInfo.PhotoListBean();
                        photoListBean.setSize_big(qiNiuImageInfo.getMsg());
                        photoListBean.setSize_small(qiNiuImageInfo.getMsg());
                        photoInfo.getPhoto_list().add(photoListBean);
                    }
                });
    }

    @Override
    public Observable<Void> pushContent(@Nullable String photoListJson,
                                        String msg, String source, String url, String locate, boolean hasTime) {

        PostActivityBean postActivityBean = new PostActivityBean();
        UserInfo mUserInfo = mIUserModel.getUserInfoNormal();
        postActivityBean.photo_list_json = photoListJson;
        postActivityBean.uid = mUserInfo.getUser_id();
        postActivityBean.token = mUserInfo.getToken();
        postActivityBean.content = url;
        postActivityBean.source = source;
        postActivityBean.description = msg;
        postActivityBean.activity_location = locate;

        Long startTime = null;
        Long endTime = null;
        if (hasTime) {
            startTime = mStartTime.toDate().getTime();
            endTime = mEndTime.toDate().getTime();

            if (endTime < startTime) {
                return Observable.error(new Throwable("结束时间不能少于开始时间"));
            }

            postActivityBean.activity_start_time = startTime / 1000;
            postActivityBean.activity_end_time = endTime / 1000;
        }


        return mPostActivityApi.post(postActivityBean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(HttpResult<Void> voidHttpResult) {
                        Logger.t("PostActivity").json(GsonUtil.getDefault().toJson(voidHttpResult));
                        if (RetrofitUtil.isSuccessful(voidHttpResult)
                                || voidHttpResult.getCode() == 201) {
                            return Observable.just(voidHttpResult.getData());
                        }
                        return Observable.error(new Throwable(voidHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());

    }
}
