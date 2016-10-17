package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/17.
 */

@Module
public class OAModule {

    private final OAContract.view mView;

    private int mPosition;

    public OAModule(OAContract.view view, int position) {
        mView = view;
        mPosition = position;
    }

    @PerFragment
    @Provides
    int providePosition() {
        return mPosition;
    }


    @PerFragment
    @Provides
    OAContract.view provideView() {
        return mView;
    }

}
