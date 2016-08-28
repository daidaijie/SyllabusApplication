package com.example.daidaijie.syllabusapplication;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.bean.StreamInfo;
import com.example.daidaijie.syllabusapplication.service.InterenetService;
import com.example.daidaijie.syllabusapplication.services.StreamService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

//        LeakCanary.install(this);
        Fresco.initialize(this);

        context = getApplicationContext();

        initGalleryFinal();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://1.1.1.2/ac_portal/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final InterenetService interenetService = retrofit.create(InterenetService.class);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                interenetService.getInternetInfo()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("ServiceTest", "onNext: " + e.getMessage());
                            }

                            @Override
                            public void onNext(String s) {
                                StreamInfo streamInfo = new StreamInfo();

                                Document doc = Jsoup.parse(s);
                                Element tables = doc.getElementsByTag("table").first();
                                Elements trs = tables.select("tr");

                                streamInfo.setName(trs.get(0).select("td").get(1).text());
                                streamInfo.setAllStream(trs.get(1).select("td").get(1).text());
                                streamInfo.setNowStream(trs.get(2).select("td").get(1).text());
                                streamInfo.setOutTime(trs.get(3).select("td").get(1).text());
                                streamInfo.setState(trs.get(4).select("td").get(1).text());

                                Log.e("ServiceTest", "onNext: " + streamInfo.toString());

                                Intent intent = StreamService.getIntent(getApplicationContext()
                                        , streamInfo);
                                startService(intent);

                            }
                        });
            }
        }, 0, 1000);


    }

    public static Context getContext() {
        return context;
    }

    private void initGalleryFinal() {
        ThemeConfig theme = new ThemeConfig.Builder()
                .build();

        ImageLoader imageloader = new FrescoImageLoader(this);
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageloader, theme)
                .build();
        GalleryFinal.init(coreConfig);
    }


}
