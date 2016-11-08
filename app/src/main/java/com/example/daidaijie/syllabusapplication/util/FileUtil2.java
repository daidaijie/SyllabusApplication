package com.example.daidaijie.syllabusapplication.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by smallfly on 16-11-8.
 * 用于各种文件操作
 */
public class FileUtil2 {


    /**
     * 检测SD是否装载, 并且可用
     * @return 检测SD是否装载, 并且可用
     */
    public static boolean isSDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 检查并且创建文件夹
     * @param dirname 目录名
     * @return 是否成功
     */
    public static boolean checkAndMakeDir(String dirname) {
        if (!isSDMounted())
            return false;
        String dirPath = Environment.getExternalStorageDirectory() + "/" + dirname;
        File dir = new File(dirPath);
        return dir.exists() || dir.mkdir();
    }

    public static String getRootPath(boolean slash){
        String path = Environment.getExternalStorageDirectory().toString();
        if (slash)
            return path + "/";
        return path;
    }

}
