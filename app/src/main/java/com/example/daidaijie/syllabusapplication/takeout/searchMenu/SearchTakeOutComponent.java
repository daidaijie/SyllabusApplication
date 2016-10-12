package com.example.daidaijie.syllabusapplication.takeout.searchMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = TakeOutModelComponent.class, modules = SearchTakeOutModule.class)
public interface SearchTakeOutComponent {

    void inject(SearchTakeOutActivity searchTakeOutActivity);
}
