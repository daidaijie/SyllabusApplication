package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/31.
 */


@PerActivity
@Component(dependencies = SchoolDymaticModelComponent.class,modules = PostDymaticModule.class)
public interface PostDymaticComponent {

    void inject(PostDymaticActivity postDymaticActivity);
}