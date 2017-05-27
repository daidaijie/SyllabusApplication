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
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.exam.detail.ExamDetailActivity;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/11.
 */
public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {


    private Activity mActivity;

    private List<Exam> mExams;

    private String weeks = "日一二三四五六";

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Exam exam = mExams.get(position);
        holder.mExamNameTextView.setText(exam.getTrueName());
        holder.mExamPositionTextView.setText("座位号 : " + exam.getExam_stu_position());
        holder.mExamRoomTextView.setText("试室　 : " + exam.getExam_location());

        DateTime now = DateTime.now();
        DateTime examTime = exam.getExamTime();

        if (DateTimeComparator.getInstance().compare(examTime, now) > 0) {
            Period period = new Period(now, examTime);
            StringBuilder sb = new StringBuilder("倒计时\n");
            int days = period.getWeeks() * 7 + period.getDays();
            if (period.getYears() != 0) {
                sb.append(period.getYears() + "年");
                sb.append(period.getMonths() + "月");
                sb.append(days + "天\n");
            } else {
                if (period.getMonths() != 0) {
                    sb.append(period.getMonths() + "月");
                    sb.append(days + "天\n");
                } else {
                    if (days != 0) {
                        sb.append(days + "天\n");
                    }
                }
            }
            sb.append(String.format("%02d:%02d",
                    period.getHours(), period.getMinutes()));

            SpannableStringBuilder style = new SpannableStringBuilder(sb);
            style.setSpan(new ForegroundColorSpan(
                            mActivity.getResources().getColor(R.color.defaultShowColor)),
                    0, 4, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            style.setSpan(new ForegroundColorSpan(
                            ThemeUtil.getInstance().colorPrimary),
                    4, sb.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            holder.mExamStateTextView.setText(style);
        } else {
            holder.mExamStateTextView.setTextColor(
                    mActivity.getResources().getColor(R.color.defaultShowColor));
            holder.mExamStateTextView.setText("已结束");
        }

        holder.mExamTimeTextView.setText("时间　 : " + exam.getTrueTime());
        holder.mExamWeekTextView.setText("　　　   " + exam.getTruePreTime());

        holder.mExamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ExamDetailActivity.getIntent(mActivity, position);
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
        @BindView(R.id.examWeekTextView)
        TextView mExamWeekTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
