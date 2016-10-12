package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.stuLibrary.ISTULibraryModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/12.
 */

public class LibraryPresenter implements LibraryContract.presenter {

    private LibraryContract.view mView;

    private ISTULibraryModel mLibraryModel;

    private int mPosition;

    @Inject
    @PerFragment
    public LibraryPresenter(LibraryContract.view view, ISTULibraryModel libraryModel, int position) {
        mView = view;
        mLibraryModel = libraryModel;
        mPosition = position;
    }

    @Override
    public void loadData() {
        mView.showRefresh(true);
        mLibraryModel.getLibraryFromNet(mPosition)
                .subscribe(new libSubscriber());
    }

    @Override
    public void start() {
        mView.showRefresh(true);
        mLibraryModel.getLibrary(mPosition)
                .subscribe(new libSubscriber());
    }

    class libSubscriber extends Subscriber<List<LibraryBean>> {
        @Override
        public void onCompleted() {
            mView.showRefresh(false);
        }

        @Override
        public void onError(Throwable e) {
            mView.showFailMessage("获取失败");
            mView.showRefresh(false);
        }

        @Override
        public void onNext(List<LibraryBean> libraryBeen) {
            mView.showData(libraryBeen);
        }
    }
}
