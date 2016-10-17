package com.example.daidaijie.syllabusapplication.officeAutomation;

import com.example.daidaijie.syllabusapplication.bean.SubCompany;
import com.example.daidaijie.syllabusapplication.util.AssetUtil;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAUtil {

    public List<SubCompany> mSubCompanies;

    public OAUtil() {
        String subCompanyString = AssetUtil.getStringFromPath("subcompany.json");
        mSubCompanies = GsonUtil.getDefault().fromJson(
                subCompanyString, new TypeToken<List<SubCompany>>() {
                }.getType()
        );
    }

    public String[] getSubCompanysString() {
        List<String> subCompanysString = new ArrayList<>();
        for (SubCompany subCompany : mSubCompanies) {
            subCompanysString.add(subCompany.getSUBCOMPANYNAME());
        }
        return subCompanysString.toArray(new String[subCompanysString.size()]);
    }
}
