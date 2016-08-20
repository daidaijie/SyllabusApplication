package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import com.example.daidaijie.syllabusapplication.activity.GradeActivity;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/19.
 */
public class GradeInfo {

    /**
     * GRADES : [[{"class_name":"[ELC1]英语(ELC1)","class_credit":"4.0","semester":"秋季学期","class_teacher":"马蕊","class_grade":"68","years":"2013-2014","class_number":"62566"},{"class_name":"[PED1041A]羽毛球（1）[PED1041]","class_credit":"1.0","semester":"秋季学期","class_teacher":"王金杰","class_grade":"80","years":"2013-2014","class_number":"62434"},{"class_name":"[CST1301A]程序设计基础[CST9104]","class_credit":"4.0","semester":"秋季学期","class_teacher":"方若宇/于津","class_grade":"93","years":"2013-2014","class_number":"64442"},{"class_name":"[ENC1101A]工程设计导论[ENC9105]","class_credit":"2.0","semester":"秋季学期","class_teacher":"陆小华/包能胜(实验)/林艺文(实验)","class_grade":"84","years":"2013-2014","class_number":"64035"},{"class_name":"[ENC1102A]化学导论[ENC9110]","class_credit":"1.0","semester":"秋季学期","class_teacher":"李庚英","class_grade":"83","years":"2013-2014","class_number":"64136"},{"class_name":"[ENC1103A]生物学导论[ENC9120]","class_credit":"1.0","semester":"秋季学期","class_teacher":"游翠红","class_grade":"75","years":"2013-2014","class_number":"64138"},{"class_name":"[CST1307A]线性代数[CST9105]","class_credit":"2.0","semester":"秋季学期","class_teacher":"廖海泳","class_grade":"78","years":"2013-2014","class_number":"64437"},{"class_name":"[CST1501A]计算科学导论[CST9106]","class_credit":"2.0","semester":"秋季学期","class_teacher":"李新","class_grade":"80","years":"2013-2014","class_number":"64446"},{"class_name":"[MAT1801A]微积分B-I","class_credit":"4.0","semester":"秋季学期","class_teacher":"李俊德","class_grade":"75","years":"2013-2014","class_number":"64125"},{"class_name":"[SOC6150B]毛泽东思想和中国特色社会主义理论体系概论","class_credit":"4.0","semester":"秋季学期","class_teacher":"陈剑屏","class_grade":"72","years":"2013-2014","class_number":"64464"}],[{"class_name":"[ELC2]英语(ELC2)","class_credit":"4.0","semester":"春季学期","class_teacher":"魏丽蕉","class_grade":"61","years":"2013-2014","class_number":"66769"},{"class_name":"[PHY1103A]普通物理实验[PHY1000]","class_credit":"2.0","semester":"春季学期","class_teacher":"蔡旭红/池凌飞/吕秀品/宋晓红/苏建新/孙国勇/王江涌","class_grade":"86","years":"2013-2014","class_number":"68127"},{"class_name":"[CST1302A]离散数学I[CST9200]","class_credit":"4.0","semester":"春季学期","class_teacher":"廖海泳","class_grade":"91","years":"2013-2014","class_number":"68413"},{"class_name":"[PHY1104A]普通物理学[PHY1030]","class_credit":"4.0","semester":"春季学期","class_teacher":"罗以琳","class_grade":"79","years":"2013-2014","class_number":"68132"},{"class_name":"[CIS6031A]汕大整合思维[CIS2031]","class_credit":"2.0","semester":"春季学期","class_teacher":"Wai-man Kwok/饶军民/孙金峰/王雨函","class_grade":"88","years":"2013-2014","class_number":"67147"},{"class_name":"[SOC6130B]思想道德修养与法律基础（道德）","class_credit":"2.0","semester":"春季学期","class_teacher":"白文君","class_grade":"80","years":"2013-2014","class_number":"66997"},{"class_name":"[MAT1803A]微积分B-II","class_credit":"4.0","semester":"春季学期","class_teacher":"谭超强","class_grade":"68","years":"2013-2014","class_number":"67340"},{"class_name":"[CST1303B]面向对象程序设计","class_credit":"3.0","semester":"春季学期","class_teacher":"姜大志","class_grade":"87","years":"2013-2014","class_number":"68710"}],[{"class_name":"[ELC3]英语(ELC3)","class_credit":"4.0","semester":"秋季学期","class_teacher":"马蕊","class_grade":"60","years":"2014-2015","class_number":"71639"},{"class_name":"[MAT2802A]概率论与数理统计（工科）[MAT1240]","class_credit":"3.0","semester":"秋季学期","class_teacher":"林小苹","class_grade":"91","years":"2014-2015","class_number":"71717"},{"class_name":"[PED1060A]网球[PED1060]","class_credit":"1.0","semester":"秋季学期","class_teacher":"丁凤华","class_grade":"78","years":"2014-2015","class_number":"72056"},{"class_name":"[CST2304A]离散数学II[CST9201]","class_credit":"2.0","semester":"秋季学期","class_teacher":"孙浩军","class_grade":"82","years":"2014-2015","class_number":"73055"},{"class_name":"[HED6003A]人生哲学[HED1912]","class_credit":"1.0","semester":"秋季学期","class_teacher":"马凤岐","class_grade":"85","years":"2014-2015","class_number":"72607"},{"class_name":"[EEG2053A]电工电子学[EEG9960]","class_credit":"4.0","semester":"秋季学期","class_teacher":"柳平/张琼/陈征(实验)/夏隽娟(实验)","class_grade":"60","years":"2014-2015","class_number":"73099"},{"class_name":"[MAT2801A]高等微积分","class_credit":"4.0","semester":"秋季学期","class_teacher":"谭超强","class_grade":"63","years":"2014-2015","class_number":"72617"},{"class_name":"[CST2103A]计算机组织与体系结构I","class_credit":"4.0","semester":"秋季学期","class_teacher":"张杰/林艺文(实验)","class_grade":"72","years":"2014-2015","class_number":"73057"},{"class_name":"[CST2202A]计算机图形学","class_credit":"2.0","semester":"秋季学期","class_teacher":"廖海泳","class_grade":"66","years":"2014-2015","class_number":"73059"}],[{"class_name":"[AED6002A]中外音乐名作欣赏[AED1010]","class_credit":"2.0","semester":"春季学期","class_teacher":"刘大伟","class_grade":"90","years":"2014-2015","class_number":"75432"},{"class_name":"[ELC4]英语(ELC4)","class_credit":"4.0","semester":"春季学期","class_teacher":"Kristin Plumlee","class_grade":"78","years":"2014-2015","class_number":"74931"},{"class_name":"[SOC6110A]马克思主义基本原理[SOC0610]","class_credit":"3.0","semester":"春季学期","class_teacher":"李曦","class_grade":"70","years":"2014-2015","class_number":"75595"},{"class_name":"[CST2351A]计算方法[CST9311]","class_credit":"2.0","semester":"春季学期","class_teacher":"熊智","class_grade":"86","years":"2014-2015","class_number":"76233"},{"class_name":"[PED1025A]武术(5)[PED1025]","class_credit":"1.0","semester":"春季学期","class_teacher":"陈国深","class_grade":"82","years":"2014-2015","class_number":"75352"},{"class_name":"[SOC6131B]思想道德修养与法律基础（法律）","class_credit":"2.0","semester":"春季学期","class_teacher":"沈忆勇","class_grade":"80","years":"2014-2015","class_number":"75607"},{"class_name":"[HED6340A]心理、思维与行为","class_credit":"2.0","semester":"春季学期","class_teacher":"康全礼","class_grade":"90","years":"2014-2015","class_number":"76979"},{"class_name":"[CST2104A]计算机组织与体系结构II","class_credit":"3.0","semester":"春季学期","class_teacher":"蔡玲如","class_grade":"76","years":"2014-2015","class_number":"76431"},{"class_name":"[CST2305B]数据结构与算法","class_credit":"4.0","semester":"春季学期","class_teacher":"于津","class_grade":"84","years":"2014-2015","class_number":"76428"},{"class_name":"[CST3255A]WEB应用技术","class_credit":"2.0","semester":"春季学期","class_teacher":"姜大志","class_grade":"78","years":"2014-2015","class_number":"76845"}],[{"class_name":"[HOS1003]大学生健康教育一","class_credit":"1.0","semester":"夏季学期","class_teacher":"王宏志","class_grade":"84","years":"2014-2015","class_number":"71533"},{"class_name":"[SOC1161A]形势与政策教育（理论）[SOC0651]","class_credit":"1.0","semester":"夏季学期","class_teacher":"陈剑屏","class_grade":"85","years":"2014-2015","class_number":"71913"},{"class_name":"[XSC1002A]军事训练和军事理论课[XSC0006]","class_credit":"3.0","semester":"夏季学期","class_teacher":"郭春娟/林兵峰/沈育德/张欣/章桂华","class_grade":"92","years":"2014-2015","class_number":"72420"},{"class_name":"[CWS6008A]科技与城市","class_credit":"1.0","semester":"夏季学期","class_teacher":"任珏(外聘)","class_grade":"90","years":"2014-2015","class_number":"72585"}],[{"class_name":"[SOC6140A]中国近现代史纲要[SOC0630]","class_credit":"2.0","semester":"秋季学期","class_teacher":"高庆荣","class_grade":"71","years":"2015-2016","class_number":"80301"},{"class_name":"[ENC3101A]工程师职业道德与责任[ENC9301]","class_credit":"1.0","semester":"秋季学期","class_teacher":"范颖晖","class_grade":"85","years":"2015-2016","class_number":"81180"},{"class_name":"[CST3401A]软件工程[CST9301]","class_credit":"3.0","semester":"秋季学期","class_teacher":"蔡浩/屈建勤","class_grade":"78","years":"2015-2016","class_number":"80923"},{"class_name":"[CST3254A]应用密码学[CST9008]","class_credit":"2.0","semester":"秋季学期","class_teacher":"方若宇","class_grade":"94","years":"2015-2016","class_number":"80919"},{"class_name":"[CST3503A]操作系统[CST9310]","class_credit":"4.0","semester":"秋季学期","class_teacher":"熊智","class_grade":"93","years":"2015-2016","class_number":"80922"},{"class_name":"[CST3504A]编译原理[CST9307]","class_credit":"3.0","semester":"秋季学期","class_teacher":"李新","class_grade":"90","years":"2015-2016","class_number":"80925"},{"class_name":"[CST3451A]软件质量与测试[CST9034]","class_credit":"2.0","semester":"秋季学期","class_teacher":"张承钿","class_grade":"79","years":"2015-2016","class_number":"80920"},{"class_name":"[CST2451A]Human Computer Interaction[CST9048]","class_credit":"2.0","semester":"秋季学期","class_teacher":"Peterson","class_grade":"80","years":"2015-2016","class_number":"80927"},{"class_name":"[CST3202A]智能系统","class_credit":"2.0","semester":"秋季学期","class_teacher":"姜大志","class_grade":"72","years":"2015-2016","class_number":"80928"},{"class_name":"[CST3257A]Andriod编程与嵌入式系统","class_credit":"2.0","semester":"秋季学期","class_teacher":"陈钦梧","class_grade":"92","years":"2015-2016","class_number":"81049"},{"class_name":"[CST3253A]数字媒体","class_credit":"2.0","semester":"秋季学期","class_teacher":"屈建勤","class_grade":"86","years":"2015-2016","class_number":"81050"}],[{"class_name":"[CST3306A]算法设计与分析[CST9001]","class_credit":"2.0","semester":"春季学期","class_teacher":"陈银冬","class_grade":"83","years":"2015-2016","class_number":"84810"},{"class_name":"[CST3502A]数据库原理[CST9309]","class_credit":"4.0","semester":"春季学期","class_teacher":"张承钿","class_grade":"68","years":"2015-2016","class_number":"84808"},{"class_name":"[CST3256A]并行程序设计[CST9043]","class_credit":"2.0","semester":"春季学期","class_teacher":"熊智","class_grade":"85","years":"2015-2016","class_number":"84815"},{"class_name":"[CST2452A]International Skill[CST9046]","class_credit":"2.0","semester":"春季学期","class_teacher":"Peterson","class_grade":"78","years":"2015-2016","class_number":"85403"},{"class_name":"[PED1084A]体适能基础理论与健跑培养计划[PED1084]","class_credit":"1.0","semester":"春季学期","class_teacher":"曲小锋","class_grade":"90","years":"2015-2016","class_number":"84193"},{"class_name":"[CST3402A]计算机网络","class_credit":"4.0","semester":"春季学期","class_teacher":"蔡伟鸿","class_grade":"75","years":"2015-2016","class_number":"85436"}],[{"class_name":"[MBI6120A]海藻资源概论[MBI1120]","class_credit":"1.0","semester":"夏季学期","class_teacher":"陈伟洲","class_grade":"84","years":"2015-2016","class_number":"79628"},{"class_name":"[CLA2200A]Western Perspectives on Modern Chinese History[CLA2200]","class_credit":"2.0","semester":"夏季学期","class_teacher":"Terry Dwight Bodenhorn","class_grade":"81","years":"2015-2016","class_number":"79340"},{"class_name":"[MBI6270A]基因、基因组与人类","class_credit":"1.0","semester":"夏季学期","class_teacher":"王树启","class_grade":"85","years":"2015-2016","class_number":"81066"}],[],[],[],[],[],[],[],[],[],[]]
     * GPA : 2.8773333333333326
     */

