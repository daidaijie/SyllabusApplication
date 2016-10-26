package com.example.daidaijie.syllabusapplication.stream;

import android.content.Intent;

import com.example.daidaijie.syllabusapplication.bean.StreamInfo;
import com.example.daidaijie.syllabusapplication.retrofitApi.SchoolInternetApi;
import com.example.daidaijie.syllabusapplication.services.StreamService;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/24.
 */

public class StreamModel implements IStreamModel {

    SchoolInternetApi mSchoolInternetApi;


    public StreamModel(SchoolInternetApi schoolInternetApi) {
        mSchoolInternetApi = schoolInternetApi;
    }

    @Override
    public Observable<StreamInfo> getStreamInfo() {
        return mSchoolInternetApi.getInternetInfo()
                .subscribeOn(Schedulers.io())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        StreamInfo.getInstance().setType(StreamInfo.TYPE_UN_CONNECT);
                    }
                })
                .flatMap(new Func1<String, Observable<StreamInfo>>() {
                    @Override
                    public Observable<StreamInfo> call(String s) {

                        StreamInfo streamInfo = StreamInfo.getInstance();

                        Document doc = Jsoup.parse(s);
                        Element tables = doc.getElementsByTag("table").first();
                        Elements trs = tables.select("tr");

                        if (trs.size() < 2) {
                            streamInfo.setType(StreamInfo.TYPE_LOGOUT);
                            return Observable.just(streamInfo);
                        }

                        streamInfo.setName(trs.get(0).select("td").get(1).text());
                        streamInfo.setAllStream(trs.get(1).select("td").get(1).text());
                        streamInfo.setNowStream(trs.get(2).select("td").get(1).text());
                        streamInfo.setOutTime(trs.get(3).select("td").get(1).text());
                        streamInfo.setState(trs.get(4).select("td").get(1).text());
                        streamInfo.setType(StreamInfo.TYPE_SUCCESS);
                        return Observable.just(streamInfo);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
