package com.example.daidaijie.syllabusapplication.main;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.BannerBeen;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface IBannerModel extends IBaseModel {

    Observable<BannerBeen> getBannerFromMemory();

    Observable<BannerBeen> getBannerFromDisk();

    Observable<BannerBeen> getBannerFromNet();

    Observable<BannerBeen> getBannerFromCache();


    void getBannerNormal(OnGetSuccessCallBack<BannerBeen> onGetCallBack);
}