    private double GPA;
    /**
     * class_name : [ELC1]英语(ELC1)
     * class_credit : 4.0
     * semester : 秋季学期
     * class_teacher : 马蕊
     * class_grade : 68
     * years : 2013-2014
     * class_number : 62566
     */

    private List<List<GradeBean>> GRADES;

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public List<List<GradeBean>> getGRADES() {
        return GRADES;
    }

    public void trimList() {
        if (GRADES == null) return;
        for (int i = 0; i < GRADES.size(); i++) {
            List<GradeBean> gradeBeen = GRADES.get(i);
            if (gradeBeen.size() == 0) {
                GRADES.remove(gradeBeen);
                --i;
            }
        }
    }

    public void setGRADES(List<List<GradeBean>> GRADES) {
        this.GRADES = GRADES;
    }

    public static class GradeBean {
        private String class_name;
        private String class_credit;
        private String semester;
        private String class_teacher;
        private String class_grade;
        private String years;
        private String class_number;

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getClass_credit() {
            return class_credit;
        }

        public void setClass_credit(String class_credit) {
            this.class_credit = class_credit;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public String getClass_teacher() {
            return class_teacher;
        }

        public void setClass_teacher(String class_teacher) {
            this.class_teacher = class_teacher;
        }

        public String getClass_grade() {
            return class_grade;
        }

        public void setClass_grade(String class_grade) {
            this.class_grade = class_grade;
        }

        public String getYears() {
            return years;
        }

        public void setYears(String years) {
            this.years = years;
        }

        public String getClass_number() {
            return class_number;
        }

        public void setClass_number(String class_number) {
            this.class_number = class_number;
        }

        public String getTrueName() {
            int startIndex = 0;
            int endIndex = class_name.length();

            int index = class_name.indexOf("]");
            if (index != -1) {
                startIndex = index + 1;
            }

            index = class_name.lastIndexOf("[");
            if (index != -1 && index > startIndex) {
                endIndex = index;
            }
            return class_name.substring(startIndex, endIndex);
        }
    }
}
