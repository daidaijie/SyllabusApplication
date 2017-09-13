package com.example.daidaijie.syllabusapplication.widget.imageview;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by liyujie on 2017/3/8.
 */

public class ImageLoader {

    public static void loadImage(ImageView imageView, int resID) {
        loadImage(imageView, resID, null);
    }

    public static void loadImage(ImageView imageView, int resID
            , @Nullable ImageLoaderOptions options) {
        loadImage(null, null, imageView, new ResIdLoader(resID), options);
    }

    public static void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, null);
    }

    public static void loadImage(ImageView imageView, String url
            , @Nullable ImageLoaderOptions options) {
        loadImage(null, null, imageView, new Stringloader(url), options);
    }

    public static void loadImage(Fragment fragment, ImageView imageView, String url
            , @Nullable ImageLoaderOptions options) {
        loadImage(null, fragment, imageView, new Stringloader(url), options);
    }

    public static void loadImage(Activity activity, ImageView imageView, String url
            , @Nullable ImageLoaderOptions options) {
        loadImage(activity, null, imageView, new Stringloader(url), options);
    }


    private static void loadImage(Activity activity, Fragment fragment, ImageView imageView, OnImageLoader onImageLoad
            , @Nullable ImageLoaderOptions options) {
        RequestManager manager;
        if (activity != null) {
            manager = Glide.with(activity);
        } else if (fragment != null) {
            manager = Glide.with(fragment);
        } else {
            manager = Glide.with(imageView.getContext());
        }
        DrawableTypeRequest request = onImageLoad.load(manager);

        if (options != null) {
            if (options.getDefaultDrawable() != ImageLoaderOptions.NULL_DRAWABLE) {
                request.placeholder(options.getDefaultDrawable());
            }
            if (options.getErrorDrawable() != ImageLoaderOptions.NULL_DRAWABLE) {
                request.error(options.getErrorDrawable());
            }
            if (options.getBitmapTransformation() != null) {
                request.bitmapTransform(options.getBitmapTransformation());
            }
            if (!options.isShowAni()) {
                request.dontAnimate();
            }
        }

        request.into(imageView);
    }

    private static class Stringloader implements OnImageLoader {

        private String url;

        public Stringloader(String url) {
            this.url = url;
        }

        @Override
        public DrawableTypeRequest load(RequestManager manager) {
            return manager.load(url);
        }
    }

    private static class ResIdLoader implements OnImageLoader {

        private int drawableId;

        public ResIdLoader(int drawableId) {
            this.drawableId = drawableId;
        }

        @Override
        public DrawableTypeRequest load(RequestManager manager) {
            return manager.load(drawableId);
        }
    }


    public interface OnImageLoader {
        DrawableTypeRequest load(RequestManager manager);
    }
}
