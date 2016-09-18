package com.example.daidaijie.syllabusapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.daidaijie.syllabusapplication.R;

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

    public static AddLessonFragment newInstance() {
        AddLessonFragment fragment = new AddLessonFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lesson, container, false);
        ButterKnife.bind(this, view);


        mAddLessonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddLessonGridActivity.class);
                startActivity(intent);
            }
        });

        mLessonTimeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHeader.attachTo(mLessonTimeRecycler);

        return view;
    }

}
