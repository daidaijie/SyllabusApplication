package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Dishes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/26.
 */

public class DishesAdapter extends RecyclerView.Adapter<DishesAdapter.ViewHolder> {

    public static final int FIRST_STICKY_VIEW = 1;
    public static final int HAS_STICKY_VIEW = 2;
    public static final int NONE_STICKY_VIEW = 3;

    Activity mActivity;

    List<Dishes> mDishesList;


    public DishesAdapter(Activity activity, List<Dishes> dishesList) {
        mActivity = activity;
        mDishesList = dishesList;
    }

    public List<Dishes> getDishesList() {
        return mDishesList;
    }

    public void setDishesList(List<Dishes> dishesList) {
        mDishesList = dishesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_dishes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Dishes dishes = mDishesList.get(position);
        holder.mDishesNameTextView.setText(dishes.getName());
        holder.mDishesPriceTextView.setText(dishes.getPrice());

        if (position == 0) {
            holder.mTvStickyHeaderView.setVisibility(View.VISIBLE);
            holder.mStickyTextView.setText(dishes.sticky);
            holder.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            if (!TextUtils.equals(dishes.sticky, mDishesList.get(position - 1).sticky)) {
                holder.mTvStickyHeaderView.setVisibility(View.VISIBLE);
                holder.mStickyTextView.setText(dishes.sticky);
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                holder.mTvStickyHeaderView.setVisibility(View.GONE);
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
        }

        holder.mDivLine.setVisibility(View.VISIBLE);

        if (position > 0 && position < mDishesList.size() - 1) {
            if (!TextUtils.equals(dishes.sticky, mDishesList.get(position + 1).sticky)) {
                holder.mDivLine.setVisibility(View.GONE);
            }
        }

        holder.itemView.setContentDescription(dishes.sticky);


    }

    @Override
    public int getItemCount() {
        if (mDishesList == null) return 0;
        return mDishesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_sticky_header_view)
        LinearLayout mTvStickyHeaderView;
        @BindView(R.id.stickyTextView)
        TextView mStickyTextView;
        @BindView(R.id.dishesNameTextView)
        TextView mDishesNameTextView;
        @BindView(R.id.dishesPriceTextView)
        TextView mDishesPriceTextView;
        @BindView(R.id.div_line)
        View mDivLine;
        @BindView(R.id.wrapperLayout)
        RelativeLayout mWrapperLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
