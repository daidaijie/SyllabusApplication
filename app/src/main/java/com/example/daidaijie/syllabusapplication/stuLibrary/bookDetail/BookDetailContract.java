package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.BookDetailBean;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface BookDetailContract {

    interface presenter extends BasePresenter {
        void handlerHtml(String html);
    }


    interface view extends BaseView<presenter> {
        void showLoading(boolean isShow);

        void loadUrl(String url);

        void showData(LibraryBean libraryBean);

        void showResult(List<BookDetailBean> mBookDetailBeen);

        void showInfoMessage(String msg);
    }

}
