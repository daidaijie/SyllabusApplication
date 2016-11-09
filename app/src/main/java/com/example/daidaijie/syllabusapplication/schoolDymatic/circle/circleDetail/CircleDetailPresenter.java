package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.CommentsBean;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.event.CircleStateChangeEvent;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.ISchoolCircleModel;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class CircleDetailPresenter implements CircleDetailContract.presenter, CirclesAdapter.OnLongClickCallBack {

    int mPosition;

    CircleDetailContract.view mView;

    ISchoolCircleModel mISchoolCircleModel;

    ICommentModel mICommentModel;

    IUserModel mIUserModel;

    @Inject
    @PerActivity
    public CircleDetailPresenter(int position,
                                 CircleDetailContract.view view,
                                 ISchoolCircleModel ISchoolCircleModel,
                                 ICommentModel commentModel,
                                 @LoginUser IUserModel userModel) {
        mPosition = position;
        mView = view;
        mISchoolCircleModel = ISchoolCircleModel;
        mICommentModel = commentModel;
        mIUserModel = userModel;
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
                        EventBus.getDefault().post(new CircleStateChangeEvent(mPosition));
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
                            if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                        }
                    }

                    @Override
                    public void onNext(final List<CommentInfo.CommentsBean> commentsBeen) {
                        mView.showData(commentsBeen);
                        mISchoolCircleModel.getCircleByPosition(mPosition, new IBaseModel.OnGetSuccessCallBack<PostListBean>() {
                            @Override
                            public void onGetSuccess(PostListBean postListBean) {
                                List<CommentsBean> beans = new ArrayList<>();
                                for (CommentInfo.CommentsBean commentsBean : commentsBeen) {
                                    CommentsBean bean = new CommentsBean();
                                    bean.setId(commentsBean.getId());
                                    bean.setUid(commentsBean.getUid());
                                    beans.add(bean);
                                }
                                postListBean.setComments(beans);
                                mView.showHeaderInfo(postListBean);
                            }
                        });
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


    @Override
    public void onLike(int position, boolean isLike, final CirclesAdapter.OnLikeStateChangeListener onLikeStateChangeListener) {
        if (isLike) {
            mISchoolCircleModel.like(mPosition)
                    .subscribe(new Subscriber<ThumbUpReturn>() {
                        @Override
                        public void onCompleted() {
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() == null) {
                                mView.showFailMessage("点赞失败");
                            } else {
                                if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                    mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                                } else {
                                    mView.showFailMessage(e.getMessage().toUpperCase());
                                }
                            }
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onNext(ThumbUpReturn thumbUpReturn) {
                            onLikeStateChangeListener.onLike(true);
                            EventBus.getDefault().post(new CircleStateChangeEvent(mPosition));
                        }
                    });
        } else {
            mISchoolCircleModel.unlike(mPosition)
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() == null) {
                                mView.showFailMessage("取消点赞失败");
                            } else {
                                if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                    mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                                } else {
                                    mView.showFailMessage(e.getMessage().toUpperCase());
                                }
                            }
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            onLikeStateChangeListener.onLike(false);
                            EventBus.getDefault().post(new CircleStateChangeEvent(mPosition));
                        }
                    });
        }

    }

    @Override
    public void onLongClick(int position, int mode) {
        mISchoolCircleModel.getCircleByPosition(mPosition, new IBaseModel.OnGetSuccessCallBack<PostListBean>() {
            @Override
            public void onGetSuccess(PostListBean postListBean) {
                mView.showContentDialog(postListBean, mIUserModel.getUserBaseBeanNormal().getLevel() > 1);
            }
        });
    }
}
