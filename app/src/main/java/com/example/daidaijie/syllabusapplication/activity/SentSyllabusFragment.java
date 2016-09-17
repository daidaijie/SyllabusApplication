package com.example.daidaijie.syllabusapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SentSyllabusFragment extends Fragment {


    @BindView(R.id.sentRecyclerView)
    RecyclerView mSentRecyclerView;
    @BindView(R.id.refreshSentLayout)
    SwipeRefreshLayout mRefreshSentLayout;
    @BindView(R.id.addSentButton)
    FloatingActionButton mAddSentButton;

    public static SentSyllabusFragment newInstance() {
        SentSyllabusFragment fragment = new SentSyllabusFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sended_syllabus, container, false);
        ButterKnife.bind(this, view);


        mAddSentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SendSyllabusActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
