package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.postContent;

import android.support.annotation.Nullable;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.bean.PostContent;
import com.example.daidaijie.syllabusapplication.bean.QiNiuImageInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.PushPostApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.ImageUploader;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class PostContentModel implements IPostContentModel {

    private List<String> mPhotoImgs;

    private IUserModel mIUserModel;

    PushPostApi pushPostApi;

    public PostContentModel(IUserModel IUserModel, PushPostApi pushPostApi) {
        mIUserModel = IUserModel;
        this.pushPostApi = pushPostApi;
        mPhotoImgs = new ArrayList<>();
    }

    @Override
    public List<String> getPhotoImgs() {
        return mPhotoImgs;
    }

    @Override
    public void postPhotoToBmob(final OnPostPhotoCallBack onPostPhotoCallBack) {
        if (mPhotoImgs.size() == 0) {
            onPostPhotoCallBack.onSuccess(null);
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
                        return ImageUploader.getObservableAsQiNiu( file);
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
                        onPostPhotoCallBack.onSuccess(photoListJsonString);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.printStack(e);
                        onPostPhotoCallBack.onFail("图片上传失败");
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
    public Observable<Void> pushContent(@Nullable String photoListJson, String content, String source) {
        PostContent postContent = new PostContent();
        postContent.content = content;
        postContent.token = mIUserModel.getUserInfoNormal().getToken();
        postContent.description = "None";
        postContent.post_type = PushPostApi.POST_TYPE_TOPIC;
        postContent.photo_list_json = photoListJson;
        postContent.source = source;
        postContent.uid = mIUserModel.getUserInfoNormal().getUser_id();

        return pushPostApi.post(postContent)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<Void>>() {
                    @Override
                    public Observable<Void> call(HttpResult<Void> voidHttpResult) {
                        if (voidHttpResult.getCode() == 201) {
                            return Observable.just(voidHttpResult.getData());
                        }
                        return Observable.error(new Throwable(voidHttpResult.getMessage()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

    }
}
