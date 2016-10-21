package com.example.daidaijie.syllabusapplication.event;

/**
 * Created by daidaijie on 2016/8/12.
 */
public class ToTopEvent {

    public boolean isShowSuccuess;

    public boolean isRefresh;

    public String msg;

    public ToTopEvent(boolean isRefresh) {
        this.isRefresh = isRefresh;
        this.isShowSuccuess = false;
    }

    public ToTopEvent(boolean isRefresh, String msg) {
        this.isShowSuccuess = true;
        this.isRefresh = isRefresh;
        this.msg = msg;
    }
}
