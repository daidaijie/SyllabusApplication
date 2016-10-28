package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/21.
 */

@Module
public class StuCircleModule {

    StuCircleContract.view mView;

    public StuCircleModule(StuCircleContract.view view) {
        mView = view;
    }

    @Provides
    @PerFragment
    StuCircleContract.view provideView() {
        return mView;
    }
}
