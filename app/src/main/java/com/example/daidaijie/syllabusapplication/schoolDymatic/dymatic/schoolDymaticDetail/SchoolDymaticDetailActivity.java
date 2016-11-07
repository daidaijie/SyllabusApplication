package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail;

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
import com.example.daidaijie.syllabusapplication.adapter.CommentAdapter;
import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.EditTextDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class SchoolDymaticDetailActivity extends BaseActivity implements SchoolDymaticDetailContract.view, SchoolDymaticAdapter.OnCommentListener, CommentAdapter.onCommentListener, SwipeRefreshLayout.OnRefreshListener, EditTextDialog.OnPostCommentCallBack {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.commentRecyclerView)
    RecyclerView mCommentRecyclerView;
    @BindView(R.id.contentRecyclerView)
    RecyclerView mContentRecyclerView;
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private CommentAdapter mCommentAdapter;
    private SchoolDymaticAdapter mSchoolDymaticAdapter;

    private static final String EXTRA_SCHOOL_DYMATIC_POS = SchoolDymaticDetailActivity.class.getCanonicalName()
            + ".SchoolDymaticPos";

    private static final String EXTRA_PHOTO_WIDTH = SchoolDymaticDetailActivity.class.getCanonicalName()
            + ".PhotoWidth";

    private static final String EXTRA_IS_COMMENT = SchoolDymaticDetailActivity.class.getCanonicalName()
            + ".isComment";

    Map<Integer, EditTextDialog> mEditTextDialogMap;

    @Inject
    SchoolDymaticDetailPresenter mDymaticDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        mSchoolDymaticAdapter = new SchoolDymaticAdapter(this, null, getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mSchoolDymaticAdapter);
        mSchoolDymaticAdapter.setCommentListener(this);

        mCommentAdapter = new CommentAdapter(this, null);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        mCommentAdapter.setCommentListener(this);
        mHeader.attachTo(mCommentRecyclerView);

        DaggerSchoolDymaticDetailComponent.builder()
                .schoolDymaticModelComponent(SchoolDymaticModelComponent.getINSTANCE(mAppComponent))
                .schoolDymaticDetailModule(new SchoolDymaticDetailModule(getIntent().getIntExtra(EXTRA_SCHOOL_DYMATIC_POS, 0), this))
                .build().inject(this);

        mSchoolDymaticAdapter.setOnLongClickCallBack(mDymaticDetailPresenter);
        mDymaticDetailPresenter.start();
        mSchoolDymaticAdapter.setOnLikeCallBack(mDymaticDetailPresenter);

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mDymaticDetailPresenter.loadData();
            }
        });

        mEditTextDialogMap = new HashMap<>();

        if (getIntent().getBooleanExtra(EXTRA_IS_COMMENT, false)) {
            mDymaticDetailPresenter.showComment(-1);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_school_dymatic_detail;
    }

    public static Intent getIntent(Context context, int position, int photoWidth) {
        return getIntent(context, position, photoWidth, false);
    }

    public static Intent getIntent(Context context, int position, int photoWidth, boolean isComment) {
        Intent intent = new Intent(context, SchoolDymaticDetailActivity.class);
        intent.putExtra(EXTRA_SCHOOL_DYMATIC_POS, position);
        intent.putExtra(EXTRA_PHOTO_WIDTH, photoWidth);
        intent.putExtra(EXTRA_IS_COMMENT, isComment);
        return intent;
    }


    @Override
    public void onComment() {
        mDymaticDetailPresenter.showComment(-1);
    }

    @Override
    public void onComment(int position) {
        mDymaticDetailPresenter.showComment(position);
    }

    @Override
    public void onRefresh() {
        mDymaticDetailPresenter.loadData();
    }


    @Override
    public void showHeaderInfo(SchoolDymatic schoolDymatic) {
        List<SchoolDymatic> schoolDymatics = new ArrayList<>();
        schoolDymatics.add(schoolDymatic);
        mSchoolDymaticAdapter.setSchoolDymatics(schoolDymatics);
        mSchoolDymaticAdapter.notifyDataSetChanged();
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
    public void showContentDialog(final SchoolDymatic schoolDymatic, boolean isShowTitle) {
        String[] items = {"复制"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            ClipboardUtil.copyToClipboard(schoolDymatic.getDescription().toString());
                        }
                    }
                });
        if (isShowTitle) {
            builder.setTitle(schoolDymatic.getUser().getAccount());
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onPostComment(int postID, String toAccount, String msg) {
        mDymaticDetailPresenter.postComment(postID, toAccount, msg);
    }
}
