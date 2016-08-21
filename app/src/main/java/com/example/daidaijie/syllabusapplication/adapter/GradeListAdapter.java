package com.example.daidaijie.syllabusapplication.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.GradeInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/20.
 */
public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.ViewHolder> {

    private Activity mActivity;

    private GradeInfo mGradeInfo;

    public GradeListAdapter(Activity activity, GradeInfo gradeInfo) {
        mActivity = activity;
        mGradeInfo = gradeInfo;
    }

    public GradeInfo getGradeInfo() {
        return mGradeInfo;
    }

    public void setGradeInfo(GradeInfo gradeInfo) {
        mGradeInfo = gradeInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_semester_grade, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        position = getItemCount() - position - 1;
        List<GradeInfo.GradeBean> gradeBeen = mGradeInfo.getGRADES().get(position);
        final boolean[] isExtend = {mGradeInfo.isExpands.get(position)};
        if (gradeBeen.size() != 0) {
            GradeInfo.GradeBean firstGradeBean = gradeBeen.get(0);
            holder.mSemesterTextView.setText(
                    firstGradeBean.getYears() + " " + firstGradeBean.getSemester());

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
                    mGradeNameTextView.setText("课程名");
                    mGradeTextView.setText("成绩");
                    mGradeTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultTextColor));
                    mCreditTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultTextColor));
                    mCreditTextView.setText("学分");
                } else {
                    GradeInfo.GradeBean gradeBean = gradeBeen.get(i - 1);
                    mGradeNameTextView.setText(gradeBean.getTrueName());
                    mGradeTextView.setText(gradeBean.getClass_grade());
                    mCreditTextView.setText(gradeBean.getClass_credit());
                }

                holder.mGradeLinearLayout.addView(view);
            }

            /*if (isExtend[0]) {
                holder.mExtendFab.setRotation(0.0f);
                holder.mGradeLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mExtendFab.setRotation(45.0f);
                holder.mGradeLinearLayout.setVisibility(View.GONE);
            }

            final int finalPosition = position;
            holder.mExtendFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExtend[0]) {
                        holder.mExtendFab.setRotation(45.0f);
                        holder.mGradeLinearLayout.setVisibility(View.GONE);
                    } else {
                        holder.mExtendFab.setRotation(0.0f);
                        holder.mGradeLinearLayout.setVisibility(View.VISIBLE);
                    }
                    isExtend[0] = !isExtend[0];
                    mGradeInfo.isExpands.set(finalPosition, isExtend[0]);
                }
            });*/
        }

    }

    @Override
    public int getItemCount() {
        if (mGradeInfo == null) {
            return 0;
        }
        return mGradeInfo.getGRADES().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.semesterTextView)
        TextView mSemesterTextView;
        @BindView(R.id.gradeLinearLayout)
        LinearLayout mGradeLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
