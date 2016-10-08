package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

public class SendSyllabusActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Logger.json(GsonUtil.getDefault().toJson(User.getInstance().getSyllabus(User.getInstance().getCurrentSemester())));


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_send_syllabus;
    }
}
