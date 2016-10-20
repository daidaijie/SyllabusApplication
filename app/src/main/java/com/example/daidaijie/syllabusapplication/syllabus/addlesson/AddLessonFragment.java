package com.example.daidaijie.syllabusapplication.syllabus.addlesson;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LessonTimeAdapter;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.TimeGrid;
import com.example.daidaijie.syllabusapplication.model.AddLessonModel;
import com.example.daidaijie.syllabusapplication.model.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddLessonFragment extends Fragment {

    @BindView(R.id.addLessonButton)
    FButton mAddLessonButton;
    @BindView(R.id.lessonTimeRecycler)
    RecyclerView mLessonTimeRecycler;
    @BindView(R.id.header)
    RecyclerViewHeader mHeader;

    AddLessonModel mAddLessonModel;

    LessonTimeAdapter mLessonTimeAdapter;

    public static final int REQUEST_ADD_TIME_GRID = 204;

    @BindView(R.id.lessonNameEditText)
    EditText mLessonNameEditText;
    @BindView(R.id.classroomEditText)
    EditText mClassroomEditText;
    @BindView(R.id.teacherEditText)
    EditText mTeacherEditText;

    public static AddLessonFragment newInstance() {
        AddLessonFragment fragment = new AddLessonFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        ButterKnife.bind(this, view);

        mAddLessonModel = AddLessonModel.getInstance();

        mAddLessonModel.mLesson = new Lesson();
        mAddLessonModel.mTimes = new ArrayList<>();

        mAddLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddLessonModel.mTimes.add(new AddLessonModel.SelectTime());
                Intent intent = AddLessonGridActivity.getIntent(getActivity(), mAddLessonModel.mTimes.size() - 1);
                startActivityForResult(intent, REQUEST_ADD_TIME_GRID);
            }
        });

        mLessonTimeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLessonTimeAdapter = new LessonTimeAdapter(getActivity(), mAddLessonModel.mTimes);
        mLessonTimeRecycler.setAdapter(mLessonTimeAdapter);

        mHeader.attachTo(mLessonTimeRecycler);

        return view;
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
//        lesson.setTimeGirds(timeGirds);
        lesson.mergeTimeGrid();
        User.getInstance().getSyllabus(User.getInstance().getCurrentSemester()).addLessonToSyllabus(
                lesson, User.getInstance().getCurrentSemester(), R.color.colorPrimary
        );
        User.getInstance().saveSyllabus();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_post_content, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {
            addLesson();
            Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
