package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/10.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Activity mActivity;

    private List<CommentInfo.CommentsBean> mCommentsBeen;

    public interface onCommentListener {
        void onComment(int position);
    }

    private onCommentListener mCommentListener;

    public onCommentListener getCommentListener() {
        return mCommentListener;
    }

    public void setCommentListener(onCommentListener commentListener) {
        mCommentListener = commentListener;
    }

    public List<CommentInfo.CommentsBean> getCommentsBeen() {
        return mCommentsBeen;
    }

    public void setCommentsBeen(List<CommentInfo.CommentsBean> commentsBeen) {
        mCommentsBeen = commentsBeen;
    }

    public CommentAdapter(Activity activity, List<CommentInfo.CommentsBean> commentsBeen) {
        mActivity = activity;
        mCommentsBeen = commentsBeen;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == 0) {
            holder.mCommentTitle.setVisibility(View.VISIBLE);
        }

        CommentInfo.CommentsBean comment = mCommentsBeen.get(position);
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

        holder.mDevLine.setVisibility(position == (mCommentsBeen.size() - 1) ?
                View.VISIBLE : View.INVISIBLE);
        final int finalPosition = position;

        holder.mCommentContextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommentListener.onComment(finalPosition);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mCommentsBeen == null) {
            return 0;
        }
        return mCommentsBeen.size();
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
        @BindView(R.id.commentContextLayout)
        RelativeLayout mCommentContextLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
