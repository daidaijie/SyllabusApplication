package com.example.daidaijie.syllabusapplication.event;

/**
 * Created by daidaijie on 2016/8/12.
 */
public class ToTopEvent {

    public boolean isShowSuccuess;

    public boolean isRefresh;

    public ToTopEvent(boolean isShowSuccuess, boolean isRefresh) {
        this.isShowSuccuess = isShowSuccuess;
        this.isRefresh = isRefresh;
    }
}
