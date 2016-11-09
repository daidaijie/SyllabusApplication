package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.postContent.IPostContentModel;

import org.joda.time.LocalDateTime;

import java.util.List;

import javax.inject.Inject;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/31.
 */

public class PostDymaticPresenter implements PostDymaticContract.presenter {

    IPostDymaticModel mIPostDymaticModel;

    PostDymaticContract.view mView;

    @Inject
    @PerActivity
    public PostDymaticPresenter(IPostDymaticModel IPostDymaticModel, PostDymaticContract.view view) {
        mIPostDymaticModel = IPostDymaticModel;
        mView = view;
    }

    @Override
    public void selectPhoto() {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(PostDymaticActivity.MAX_IMG_NUM - mIPostDymaticModel.getPhotoImgs().size())
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setEnablePreview(false)
                .setCropReplaceSource(false)//配置裁剪图片时是否替换原始图片，默认不替换
                .setForceCrop(false)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .build();

        GalleryFinal.openGalleryMuti(200, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                for (PhotoInfo photoInfo : resultList) {
                    mIPostDymaticModel.getPhotoImgs().add("file://" + photoInfo.getPhotoPath());
                    mView.setUpFlow(mIPostDymaticModel.getPhotoImgs());
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                mView.showFailMessage(errorMsg);
            }
        });

    }

    @Override
    public void unSelectPhoto(int position) {
        mIPostDymaticModel.getPhotoImgs().remove(position);
        mView.setUpFlow(mIPostDymaticModel.getPhotoImgs());
    }

    @Override
    public boolean isNonePhoto() {
        return mIPostDymaticModel.getPhotoImgs().size() == 0;
    }

    @Override
    public void postContent(final String msg, final String source, final String url, final String locate, final boolean hasTime) {
        if (isNonePhoto() && msg.isEmpty()) {
            mView.showWarningMessage("请输入文字或者选择图片!");
            return;
        }

        if (source.trim().isEmpty()) {
            mView.showWarningMessage("请输入发布源!");
            return;
        }

        mView.showLoading(true);
        mIPostDymaticModel.postPhotoToBmob(new IPostDymaticModel.OnPostCallBack() {
            @Override
            public void onSuccess(String photoJson) {
                mIPostDymaticModel.pushContent(photoJson, msg, source, url, locate, hasTime)
                        .subscribe(new Subscriber<Void>() {
                            @Override
                            public void onCompleted() {
                                mView.showLoading(false);
                                mView.onPostFinishCallBack();
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.showLoading(false);
                                if (e.getMessage() == null) {
                                    mView.showFailMessage("发送失败");
                                } else {
                                    if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                        mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                                    } else {
                                        mView.showFailMessage(e.getMessage().toUpperCase());
                                    }
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
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void setTime(boolean isStart, int year, int month, int day, int hour, int minute) {
        LocalDateTime localDateTime = new LocalDateTime(year, month, day, hour, minute);
        if (isStart) {
            mIPostDymaticModel.setStartTime(localDateTime);
            mView.setStartTimeString(mIPostDymaticModel.getStartTimeString());
        } else {
            mIPostDymaticModel.setEndTime(localDateTime);
            mView.setEndTimeString(mIPostDymaticModel.getEndTimeString());
        }
    }

    @Override
    public void selectTime(boolean isStart) {
        LocalDateTime localDateTime = null;
        if (isStart) {
            localDateTime = mIPostDymaticModel.getStartTime();
        } else {
            localDateTime = mIPostDymaticModel.getEndTime();
        }
        mView.selectTime(isStart, localDateTime.getYear(), localDateTime.getMonthOfYear(),
                localDateTime.getDayOfMonth(), localDateTime.getHourOfDay(), localDateTime.getMinuteOfHour());
    }


    @Override
    public void start() {
        mView.setUpFlow(mIPostDymaticModel.getPhotoImgs());

        mView.setStartTimeString(mIPostDymaticModel.getStartTimeString());
        mView.setEndTimeString(mIPostDymaticModel.getEndTimeString());
    }
}
