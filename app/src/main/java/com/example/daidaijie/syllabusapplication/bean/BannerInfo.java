package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/7.
 */
public class BannerInfo {

    /**
     * notifications : [{"id":0,"link":"http://119.29.95.245:8000/youzhiyouheng.jpg","description":"有志有恒咯","url":"http://119.29.95.245:8000/youzhiyouheng.jpg"}]
     * timestamp : 1468899569
     */

    private LatestBean latest;

    public LatestBean getLatest() {
        return latest;
    }

    public void setLatest(LatestBean latest) {
        this.latest = latest;
    }

    public static class LatestBean {
        private int timestamp;
        /**
         * id : 0
         * link : http://119.29.95.245:8000/youzhiyouheng.jpg
         * description : 有志有恒咯
         * url : http://119.29.95.245:8000/youzhiyouheng.jpg
         */

        @SerializedName("notifications")
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
}
