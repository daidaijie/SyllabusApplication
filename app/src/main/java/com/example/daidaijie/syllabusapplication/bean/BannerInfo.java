package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by daidaijie on 2016/8/7.
 */
public class BannerInfo {

    /**
     * notifications : [{"id":0,"link":"http://119.29.95.245:8000/youzhiyouheng.jpg","description":"有志有恒咯","url":"http://119.29.95.245:8000/youzhiyouheng.jpg"}]
     * timestamp : 1468899569
     */

    @SerializedName("latest")
    private BannerBeen mBannerBeen;

    public BannerBeen getBannerBeen() {
        return mBannerBeen;
    }

    public void setBannerBeen(BannerBeen bannerBeen) {
        this.mBannerBeen = bannerBeen;
    }
}
