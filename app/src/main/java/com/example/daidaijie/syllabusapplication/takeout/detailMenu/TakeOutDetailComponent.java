package com.example.daidaijie.syllabusapplication.takeout.detailMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = TakeOutModelComponent.class, modules = TakeOutDetailModule.class)
public interface TakeOutDetailComponent {

    void inject(TakeOutDetailMenuActivity takeOutDetailMenuActivity);
}
