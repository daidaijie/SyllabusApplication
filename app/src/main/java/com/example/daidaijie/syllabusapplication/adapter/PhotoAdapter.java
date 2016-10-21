package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.other.PhotoDetailActivity;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


    private Activity mActivity;

    private PhotoInfo mPhotoInfo;

    private int mWidth;

    public PhotoAdapter(Activity activity, PhotoInfo photoInfo, int width) {
        mActivity = activity;
        mPhotoInfo = photoInfo;
        mWidth = width;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        //之前显示不出来是因为没设置item_photo的大小
        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        PhotoInfo.PhotoListBean photoBean = mPhotoInfo.getPhoto_list().get(position);

        int width = mWidth / getItemCount() - DensityUtil.dip2px(mActivity,2);

        ViewGroup.LayoutParams layoutParams = holder.mPhotoSimpleDraweeView.getLayoutParams();
        layoutParams.width = width;

        holder.mPhotoSimpleDraweeView.setImageURI(photoBean.getSize_small());

        holder.mPhotoSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PhotoDetailActivity.getIntent(mActivity,
                        mPhotoInfo.getBigUrls(), position);
                mActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhotoInfo.getPhoto_list().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photoSimpleDraweeView)
        SimpleDraweeView mPhotoSimpleDraweeView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
