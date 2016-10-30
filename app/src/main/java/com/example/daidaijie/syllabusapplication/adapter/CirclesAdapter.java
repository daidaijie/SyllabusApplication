package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.bean.PostUserBean;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail.CircleDetailActivity;
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.widget.ThumbUpView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class CirclesAdapter extends RecyclerView.Adapter<CirclesAdapter.ViewHolder> {

    public static final String TAG = "CirclesAdapter";

    Activity mActivity;

    private List<PostListBean> mPostListBeen;

    private int mWidth;

    //判断是列表还是详情，列表是false，详情是true
    private boolean isOnlyOne;

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

    public CirclesAdapter(Activity activity, List<PostListBean> postListBeen) {
        mActivity = activity;
        mPostListBeen = postListBeen;
        isOnlyOne = false;
    }

    public CirclesAdapter(Activity activity, List<PostListBean> postListBeen, int width) {
        mActivity = activity;
        mPostListBeen = postListBeen;
        mWidth = width;
        isOnlyOne = true;
    }

    public void setPostListBeen(List<PostListBean> postListBeen) {
        mPostListBeen = postListBeen;
    }


    public List<PostListBean> getPostListBeen() {
        return mPostListBeen;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_circle, parent, false);

        //没传入就直接计算
        if (mWidth == 0) {
            mWidth = parent.getWidth() - DensityUtil.dip2px(mActivity, 48 + 1) + 2;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PostListBean postBean = mPostListBeen.get(position);

        PostUserBean user = postBean.getUser();

        holder.mHeadImageDraweeView.setImageURI(user.getImage());
        holder.mNicknameTextView.setText(user.getNickname());

        holder.mPostInfoTextView.setText(postBean.getPost_time());
        if (postBean.getSource() != null) {
            String str = "来自 " + postBean.getSource();
            SpannableStringBuilder style = new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(ThemeUtil.getInstance().colorPrimary),
                    3, str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            holder.mPostDeviceTextView.setText(style);
        } else {
            holder.mPostDeviceTextView.setText("来自 火星");
        }

        holder.mContentTextView.setText(postBean.getContent());
        holder.mZanTextView.setText("赞 [" + postBean.getThumb_ups().size() + "]");
        holder.mCommentTextView.setText("评论 [" + postBean.getComments().size() + "]");

        mWidth = mWidth > holder.mContentTextView.getWidth() ? mWidth : holder.mContentTextView.getWidth();

        if (postBean.getPhoto_list_json() != null && !postBean.getPhoto_list_json().isEmpty()) {
            holder.mPhotoRecyclerView.setVisibility(View.VISIBLE);
            PhotoInfo photoInfo = GsonUtil.getDefault()
                    .fromJson(postBean.getPhoto_list_json(), PhotoInfo.class);
            PhotoAdapter photoAdapter = new PhotoAdapter(mActivity, photoInfo,
                    mWidth);
            holder.mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,
                    LinearLayoutManager.HORIZONTAL, false));
            holder.mPhotoRecyclerView.setAdapter(photoAdapter);
        } else {
            holder.mPhotoRecyclerView.setVisibility(View.GONE);
        }

        /**
         * 点赞
         */
        holder.mThumbUpView.setEnabled(false);

        if (postBean.isMyLove) {
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
                    mOnLikeCallBack.onLike(position, !postBean.isMyLove, new OnLikeStateChangeListener() {
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
                            holder.mZanTextView.setText("赞 [" + postBean.getThumb_ups().size() + "]");
                            holder.mThumbUpLinearLayout.setEnabled(true);
                        }
                    });
                }
            }
        });


        if (!isOnlyOne) {
            holder.mItemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CircleDetailActivity.getIntent(mActivity, position, mWidth);
                    mActivity.startActivity(intent);
                }
            });
            holder.mCommentLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CircleDetailActivity.getIntent(mActivity, position, mWidth, true);
                    mActivity.startActivity(intent);
                }
            });
            holder.mContentTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = CircleDetailActivity.getIntent(mActivity, position, mWidth);
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

//        String[] items = {"复制"};
//        AlertDialog dialog = new AlertDialog.Builder(mActivity)
//                .setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            ClipboardUtil.copyToClipboard(holder.mContentTextView.getText().toString());
//                        }
//                    }
//                })
//                .create();
//        dialog.show();
    }


    @Override
    public int getItemCount() {
        if (mPostListBeen == null) {
            return 0;
        }
        return mPostListBeen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.headImageDraweeView)
        SimpleDraweeView mHeadImageDraweeView;
        @BindView(R.id.nicknameTextView)
        TextView mNicknameTextView;
        @BindView(R.id.postInfoTextView)
        TextView mPostInfoTextView;
        @BindView(R.id.contentTextView)
        TextView mContentTextView;
        @BindView(R.id.photoRecyclerView)
        RecyclerView mPhotoRecyclerView;
        @BindView(R.id.zanTextView)
        TextView mZanTextView;
        @BindView(R.id.commentTextView)
        TextView mCommentTextView;
        @BindView(R.id.postDeviceTextView)
        TextView mPostDeviceTextView;
        @BindView(R.id.thumbUpView)
        ThumbUpView mThumbUpView;
        @BindView(R.id.thumbUpLinearLayout)
        LinearLayout mThumbUpLinearLayout;
        @BindView(R.id.itemCardView)
        CardView mItemCardView;
        @BindView(R.id.commentLinearLayout)
        LinearLayout mCommentLinearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
