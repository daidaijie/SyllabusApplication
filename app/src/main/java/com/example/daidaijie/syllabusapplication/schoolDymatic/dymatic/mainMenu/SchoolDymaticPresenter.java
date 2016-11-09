package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.bean.ThumbUpReturn;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.ISchoolDymaticModel;
import com.example.daidaijie.syllabusapplication.user.IUserModel;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class SchoolDymaticPresenter implements SchoolDymaticContract.presenter, SchoolDymaticAdapter.OnLongClickCallBack {

    SchoolDymaticContract.view mView;

    ISchoolDymaticModel mISchoolDymaticModel;

    IUserModel mIUserModel;

    @Inject
    @PerFragment
    public SchoolDymaticPresenter(SchoolDymaticContract.view view,
                                  ISchoolDymaticModel ISchoolDymaticModel,
                                  @LoginUser IUserModel userModel) {
        mView = view;
        mISchoolDymaticModel = ISchoolDymaticModel;
        mIUserModel = userModel;
    }

    @Override
    public void refresh() {
        mView.loadStart();
        mView.showRefresh(true);
        mISchoolDymaticModel.loadSchoolDynamicListFromNet()
                .subscribe(new Subscriber<List<SchoolDymatic>>() {
                    @Override
                    public void onCompleted() {
                        mView.showRefresh(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefresh(false);
                        mView.showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(List<SchoolDymatic> schoolDymatics) {
                        mView.showData(schoolDymatics);
                    }
                });
    }

    @Override
    public void loadData() {
        mISchoolDymaticModel.getSchoolDynamicListFromNet()
                .subscribe(new Subscriber<List<SchoolDymatic>>() {
                    @Override
                    public void onCompleted() {
                        mView.loadMoreFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadMoreFinish();

                        /**
                         * 这里表明滚动到底部了,不要问我为什么，问晓拂
                         */
                        if (e instanceof JsonSyntaxException) {
                            mView.loadEnd();
                            mView.showInfoMessage("已经是最后一条");
                            return;
                        }
                        mView.showFailMessage("获取失败");
                    }

                    @Override
                    public void onNext(List<SchoolDymatic> schoolDymatics) {
                        mView.showData(schoolDymatics);
                    }
                });

    }

    @Override
    public void handlerFAB() {
        if (mIUserModel.getUserBaseBeanNormal().getLevel() < 1) {
            mView.showInfoMessage("只有组织和社团负责人可以发布，请联系管理员获得权限");
        } else {
            mView.toPostDymatic();
        }
    }

    @Override
    public void deletePost(int position) {
        mView.showLoading(true);
        mISchoolDymaticModel.deletePost(position)
                .subscribe(new Subscriber<List<SchoolDymatic>>() {
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
                    public void onNext(List<SchoolDymatic> schoolDymatics) {
                        mView.showData(schoolDymatics);
                    }
                });
    }

    @Override
    public void start() {
        refresh();
    }

    @Override
    public void onLike(int position, boolean isLike, final SchoolDymaticAdapter.OnLikeStateChangeListener onLikeStateChangeListener) {
        if (isLike) {
            mISchoolDymaticModel.like(position)
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
            mISchoolDymaticModel.unlike(position)
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
                        }
                    });
        }

    }

    @Override
    public void onLongClick(final int position, final int mode) {
        mISchoolDymaticModel.getDymaticByPosition(position, new IBaseModel.OnGetSuccessCallBack<SchoolDymatic>() {
            @Override
            public void onGetSuccess(SchoolDymatic schoolDymatic) {
                boolean canDelete = false;
                if (mIUserModel.getUserBaseBeanNormal().getLevel() > 1 ||
                        schoolDymatic.getUser().getId() == mIUserModel.getUserBaseBeanNormal().getId()) {
                    canDelete = true;
                }
                mView.showContentDialog(schoolDymatic, mIUserModel.getUserBaseBeanNormal().getLevel() > 1,
                        mode == CirclesAdapter.MODE_ITEM_CLICK && canDelete, position);
            }
        });
    }
}
