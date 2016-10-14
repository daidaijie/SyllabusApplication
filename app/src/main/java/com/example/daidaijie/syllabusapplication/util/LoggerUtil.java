package com.example.daidaijie.syllabusapplication.util;

import com.example.daidaijie.syllabusapplication.App;
import com.orhanobut.logger.Logger;

/**
 * Created by daidaijie on 2016/10/12.
 */

public class LoggerUtil {

    public static void e(String msg) {
        if (App.isLogger) {
            Logger.e(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (App.isLogger) {
            Logger.t(tag).e(msg);
        }
    }


    public static void printStack(Throwable e) {
        if (App.isLogger) {
            e.printStackTrace();
        }
    }

}
