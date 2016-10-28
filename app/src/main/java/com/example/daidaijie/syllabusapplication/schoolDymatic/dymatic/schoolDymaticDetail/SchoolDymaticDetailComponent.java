package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.schoolDymaticDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerActivity
@Component(dependencies = SchoolDymaticModelComponent.class, modules = SchoolDymaticDetailModule.class)
public interface SchoolDymaticDetailComponent {

    void inject(SchoolDymaticDetailActivity circleDetailActivity);
}
