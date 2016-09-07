package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/9/5.
 */
public class LibraryBean implements Serializable{

    /**
     * <td><span class="title"><a href="http://opac.lib.stu.edu.cn:83/opac/bookinfo.aspx?ctrlno=1935287" target="_blank">机械产品可靠性设计与试验 [专著]</a></span></td>
     * <td>许卫宝, 钟涛编著</td>
     * <td>国防工业出版社</td>
     * <td>2015.7</td>
     * <td class="tbr">TH122/201507J</td>
     * <td class="tbr">1</td>
     * <td class="tbr">1</td>
     * <td style="display:none">
     */
    private String url;

    private String name;

    private String author;

    private String press;

    private String pressDate;

    private String takeNum;

    private int nowNum;

    private int allNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getPressDate() {
        return pressDate;
    }

    public void setPressDate(String pressDate) {
        this.pressDate = pressDate;
    }

    public String getTakeNum() {
        return takeNum;
    }

    public void setTakeNum(String takeNum) {
        this.takeNum = takeNum;
    }

    public int getNowNum() {
        return nowNum;
    }

    public void setNowNum(int nowNum) {
        this.nowNum = nowNum;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
