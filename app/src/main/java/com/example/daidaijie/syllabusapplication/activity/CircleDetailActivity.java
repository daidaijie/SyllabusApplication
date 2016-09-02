package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.CommentAdapter;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostCommentBean;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.CircleCommentsService;
import com.example.daidaijie.syllabusapplication.service.SendCommentService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CircleDetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.commentRecyclerView)
    RecyclerView mCommentRecyclerView;

    RecyclerView mContentRecyclerView;
    @BindView(R.id.commentEditext)
    EditText mCommentEditext;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;
    @BindView(R.id.sendCommentButton)
    Button mSendCommentButton;
    @BindView(R.id.commentInputLayout)
    LinearLayout mCommentInputLayout;
    private CirclesAdapter mCirclesAdapter;

    private CommentAdapter mCommentAdapter;

    private List<PostListBean> mPostListBeen;

    private CommentInfo mCommentInfo;

    private PostListBean mPostListBean;

    private static final String EXTRA_POST_BEAN =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PostBean";

    private static final String EXTRA_PHOTO_WIDTH =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PhotoWidth";

    private static final String EXTRA_IS_COMMENT =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.isComment";


    private int mVisibleHeight;
    private boolean mIsKeyboardShow;

    private boolean mIsComment;

    //上一条评论
    private int lastPostion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }
        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight();
            }
        });

        mPostListBean = (PostListBean) getIntent().getSerializableExtra(EXTRA_POST_BEAN);

        mPostListBeen = new ArrayList<>();
        mPostListBeen.add(mPostListBean);

        lastPostion = 0;
        mCirclesAdapter = new CirclesAdapter(this, mPostListBeen,
                getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));
        mCirclesAdapter.setCommentListener(new CirclesAdapter.OnCommentListener() {
            @Override
            public void onComment() {
                showInput();
                mCommentEditext.setHint("评论该动态");
                if (lastPostion != 0) {
                    mCommentEditext.setText("");
                }
                lastPostion = 0;
            }
        });
        //以后一定要记住这句话
        mContentRecyclerView = new RecyclerView(this);
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mCirclesAdapter);

        mCommentInfo = null;
        mCommentAdapter = new CommentAdapter(this, mCommentInfo);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentAdapter.setHeaderView(mContentRecyclerView);
        mCommentAdapter.setCommentListener(new CommentAdapter.onCommentListener() {
            @Override
            public void onComment(int position) {
                showInput();
                CommentInfo.CommentsBean.UserBean user = mCommentInfo.getComments().get(position).getUser();
                String userName = user.getNickname().trim().isEmpty() ? user.getAccount() : user.getNickname();
                mCommentEditext.setHint("回复" + userName + ":");
                if (lastPostion != position + 1) {
                    mCommentEditext.setText("");
                }
                lastPostion = position + 1;
            }
        });

        mCommentRecyclerView.setAdapter(mCommentAdapter);

        getComment();

        mIsComment = getIntent().getBooleanExtra(EXTRA_IS_COMMENT, false);
        if (mIsComment) {
            showInput();
            mCommentEditext.setHint("评论该动态");
            if (lastPostion != 0) {
                mCommentEditext.setText("");
            }
            lastPostion = 0;
        }

    }

    private void getKeyboardHeight() {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);

        int visibleHeight = r.height();

        if (mVisibleHeight == 0) {
            mVisibleHeight = visibleHeight;
            return;
        }

        if (mVisibleHeight == visibleHeight) {
            return;
        }

        mVisibleHeight = visibleHeight;

        int mRootHeight = mRootView.getHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRootHeight -= getStatusBarHeight();
        }

//        Toast.makeText(CircleDetailActivity.this, "height" + mVisibleHeight + " " + mRootHeight, Toast.LENGTH_SHORT).show();
        // Magic is here
        if (mVisibleHeight != mRootHeight) {
            mIsKeyboardShow = true;
        } else {
            mIsKeyboardShow = false;
        }
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mCommentInputLayout.getLayoutParams();
        layoutParams.bottomMargin = mRootHeight - mVisibleHeight;
        mRootView.requestLayout();
        if (mIsKeyboardShow == false) {
            hideInput();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_circle_detail;
    }

    public static Intent getIntent(Context context, PostListBean postBean, int photoWidth) {
        return getIntent(context, postBean, photoWidth, false);
    }

    public static Intent getIntent(Context context, PostListBean postBean, int photoWidth, boolean isComment) {
        Intent intent = new Intent(context, CircleDetailActivity.class);
        intent.putExtra(EXTRA_POST_BEAN, postBean);
        intent.putExtra(EXTRA_PHOTO_WIDTH, photoWidth);
        intent.putExtra(EXTRA_IS_COMMENT, isComment);
        return intent;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void showInput() {
        mCommentInputLayout.setVisibility(View.VISIBLE);
        mCommentEditext.setFocusable(true);
        mCommentEditext.setFocusableInTouchMode(true);
        mCommentEditext.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mCommentEditext, InputMethodManager.SHOW_FORCED);
    }

    private void hideInput() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mCommentEditext.getWindowToken(), 0);
        mCommentInputLayout.setVisibility(View.GONE);
    }

    private void getComment() {
        Retrofit retrofit = RetrofitUtil.getDefault();
        CircleCommentsService commentsService = retrofit.create(CircleCommentsService.class);
        commentsService.get_comments(mPostListBean.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentInfo>() {
                    @Override
                    public void onCompleted() {
                        mCommentAdapter.setCommentInfo(mCommentInfo);
                        mCommentAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CommentInfo commentInfo) {
                        mCommentInfo = commentInfo;
                    }
                });
    }

    private void sendComment() {
        if (mCommentEditext.getText().toString().trim().isEmpty()) {
            SnackbarUtil.ShortSnackbar(
                    mCommentEditext, "消息不能为空", SnackbarUtil.Warning
            ).show();
            return;
        }
        String postContent = mCommentEditext.getText().toString();
        if (lastPostion != 0) {
            postContent = "@" + mCommentInfo.getComments().get(lastPostion - 1).getUser().getName()
                    + ": " + postContent;
        }

//        Log.e("PostCommentBean", "sendComment: " + User.getInstance().getUserBaseBean().getId());
//        Log.e("PostCommentBean", "sendComment: " + User.getInstance().getUserInfo().getToken());

        // TODO: 2016/9/2 别的设备登录这里怎么破.....
        PostCommentBean postCommentBean = new PostCommentBean(
                mPostListBeen.get(0).getId(),
                User.getInstance().getUserBaseBean().getId(),
                postContent,
                User.getInstance().getUserInfo().getToken()
        );
        SendCommentService sendCommentService
                = RetrofitUtil.getDefault().create(SendCommentService.class);
        sendCommentService.sendComment(postCommentBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        // TODO: 2016/8/18 这里要动态更改评论的数量
                        mCommentEditext.setText("");
                        getComment();
                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackbarUtil.ShortSnackbar(
                                mCommentEditext, "发送失败", SnackbarUtil.Alert
                        ).show();
                        Log.d("发送失败", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
    }

    @OnClick(R.id.sendCommentButton)
    public void onClick() {
        sendComment();
        hideInput();
    }
}
