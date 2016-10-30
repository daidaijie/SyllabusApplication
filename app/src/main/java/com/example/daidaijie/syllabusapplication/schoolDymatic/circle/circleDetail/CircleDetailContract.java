package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail;

import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface CircleDetailContract {

    interface presenter extends BasePresenter, CirclesAdapter.OnLikeCallBack {
        void loadData();

        void showComment(int position);

        void postComment(int postID, String toAccount, String msg);
    }

    interface view extends BaseView<presenter> {

        void showHeaderInfo(PostListBean postListBean);

        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void showData(List<CommentInfo.CommentsBean> commentsBeen);

        void showCommentDialog(int position, String msg);

        void clearDialog(int position);

        void showContentDialog(PostListBean postListBean,boolean isShowTitle);
    }
}
