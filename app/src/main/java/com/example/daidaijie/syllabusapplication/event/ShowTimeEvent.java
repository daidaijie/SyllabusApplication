package com.example.daidaijie.syllabusapplication.event;

/**
 * Created by daidaijie on 2016/7/20.
 */
public class ShowTimeEvent {
    public int messageWeek;

    public boolean isHide;

    public ShowTimeEvent(int messageWeek) {
        this.messageWeek = messageWeek;
        isHide = false;
    }

    public ShowTimeEvent(int messageWeek, boolean isHide) {
        this.messageWeek = messageWeek;
        this.isHide = isHide;
    }
}
