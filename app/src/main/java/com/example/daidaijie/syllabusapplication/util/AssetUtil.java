package com.example.daidaijie.syllabusapplication.util;

import android.content.Context;

import com.example.daidaijie.syllabusapplication.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class AssetUtil {

    public static String getStringFromPath(String path) {
        StringBuffer fileData = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(
                    App.getContext().getResources().getAssets().open(path), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = "";
            String Result = "";

            while ((line = br.readLine()) != null)
                Result += line;
            return Result;
        } catch (IOException e) {
            return "";
        }
    }
}
