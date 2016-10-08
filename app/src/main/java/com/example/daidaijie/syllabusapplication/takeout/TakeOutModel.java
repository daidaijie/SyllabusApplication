package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.service.TakeOutInfoApi;
import com.example.daidaijie.syllabusapplication.takeout.mainMenu.ITakeOutModel;

import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/8.
 */

public class TakeOutModel implements ITakeOutModel {

    private Realm mRealm;
    private TakeOutInfoApi mTakeOutInfoApi;

    public TakeOutModel(Realm realm, TakeOutInfoApi takeOutInfoApi) {
        mRealm = realm;
        mTakeOutInfoApi = takeOutInfoApi;
    }

    @Override
    public void loadDataFromNet(final OnLoadListener onLoadListener) {
        /**
         * 获取除了详细菜单之外的信息
         */
        mTakeOutInfoApi.getTokenResult("name,long_number,short_number,condition")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BmobResult<TakeOutInfoBean>>() {

                    List<TakeOutInfoBean> takeOutInfoBeen;

                    @Override
                    public void onCompleted() {
                        onLoadListener.onLoadSuccess(takeOutInfoBeen);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadListener.onLoadFail();
                    }

                    @Override
                    public void onNext(BmobResult<TakeOutInfoBean> bmobResult) {
                        takeOutInfoBeen = bmobResult.getResults();
                        /*for (TakeOutInfoBean takeOutInfoBean : takeOutInfoBeen) {
                            TakeOutInfoBean hasBean = TakeOutManager.getInstance().getBeanByID(takeOutInfoBean.getObjectId());
                            if (hasBean != null) {
                                takeOutInfoBean.setMenu(hasBean.getMenu());
                                takeOutInfoBean.setTakeOutSubMenus(hasBean.getTakeOutSubMenus());
                            }
                        }*/
                    }
                });
    }

    @Override
    public void loadDataFromDist(OnLoadListener onLoadListener) {

    }
}
