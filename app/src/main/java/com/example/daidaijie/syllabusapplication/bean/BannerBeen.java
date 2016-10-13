package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by daidaijie on 2016/10/13.
 */

public class BannerBeen extends RealmObject {
    private int timestamp;
    /**
     * id : 0
     * link : http://119.29.95.245:8000/youzhiyouheng.jpg
     * description : 有志有恒咯
     * url : http://119.29.95.245:8000/youzhiyouheng.jpg
     */

    private String mBannersString;

    public String getBannersString() {
        return mBannersString;
    }

    public void convertBannersString() {
        mBannersString = GsonUtil.getDefault().toJson(mBanners);
    }

    public void convertBanners() {
        mBanners = GsonUtil.getDefault().fromJson(mBannersString,
                new TypeToken<List<Banner>>() {
                }.getType());
    }

    @SerializedName("notifications")
    @Ignore
    private List<Banner> mBanners;

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public List<Banner> getBanners() {
        return mBanners;
    }

    public void setBanners(List<Banner> banners) {
        mBanners = banners;
    }
}
