package com.example.daidaijie.syllabusapplication.stuLibrary;

import com.example.daidaijie.syllabusapplication.bean.LibSearchBean;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.event.LibPageCountEvent;
import com.example.daidaijie.syllabusapplication.retrofitApi.LibraryApi;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/12.
 */

public class STULibraryModel implements ISTULibraryModel {

    private LibraryApi mLibraryApi;
    private LibSearchBean mLibSearchBean;

    private Map<Integer, List<LibraryBean>> mStoreQueryMap;

    public STULibraryModel(LibraryApi libraryApi, LibSearchBean searchBean) {
        mLibraryApi = libraryApi;
        mLibSearchBean = searchBean;
        mStoreQueryMap = new HashMap<>();
    }


    @Override
    public Observable<List<LibraryBean>> getLibrary(int position) {
        return Observable.concat(getLibraryFromMemory(position), getLibraryFromNet(position))
                .takeFirst(new Func1<List<LibraryBean>, Boolean>() {
                    @Override
                    public Boolean call(List<LibraryBean> libraryBeen) {
                        return libraryBeen != null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<LibraryBean>> getLibraryFromMemory(final int position) {
        return Observable.create(new Observable.OnSubscribe<List<LibraryBean>>() {
            @Override
            public void call(Subscriber<? super List<LibraryBean>> subscriber) {
                LoggerUtil.e("stulibrary", "position" + position + "" + (mStoreQueryMap.get(position) == null) + "");
                subscriber.onNext(mStoreQueryMap.get(position));
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<List<LibraryBean>> getLibraryFromNet(final int position) {
        Map<String, String> qureyMap = new HashMap<>();
        try {
            qureyMap.put(mLibSearchBean.getTag(), URLEncoder.encode(mLibSearchBean.getWord(), "gb2312"));
        } catch (UnsupportedEncodingException e) {
            List<LibraryBean> libraryBeen = new ArrayList<>();
            return Observable.just(libraryBeen);
        }
        qureyMap.put("dt", "ALL");
        qureyMap.put("cl", "ALL");
        qureyMap.put("dept", "ALL");
        qureyMap.put("sf", mLibSearchBean.getSf());
        qureyMap.put("ob", mLibSearchBean.getOb());
        qureyMap.put("page", position + "");
        qureyMap.put("dp", "10");
        qureyMap.put("sm", "table");

        return mLibraryApi
                .getLibrary(qureyMap)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<String, List<LibraryBean>>() {
                    @Override
                    public List<LibraryBean> call(String s) {
                        List<LibraryBean> libraryBeen = new ArrayList<>();

                        Element body = Jsoup.parseBodyFragment(s).body();

                        if (position == 1) {
                            Element countString = body.select("span#ctl00_ContentPlaceHolder1_countlbl").first();

                            if (!countString.text().trim().isEmpty()) {
                                EventBus.getDefault().post(new LibPageCountEvent(Integer.parseInt(countString.text().trim())));
                            }

                        }
                        Element table = body.select("table.tb").first();
                        Elements items = table.getElementsByTag("tr");
                        items.remove(0);

                        for (Element item : items) {
                            LibraryBean libraryBean = new LibraryBean();

                            Elements tdItem = item.getElementsByTag("td");

                            Element href = tdItem.get(1).getElementsByTag("span").first().getElementsByTag("a").first();
                            libraryBean.setUrl(href.attr("href"));

                            libraryBean.setName(tdItem.get(1).text());
                            libraryBean.setAuthor(tdItem.get(2).text());
                            libraryBean.setPress(tdItem.get(3).text());
                            libraryBean.setPressDate(tdItem.get(4).text());
                            libraryBean.setTakeNum(tdItem.get(5).text());
                            libraryBean.setAllNum(Integer.parseInt(tdItem.get(6).text()));
                            libraryBean.setNowNum(Integer.parseInt(tdItem.get(7).text()));
                            libraryBeen.add(libraryBean);
                        }
                        mStoreQueryMap.put(position, libraryBeen);
                        LoggerUtil.e("stulibrary", (mStoreQueryMap.get(position) == null) + "");
                        return libraryBeen;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public LibraryBean getLibraryBean(int position, int subPosition) {
        return mStoreQueryMap.get(position).get(subPosition);
    }


}
