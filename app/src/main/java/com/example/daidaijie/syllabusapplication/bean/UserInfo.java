package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/7/17.
 */
public class UserInfo extends RealmObject {

    /**
     * nickname : 余强
     * avatar : http://file.bmob.cn/M02/AE/32/oYYBAFdnwZiAe4EBAABaXRzBuAk291.jpg
     * token : 453295
     * classes : [{"days":{"w1":"None","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"89"},"room":"G座303","credit":"2.0","id":"80927","teacher":"Peterson","duration":"1 -16","name":"[CST2451A]Human Computer Interaction"},{"days":{"w1":"None","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"12"},"room":"E203","credit":"2.0","id":"80928","teacher":"姜大志","duration":"1 -16","name":"[CST3202A]智能系统"},{"days":{"w1":"None","w4":"67","w6":"None","w0":"None","w3":"None","w2":"None","w5":"None"},"room":"E308","credit":"2.0","id":"81050","teacher":"屈建勤","duration":"1 -16","name":"[CST3253A]数字媒体"},{"days":{"w1":"None","w4":"34","w6":"None","w0":"None","w3":"None","w2":"None","w5":"None"},"room":"D座203","credit":"2.0","id":"80919","teacher":"方若宇","duration":"1 -16","name":"[CST3254A]应用密码学"},{"days":{"w1":"None","w4":"None","w6":"None","w0":"None","w3":"34","w2":"None","w5":"None"},"room":"D座407","credit":"2.0","id":"81049","teacher":"陈钦梧","duration":"1 -16","name":"[CST3257A]Andriod编程与嵌入式系统"},{"days":{"w1":"None","w4":"None","w6":"None","w0":"None","w3":"None","w2":"ABC","w5":"None"},"room":"E407","credit":"3.0","id":"80923","teacher":"蔡浩/屈建勤","duration":"1 -16","name":"[CST3401A]软件工程"},{"days":{"w1":"AB","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"None"},"room":"D座301","credit":"2.0","id":"80920","teacher":"张承钿","duration":"1 -16","name":"[CST3451A]软件质量与测试"},{"days":{"w1":"89","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"67"},"room":"D座401","credit":"4.0","id":"80922","teacher":"熊智","duration":"1 -16","name":"[CST3503A]操作系统"},{"days":{"w1":"None","w4":"单AB","w6":"None","w0":"None","w3":"None","w2":"67","w5":"None"},"room":"G座302","credit":"3.0","id":"80925","teacher":"李新","duration":"1 -16","name":"[CST3504A]编译原理"},{"days":{"w1":"None","w4":"None","w6":"双6789","w0":"None","w3":"None","w2":"None","w5":"None"},"room":"讲堂五","credit":"1.0","id":"81180","teacher":"范颖晖","duration":"1 -8","name":"[ENC3101A]工程师职业道德与责任"},{"days":{"w1":"None","w4":"None","w6":"None","w0":"None","w3":"AB","w2":"None","w5":"None"},"room":"E306","credit":"2.0","id":"80301","teacher":"高庆荣","duration":"1 -16","name":"[SOC6140A]中国近现代史纲要"}]
     * user_id : 9
     */

    private String username;
    private String nickname;
    private String avatar;
    private String token;

    @PrimaryKey
    private int user_id;
    /**
     * days : {"w1":"None","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"89"}
     * room : G座303
     * credit : 2.0
     * id : 80927
     * teacher : Peterson
     * duration : 1 -16
     * name : [CST2451A]Human Computer Interaction
     */

    @Ignore
    private List<Lesson> classes;

    public String getNickname() {
        if (nickname == null || nickname.isEmpty()) {
            return username;
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        if (avatar == null || avatar.isEmpty()) {
            return "res://" + App.getContext().getPackageName()
                    + "/" + R.drawable.ic_syllabus_icon;
        }
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<Lesson> getClasses() {
        return classes;
    }

    public void setClasses(List<Lesson> classes) {
        this.classes = classes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
