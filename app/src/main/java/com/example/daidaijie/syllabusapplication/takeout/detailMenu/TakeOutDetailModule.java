package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class TakeOutDetailModule {

    private final TakeOutDetailContract.view mView;

    private String objectID;

    public TakeOutDetailModule(TakeOutDetailContract.view view, String objectID) {
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
    TakeOutDetailContract.view provideTakeOutView() {
        return mView;
    }

}
