package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import com.example.daidaijie.syllabusapplication.di.qualifier.position.SubPosition;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SuperPosition;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/22.
 */

@Module
public class BookDetailModule {

    int position;

    int subPosition;

    BookDetailContract.view mView;

    public BookDetailModule(BookDetailContract.view view, int position, int subPosition) {
        mView = view;
        this.position = position;
        this.subPosition = subPosition;
    }

    @PerActivity
    @Provides
    BookDetailContract.view provideView() {
        return mView;
    }

    @PerActivity
    @SuperPosition
    @Provides
    int providePosition() {
        return position;
    }

    @PerActivity
    @SubPosition
    @Provides
    int provideSubPosition() {
        return subPosition;
    }

}
