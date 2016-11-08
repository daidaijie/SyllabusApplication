package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;
import com.example.daidaijie.syllabusapplication.other.CommonWebActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail.SchoolDymaticDetailActivity;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.widget.ThumbUpView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/9/15.
 */
public class SchoolDymaticAdapter extends RecyclerView.Adapter<SchoolDymaticAdapter.ViewHolder> {

    private Activity mActivity;
    private List<SchoolDymatic> mSchoolDymatics;

    private int mWidth;

    //判断是列表还是详情，列表是false，详情是true
    private boolean isOnlyOne;

    public SchoolDymaticAdapter(Activity activity, List<SchoolDymatic> schoolDymatics) {
        mActivity = activity;
        mSchoolDymatics = schoolDymatics;
        isOnlyOne = false;
    }

    public SchoolDymaticAdapter(Activity activity, List<SchoolDymatic> schoolDymatics, int width) {
        mActivity = activity;
        mSchoolDymatics = schoolDymatics;
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


    public List<SchoolDymatic> getSchoolDymatics() {
        return mSchoolDymatics;
    }

    public void setSchoolDymatics(List<SchoolDymatic> schoolDymatics) {
        mSchoolDymatics = schoolDymatics;
    }

    public interface OnLikeStateChangeListener {
        void onLike(boolean isLike);

        void onFinish();
    }

    public interface OnLikeCallBack {
        void onLike(int position, boolean isLike, OnLikeStateChangeListener onLikeStateChangeListener);
    }


    OnLikeCallBack mOnLikeCallBack;

    public OnLikeCallBack getOnLikeCallBack() {
        return mOnLikeCallBack;
    }

    public void setOnLikeCallBack(OnLikeCallBack onLikeCallBack) {
        mOnLikeCallBack = onLikeCallBack;
    }

    public static final int MODE_ITEM_CLICK = 0;
    public static final int MODE_TEXT_CLICK = 1;

    public interface OnLongClickCallBack {
        void onLongClick(int position, int mode);
    }

    OnLongClickCallBack mOnLongClickCallBack;

    public OnLongClickCallBack getOnLongClickCallBack() {
        return mOnLongClickCallBack;
    }

    public void setOnLongClickCallBack(OnLongClickCallBack onLongClickCallBack) {
        mOnLongClickCallBack = onLongClickCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_school_dymamic, parent, false);

        //没传入就直接计算
        if (mWidth == 0) {
            mWidth = parent.getWidth() - DensityUtil.dip2px(mActivity, 48 + 1) + 2;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SchoolDymatic schoolDymatic = mSchoolDymatics.get(position);

        holder.mNicknameTextView.setText(schoolDymatic.getSource());

        holder.mPostTimeTextView.setText("发布时间: " + schoolDymatic.getPost_time());

        if (schoolDymatic.getActivity_start_time() != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(schoolDymatic.getActivity_start_time().substring(0, schoolDymatic.getActivity_start_time().length() - 3));
            if (schoolDymatic.getActivity_end_time() != null && !schoolDymatic.getActivity_end_time().trim().isEmpty()) {
                sb.append(" —> ");
                sb.append(schoolDymatic.getActivity_end_time().substring(0, schoolDymatic.getActivity_end_time().length() - 3));
            }
            holder.mPostInfoTextView.setText("活动时间: " + sb.toString());
        } else {
            holder.mPostInfoTextView.setVisibility(View.GONE);
        }


        if (schoolDymatic.getActivity_location() == null || schoolDymatic.getActivity_location().isEmpty()
                || schoolDymatic.getActivity_location().equals("未指定")) {
            holder.mPostLocateTextView.setVisibility(View.GONE);
        } else {
            holder.mPostLocateTextView.setVisibility(View.VISIBLE);
            holder.mPostLocateTextView.setText("活动地点: " + schoolDymatic.getActivity_location());
        }

        holder.mContentTextView.setText(schoolDymatic.getDescription());

        mWidth = mWidth > holder.mContentTextView.getWidth() ? mWidth : holder.mContentTextView.getWidth();

        if (schoolDymatic.getPhoto_list_json() != null && !schoolDymatic.getPhoto_list_json().isEmpty()) {
            holder.mPhotoRecyclerView.setVisibility(View.VISIBLE);
            PhotoInfo photoInfo = GsonUtil.getDefault()
                    .fromJson(schoolDymatic.getPhoto_list_json(), PhotoInfo.class);
            PhotoAdapter photoAdapter = new PhotoAdapter(mActivity, photoInfo,
                    mWidth);
            holder.mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,
                    LinearLayoutManager.HORIZONTAL, false));
            holder.mPhotoRecyclerView.setAdapter(photoAdapter);
        } else {
            holder.mPhotoRecyclerView.setVisibility(View.GONE);
        }

        holder.mZanTextView.setText("赞 [" + schoolDymatic.getThumb_ups().size() + "]");
        holder.mCommentTextView.setText("评论 [" + schoolDymatic.getComments().size() + "]");

        /**
         * 点赞
         */
        holder.mThumbUpView.setEnabled(false);

        if (schoolDymatic.isMyLove) {
            holder.mThumbUpView.setLike();
        } else {
            holder.mThumbUpView.setUnLike();
        }

        holder.mThumbUpLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLikeCallBack != null) {
                    holder.mZanTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultDarkBackgroundSelect));
                    holder.mThumbUpLinearLayout.setEnabled(false);
                    mOnLikeCallBack.onLike(position, !schoolDymatic.isMyLove, new OnLikeStateChangeListener() {
                        @Override
                        public void onLike(boolean isLike) {
                            if (isLike) {
                                holder.mThumbUpView.Like();
                            } else {
                                holder.mThumbUpView.UnLike();
                            }
                        }

                        @Override
                        public void onFinish() {
                            holder.mZanTextView.setTextColor(mActivity.getResources().getColor(R.color.defaultShowColor));
                            holder.mZanTextView.setText("赞 [" + schoolDymatic.getThumb_ups().size() + "]");
                            holder.mThumbUpLinearLayout.setEnabled(true);
                        }
                    });
                }
            }
        });

        holder.mWebLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (schoolDymatic.getContent() != null && !schoolDymatic.getContent().isEmpty()) {
                    Intent intent = CommonWebActivity.getIntent(mActivity, schoolDymatic.getContent(), "推文详情");
                    mActivity.startActivity(intent);
                } else {
                    Toast.makeText(mActivity, "本篇无推文", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!isOnlyOne) {
            holder.mItemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SchoolDymaticDetailActivity.getIntent(mActivity, position, mWidth);
                    mActivity.startActivity(intent);
                }
            });
            holder.mCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SchoolDymaticDetailActivity.getIntent(mActivity, position, mWidth, true);
                    mActivity.startActivity(intent);
                }
            });
            holder.mContentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = SchoolDymaticDetailActivity.getIntent(mActivity, position, mWidth);
                    mActivity.startActivity(intent);
                }
            });

        } else {
            holder.mItemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.mCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentListener.onComment();
                }
            });
        }

        holder.mContentTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickCallBack != null) {
                    mOnLongClickCallBack.onLongClick(position, MODE_TEXT_CLICK);
                }
                return true;
            }
        });

        holder.mItemCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnLongClickCallBack != null) {
                    mOnLongClickCallBack.onLongClick(position, MODE_ITEM_CLICK);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mSchoolDymatics == null) {
            return 0;
        } else {
            return mSchoolDymatics.size();
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
        @BindView(R.id.postTimeTextView)
        TextView mPostTimeTextView;
        @BindView(R.id.postLocateTextView)
        TextView mPostLocateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
