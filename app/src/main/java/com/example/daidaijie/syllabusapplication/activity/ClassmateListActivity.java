package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.StudentInfoAdapter;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;

public class ClassmateListActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.classmateRecyclerView)
    RecyclerView mClassmateRecyclerView;

    public static final String EXTRA_STUDENT_LIST =
            "com.example.daidaijie.syllabusapplication.student_list";

    public static final String EXTRA_BG_COLOR = "com.example.daidaijie.syllabusapplication.bg_color";

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
        mToolbar.setBackgroundColor(getResources()
                .getColor(getIntent().getIntExtra(EXTRA_BG_COLOR,R.color.colorPrimary)));
        setSupportActionBar(mToolbar);
        setupToolbar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
