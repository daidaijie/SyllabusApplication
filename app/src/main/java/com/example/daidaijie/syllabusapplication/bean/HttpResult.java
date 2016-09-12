package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/9/12.
 */
public class HttpResult<T> {

    private T data;

    private int code;
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
