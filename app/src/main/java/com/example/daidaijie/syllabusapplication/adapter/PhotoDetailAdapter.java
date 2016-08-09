package com.example.daidaijie.syllabusapplication.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by daidaijie on 2016/8/9.
 */
public class PhotoDetailAdapter extends PagerAdapter {


    private Activity mActivity;

    private PhotoInfo mPhotoInfo;

    public PhotoDetailAdapter(Activity activity, PhotoInfo photoInfo) {
        mActivity = activity;
        mPhotoInfo = photoInfo;
    }

    @Override
    public int getCount() {
        return mPhotoInfo.getPhoto_list().size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoInfo.PhotoListBean photoBean = mPhotoInfo.getPhoto_list().get(position);
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View rootView = inflater.inflate(R.layout.item_photo_detail, null, false);
        SimpleDraweeView mPhotoSimpleDraweeView
                = (SimpleDraweeView) rootView.findViewById(R.id.photoSimpleDraweeView);

        mPhotoSimpleDraweeView.setImageURI(photoBean.getSize_small());
        container.addView(rootView);
        return rootView;
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
