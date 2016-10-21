package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle;

import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerModule
@Component(dependencies = UserComponent.class, modules = StuCircleModelModule.class)
public abstract class StuCircleModelComponent {

    private static StuCircleModelComponent INSTANCE;

    public static StuCircleModelComponent getInstance() {
        if (INSTANCE == null) {
            INSTANCE = DaggerStuCircleModelComponent.builder()
                    .userComponent(UserComponent.getINSTANCE())
                    .stuCircleModelModule(new StuCircleModelModule())
                    .build();
        }
        return INSTANCE;
    }

    public static void destory() {
        INSTANCE = null;
    }

    public abstract ISchoolCircleModel getSchoolCircleModel();
}
