package com.example.daidaijie.syllabusapplication.widget.imageview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.daidaijie.syllabusapplication.R;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by daidaijie on 17-9-13.
 */

@SuppressLint("AppCompatCustomView")
public class SyllabusImageView extends ImageView {

    private ImageLoaderOptions options;


    public SyllabusImageView(Context context) {
        this(context, null);
    }

    public SyllabusImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SyllabusImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        options = new ImageLoaderOptions();

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SyllabusImageView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        int srcId = -1;
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SyllabusImageView_srcImage:
                    srcId = typedArray.getResourceId(attr, -1);
                    break;
                case R.styleable.SyllabusImageView_placeHolderImage:
                    options.setDefaultDrawable(typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.SyllabusImageView_roundCornerRadius:
                    options.setBitmapTransformation(new RoundedCornersTransformation(
                            context, typedArray.getDimensionPixelSize(attr, 0), 0
                    ));
                    break;
            }
        }
        if (typedArray != null) {
            typedArray.recycle();
        }
        if (srcId != -1) {
            ImageLoader.loadImage(this, srcId, options);
        }
    }

    public void setImageURI(String url) {
        ImageLoader.loadImage(this, url, options);
    }

    public void setImageURIWithActivity(String url, Activity activity) {
        ImageLoader.loadImage(activity, this, url, options);
    }

    public void setImageURIWithSupportFragment(String url, Fragment fragment) {
        ImageLoader.loadImage(fragment, this, url, options);
    }


}
