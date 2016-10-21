package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.circleDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.ISchoolCircleModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class CircleDetailPresenter implements CircleDetailContract.presenter {

    int mPosition;

    CircleDetailContract.view mView;

    ISchoolCircleModel mISchoolCircleModel;

    ICommentModel mICommentModel;

    @Inject
    @PerActivity
    public CircleDetailPresenter(int position,
                                 CircleDetailContract.view view,
                                 ISchoolCircleModel ISchoolCircleModel,
                                 ICommentModel commentModel) {
        mPosition = position;
        mView = view;
        mISchoolCircleModel = ISchoolCircleModel;
        mICommentModel = commentModel;
    }

    @Override
    public void loadData() {
        mView.showRefresh(true);
        mICommentModel.getCommentsFromNet()
                .subscribe(new Subscriber<List<CommentInfo.CommentsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefresh(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取评论失败!");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                    }

                    @Override
                    public void onNext(List<CommentInfo.CommentsBean> commentsBeen) {
                        mView.showData(commentsBeen);
                    }
                });
    }

    @Override
    public void showComment(final int position) {
        if (position == -1) {
            mISchoolCircleModel.getCircleByPosition(mPosition, new IBaseModel.OnGetSuccessCallBack<PostListBean>() {
                @Override
                public void onGetSuccess(PostListBean postListBean) {
                    mView.showCommentDialog(-1, postListBean.getUser().getNickname());
                }
            });
            return;
        }

        mICommentModel.getCommentNormal(position, new IBaseModel.OnGetSuccessCallBack<CommentInfo.CommentsBean>() {
            @Override
            public void onGetSuccess(CommentInfo.CommentsBean commentsBean) {
                mView.showCommentDialog(commentsBean.getId(), commentsBean.getUser().getNickname());
            }
        });
    }

    @Override
    public void postComment(final int postID, String toAccount, String msg) {
        mView.showRefresh(true);
        mICommentModel.sendCommentToNet("@" + toAccount + ": " + msg)
                .subscribe(new Subscriber<List<CommentInfo.CommentsBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                        mView.clearDialog(postID);
                        mView.showSuccessMessage("评论成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefresh(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("评论失败");
                        } else {
                            mView.showFailMessage(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(List<CommentInfo.CommentsBean> commentsBeen) {
                        mView.showData(commentsBeen);
                    }
                });
    }

    @Override
    public void start() {
        mISchoolCircleModel.getCircleByPosition(mPosition, new IBaseModel.OnGetSuccessCallBack<PostListBean>() {
            @Override
            public void onGetSuccess(PostListBean postListBean) {
                mView.showHeaderInfo(postListBean);
                mICommentModel.setPostId(postListBean.getId());
            }
        });
    }
}
