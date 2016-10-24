package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.example.daidaijie.syllabusapplication.widget.CallPhoneDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/25.
 */

public class TakeOutMenuAdapter extends RecyclerView.Adapter<TakeOutMenuAdapter.ViewHolder> {

    Activity mActivity;

    List<TakeOutInfoBean> mTakeOutInfoBeen;

    public interface OnItemClickListener {
        void onItemClick(String objectID);
    }

    OnItemClickListener mOnItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public TakeOutMenuAdapter(Activity activity, List<TakeOutInfoBean> takeOutInfoBeen) {
        mActivity = activity;
        mTakeOutInfoBeen = takeOutInfoBeen;
    }

    public List<TakeOutInfoBean> getTakeOutInfoBeen() {
        return mTakeOutInfoBeen;
    }

    public void setTakeOutInfoBeen(List<TakeOutInfoBean> takeOutInfoBeen) {
        mTakeOutInfoBeen = takeOutInfoBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_takeout_menu, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TakeOutInfoBean takeOutInfoBean = mTakeOutInfoBeen.get(position);
        holder.mTakeoutNameTextView.setText(takeOutInfoBean.getName());
        holder.mLongNumberTextView.setText("长号 : " + takeOutInfoBean.getLong_number());
        holder.mShortNumberTextView.setText("短号 : " + takeOutInfoBean.getShort_number());
        holder.mConditionTextView.setText("备注 : " + takeOutInfoBean.getCondition());

        holder.mCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mOnItemClickListener.onItemClick(takeOutInfoBean.getObjectId());
            }
        });

        holder.mCallPhoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takeOutInfoBean.getPhoneList().length == 0) return;
                AlertDialog dialog = CallPhoneDialog.
                        createDialog(mActivity, takeOutInfoBean.getPhoneList());
                dialog.show();
            }
        });

        holder.mBuyNumTextView.setText(takeOutInfoBean.getTakeOutBuyBean().getNum() + "");
        if (takeOutInfoBean.getTakeOutBuyBean().getNum() == 0) {
            holder.mBuyNumTextView.setVisibility(View.GONE);
        } else {
            holder.mBuyNumTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (mTakeOutInfoBeen == null) {
            return 0;
        }
        return mTakeOutInfoBeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.takeoutNameTextView)
        TextView mTakeoutNameTextView;
        @BindView(R.id.longNumberTextView)
        TextView mLongNumberTextView;
        @BindView(R.id.shortNumberTextView)
        TextView mShortNumberTextView;
        @BindView(R.id.conditionTextView)
        TextView mConditionTextView;
        @BindView(R.id.cardItem)
        CardView mCardItem;
        @BindView(R.id.callPhoneFab)
        FloatingActionButton mCallPhoneFab;
        @BindView(R.id.buyNumTextView)
        TextView mBuyNumTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
