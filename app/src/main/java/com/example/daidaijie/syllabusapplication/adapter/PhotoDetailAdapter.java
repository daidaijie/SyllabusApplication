package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PhotoDetailAdapter extends PagerAdapter {


    private Activity mActivity;

    private List<String> mPhotoInfo;


    public interface OnPhotoLongClickListener {
        void onLongClick(int position);
    }

    OnPhotoLongClickListener mOnPhotoLongClickListener;

    public OnPhotoLongClickListener getOnPhotoLongClickListener() {
        return mOnPhotoLongClickListener;
    }

    public void setOnPhotoLongClickListener(OnPhotoLongClickListener onPhotoLongClickListener) {
        mOnPhotoLongClickListener = onPhotoLongClickListener;
    }

    public PhotoDetailAdapter(Activity activity, List<String> photoInfo) {
        mActivity = activity;
        mPhotoInfo = photoInfo;
    }

    @Override
    public int getCount() {
        return mPhotoInfo.size();
    }

    public List<String> getPhotoInfo() {
        return mPhotoInfo;
    }

    public void setPhotoInfo(List<String> photoInfo) {
        mPhotoInfo = photoInfo;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(mActivity);
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(mPhotoInfo.get(position));
        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());

        try {
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnPhotoLongClickListener != null) {
                    mOnPhotoLongClickListener.onLongClick(position);
                }
                return true;
            }
        });

        return photoDraweeView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
