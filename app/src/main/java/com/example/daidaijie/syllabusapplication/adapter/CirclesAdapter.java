package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.StuCircleFragment;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class CirclesAdapter extends RecyclerView.Adapter<CirclesAdapter.ViewHolder> {

    Activity mActivity;

    private List<PostListBean> mPostListBeen;

    public CirclesAdapter(Activity activity, List<PostListBean> postListBeen) {
        mActivity = activity;
        mPostListBeen = postListBeen;
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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostListBean postBean = mPostListBeen.get(position);

        PostListBean.PostUserBean user = postBean.getUser();

        if (user.getImage() != null) {
            holder.mHeadImageDraweeView.setImageURI(user.getImage());
        } else {
            holder.mHeadImageDraweeView.setImageURI(
                    Uri.parse("res://" + mActivity.getPackageName()
                            + "/" + R.drawable.ic_syllabus_icon)
            );
        }
        holder.mNicknameTextView.setText(
                user.getNickname().trim().isEmpty() ? user.getAccount() : user.getNickname()
        );
        holder.mPostInfoTextView.setText(postBean.getPost_time()
                + "　来自" + postBean.getSource() + "客户端");
        holder.mContentTextView.setText(postBean.getContent());
        holder.mZanTextView.setText("赞[" + postBean.getThumb_ups().size() + "]");
        holder.mCommentTextView.setText("评论[" + postBean.getComments().size() + "]");

        if (postBean.getPhoto_list_json() != null && !postBean.getPhoto_list_json().isEmpty()) {
            Log.d(StuCircleFragment.TAG, "onBindViewHolder: " + "photoList");
            PhotoInfo photoInfo = GsonUtil.getDefault().fromJson(postBean.getPhoto_list_json(), PhotoInfo.class);
            PhotoAdapter photoAdapter = new PhotoAdapter(mActivity, photoInfo,
                    holder.mPhotoRecyclerView.getWidth());
            holder.mPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity,
                    LinearLayoutManager.HORIZONTAL,false));
            holder.mPhotoRecyclerView.setAdapter(photoAdapter);
            holder.mPhotoRecyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.mPhotoRecyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
