package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/30.
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {


    private Activity mActivity;

    private int selectItem;

    public interface onItemClickListener {
        void onClick(int position);
    }

    onItemClickListener mOnItemClickListener;

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    public onItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public WeekAdapter(Activity activity) {
        mActivity = activity;
        selectItem = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);

        View view = inflater.inflate(R.layout.item_week_select, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mWeekTextView.setText(position + 1 + "");
        holder.mDivLine.setVisibility(position == 15 ? View.INVISIBLE : View.VISIBLE);

        holder.mWeekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });

        if (position == selectItem) {
            holder.mWeekTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.bg_select_week));
            holder.mWeekTextView.setTextColor(mActivity.getResources().getColor(R.color.material_white));
        }else{
            holder.mWeekTextView.setBackground(null);
            holder.mWeekTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultShowColor));
        }
    }

    @Override
    public int getItemCount() {
        return 16;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.weekLayout)
        RelativeLayout mWeekLayout;
        @BindView(R.id.weekTextView)
        TextView mWeekTextView;
        @BindView(R.id.div_line)
        View mDivLine;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
