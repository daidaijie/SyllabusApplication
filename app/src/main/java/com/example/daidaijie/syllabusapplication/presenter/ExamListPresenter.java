package com.example.daidaijie.syllabusapplication.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.daidaijie.syllabusapplication.activity.ExamActivity;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;
import com.example.daidaijie.syllabusapplication.service.ExamInfoService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/8/4.
 */
public class ExamListPresenter extends IExamListPresenter {
    @Override
    public void getExamList(final Context context) {
/*
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
*/
    }
}
