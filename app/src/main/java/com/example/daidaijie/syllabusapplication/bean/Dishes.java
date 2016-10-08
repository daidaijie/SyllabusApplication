package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/9/25.
 */

public class Dishes extends RealmObject implements Serializable {

    /**
     * price : 8
     * dist : 咸菜肉片粥
     */

    @Expose
    private String price;

    @SerializedName("dist")
    @Expose
    private String name;

    public String sticky;

    public int subMenuPos;

    public int mPos;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dishes dishes = (Dishes) o;

        if (price != null ? !price.equals(dishes.price) : dishes.price != null) return false;
        return name != null ? name.equals(dishes.name) : dishes.name == null;

    }

    @Override
    public int hashCode() {
        int result = price != null ? price.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
