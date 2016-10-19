package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.AssetUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/8/22.
 */
public class OABean implements Serializable {

    /**
     * DOCCONTENT : 汕头大学“创新强校工程”2017年拟安排专项资金考核自评材料公示!@#$%^&*<p style="text-align: center;">
     * <span style="font-size:16px;"><span style="font-size:22px;">汕头大学&ldquo;创新强校工程&rdquo;2017年拟安排专项资金考核自评材料公示</span></span></p>
     * <p>
     * &nbsp;</p>
     * <p>
     * <span style="font-size:18px;"><span style="font-family: 宋体;"><span style="width: 100%; vertical-align: top;">各单位：</span></span></span></p>
     * <p>
     * <span style="font-size:18px;"><span style="font-family: 宋体;"><span id="spanContent" style="width: 100%; vertical-align: top;">&nbsp; 兹定于<span lang="EN-US">2016</span>年7月<span lang="EN-US">15</span>日至19日，在发展规划办（新行政中心3楼学报隔壁）公示汕头大学&ldquo;创新强校工程&rdquo;<span lang="EN-US">2017</span>年拟安排专项资金考核自评材料，欢迎教职员工前往阅览（暑假期间，请提前预约）。如对材料有异议，请以书面形式向发展规划办反映。</span></span></span></p>
     * <p>
     * <span style="font-size:18px;"><span style="font-family: 宋体;"><span id="spanContent" style="width: 100%; vertical-align: top;"><span lang="EN-US">&nbsp; </span>联系电话：<span lang="EN-US">0754-86503298 &nbsp;&nbsp; </span>电子邮箱：<span lang="EN-US">o_ghb@stu.edu.cn </span></span></span></span></p>
     * <p/>
     * ID : 7640
     * DOCSUBJECT : 汕头大学“创新强校工程”2017年拟安排专项资金考核自评材料公示
     * ACCESSORYCOUNT : 0
     * DOCVALIDDATE : 2016-07-15
     * DOCVALIDTIME : 17:24:51
     * OWNERID : 147
     * LOGINID : ctang
     * LASTNAME : 汤灿
     * SUBCOMPANY_ID : 101
     * SUBCOMPANYNAME : 发展规划办
     * DEPARTMENTNAME : 发展规划办办公室
     */

    private String DOCCONTENT;
    private int ID;
    private String DOCSUBJECT;
    private int ACCESSORYCOUNT;
    private String DOCVALIDDATE;
    private String DOCVALIDTIME;
    private int OWNERID;
    private String LOGINID;
    private String LASTNAME;
    private int SUBCOMPANY_ID;
    private String SUBCOMPANYNAME;
    private String DEPARTMENTNAME;

    private boolean isRead;

    public String getDOCCONTENT() {
        return DOCCONTENT;
    }

    public void setDOCCONTENT(String DOCCONTENT) {
        this.DOCCONTENT = DOCCONTENT;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDOCSUBJECT() {
        return DOCSUBJECT;
    }

    public void setDOCSUBJECT(String DOCSUBJECT) {
        this.DOCSUBJECT = DOCSUBJECT;
    }

    public int getACCESSORYCOUNT() {
        return ACCESSORYCOUNT;
    }

    public void setACCESSORYCOUNT(int ACCESSORYCOUNT) {
        this.ACCESSORYCOUNT = ACCESSORYCOUNT;
    }

    public String getDOCVALIDDATE() {
        return DOCVALIDDATE;
    }

    public void setDOCVALIDDATE(String DOCVALIDDATE) {
        this.DOCVALIDDATE = DOCVALIDDATE;
    }

    public String getDOCVALIDTIME() {
        return DOCVALIDTIME;
    }

    public void setDOCVALIDTIME(String DOCVALIDTIME) {
        this.DOCVALIDTIME = DOCVALIDTIME;
    }

    public int getOWNERID() {
        return OWNERID;
    }

    public void setOWNERID(int OWNERID) {
        this.OWNERID = OWNERID;
    }

    public String getLOGINID() {
        return LOGINID;
    }

    public void setLOGINID(String LOGINID) {
        this.LOGINID = LOGINID;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    public void setLASTNAME(String LASTNAME) {
        this.LASTNAME = LASTNAME;
    }

    public int getSUBCOMPANY_ID() {
        return SUBCOMPANY_ID;
    }

    public void setSUBCOMPANY_ID(int SUBCOMPANY_ID) {
        this.SUBCOMPANY_ID = SUBCOMPANY_ID;
    }

    public String getSUBCOMPANYNAME() {
        return SUBCOMPANYNAME;
    }

    public void setSUBCOMPANYNAME(String SUBCOMPANYNAME) {
        this.SUBCOMPANYNAME = SUBCOMPANYNAME;
    }

    public String getDEPARTMENTNAME() {
        return DEPARTMENTNAME;
    }

    public void setDEPARTMENTNAME(String DEPARTMENTNAME) {
        this.DEPARTMENTNAME = DEPARTMENTNAME;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getContent() {

        String oaContent = "";
        String label = "!@#$%^&*";

        oaContent = getDOCCONTENT();
        int index = oaContent.indexOf(label) + label.length();
        oaContent = oaContent.substring(index);

        Document contentDoc = Jsoup.parse(oaContent);

        Elements imgs = contentDoc.select("img");
        for (Element img : imgs) {
            imgs.attr("style", "width: 100%;");
        }

        Elements tables = contentDoc.getElementsByTag("table");
        for (Element table : tables) {
            table.attr("width", "100%");
            table.attr("style", "width: 100%;");
            Elements trs = table.select("tr");
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                double witdh = 100.0 / tds.size();
                for (Element td : tds) {
                    String colspan = td.attr("colspan");
                    if (colspan.trim().isEmpty()) {
                        td.attr("style", "width:" + witdh + "%");
                    } else {
                        td.attr("style", "width:" + witdh * Integer.parseInt(colspan) + "%");
                    }
                    td.removeAttr("nowrap");
                }
            }
        }

        Document doc = Jsoup.parse(AssetUtil.getStringFromPath("index.html"));
        Element div = doc.select("div#div_doc").first();
        div.append(contentDoc.toString());
        div = doc.select("div#div_accessory").first();

        if (getACCESSORYCOUNT() == 0) {
            div.remove();
        } else {
        }

        return doc.toString();
    }
}
