package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class LibraryModule {

    private final LibraryContract.view mView;

    private int mPosition;

    public LibraryModule(LibraryContract.view view, int position) {
        mView = view;
        mPosition = position;
    }

    @Provides
    @PerFragment
    int providePosition() {
        return mPosition;
    }

    @Provides
    @PerFragment
    LibraryContract.view provideLibraryView() {
        return mView;
    }


}
