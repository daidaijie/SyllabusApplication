package com.example.daidaijie.syllabusapplication.event;

/**
 * Created by daidaijie on 2016/11/3.
 */

public class InternetOpenEvent {
    boolean isOpen;

    public InternetOpenEvent(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
