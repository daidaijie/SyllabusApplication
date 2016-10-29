package com.example.daidaijie.syllabusapplication.schoolDymatic.personal;

import android.graphics.Bitmap;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.event.UpdateUserInfoEvent;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import id.zelory.compressor.Compressor;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class PersonalPresenter implements PersonalContract.presenter {

    IUserModel mIUserModel;

    IPersonalModel mIPersonalModel;

    PersonalContract.view mView;

    String newImageFileName;

    @Inject
    @PerActivity
    public PersonalPresenter(@LoginUser IUserModel IUserModel,
                             PersonalContract.view view,
                             IPersonalModel personalModel) {
        mIUserModel = IUserModel;
        mView = view;
        mIPersonalModel = personalModel;
    }

    @Override
    public void start() {
        UserBaseBean userBaseBean = mIUserModel.getUserBaseBeanNormal();
        mView.showUserBase(userBaseBean);
        newImageFileName = null;
    }

    @Override
    public void selectHeadImg() {
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
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
                                Compressor compressor = new Compressor.Builder(App.getContext())
                                        .setQuality(80)
                                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                        .build();
                                return compressor.compressToFileAsObservable(new File(s));
                            }
                        }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<File>() {
                            @Override
                            public void call(File file) {
                                mView.showHead(file.toURI().toString());
                                newImageFileName = file.toString();
                            }
                        });
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                mView.showFailMessage("选择失败");
            }
        });
    }

    @Override
    public void pushData(final String nickName, final String profile) {
        mIPersonalModel.postPhotoToBmob(newImageFileName, new IPersonalModel.OnPostPhotoCallBack() {
            @Override
            public void onSuccess(final String photoJson) {
                mIPersonalModel.updateUserInfo(nickName, profile, photoJson)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                UserInfo userInfo = mIUserModel.getUserInfoNormal();
                                UserBaseBean baseBean = mIUserModel.getUserBaseBeanNormal();
                                userInfo.setNickname(nickName);
                                baseBean.setNickname(nickName);
                                baseBean.setProfile(profile);
                                if (photoJson != null && !photoJson.isEmpty()) {
                                    userInfo.setAvatar(photoJson);
                                    baseBean.setImage(photoJson);
                                }
                                mIUserModel.updateUserInfo(userInfo);
                                mIUserModel.updateUserBaseBean(baseBean);

                                mView.showSuccessMessage("更新成功");

                                EventBus.getDefault().post(new UpdateUserInfoEvent());
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e.getMessage() == null) {
                                    mView.showFailMessage("更新失败");
                                } else {
                                    mView.showFailMessage(e.getMessage());
                                }
                            }

                            @Override
                            public void onNext(Void aVoid) {

                            }
                        });
            }

            @Override
            public void onFail(String msg) {
                mView.showFailMessage(msg);
            }
        });
    }


}
