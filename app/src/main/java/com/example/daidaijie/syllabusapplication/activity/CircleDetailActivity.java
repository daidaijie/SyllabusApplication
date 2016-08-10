package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CircleDetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentRecyclerView)
    RecyclerView mContentRecyclerView;

    private CirclesAdapter mCirclesAdapter;

    private List<PostListBean> mPostListBean;

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

        mPostListBean = new ArrayList<>();
        mPostListBean.add((PostListBean) getIntent().getSerializableExtra(EXTRA_POST_BEAN));

        mCirclesAdapter = new CirclesAdapter(this, mPostListBean,
                getIntent().getIntExtra(EXTRA_PHOTO_WIDTH, 0));

        //以后一定要记住这句话
        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentRecyclerView.setAdapter(mCirclesAdapter);
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
}
