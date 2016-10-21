package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.postContent;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDynamatic.circle.StuCircleModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerActivity
@Component(dependencies = StuCircleModelComponent.class, modules = PostConentModule.class)
public interface PostContentComponent {

    void inject(PostContentActivity postContentActivity);
}
