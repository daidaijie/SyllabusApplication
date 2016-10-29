package com.example.daidaijie.syllabusapplication.syllabus.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.IConfigModel;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.event.SettingWeekEvent;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import id.zelory.compressor.Compressor;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/7/25.
 */
public class SyllabusMainPresenter implements SyllabusContract.presenter {

    private IUserModel mIUserModel;

    private ILoginModel mILoginModel;

    private IConfigModel mIConfigModel;

    private SyllabusContract.view mView;

    @Inject
    @PerActivity
    public SyllabusMainPresenter(@LoginUser IUserModel IUserModel,
                                 ILoginModel ILoginModel,
                                 IConfigModel configModel,
                                 SyllabusContract.view view) {
        mIUserModel = IUserModel;
        mILoginModel = ILoginModel;
        mIConfigModel = configModel;
        mView = view;
    }

    @Override
    public void loadUserInfo() {
        mIUserModel.getUserInfo()
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo userInfo) {
                        mView.showUserInfo(userInfo);
                    }
                });
    }

    @Override
    public void loadWallpaper() {
        String wallPaperName = mIConfigModel.getWallPaper();
        Bitmap wallPaperBitmap;
        if (!wallPaperName.isEmpty() && new File(wallPaperName).exists()) {
            wallPaperBitmap = BitmapFactory.decodeFile(wallPaperName);
        } else {
            wallPaperBitmap = BitmapFactory.decodeResource(App.getContext().getResources()
                    , R.drawable.background);
        }
        mView.setBackground(wallPaperBitmap);
    }

    @Override
    public void setWallpaper(int deviceWidth, int deviceHeight) {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropHeight(deviceHeight)
                .setCropWidth(deviceWidth)
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
                                mIConfigModel.setWallPaper(file.toString());
                                mView.setBackground(BitmapFactory.decodeFile(file.toString()));
                                mView.showSuccessMessage("设置壁纸成功");
                            }
                        });
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                mView.showFailMessage("设置失败");
            }
        });
    }

    @Override
    public void moveToNowWeek(long startTime) {
        LocalDate date = new LocalDate(startTime);
        Period period = new Period(date, LocalDate.now(), PeriodType.weeks());
        int week = period.getWeeks() + 1;
        if (week < 1) {
            week = 1;
        } else if (week > 16) {
            week = 16;
        }

        mView.moveToWeek(week);
    }

    @Override
    public void settingWeek(LocalDate date, int week) {
        date = date.plusDays(-(date.getDayOfWeek() % 7));
        date = date.plusWeeks(-(week - 1));
        Semester semester = mILoginModel.getCurrentSemester();
        semester.setStartWeekTime(date.toDate().getTime());
        mILoginModel.updateSemester(semester);
        moveToNowWeek(semester.getStartWeekTime());
    }

    @Override
    public void start() {
        loadUserInfo();
        Semester semester = mILoginModel.getCurrentSemester();
        mView.showSemester(semester);
        loadWallpaper();

        if (semester.getStartWeekTime() != 0) {
            moveToNowWeek(semester.getStartWeekTime());
        }
    }

}