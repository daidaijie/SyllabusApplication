package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/8/28.
 */
public class StreamInfo implements Serializable{

    private String name;

    private String allStream;

    private String nowStream;

    private String outTime;

    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllStream() {
        return allStream;
    }

    public void setAllStream(String allStream) {
        this.allStream = allStream;
    }

    public String getNowStream() {
        return nowStream;
    }

    public void setNowStream(String nowStream) {
        this.nowStream = nowStream;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "StreamInfo{" +
                "name='" + name + '\'' +
                ", allStream='" + allStream + '\'' +
                ", nowStream='" + nowStream + '\'' +
                ", outTime='" + outTime + '\'' +
                ", state='" + state + '\'' +
                ", allByte='" + getAllByte() + '\'' +
                ", nowByte='" + getNowByte() + '\'' +
                '}';
    }

    public double getAllByte() {
        return convertStream(allStream);
    }

    public double getNowByte() {
        return convertStream(nowStream);
    }

    private double convertStream(String stream) {
        String tmpStream = stream;
        if (tmpStream.contains("G")) {
            return Double.parseDouble(tmpStream.replace("G", "")) * 1024 * 1024;
        }
        if (tmpStream.contains("M")) {
            return Double.parseDouble(tmpStream.replace("M", "")) * 1024;
        }
        return 0;
    }
}
