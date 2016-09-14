package com.example.daidaijie.syllabusapplication.bean;

import android.content.Context;

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

    public static boolean hasRead(OABean oaBean) {
        Realm realm = Realm.getDefaultInstance();

        OARead results = realm.where(OARead.class)
                .equalTo("id", oaBean.getID()).findFirst();
        if (results != null && results.isRead()) {
            realm.close();
            return true;
        } else {
            realm.close();
            return false;
        }
    }

    public static void save(OABean oaBean) {
        Realm createRealm = Realm.getDefaultInstance();
        createRealm.beginTransaction();
        OARead oaRead = createRealm.createObject(OARead.class);
        oaRead.setId(oaBean.getID());
        oaRead.setRead(true);
        createRealm.commitTransaction();
        createRealm.close();
    }
}
