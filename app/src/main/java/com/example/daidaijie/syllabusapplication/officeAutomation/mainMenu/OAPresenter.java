package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.officeAutomation.IOAModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/17.
 */

public class OAPresenter implements OAContract.presenter {

    private IOAModel mIOAModel;
    private OAContract.view mView;

    private int mPosition;

    @Inject
    @PerFragment
    public OAPresenter(IOAModel IOAModel, OAContract.view view, int position) {
        mIOAModel = IOAModel;
        mView = view;
        mPosition = position;
    }

    @Override
    public void loadData() {
        mView.showRefresh(true);
        mIOAModel.getLibraryFromNet(mPosition)
                .subscribe(new OASubscriber());
    }

    @Override
    public void setRead(OABean oaBean, boolean isRead) {
        mIOAModel.setRead(oaBean, isRead);
    }

    @Override
    public void start() {
        mView.showRefresh(true);
        mIOAModel.getLibrary(mPosition)
                .subscribe(new OASubscriber());
    }

    class OASubscriber extends Subscriber<List<OABean>> {
        @Override
        public void onCompleted() {
            mView.showRefresh(false);
        }

        @Override
        public void onError(Throwable e) {
            LoggerUtil.printStack(e);
            mView.showFailMessage("获取失败");
            mView.showRefresh(false);
        }

        @Override
        public void onNext(List<OABean> oaBeen) {
            mView.showData(oaBeen);
        }
    }
}
