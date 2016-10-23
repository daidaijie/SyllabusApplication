package com.example.daidaijie.syllabusapplication.main;

import android.app.Activity;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.qualifier.semester.CurrentSemester;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.other.update.IUpdateModel;
import com.example.daidaijie.syllabusapplication.other.update.UpdateModel;
import com.example.daidaijie.syllabusapplication.retrofitApi.BannerApi;
import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateApi;
import com.example.daidaijie.syllabusapplication.widget.SelectSemesterBuilder;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class MainModule {

    private final MainContract.view mView;

    private Activity mActivity;


    public MainModule(MainContract.view view, Activity activity) {
        mView = view;
        mActivity = activity;
    }

    @Provides
    @PerActivity
    MainContract.view provideView() {
        return mView;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    IBannerModel provideBannerModel(@DefaultRealm Realm realm,
                                    @SchoolRetrofit Retrofit retrofit) {
        return new BannerModel(realm, retrofit.create(BannerApi.class));
    }

    @Provides
    @PerActivity
    IUpdateModel provideUpdateModel(@SchoolRetrofit Retrofit retrofit) {
        return new UpdateModel(retrofit.create(UpdateApi.class));
    }


}
