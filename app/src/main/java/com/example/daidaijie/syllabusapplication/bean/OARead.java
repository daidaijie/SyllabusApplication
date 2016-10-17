package com.example.daidaijie.syllabusapplication.bean;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/9/13.
 */
public class OARead extends RealmObject {

    @PrimaryKey
    private long id;

    private boolean isRead;

    public OARead() {
    }

    public OARead(long id, boolean isRead) {
        this.id = id;
        this.isRead = isRead;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
