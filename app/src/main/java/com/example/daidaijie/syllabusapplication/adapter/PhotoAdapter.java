package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.StuCircleFragment;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {


    private Activity mActivity;

    private PhotoInfo mPhotoInfo;

    public PhotoAdapter(Activity activity, PhotoInfo photoInfo) {
        mActivity = activity;
        mPhotoInfo = photoInfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View view = inflater.inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoInfo.PhotoListBean photoBean = mPhotoInfo.getPhoto_list().get(position);
        Log.d(StuCircleFragment.TAG, "onBindViewHolder: "+photoBean.getSize_small());
        holder.mPhotoSimpleDraweeView.setImageURI(photoBean.getSize_small());
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
