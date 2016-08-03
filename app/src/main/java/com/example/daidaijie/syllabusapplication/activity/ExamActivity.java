package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.io.Serializable;
import java.util.List;

public class ExamActivity extends BaseActivity {

    private static final String EXTRA_EXAM_LIST
            = "com.example.daidaijie.syllabusapplication.activity.ExamList";

    private List<Exam> mExams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_exam;
    }

    public static Intent getIntent(Context packageContext, List<Exam> mExams) {
        Intent intent = new Intent(packageContext, ExamActivity.class);
        intent.putExtra(EXTRA_EXAM_LIST, (Serializable) mExams);
        return intent;
    }
}
