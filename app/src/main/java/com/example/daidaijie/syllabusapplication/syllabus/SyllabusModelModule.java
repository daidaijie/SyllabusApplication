package com.example.daidaijie.syllabusapplication.syllabus;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.user.LoginUser;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.IUserModel;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * Created by daidaijie on 2016/10/19.
 */

@Module
public class SyllabusModelModule {

    @PerModule
    @Provides
    ISyllabusModel provideSyllabusModel(ILoginModel loginModel,
                                        @LoginUser IUserModel userModel,
                                        @UserRealm Realm realm) {
        return new SyllabusModel(loginModel, userModel, realm);
    }
}
