package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/10/20.
 */

public class LessonID extends RealmObject {
    long id;

    public LessonID() {
    }

    public LessonID(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonID lessonID = (LessonID) o;

        return id == lessonID.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
