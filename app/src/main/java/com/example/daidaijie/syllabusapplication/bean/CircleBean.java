package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class CircleBean {


    /**
     * content : 如果汕大课程表是个网页app
     * user : {"account":"15jhzhao3","nickname":"HoraceChiu","image":"http://file.bmob.cn/M03/EF/4D/oYYBAFebXOKAFp0WAACB0FUPsuY523.jpg","id":2184}
     * description :
     * comments : [{"uid":1,"id":329}]
     * source : iOS
     * photo_list_json : null
     * post_type : 0
     * id : 503
     * post_time : 2016-08-07 02:08:24
     * thumb_ups : [{"uid":2,"id":1158},{"uid":9,"id":1160}]
     */

    private List<PostListBean> post_list;

    public List<PostListBean> getPost_list() {
        return post_list;
    }

    public void setPost_list(List<PostListBean> post_list) {
        this.post_list = post_list;
    }


}
