package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostCommentBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.CircleCommentsApi;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class CommentModel implements ICommentModel {

    int mPostId;

    CircleCommentsApi mCircleCommentsApi;

    List<CommentInfo.CommentsBean> mCommentsBeens;

    IUserModel mIUserModel;

    public CommentModel(CircleCommentsApi circleCommentsApi, IUserModel userModel) {
        mCircleCommentsApi = circleCommentsApi;
        mIUserModel = userModel;
    }

    public void setPostId(int postId) {
        mPostId = postId;
    }

    @Override
    public Observable<List<CommentInfo.CommentsBean>> getCommentsFromNet() {
        return mCircleCommentsApi.getComments(mPostId)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<CommentInfo>, Observable<List<CommentInfo.CommentsBean>>>() {
                    @Override
                    public Observable<List<CommentInfo.CommentsBean>> call(HttpResult<CommentInfo> commentInfoHttpResult) {
                        if (RetrofitUtil.isSuccessful(commentInfoHttpResult)) {
                            mCommentsBeens = commentInfoHttpResult.getData().getComments();
                            return Observable.just(mCommentsBeens);
                        } else if (commentInfoHttpResult.getMessage().toLowerCase().equals("no resources yet")) {
                            return Observable.just(null);
                        } else {
                            return Observable.error(new Throwable(commentInfoHttpResult.getMessage()));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void getCommentNormal(int position, IBaseModel.OnGetSuccessCallBack<CommentInfo.CommentsBean> getSuccessCallBack) {
        getSuccessCallBack.onGetSuccess(mCommentsBeens.get(position));
    }

    @Override
    public Observable<List<CommentInfo.CommentsBean>> sendCommentToNet(String msg) {
        PostCommentBean postCommentBean = new PostCommentBean(
                mPostId,
                mIUserModel.getUserInfoNormal().getUser_id(),
                msg,
                mIUserModel.getUserInfoNormal().getToken()
        );

        return mCircleCommentsApi.sendComment(postCommentBean)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<HttpResult<Void>, Observable<List<CommentInfo.CommentsBean>>>() {
                    @Override
                    public Observable<List<CommentInfo.CommentsBean>> call(HttpResult<Void> voidHttpResult) {
                        if (voidHttpResult.getCode() == 201) {
                            return getCommentsFromNet();
                        }
                        return Observable.error(new Throwable(voidHttpResult.getMessage()));
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}
