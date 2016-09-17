package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.bean.CollectionInfo;

/**
 * Created by daidaijie on 2016/9/17.
 */
public class SyllabusCollectionModel {

    private CollectionInfo mCollectionInfo;

    public CollectionInfo getCollectionInfo() {
        return mCollectionInfo;
    }

    public void setCollectionInfo(CollectionInfo collectionInfo) {
        mCollectionInfo = collectionInfo;
    }

    private static SyllabusCollectionModel ourInstance = new SyllabusCollectionModel();

    public static SyllabusCollectionModel getInstance() {
        return ourInstance;
    }

    private SyllabusCollectionModel() {
    }
}
