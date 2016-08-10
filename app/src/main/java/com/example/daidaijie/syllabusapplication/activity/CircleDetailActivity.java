package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.adapter.CommentAdapter;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.service.CircleCommentsService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CircleDetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentRecyclerView)
    RecyclerView mContentRecyclerView;

    @BindView(R.id.commentRecyclerView)
    RecyclerView mCommentRecyclerView;

    private CirclesAdapter mCirclesAdapter;

    private CommentAdapter mCommentAdapter;

    private List<PostListBean> mPostListBeen;

    private PostListBean mPostListBean;

    private static final String EXTRA_POST_BEAN =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PostBean";

    private static final String EXTRA_PHOTO_WIDTH =
            "com.example.daidaijie.syllabusapplication.activity/CircleDetailActivity.PhotoWidth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostListBean = (PostListBean) getIntent().getSerializableExtra(EXTRA_POST_BEAN);

        mPostListBeen = new ArrayList<>();
        mPostListBeen.add(mPostListBean);

        mCirclesAdapter = new CirclesAdapter(this, mPostListBeen,
                getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));
        //以后一定要记住这句话
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mCirclesAdapter);

        mCommentAdapter = new CommentAdapter(this, null);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentRecyclerView.setAdapter(mCommentAdapter);

        getComment();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_circle_detail;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context, PostListBean postBean, int photoWidth) {
        Intent intent = new Intent(context, CircleDetailActivity.class);
        intent.putExtra(EXTRA_POST_BEAN, postBean);
        intent.putExtra(EXTRA_PHOTO_WIDTH, photoWidth);
        return intent;
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

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CommentInfo commentInfo) {
                        mCommentAdapter.setCommentInfo(commentInfo);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                });
    }

}
