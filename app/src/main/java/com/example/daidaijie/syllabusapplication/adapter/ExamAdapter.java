package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.ExamDetailActivity;
import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.util.List;

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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Exam exam = mExams.get(position);
        holder.mExamNameTextView.setText(exam.getTrueName());
        holder.mExamPositionTextView.setText("座位号 : " + exam.getExam_stu_position());
        holder.mExamRoomTextView.setText("试室　 : " + exam.getExam_location());
        holder.mExamTimeTextView.setText("时间　 : " + exam.getTrueTime());

        /*holder.mExamStateTextView.setTextColor(
                mActivity.getResources().getColor(R.color.defaultShowColor));
        holder.mExamStateTextView.setText("已结束");*/

        String str = "倒计时\n" + "1天\n12:09:10";
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(
                        mActivity.getResources().getColor(R.color.defaultShowColor)),
                0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        style.setSpan(new ForegroundColorSpan(
                        mActivity.getResources().getColor(R.color.colorPrimaryDark)),
                4, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        holder.mExamStateTextView.setText(style);

        holder.mExamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ExamDetailActivity.getIntent(mActivity, exam);
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mExams == null) return 0;
        return mExams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.examLayout)
        RelativeLayout mExamLayout;
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
