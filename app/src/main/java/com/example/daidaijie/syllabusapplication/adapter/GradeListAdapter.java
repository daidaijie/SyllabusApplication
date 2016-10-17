package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.GradeBean;
import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

/**
 * Created by daidaijie on 2016/8/20.
 */
public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.ViewHolder> {

    private Activity mActivity;

    private List<SemesterGrade> mSemesterGrades;

    public interface OnExpandChangeListener {
        void onExpandChange(int position, boolean isExpand);
    }

    OnExpandChangeListener mChangeListener;

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public OnExpandChangeListener getChangeListener() {
        return mChangeListener;
    }

    public void setChangeListener(OnExpandChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public GradeListAdapter(Activity activity, List<SemesterGrade> semesterGrades) {
        mActivity = activity;
        mSemesterGrades = semesterGrades;
    }

    public List<SemesterGrade> getSemesterGrades() {
        return mSemesterGrades;
    }

    public void setSemesterGrades(List<SemesterGrade> semesterGrades) {
        mSemesterGrades = semesterGrades;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_semester_grade, parent, false);

        return new ViewHolder(view);
    }

    public void setAllIsExpand(boolean isExpand) {
        for (SemesterGrade grades : mSemesterGrades) {
            grades.setExpand(isExpand);
        }
        this.notifyDataSetChanged();
    }

    public boolean getAllNotExpand() {
        boolean isNotExpand = true;
        for (SemesterGrade grades : mSemesterGrades) {
            if (grades.getExpand()) {
                isNotExpand = false;
                break;
            }
        }
        return isNotExpand;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SemesterGrade semesterGrade = mSemesterGrades.get(position);
        RealmList<GradeBean> gradeBeen = semesterGrade.getGradeBeen();
        if (gradeBeen.size() != 0) {
            GradeBean firstGradeBean = gradeBeen.get(0);
            holder.mSemesterTextView.setText(
                    firstGradeBean.getYears() + " " + firstGradeBean.getSemester());

            holder.mSumNumTextView.setText(gradeBeen.size() + "");
            holder.mSumCreditTextView.setText(String.format("%.1f", semesterGrade.getCredit()));
            holder.mSumGpaTextView.setText(String.format("%.2f", semesterGrade.getGpa()));


            holder.mGradeLinearLayout.removeAllViews();

            for (int i = 0; i <= gradeBeen.size(); i++) {
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                View view = inflater.inflate(R.layout.item_grade, null, false);
                TextView mGradeNameTextView = (TextView) view.findViewById(R.id.gradeNameTextView);
                TextView mGradeTextView = (TextView) view.findViewById(R.id.gradeTextView);
                TextView mCreditTextView = (TextView) view.findViewById(R.id.creditTextView);
                View mDivLine = view.findViewById(R.id.div_line);
                mDivLine.setVisibility((i == gradeBeen.size()) ? View.INVISIBLE : View.VISIBLE);

                if (i == 0) {
                    mGradeNameTextView.setText(Html.fromHtml("<b>课程名</b>"));
                    mGradeTextView.setText(Html.fromHtml("<b>成绩</b>"));
                    mCreditTextView.setText(Html.fromHtml("<b>学分</b>"));
                } else {
                    GradeBean gradeBean = gradeBeen.get(i - 1);
                    mGradeNameTextView.setText(gradeBean.getTrueName());
                    mGradeTextView.setText(gradeBean.getClass_grade());
                    mCreditTextView.setText(gradeBean.getClass_credit());
                }

                holder.mGradeLinearLayout.addView(view);
            }

            if (semesterGrade.getExpand()) {
                holder.mExtendCardView.setRotation(0.0f);
                holder.mGradeLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mExtendCardView.setRotation(180.0f);
                holder.mGradeLinearLayout.setVisibility(View.GONE);
            }

            holder.mExtendCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (semesterGrade.getExpand()) {
                        holder.mExtendCardView.setRotation(180.0f);
                        holder.mGradeLinearLayout.setVisibility(View.GONE);
                    } else {
                        holder.mExtendCardView.setRotation(0.0f);
                        holder.mGradeLinearLayout.setVisibility(View.VISIBLE);
                    }
                    semesterGrade.setExpand(!semesterGrade.getExpand());
                    if (mChangeListener != null) {
                        mChangeListener.onExpandChange(position, semesterGrade.getExpand());
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mSemesterGrades == null) {
            return 0;
        }
        return mSemesterGrades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sumNumTextView)
        TextView mSumNumTextView;
        @BindView(R.id.sumCreditTextView)
        TextView mSumCreditTextView;
        @BindView(R.id.sumGpaTextView)
        TextView mSumGpaTextView;
        @BindView(R.id.semesterTextView)
        TextView mSemesterTextView;
        @BindView(R.id.gradeLinearLayout)
        LinearLayout mGradeLinearLayout;
        @BindView(R.id.extendCardView)
        CardView mExtendCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
