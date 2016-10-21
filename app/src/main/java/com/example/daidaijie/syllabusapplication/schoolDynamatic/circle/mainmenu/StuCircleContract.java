package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.adapter.CirclesAdapter;
import com.example.daidaijie.syllabusapplication.base.BasePresenter;
import com.example.daidaijie.syllabusapplication.base.BaseView;
import com.example.daidaijie.syllabusapplication.bean.PostListBean;

import java.util.List;

/**
 * Created by daidaijie on 2016/10/8.
 */

public interface StuCircleContract {

    interface presenter extends BasePresenter, CirclesAdapter.OnLikeCallBack {
        void refresh();

        void loadData();
    }

    interface view extends BaseView<presenter> {

        void showRefresh(boolean isShow);

        void showFailMessage(String msg);

        void showSuccessMessage(String msg);

        void loadMoreFinish();

        void showData(List<PostListBean> postListBeen);
    }
}
