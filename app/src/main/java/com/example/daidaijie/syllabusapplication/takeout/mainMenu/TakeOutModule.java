package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class TakeOutModule {

    private final TakeOutContract.view mView;

    public TakeOutModule(TakeOutContract.view view) {
        mView = view;
    }

    @Provides
    @PerActivity
    TakeOutContract.view provideTakeOutView() {
        return mView;
    }


}
