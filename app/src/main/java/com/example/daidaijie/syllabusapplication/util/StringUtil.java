package com.example.daidaijie.syllabusapplication.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by daidaijie on 2016/8/2.
 */
public class StringUtil {

    public static boolean isNumberic(String txt) {
        Pattern p = Pattern.compile("[0-9]*");
        return p.matcher(txt).matches();
    }

    public static boolean isPrice(String txt) {
        Pattern p = Pattern.compile("[0-9]+(\\.\\d{1,2})?");
        return p.matcher(txt).matches();
    }
}
