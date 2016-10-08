package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.qualifier.retrofitQualifier.BmobRetrofit;
import com.example.daidaijie.syllabusapplication.service.TakeOutInfoApi;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutContract;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModel;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class TakeOutModule {

    private final TakeOutContract.TakeOutView mTakeOutView;

    public TakeOutModule(TakeOutContract.TakeOutView takeOutView) {
        mTakeOutView = takeOutView;
    }

    @Provides
    @PerActivity
    TakeOutContract.TakeOutView provideTakeOutView() {
        return mTakeOutView;
    }

    @Provides
    @PerActivity
    ITakeOutModel provideTakeOutModel(@DefaultRealm Realm realm, @BmobRetrofit Retrofit retrofit) {
        return new TakeOutModel(realm, retrofit.create(TakeOutInfoApi.class));
    }
}
