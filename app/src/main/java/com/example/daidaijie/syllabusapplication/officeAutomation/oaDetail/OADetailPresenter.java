package com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail;

import android.graphics.Bitmap;
import android.os.SystemClock;

import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OAFileBean;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SubPosition;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SuperPosition;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.officeAutomation.IOAModel;
import com.example.daidaijie.syllabusapplication.util.BitmapSaveUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/17.
 */

public class OADetailPresenter implements OADetailContract.presenter {

    IOAModel mIOAModel;

    OADetailContract.view mView;

    int mPosition;

    int mSubPosition;

    @Inject
    @PerActivity
    public OADetailPresenter(IOAModel IOAModel, OADetailContract.view view,
                             @SuperPosition int position, @SubPosition int subPosition) {
        mIOAModel = IOAModel;
        mView = view;
        mPosition = position;
        mSubPosition = subPosition;
    }

    @Override
    public void loadData() {
        OABean oaBean = mIOAModel.getOABean(mPosition, mSubPosition);
        mIOAModel.getOAFileListFromNet(oaBean.getID())
                .subscribe(new Subscriber<List<OAFileBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showFileLoadFailMessage("获取附件失败");
                    }

                    @Override
                    public void onNext(List<OAFileBean> oaFileBeen) {
                        mView.showOAFile(oaFileBeen);
                    }
                });
    }

    @Override
    public void screenShot() {
        Bitmap webViewScreen = mView.captureScreen();

        if (webViewScreen != null) {
            BitmapSaveUtil.saveFile(webViewScreen, SystemClock.currentThreadTimeMillis() + ".jpg", 80, new BitmapSaveUtil.OnSaveFileCallBack() {
                @Override
                public void onSuccess() {
                    mView.showSuccessMessage("截取成功");
                }

                @Override
                public void onFail(String msg) {
                    mView.showFailMessage(msg);
                }
            });
        } else {
            mView.showFailMessage("图片太大，无法截取");
        }
    }

    @Override
    public void start() {
        OABean oaBean = mIOAModel.getOABean(mPosition, mSubPosition);
        mView.showView(oaBean.getContent());
        if (oaBean.getACCESSORYCOUNT() > 0) {
            loadData();
        }
    }
}
