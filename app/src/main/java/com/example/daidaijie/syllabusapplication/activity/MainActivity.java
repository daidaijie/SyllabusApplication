package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.example.daidaijie.syllabusapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Uri> uris = new ArrayList<>();

        uris.add(Uri.parse("http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg"));
        uris.add(Uri.parse("http://img2.3lian.com/2014/f2/37/d/39.jpg"));
        uris.add(Uri.parse("http://img2.3lian.com/2014/f2/37/d/39.jpg"));

        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, uris).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private class BannerImageHolderView implements Holder<Uri> {
        SimpleDraweeView draweeView;

        @Override
        public View createView(Context context) {
            draweeView = new SimpleDraweeView(context);
            return draweeView;
        }

        @Override
        public void UpdateUI(Context context, int position, Uri data) {
            draweeView.setImageURI(data);
        }

    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(3000);
    }
}
