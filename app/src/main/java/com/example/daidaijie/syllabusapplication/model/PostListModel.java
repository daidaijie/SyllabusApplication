package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.bean.PostListBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/9/13.
 */
public class PostListModel {

    public List<PostListBean> mPostListBeen;

    private static PostListModel ourInstance = new PostListModel();

    public static PostListModel getInstance() {
        return ourInstance;
    }

    private PostListModel() {

    }
}
