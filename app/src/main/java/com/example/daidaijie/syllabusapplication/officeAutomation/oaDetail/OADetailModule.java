package com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail;

import com.example.daidaijie.syllabusapplication.di.qualifier.position.SubPosition;
import com.example.daidaijie.syllabusapplication.di.qualifier.position.SuperPosition;
import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/17.
 */

@Module
public class OADetailModule {

    private final OADetailContract.view mView;

    private int mPosition;

    private int mSubPosition;

    public OADetailModule(OADetailContract.view view, int position,int subPosition) {
        mView = view;
        mPosition = position;
        mSubPosition = subPosition;
    }

    @PerFragment
    @Provides
    @SuperPosition
    int providePosition() {
        return mPosition;
    }

    @PerFragment
    @Provides
    @SubPosition
    int provideSubPosition() {
        return mSubPosition;
    }


    @PerFragment
    @Provides
    OADetailContract.view provideView() {
        return mView;
    }

}
