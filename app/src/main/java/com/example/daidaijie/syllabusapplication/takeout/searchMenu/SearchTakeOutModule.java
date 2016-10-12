package com.example.daidaijie.syllabusapplication.takeout.searchMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class SearchTakeOutModule {

    private final SearchTakeOutContract.view mView;

    private String objectID;

    public SearchTakeOutModule(SearchTakeOutContract.view view, String objectID) {
        mView = view;
        this.objectID = objectID;
    }

    @Provides
    @PerActivity
    String provideObjectID() {
        return objectID;
    }

    @Provides
    @PerActivity
    SearchTakeOutContract.view provideTakeOutView() {
        return mView;
    }


}
