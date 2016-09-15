package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.SchoolDynamic;
import com.example.daidaijie.syllabusapplication.widget.ThumbUpView;
import com.liaoinstan.springview.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/15.
 */
public class SchoolDymamicAdapter extends RecyclerView.Adapter<SchoolDymamicAdapter.ViewHolder> {

    private Activity mActivity;
    private List<SchoolDynamic> mSchoolDynamics;

    private int mWidth;

    //判断是列表还是详情，列表是false，详情是true
    private boolean isOnlyOne;

    public SchoolDymamicAdapter(Activity activity, List<SchoolDynamic> schoolDynamics) {
        mActivity = activity;
        mSchoolDynamics = schoolDynamics;
        isOnlyOne = false;
    }

    public SchoolDymamicAdapter(Activity activity, List<SchoolDynamic> schoolDynamics, int width) {
        mActivity = activity;
        mSchoolDynamics = schoolDynamics;
        this.mWidth = width;
        isOnlyOne = true;
    }

    public interface OnCommentListener {
        void onComment();
    }

    public OnCommentListener getCommentListener() {
        return mCommentListener;
    }

    public void setCommentListener(OnCommentListener commentListener) {
        mCommentListener = commentListener;
    }

    OnCommentListener mCommentListener;

    public List<SchoolDynamic> getSchoolDynamics() {
        return mSchoolDynamics;
    }

    public void setSchoolDynamics(List<SchoolDynamic> schoolDynamics) {
        mSchoolDynamics = schoolDynamics;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_school_dymamic, parent, false);

        //没传入就直接计算
        if (mWidth == 0) {
            mWidth = parent.getWidth() - DensityUtil.dip2px(mActivity, 48 + 7f) + 1;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mSchoolDynamics == null) {
            return 0;
        } else {
            return mSchoolDynamics.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nicknameTextView)
        TextView mNicknameTextView;
        @BindView(R.id.postInfoTextView)
        TextView mPostInfoTextView;
        @BindView(R.id.contentTextView)
        TextView mContentTextView;
        @BindView(R.id.photoRecyclerView)
        RecyclerView mPhotoRecyclerView;
        @BindView(R.id.horDivLine)
        View mHorDivLine;
        @BindView(R.id.thumbUpView)
        ThumbUpView mThumbUpView;
        @BindView(R.id.zanTextView)
        TextView mZanTextView;
        @BindView(R.id.thumbUpLinearLayout)
        LinearLayout mThumbUpLinearLayout;
        @BindView(R.id.commentTextView)
        TextView mCommentTextView;
        @BindView(R.id.commentLinearLayout)
        LinearLayout mCommentLinearLayout;
        @BindView(R.id.webTextView)
        TextView mWebTextView;
        @BindView(R.id.webLinearLayout)
        LinearLayout mWebLinearLayout;
        @BindView(R.id.itemCardView)
        CardView mItemCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
