package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/11.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private Activity mActivity;

    private List<Exam> mExams;

    public ExamAdapter(Activity activity, List<Exam> exams) {
        mActivity = activity;
        mExams = exams;
    }

    public void setExams(List<Exam> exams) {
        mExams = exams;
    }

    @Override
    public ExamAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_exam, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExamAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mExams == null) return 0;
        return mExams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
