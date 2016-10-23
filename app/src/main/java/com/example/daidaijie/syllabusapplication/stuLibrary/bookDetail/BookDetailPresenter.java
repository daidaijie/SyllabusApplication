package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import com.example.daidaijie.syllabusapplication.bean.BookDetailBean;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SubPosition;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SuperPosition;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.stuLibrary.ISTULibraryModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class BookDetailPresenter implements BookDetailContract.presenter {

    String url;

    ISTULibraryModel mLibraryModel;

    BookDetailContract.view mView;

    private int position;

    private int subPosition;

    @Inject
    @PerActivity
    public BookDetailPresenter(ISTULibraryModel libraryModel,
                               BookDetailContract.view view,
                               @SuperPosition int position,
                               @SubPosition int subPosition) {
        mLibraryModel = libraryModel;
        mView = view;
        this.position = position;
        this.subPosition = subPosition;
    }

    @Override
    public void start() {
        LibraryBean libraryBean = mLibraryModel.getLibraryBean(position, subPosition);

        url = "http://opac.lib.stu.edu.cn:83/opac/" + libraryBean.getUrl();

        mView.showData(libraryBean);

        mView.loadUrl(url);

        mView.showLoading(true);

    }


    @Override
    public void handlerHtml(String html) {
        Observable.just(html)
                .observeOn(Schedulers.computation())
                .flatMap(new Func1<String, Observable<List<BookDetailBean>>>() {
                    @Override
                    public Observable<List<BookDetailBean>> call(String s) {

                        List<BookDetailBean> bookDetailBeen = new ArrayList<>();

                        Element body = Jsoup.parseBodyFragment(s).body();
                        Element table = body.select("table.tb").first();
                        Elements items = table.getElementsByTag("tr");
                        items.remove(0);
                        for (Element tr : items) {
                            BookDetailBean bookDetailBean = new BookDetailBean();

                            Elements tds = tr.getElementsByTag("td");
                            Element span1 = tds.get(0).getElementsByTag("span").first();
                            Element span2 = tds.get(5).getElementsByTag("span").first();
                            Element td1 = tds.get(6);

                            bookDetailBean.setBook(span1.text());
                            bookDetailBean.setState(span2.text());
                            bookDetailBean.setType(td1.text());

                            bookDetailBeen.add(bookDetailBean);
                        }
                        return Observable.just(bookDetailBeen);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookDetailBean>>() {
                    @Override
                    public void onCompleted() {
                        mView.showLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showLoading(false);
                        LoggerUtil.e("html", e.getMessage());
                    }

                    @Override
                    public void onNext(List<BookDetailBean> bookDetailBeen) {
                        if (bookDetailBeen == null || bookDetailBeen.size() == 0) {
                            mView.showInfoMessage("查找不到图书位置");
                        } else {
                            mView.showResult(bookDetailBeen);
                        }
                    }
                });
    }
}
