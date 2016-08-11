package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.liaoinstan.springview.utils.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/10.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Activity mActivity;

    private CommentInfo mCommentInfo;

    private View mHeaderView;

    private View mFooterView;

    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    public CommentAdapter(Activity activity, CommentInfo commentInfo) {
        mActivity = activity;
        mCommentInfo = commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        mCommentInfo = commentInfo;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (mHeaderView != null && position == 0) {
            return TYPE_HEADER;
        }
        if (mFooterView != null && position == getItemCount() - 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        }

        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (mHeaderView != null) position -= 1;

            if (position == 0) {
                holder.mCommentTitle.setVisibility(View.VISIBLE);
            }

            CommentInfo.CommentsBean comment = mCommentInfo.getComments().get(position);
            holder.mHeadImageDraweeView.setImageURI(
                    Uri.parse("res://" + mActivity.getPackageName()
                            + "/" + R.drawable.ic_syllabus_icon)
            );
            CommentInfo.CommentsBean.UserBean user = comment.getUser();
            holder.mNicknameTextView.setText(
                    user.getNickname().trim().isEmpty() ? user.getAccount() : user.getNickname()
            );

            holder.mTimeTextView.setText(comment.getPost_time());
            holder.mContentTextView.setText(comment.getComment());

            holder.mDevLine.setVisibility(position == (mCommentInfo.getComments().size() - 1) ?
                    View.VISIBLE : View.INVISIBLE);
        } else if (getItemViewType(position) == TYPE_HEADER) {
            return;
        } else {
            return;
        }

    }

    @Override
    public int getItemCount() {
        if (mCommentInfo == null) return 1;
        if (mHeaderView == null && mFooterView == null) {
            return mCommentInfo.getComments().size();
        } else if (mHeaderView == null && mFooterView != null) {
            return mCommentInfo.getComments().size() + 1;
        } else if (mHeaderView != null && mFooterView == null) {
            return mCommentInfo.getComments().size() + 1;
        } else {
            return mCommentInfo.getComments().size() + 2;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.headImageDraweeView)
        SimpleDraweeView mHeadImageDraweeView;
        @BindView(R.id.nicknameTextView)
        TextView mNicknameTextView;
        @BindView(R.id.timeTextView)
        TextView mTimeTextView;
        @BindView(R.id.contentTextView)
        TextView mContentTextView;
        @BindView(R.id.devLine)
        View mDevLine;
        @BindView(R.id.commentTitle)
        TextView mCommentTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            if (itemView == mHeaderView) {
                return;
            }
            if (itemView == mFooterView) {
                return;
            }
            ButterKnife.bind(this, itemView);
        }
    }
}
