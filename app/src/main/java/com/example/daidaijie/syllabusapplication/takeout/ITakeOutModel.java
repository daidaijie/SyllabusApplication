package com.example.daidaijie.syllabusapplication.takeout;

import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import java.util.List;
import java.util.PriorityQueue;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface ITakeOutModel {


    /**
     * 多个的获取
     */
    Observable<List<TakeOutInfoBean>> getDataFromMemory();

    Observable<List<TakeOutInfoBean>> getDataFromDist();

    Observable<List<TakeOutInfoBean>> getDataFromNet();

    Observable<List<TakeOutInfoBean>> getData();


    /**
     * 单个的获取
     */
    Observable<TakeOutInfoBean> getItemFromMemory(String objectID);

    Observable<TakeOutInfoBean> getItemFromDist(String objectID);

    Observable<TakeOutInfoBean> getItemFromNet(String objectID);

    Observable<TakeOutInfoBean> getItem(String objectID);

    TakeOutInfoBean getTakeOutInfoBeanById(String objectID);

}
