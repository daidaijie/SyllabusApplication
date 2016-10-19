package com.example.daidaijie.syllabusapplication.officeAutomation;

import com.example.daidaijie.syllabusapplication.bean.OASearchBean;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.OARetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.retrofitApi.OAApi;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/17.
 */

@Module
public class OAModelModule {

    private OASearchBean mOASearchBean;

    public OAModelModule(OASearchBean OASearchBean) {
        mOASearchBean = OASearchBean;
    }

    @PerModule
    @Provides
    OASearchBean provideOASearchBean() {
        return mOASearchBean;
    }

    @PerModule
    @Provides
    IOAModel provideOAModel(@OARetrofit Retrofit retrofit,
                            OASearchBean searchBean,
                            @UserRealm Realm realm) {
        return new OAModel(retrofit.create(OAApi.class), searchBean, realm);
    }
}
