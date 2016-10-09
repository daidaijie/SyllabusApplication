package com.example.daidaijie.syllabusapplication.takeout.searchDish;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.takeout.ITakeOutModel;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/10/9.
 */

public class SearchTakeOutPresenter implements SearchTakeOutContract.presenter {

    private String objectID;

    private SearchTakeOutContract.view mView;

    private ITakeOutModel mTakeOutModel;

    private TakeOutInfoBean mTakeOutInfoBean;

    @Inject
    @PerActivity
    public SearchTakeOutPresenter(SearchTakeOutContract.view view, ITakeOutModel takeOutModel, String objectID) {
        mView = view;
        mTakeOutModel = takeOutModel;
        this.objectID = objectID;
    }

    @Override
    public void start() {

    }

    @Override
    public void search(String keyWord) {

    }
}
