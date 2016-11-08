package com.example.daidaijie.syllabusapplication.util;

import android.os.AsyncTask;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.other.update.IDownloadView;
import com.example.daidaijie.syllabusapplication.other.update.UpdateInstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by smallfly on 16-11-8.
 * 下载APK类
 */
public class UpdateAsync extends AsyncTask<Void, Integer, File> {

    public static String TAG = "UpdateAsync";

    private IDownloadView updateView;
    private UpdateInstaller updateInstaller;
    private String address;
    private String filename;
    private boolean cancelDownload = false;


    public UpdateAsync(IDownloadView updateView, UpdateInstaller updateInstaller, String address, String filename){
        this.updateView = updateView;
        this.updateInstaller = updateInstaller;
        this.address = address;
        this.filename = filename;
    }

    public void setCancelDownload(boolean cancelDownload){
        this.cancelDownload = cancelDownload;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        updateView.showProgress(values[0], values[1]);
    }

    @Override
    protected void onPostExecute(File file) {
        // 安装APK
        updateInstaller.installUpdate(file);
    }

    @Override
    protected File doInBackground(Void... params) {
        if (!FileUtil2.isSDMounted()){
            Log.d(TAG, "SD卡为装载");
            return null;
        }

        try {
            URL downloadUrl = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setConnectTimeout(4000);
            connection.connect();

            // 文件大小
            int length = connection.getContentLength();
            InputStream is = connection.getInputStream();


            String fileSavePath = FileUtil2.getRootPath(true) + filename;
            Log.d(TAG, "文件存储位置: " + fileSavePath);

            File apkFile = new File(fileSavePath);
            FileOutputStream fos = new FileOutputStream(apkFile);

            byte[] buf = new byte[1024 * 4];
            // 记录已经下载的字节数
            int count = 0;
            // 写入到文件中
            int numRead;
            Log.d(TAG, "开始下载文件");
            int delay = 0;
            while ((numRead = is.read(buf)) > 0){
                if (cancelDownload){
                    fos.close();
                    is.close();
                    return null;
                }
                fos.write(buf, 0, numRead);
                ++delay;
                count += numRead;
                // 不需要每下载一点数据就重新更新进度条,这样会影响效率
                if (delay % 10 == 0 || count == length)
                    publishProgress(count / 1024, length / 1024);
//                Log.d(TAG, "进度: " + numRead + ", " + count);
            }
            // 下载完成
            Log.d(TAG, "下载完成");
            fos.close();
            is.close();
            return apkFile;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
