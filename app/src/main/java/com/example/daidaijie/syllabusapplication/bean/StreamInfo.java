package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/8/28.
 */
public class StreamInfo implements Serializable {

    private int type;

    public static final int TYPE_UN_CONNECT = 0;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_LOGOUT = 2;

    private static StreamInfo ourInstance = new StreamInfo();

    public static StreamInfo getInstance() {
        return ourInstance;
    }

    private StreamInfo() {
    }

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s : %s\n", "用户名", name));
        sb.append(String.format("%-8s : %s\n", "日流量额", allStream));
        sb.append(String.format("%-8s : %s\n", "当天流量", nowStream));
        sb.append(String.format("%-8s : %s\n", "过期时间", outTime));
        sb.append(String.format("%-8s : %s", "账号状态", state));
        return sb.toString();
    }

    public double getAllByte() {
        return convertStream(allStream);
    }

    public double getNowByte() {
        return convertStream(nowStream);
    }

    private double convertStream(String stream) {
        String tmpStream = stream;
        // TODO: 2016/9/3 不明觉厉的问题
        if (tmpStream == null) return 0.0;
        if (tmpStream.contains("G")) {
            return Double.parseDouble(tmpStream.replace("G", "")) * 1024 * 1024;
        }
        if (tmpStream.contains("M")) {
            return Double.parseDouble(tmpStream.replace("M", "")) * 1024;
        }
        return 0;
    }
}
