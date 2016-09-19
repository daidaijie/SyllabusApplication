package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.AddLessonModel;
import com.example.daidaijie.syllabusapplication.widget.LessonDetailLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/20.
 */
public class LessonTimeAdapter extends RecyclerView.Adapter<LessonTimeAdapter.ViewHolder> {

    private Activity mActivity;
    private List<AddLessonModel.SelectTime> mTimes;

    public LessonTimeAdapter(Activity activity, List<AddLessonModel.SelectTime> times) {
        mActivity = activity;
        mTimes = times;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public List<AddLessonModel.SelectTime> getTimes() {
        return mTimes;
    }

    public void setTimes(List<AddLessonModel.SelectTime> times) {
        mTimes = times;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_lesson_time, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AddLessonModel.SelectTime time = mTimes.get(position);
        holder.mLessonTimeLayout.setTitleText(Html.fromHtml(time.toHtmlString()));
    }

    @Override
    public int getItemCount() {
        if (mTimes == null) {
            return 0;
        }
        return mTimes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lessonTimeLayout)
        LessonDetailLayout mLessonTimeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
