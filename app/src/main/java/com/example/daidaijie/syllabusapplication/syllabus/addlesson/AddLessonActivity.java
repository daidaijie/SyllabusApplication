package com.example.daidaijie.syllabusapplication.syllabus.addlesson;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LessonTimeAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.TimeGrid;
import com.example.daidaijie.syllabusapplication.model.AddLessonModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import info.hoang8f.widget.FButton;

public class AddLessonActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.lessonTimeRecycler)
    RecyclerView mLessonTimeRecycler;
    @BindView(R.id.lessonNameEditText)
    EditText mLessonNameEditText;
    @BindView(R.id.classroomEditText)
    EditText mClassroomEditText;
    @BindView(R.id.teacherEditText)
    EditText mTeacherEditText;
    @BindView(R.id.addLessonButton)
    FButton mAddLessonButton;
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;

    AddLessonModel mAddLessonModel;

    LessonTimeAdapter mLessonTimeAdapter;

    public static final int REQUEST_ADD_TIME_GRID = 204;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAddLessonModel = AddLessonModel.getInstance();

        mAddLessonModel.mLesson = new Lesson();
        mAddLessonModel.mTimes = new ArrayList<>();

        mAddLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddLessonModel.mTimes.add(new AddLessonModel.SelectTime());
                Intent intent = AddLessonGridActivity.getIntent(AddLessonActivity.this, mAddLessonModel.mTimes.size() - 1);
                startActivityForResult(intent, REQUEST_ADD_TIME_GRID);
            }
        });

        mLessonTimeRecycler.setLayoutManager(new LinearLayoutManager(this));
        mLessonTimeAdapter = new LessonTimeAdapter(this, mAddLessonModel.mTimes);
        mLessonTimeRecycler.setAdapter(mLessonTimeAdapter);

        mHeader.attachTo(mLessonTimeRecycler);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_lesson;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_TIME_GRID) {
            if (resultCode == AddLessonGridActivity.RESULT_OK) {
                mLessonTimeAdapter.notifyDataSetChanged();
            } else {
                mAddLessonModel.mTimes.remove(mAddLessonModel.mTimes.size() - 1);
                mLessonTimeAdapter.notifyDataSetChanged();
            }
        }
    }

    private void addLesson() {

        if (mLessonNameEditText.getText().toString().trim().isEmpty()) {
            mClassroomEditText.setError("课程名不能为空");
            return;
        }

        Lesson lesson = new Lesson();
        lesson.setId(System.currentTimeMillis() + "");
        lesson.setTYPE(Lesson.TYPE_DIY);
        lesson.setName(mLessonNameEditText.getText().toString().trim());
        lesson.setRoom(mClassroomEditText.getText().toString().trim());
        lesson.setTeacher(mTeacherEditText.getText().toString().trim());
        List<TimeGrid> timeGirds = new ArrayList<>();
        for (AddLessonModel.SelectTime selectTime : mAddLessonModel.mTimes) {
            long selectWeekInt = 0;
            for (int i = 0; i < 16; i++) {
                if (selectTime.selectWeeks.get(i)) {
                    selectWeekInt += (1 << i);
                }
            }

            for (int i = 0; i < 7; i++) {
                boolean flag = false;
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < 13; j++) {
                    if (selectTime.mSelectTimes.get(i).get(j)) {
                        sb.append(Syllabus.time2char((j + 1)));
                        flag = true;
                    }
                }
                if (flag) {
                    TimeGrid timeGird = new TimeGrid();
                    timeGird.setWeekDate(i);
                    timeGird.setWeekOfTime(selectWeekInt);
                    timeGird.setTimeList(sb.toString());
                    timeGirds.add(timeGird);
                }
            }

        }
//        lesson.setTimeGrids(timeGirds);
        lesson.mergeTimeGrid();
        User.getInstance().getSyllabus(User.getInstance().getCurrentSemester()).addLessonToSyllabus(
                lesson, User.getInstance().getCurrentSemester(), R.color.colorPrimary
        );
        User.getInstance().saveSyllabus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (mAddLessonModel.mTimes.size() != 0
                || mClassroomEditText.getText().toString().trim().length() != 0
                || mLessonNameEditText.getText().toString().trim().length() != 0
                || mTeacherEditText.getText().toString().trim().length() != 0
                ) {
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            int padding = DensityUtil.dip2px(this, 16);
            textView.setPadding(padding + padding / 2, padding, padding, padding);
            textView.setText("你正在编辑中,是否要退出?");
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setView(textView)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AddLessonActivity.super.onBackPressed();
                        }
                    }).create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            addLesson();
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
