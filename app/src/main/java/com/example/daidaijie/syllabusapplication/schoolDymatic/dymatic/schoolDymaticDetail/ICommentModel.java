package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/21.
 */

public interface ICommentModel {

    void setPostId(int postId);

    Observable<List<CommentInfo.CommentsBean>> getCommentsFromNet();

    void getCommentNormal(int position, IBaseModel.OnGetSuccessCallBack<CommentInfo.CommentsBean> getSuccessCallBack);

    Observable<List<CommentInfo.CommentsBean>> sendCommentToNet(String msg);
}
