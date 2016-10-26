package com.example.daidaijie.syllabusapplication.adapter;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/7/30.
 */
public class StudentInfoAdapter extends RecyclerView.Adapter<StudentInfoAdapter.ViewHolder> {

    AppCompatActivity mActivity;

    private List<StudentInfo> mStudentInfos;


    public StudentInfoAdapter(AppCompatActivity activity, List<StudentInfo> studentInfos) {
        mActivity = activity;
        mStudentInfos = studentInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_student_info, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StudentInfo studentInfo = mStudentInfos.get(position);
        holder.mStudentNameTextView.setText(studentInfo.getName());
        holder.mStudentNoTextView.setText(studentInfo.getNumber());
        holder.mStudentMajorTextView.setText(studentInfo.getMajor());

        GradientDrawable shape = (GradientDrawable) mActivity.getResources()
                .getDrawable(R.drawable.bg_sex);
        shape.setColor(mActivity.getResources().getColor(
                studentInfo.getGender().equals("男") ?
                        R.color.material_blue_300 : R.color.material_pink_300));
        holder.mSexView.setBackgroundDrawable(shape);
        holder.mDevLine.setVisibility(position == (mStudentInfos.size() - 1)
                ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        if (mStudentInfos == null) {
            return 0;
        }
        return mStudentInfos.size();
    }

    //设置Item点击回调接口
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public List<StudentInfo> getStudentInfos() {
        return mStudentInfos;
    }

    public void setStudentInfos(List<StudentInfo> studentInfos) {
        mStudentInfos = studentInfos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.studentNameTextView)
        TextView mStudentNameTextView;
        @BindView(R.id.studentNoTextView)
        TextView mStudentNoTextView;
        @BindView(R.id.studentMajorTextView)
        TextView mStudentMajorTextView;
        @BindView(R.id.sexView)
        View mSexView;
        @BindView(R.id.devLine)
        View mDevLine;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


