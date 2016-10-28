package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.circleDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.StuCircleModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerActivity
@Component(dependencies = StuCircleModelComponent.class, modules = CircleDetailModule.class)
public interface CircleDetailComponent {

    void inject(CircleDetailActivity circleDetailActivity);
}
