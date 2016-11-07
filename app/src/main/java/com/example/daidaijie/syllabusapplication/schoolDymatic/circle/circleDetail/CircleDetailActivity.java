package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.CommentAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.StuCircleModelComponent;
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.EditTextDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class CircleDetailActivity extends BaseActivity implements CircleDetailContract.view, SwipeRefreshLayout.OnRefreshListener, CommentAdapter.onCommentListener, CirclesAdapter.OnCommentListener, EditTextDialog.OnPostCommentCallBack {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentRecyclerView)
    RecyclerView mContentRecyclerView;
    @BindView(R.id.commentRecyclerView)
    RecyclerView mCommentRecyclerView;
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CirclesAdapter mCirclesAdapter;

    private CommentAdapter mCommentAdapter;

    private static final String EXTRA_POST_BEAN_POS = CircleDetailActivity.class.getCanonicalName()
            + ".PostBeanPos";

    private static final String EXTRA_PHOTO_WIDTH = CircleDetailActivity.class.getCanonicalName()
            + ".PhotoWidth";

    private static final String EXTRA_IS_COMMENT = CircleDetailActivity.class.getCanonicalName()
            + ".isComment";

    Map<Integer, EditTextDialog> mEditTextDialogMap;

    @Inject
    CircleDetailPresenter mCircleDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        mCirclesAdapter = new CirclesAdapter(this, null, getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mCirclesAdapter);
        mCirclesAdapter.setCommentListener(this);

        mCommentAdapter = new CommentAdapter(this, null);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.setCommentListener(this);
        mHeader.attachTo(mCommentRecyclerView);

        DaggerCircleDetailComponent.builder()
                .stuCircleModelComponent(StuCircleModelComponent.getInstance(mAppComponent))
                .circleDetailModule(new CircleDetailModule(getIntent().getIntExtra(EXTRA_POST_BEAN_POS, 0), this))
                .build().inject(this);

        mCircleDetailPresenter.start();
        mCirclesAdapter.setOnLikeCallBack(mCircleDetailPresenter);

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mCircleDetailPresenter.loadData();
            }
        });

        mEditTextDialogMap = new HashMap<>();

        if (getIntent().getBooleanExtra(EXTRA_IS_COMMENT, false)) {
            mCircleDetailPresenter.showComment(-1);
        }

        mCirclesAdapter.setOnLongClickCallBack(mCircleDetailPresenter);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_circle_detail;
    }

    public static Intent getIntent(Context context, int position, int photoWidth) {
        return getIntent(context, position, photoWidth, false);
    }

    public static Intent getIntent(Context context, int position, int photoWidth, boolean isComment) {
        Intent intent = new Intent(context, CircleDetailActivity.class);
        intent.putExtra(EXTRA_POST_BEAN_POS, position);
        intent.putExtra(EXTRA_PHOTO_WIDTH, photoWidth);
        intent.putExtra(EXTRA_IS_COMMENT, isComment);
        return intent;
    }

    @Override
    public void showCommentDialog(int position, String msg) {
        EditTextDialog dialog = mEditTextDialogMap.get(position);
        if (dialog == null) {
            dialog = new EditTextDialog();
            dialog.setPostUser(msg);
            dialog.setPostID(position);
            dialog.setOnPostCommentCallBack(this);
            mEditTextDialogMap.put(position, dialog);
        }
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void clearDialog(int position) {
        mEditTextDialogMap.remove(position);
    }

    @Override
    public void showContentDialog(final PostListBean postListBean, boolean isShowTitle) {
        String[] items = {"复制"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ClipboardUtil.copyToClipboard(postListBean.getContent().toString());
                        }
                    }
                });
        if (isShowTitle) {
            builder.setTitle(postListBean.getUser().getAccount());
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showHeaderInfo(PostListBean postListBean) {
        List<PostListBean> postListBeen = new ArrayList<>();
        postListBeen.add(postListBean);
        mCirclesAdapter.setPostListBeen(postListBeen);
        mCirclesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRefresh(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mCommentRecyclerView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mCommentRecyclerView, msg, SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showData(List<CommentInfo.CommentsBean> commentsBeen) {
        mCommentAdapter.setCommentsBeen(commentsBeen);
        mCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        mCircleDetailPresenter.loadData();
    }

    @Override
    public void onComment(int position) {
        mCircleDetailPresenter.showComment(position);
    }

    @Override
    public void onComment() {
        mCircleDetailPresenter.showComment(-1);
    }

    @Override
    public void onPostComment(int postID, String toAccount, String msg) {
        mCircleDetailPresenter.postComment(postID, toAccount, msg);
    }
}
