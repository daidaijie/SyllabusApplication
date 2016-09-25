package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daidaijie on 2016/9/25.
 */

public class Dishes {

    /**
     * price : 8
     * dist : 咸菜肉片粥
     */

    private String price;

    @SerializedName("dist")
    private String nName;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }
}
