package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/10/23.
 */

public class UpdateInfoBean {


    /**
     * versionDate : 1473522326
     * versionName : v1.4
     * apk_file_name : syllabusv1.4.apk
     * versionDescription : 不能下载更新的同学请在文档管理根目录下建立download文件夹
     课表界面可以设定默认课表这样就不需要每次都切换学期
     修复图片上传BUG, 赶紧换上钟意的头像吧!
     修复一键连接校园网
     增加消息圈的未读消息, 以便看回复或者换课
     更换关于我们界面二维码
     2.0正在开发中!!!

     * versionCode : 39
     * versionReleaser : xiaofud
     * download_address : http://git.oschina.net/daidaijie/syllabus_v1.4/raw/master/syllabus.apk
     */

    private int versionDate;
    private String versionName;
    private String apk_file_name;
    private String versionDescription;
    private String versionCode;
    private String versionReleaser;
    private String download_address;

    public int getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(int versionDate) {
        this.versionDate = versionDate;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApk_file_name() {
        return apk_file_name;
    }

    public void setApk_file_name(String apk_file_name) {
        this.apk_file_name = apk_file_name;
    }

    public String getVersionDescription() {
        return versionDescription;
    }

    public void setVersionDescription(String versionDescription) {
        this.versionDescription = versionDescription;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionReleaser() {
        return versionReleaser;
    }

    public void setVersionReleaser(String versionReleaser) {
        this.versionReleaser = versionReleaser;
    }

    public String getDownload_address() {
        return download_address;
    }

    public void setDownload_address(String download_address) {
        this.download_address = download_address;
    }

    public int getIntVersionCode(){
        return Integer.parseInt(versionCode);
    }
}
