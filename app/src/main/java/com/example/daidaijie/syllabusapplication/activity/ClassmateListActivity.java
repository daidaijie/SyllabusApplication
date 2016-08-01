package com.example.daidaijie.syllabusapplication.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StudentInfoAdapter;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

public class ClassmateListActivity extends BaseActivity {


    public static final String EXTRA_STUDENT_LIST =
            "com.example.daidaijie.syllabusapplication.student_list";

    public static final String EXTRA_BG_COLOR = "com.example.daidaijie.syllabusapplication.bg_color";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.classmateRecyclerView)
    RecyclerView mClassmateRecyclerView;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.classmateRootLayout)
    LinearLayout mClassmateRootLayout;

    private List<StudentInfo> mStudentInfos;

    private StudentInfoAdapter mStudentInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStudentInfos = (List<StudentInfo>) getIntent().getSerializableExtra(EXTRA_STUDENT_LIST);
        mStudentInfoAdapter = new StudentInfoAdapter(this, mStudentInfos);
        mClassmateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mClassmateRecyclerView.setAdapter(mStudentInfoAdapter);

        mToolbar.setTitle("");
        int bgColor = getResources()
                .getColor(getIntent().getIntExtra(EXTRA_BG_COLOR, R.color.colorPrimary));
        mToolbar.setBackgroundColor(bgColor);
        mClassmateRootLayout.setBackgroundColor(bgColor);
        setSupportActionBar(mToolbar);
        setupToolbar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(300));
        }

        /*mClassmateRecyclerView.setTranslationX(deviceWidth / 2);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(
                mClassmateRecyclerView, "translationX", deviceWidth / 2, 0.0f
        );
        mClassmateRecyclerView.setAlpha(0.0f);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(
                mClassmateRecyclerView, "alpha", 0.0f, 1.0f
        );
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorA);
        animatorSet.setDuration(600);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animatorSet.start();
            }
        }, 200);*/

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_classmate_list;
    }

    public static Intent getIntent(Context packageContext, List<StudentInfo> studentInfos, int bgColor) {
        Intent intent = new Intent(packageContext, ClassmateListActivity.class);
        intent.putExtra(EXTRA_STUDENT_LIST, (Serializable) studentInfos);
        intent.putExtra(EXTRA_BG_COLOR, bgColor);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onDestroy();
    }
}
