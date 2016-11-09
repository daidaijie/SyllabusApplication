package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.ISchoolCircleModel;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class StuCirclePresenter implements StuCircleContract.presenter, CirclesAdapter.OnLongClickCallBack {

    ISchoolCircleModel mISchoolCircleModel;

    StuCircleContract.view mView;

    IUserModel mIUserModel;

    @Inject
    @PerFragment
    public StuCirclePresenter(ISchoolCircleModel ISchoolCircleModel,
                              StuCircleContract.view view,
                              @LoginUser IUserModel userModel) {
        mISchoolCircleModel = ISchoolCircleModel;
        mView = view;
        mIUserModel = userModel;
    }

    @Override
    public void refresh() {
        mView.showRefresh(true);
        mISchoolCircleModel.loadCircleListFromNet()
                .subscribe(new Subscriber<List<PostListBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoggerUtil.printStack(e);
                        mView.showRefresh(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                    }

                    @Override
                    public void onNext(List<PostListBean> postListBeen) {
                        mView.showData(postListBeen);
                    }
                });
    }

    @Override
    public void loadData() {
        mISchoolCircleModel.getCircleListFromNet()
                .subscribe(new Subscriber<List<PostListBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.loadMoreFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e.getMessage() == null) {
                            mView.showFailMessage("获取失败");
                        } else {
                            mView.showFailMessage(e.getMessage().toUpperCase());
                        }
                        mView.loadMoreFinish();
                    }

                    @Override
                    public void onNext(List<PostListBean> postListBeen) {
                        mView.showData(postListBeen);
                    }
                });
    }

    @Override
    public void deletePost(int position) {
        mView.showLoading(true);
        mISchoolCircleModel.deletePost(position)
                .subscribe(new Subscriber<List<PostListBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showLoading(false);
                        mView.showSuccessMessage("删除成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        if (e.getMessage() == null) {
                            mView.showFailMessage("删除失败");
                        } else {
                            if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                        }
                    }

                    @Override
                    public void onNext(List<PostListBean> postListBeen) {
                        mView.showData(postListBeen);
                    }
                });
    }

    @Override
    public void onLike(int position, boolean isLike, final CirclesAdapter.OnLikeStateChangeListener onLikeStateChangeListener) {
        if (isLike) {
            mISchoolCircleModel.like(position)
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
                        }
                    });
        } else {
            mISchoolCircleModel.unlike(position)
                    .subscribe(new Subscriber<Void>() {
                        @Override
                        public void onCompleted() {
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e.getMessage() == null) {
                                if (e.getMessage().toUpperCase().equals("UNAUTHORIZED")) {
                                    mView.showFailMessage(App.getContext().getResources().getString(R.string.UNAUTHORIZED));
                                } else {
                                    mView.showFailMessage(e.getMessage().toUpperCase());
                                }
                            } else {
                                mView.showFailMessage(e.getMessage().toUpperCase());
                            }
                            onLikeStateChangeListener.onFinish();
                        }

                        @Override
                        public void onNext(Void aVoid) {
                            onLikeStateChangeListener.onLike(false);
                        }
                    });
        }
    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void onLongClick(final int position, final int mode) {
        mISchoolCircleModel.getCircleByPosition(position, new IBaseModel.OnGetSuccessCallBack<PostListBean>() {
            @Override
            public void onGetSuccess(PostListBean postListBean) {
                boolean canDelete = false;
                if (mIUserModel.getUserBaseBeanNormal().getLevel() > 1 ||
                        postListBean.getUser().getId() == mIUserModel.getUserBaseBeanNormal().getId()) {
                    canDelete = true;
                }
                mView.showContentDialog(postListBean, mIUserModel.getUserBaseBeanNormal().getLevel() > 1,
                        mode == CirclesAdapter.MODE_ITEM_CLICK && canDelete, position);
            }
        });
    }
}
