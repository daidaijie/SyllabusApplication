package com.example.daidaijie.syllabusapplication.main;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Banner;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/8/8.
 */
public class BannerManager {

    private Context mContext;

    public static final String sSaveName = "BannerManager";

    public static final String sSaveBanners = "mBanners";

    public static final String sSaveTimestamp = "mTimestamp";

    public List<Banner> mBanners;

    public int mTimestamp;

    private static BannerManager ourInstance = new BannerManager();

    public static BannerManager getInstance() {
        return ourInstance;
    }

    private BannerManager() {
        mContext = App.getContext();
        SharedPreferences preferences = mContext.getSharedPreferences("BannerManager"
                , Context.MODE_PRIVATE);
        String bannersString = preferences.getString(sSaveBanners, "");
        if (bannersString.isEmpty()) {
            mBanners = new ArrayList<>();
        } else {
            mBanners = GsonUtil.getDefault().fromJson(bannersString
                    , new TypeToken<List<Banner>>() {
                    }.getType());
        }
        mTimestamp = preferences.getInt(sSaveTimestamp, 0);
    }


    public List<Banner> getBanners() {
        return mBanners;
    }

    public void setBanners(List<Banner> banners) {
        SharedPreferences preferences = mContext.getSharedPreferences("BannerManager"
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String bannerString = GsonUtil.getDefault().toJson(banners);
        editor.putString(sSaveBanners, bannerString);
        editor.commit();
        mBanners = banners;
    }

    public int getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(int timestamp) {
        SharedPreferences preferences = mContext.getSharedPreferences("BannerManager"
                , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(sSaveTimestamp, timestamp);
        editor.commit();
        mTimestamp = timestamp;
    }
}
