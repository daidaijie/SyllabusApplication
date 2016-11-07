package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu;

import com.example.daidaijie.syllabusapplication.adapter.SchoolDymaticAdapter;
import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/21.
 */

public class SchoolDymaticContract {

    interface presenter extends BasePresenter, SchoolDymaticAdapter.OnLikeCallBack {
        void refresh();

        void loadData();

        void handlerFAB();

        void deletePost(int position);

    }

    interface view extends BaseView<presenter> {

        void showLoading(boolean isShow);

        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void showInfoMessage(String msg);

        void loadMoreFinish();

        void showData(List<SchoolDymatic> schoolDymatics);

        void loadEnd();

        void loadStart();

        void toPostDymatic();

        void showContentDialog(SchoolDymatic schoolDymatic, boolean isShowTitle, boolean isShowDelete, int position);

        void showEnsureDeleteDialog(final int position);

    }

}
