package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/9/17.
 */
public class CollectionInfo extends RealmObject {


    private List<CollectionBean> collection_ids;

    public List<CollectionBean> getCollection_ids() {
        return collection_ids;
    }

    public void setCollection_ids(List<CollectionBean> collection_ids) {
        this.collection_ids = collection_ids;
    }
}
