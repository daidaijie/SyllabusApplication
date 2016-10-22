package com.example.daidaijie.syllabusapplication.schoolDynamatic.personal;

import com.example.daidaijie.syllabusapplication.retrofitApi.UpdateUserApi;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class PersonalModel implements IPersonalModel {

    UpdateUserApi  mUpdateUserApi;

    public PersonalModel(UpdateUserApi updateUserApi) {
        mUpdateUserApi = updateUserApi;
    }
}
