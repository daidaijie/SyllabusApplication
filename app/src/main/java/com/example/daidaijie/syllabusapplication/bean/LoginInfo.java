package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/8/28.
 */
public class LoginInfo implements Serializable{


    /**
     * success : true
     * msg : logon success
     * action : logout
     * pop : 0
     * userName : 13yjli3
     * location : http://1.1.1.2/ac_portal/proxy.html?type=logout
     */

    private boolean success;
    private String msg;
    private String action;
    private int pop;
    private String userName;
    private String location;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
