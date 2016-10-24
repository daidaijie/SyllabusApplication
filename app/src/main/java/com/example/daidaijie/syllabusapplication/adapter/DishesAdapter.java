package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Dishes;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;

import java.util.List;
import java.util.Map;

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

    Map<Dishes, Integer> mBuyMap;

    /**
     * mode = 0表示默认状态，1表示是搜索栏的状态
     */
    int mode;

    /**
     * mode=1的时候用到
     */
    String mKeyword;

    public DishesAdapter(Activity activity, List<Dishes> dishesList, Map<Dishes, Integer> buyMap) {
        mActivity = activity;
        mDishesList = dishesList;
        mBuyMap = buyMap;
        mKeyword = "";
        mode = 0;
    }

    public DishesAdapter(Activity activity, List<Dishes> dishesList, Map<Dishes, Integer> buyMap, String keyword) {
        mActivity = activity;
        mDishesList = dishesList;
        mBuyMap = buyMap;
        mKeyword = keyword;
        mode = 1;
    }


    public Map<Dishes, Integer> getBuyMap() {
        return mBuyMap;
    }

    public void setBuyMap(Map<Dishes, Integer> buyMap) {
        mBuyMap = buyMap;
    }

    public List<Dishes> getDishesList() {
        return mDishesList;
    }

    public void setDishesList(List<Dishes> dishesList) {
        mDishesList = dishesList;
    }



    public interface OnNumChangeListener {
        void onAddNum(View v, int position);

        void onReduceNum(int position);
    }

    private OnNumChangeListener mOnNumChangeListener;

    public OnNumChangeListener getOnNumChangeListener() {
        return mOnNumChangeListener;
    }

    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        mOnNumChangeListener = onNumChangeListener;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_dishes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Dishes dishes = mDishesList.get(position);
        holder.mDishesPriceTextView.setText(dishes.getPrice());


        if (mode == 0) {
            holder.mDishesNameTextView.setText(dishes.getName());
        } else {
            if (mKeyword.trim().isEmpty()) {
                holder.mDishesNameTextView.setText(dishes.getName());
            } else {
                SpannableStringBuilder style = new SpannableStringBuilder(dishes.getName());
                int index = dishes.getName().indexOf(mKeyword);
                style.setSpan(new ForegroundColorSpan(ThemeUtil.getInstance().colorPrimary),
                        index, index + mKeyword.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                );

                holder.mDishesNameTextView.setText(style);
            }
        }

        if (mode == 0) {
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
        } else {
            holder.mTvStickyHeaderView.setVisibility(View.GONE);
            holder.itemView.setTag(NONE_STICKY_VIEW);
        }

        holder.mDivLine.setVisibility(View.VISIBLE);

        if (position > 0 && position < mDishesList.size() - 1) {
            if (!TextUtils.equals(dishes.sticky, mDishesList.get(position + 1).sticky)) {
                holder.mDivLine.setVisibility(View.GONE);
            }
        }

        holder.itemView.setContentDescription(dishes.subMenuPos + "");

        GradientDrawable addDrawable = (GradientDrawable) mActivity.getResources().getDrawable(R.drawable.bg_add_dishes);
        GradientDrawable minusDrawable = (GradientDrawable) mActivity.getResources().getDrawable(R.drawable.bg_minus_dishes);

        addDrawable.setColor(ThemeUtil.getInstance().colorPrimary);
        holder.mAddButton.setBackgroundDrawable(addDrawable);

        minusDrawable.setStroke(DensityUtil.dip2px(mActivity, 1), ThemeUtil.getInstance().colorPrimary);
        holder.mMinusButton.setBackgroundDrawable(minusDrawable);

        if (mBuyMap.get(dishes) != null && mBuyMap.get(dishes) > 0) {
            holder.mMinusButton.setVisibility(View.VISIBLE);
            holder.mBuyNumTextView.setVisibility(View.VISIBLE);
            holder.mBuyNumTextView.setText(mBuyMap.get(dishes) + "");
        } else {
            holder.mBuyNumTextView.setText("0");
            holder.mMinusButton.setVisibility(View.GONE);
            holder.mBuyNumTextView.setVisibility(View.GONE);
        }

        holder.mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onAddNum(v, position);
                }
            }
        });

        holder.mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnNumChangeListener != null) {
                    mOnNumChangeListener.onReduceNum(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mDishesList == null) return 0;
        return mDishesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.minusButton)
        ImageButton mMinusButton;
        @BindView(R.id.buyNumTextView)
        TextView mBuyNumTextView;
        @BindView(R.id.addButton)
        ImageButton mAddButton;
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
