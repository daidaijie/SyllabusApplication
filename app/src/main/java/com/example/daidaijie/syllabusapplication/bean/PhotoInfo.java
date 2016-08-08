package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PhotoInfo {

    /**
     * size_big : http://bmob-cdn-5361.b0.upaiyun.com/2016/08/05/87afbb2890844596a676b66cd42bc73d.jpg
     * size_small : http://bmob-cdn-5361.b0.upaiyun.com/2016/08/05/87afbb2890844596a676b66cd42bc73d.jpg
     */

    private List<PhotoListBean> photo_list;

    public List<PhotoListBean> getPhoto_list() {
        return photo_list;
    }

    public void setPhoto_list(List<PhotoListBean> photo_list) {
        this.photo_list = photo_list;
    }

    public static class PhotoListBean {
        private String size_big;
        private String size_small;

        public String getSize_big() {
            return size_big;
        }

        public void setSize_big(String size_big) {
            this.size_big = size_big;
        }

        public String getSize_small() {
            return size_small;
        }

        public void setSize_small(String size_small) {
            this.size_small = size_small;
        }
    }
}
