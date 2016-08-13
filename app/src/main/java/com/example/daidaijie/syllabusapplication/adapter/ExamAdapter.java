package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_exam, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exam exam = mExams.get(position);
        holder.mExamNameTextView.setText(exam.getTrueName());
        holder.mExamPositionTextView.setText("座位号 : " + exam.getExam_stu_position());
        holder.mExamRoomTextView.setText("试室　 : " + exam.getExam_location());
        holder.mExamTimeTextView.setText("时间　 : " + exam.getTrueTime());

    }

    @Override
    public int getItemCount() {
        if (mExams == null) return 0;
        return mExams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.examNameTextView)
        TextView mExamNameTextView;
        @BindView(R.id.examTimeTextView)
        TextView mExamTimeTextView;
        @BindView(R.id.examRoomTextView)
        TextView mExamRoomTextView;
        @BindView(R.id.examPositionTextView)
        TextView mExamPositionTextView;
        @BindView(R.id.examStateTextView)
        TextView mExamStateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
