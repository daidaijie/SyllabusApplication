package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.CommentInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/10.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Activity mActivity;

    private CommentInfo mCommentInfo;


    public CommentAdapter(Activity activity, CommentInfo commentInfo) {
        mActivity = activity;
        mCommentInfo = commentInfo;
    }

    public void setCommentInfo(CommentInfo commentInfo) {
        mCommentInfo = commentInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        if (mCommentInfo == null) return 0;
        return mCommentInfo.getComments().size();
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
