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
    private String name;

    public String sticky;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
