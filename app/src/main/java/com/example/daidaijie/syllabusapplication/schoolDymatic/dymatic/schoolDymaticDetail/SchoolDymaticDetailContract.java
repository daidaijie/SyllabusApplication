package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail;

import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface SchoolDymaticDetailContract {

    interface presenter extends BasePresenter, SchoolDymaticAdapter.OnLikeCallBack {
        void loadData();

        void showComment(int position);

        void postComment(int postID, String toAccount, String msg);
    }

    interface view extends BaseView<presenter> {

        void showHeaderInfo(SchoolDymatic schoolDymatic);

        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void showData(List<CommentInfo.CommentsBean> commentsBeen);

        void showCommentDialog(int position, String msg);

        void clearDialog(int position);

        void showContentDialog(SchoolDymatic schoolDymatic, boolean isShowTitle);
    }
}
