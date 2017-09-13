package com.example.daidaijie.syllabusapplication.widget.imageview;

import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by liyujie on 2017/3/8.
 */

public class ImageLoaderOptions {

    public static final int NULL_DRAWABLE = -1;

    @DrawableRes
    private int errorDrawable = NULL_DRAWABLE;  //加载错误的时候显示的drawable

    @DrawableRes
    private int defaultDrawable = NULL_DRAWABLE;//加载中的时候显示的drawable

    private boolean showAni = true;

    private Transformation<Bitmap> bitmapTransformation;

    public int getErrorDrawable() {
        return errorDrawable;
    }

    public void setErrorDrawable(int errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    public int getDefaultDrawable() {
        return defaultDrawable;
    }

    public void setDefaultDrawable(int defaultDrawable) {
        this.defaultDrawable = defaultDrawable;
    }

    public Transformation<Bitmap> getBitmapTransformation() {
        return bitmapTransformation;
    }

    public void setBitmapTransformation(Transformation<Bitmap> bitmapTransformation) {
        this.bitmapTransformation = bitmapTransformation;
    }

    public boolean isShowAni() {
        return showAni;
    }

    public void setShowAni(boolean showAni) {
        this.showAni = showAni;
    }
}
