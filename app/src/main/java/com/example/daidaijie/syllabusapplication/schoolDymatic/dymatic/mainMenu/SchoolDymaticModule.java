package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/21.
 */


@Module
public class SchoolDymaticModule {

    SchoolDymaticContract.view mView;

    public SchoolDymaticModule(SchoolDymaticContract.view view) {
        mView = view;
    }

    @Provides
    @PerFragment
    SchoolDymaticContract.view provideView() {
        return mView;
    }

}
