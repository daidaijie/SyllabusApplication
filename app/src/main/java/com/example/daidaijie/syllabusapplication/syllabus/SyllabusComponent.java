package com.example.daidaijie.syllabusapplication.syllabus;

import com.example.daidaijie.syllabusapplication.IConfigModel;
import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/19.
 */
@PerModule
@Component(dependencies = UserComponent.class, modules = SyllabusModelModule.class)
public abstract class SyllabusComponent {

    private static SyllabusComponent INSTANCE;

    public static SyllabusComponent newInstance() {
        if (INSTANCE == null) {
            INSTANCE = DaggerSyllabusComponent.builder()
                    .userComponent(UserComponent.getINSTANCE())
                    .syllabusModelModule(new SyllabusModelModule())
                    .build();
        }
        return INSTANCE;
    }


    public static SyllabusComponent getINSTANCE() {
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }

    public abstract ISyllabusModel getSyllabusModel();

    public abstract ILoginModel getLoginModel();

    public abstract IConfigModel getConfigModel();

}
