package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.bean.ExamInfo;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/4.
 */
public class ExamListAdapter extends RecyclerView.Adapter<ExamListAdapter.ViewHolder>{

    AppCompatActivity mActivity;
    List<Exam> mExamList;

    public ExamListAdapter(AppCompatActivity activity, List<Exam> examList) {
        mActivity = activity;
        mExamList = examList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mExamList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
