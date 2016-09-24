package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/19.
 * 课表类，储存一个课表
 */
public class Syllabus {


    public static int countOfDay = 7;
    public static int countOfTime = 13;

    private List<List<SyllabusGrid>> mSyllabusGrids;

    private static final String timeLists = "1234567890ABC";

    public static int[] bgColors = {
//            R.color.material_amber_300,

/*            R.color.material_blue_300,
            R.color.material_blue_400,
            R.color.material_blue_300,
            R.color.material_blue_600,
            R.color.material_blue_A200,
            R.color.material_blue_A400,
            R.color.material_indigo_300,
            R.color.material_indigo_400,
            R.color.material_indigo_300,
            R.color.material_indigo_600,
            R.color.material_indigo_A200,
            R.color.material_indigo_A400,*/

            //橙色系列
            /*R.color.material_orange_200,
            R.color.material_orange_300,
            R.color.material_orange_400,
            R.color.material_orange_300,
            R.color.material_orange_600,
            R.color.material_orange_A100,
            R.color.material_orange_A200,
            R.color.material_orange_A400,
            R.color.material_deepOrange_200,
            R.color.material_deepOrange_300,
            R.color.material_deepOrange_400,
            R.color.material_deepOrange_300,
            R.color.material_deepOrange_600,
            R.color.material_deepOrange_A100,
            R.color.material_deepOrange_A200,
            R.color.material_deepOrange_A400,*/
            R.color.material_cyan_500,
            R.color.material_deepOrange_300,
            R.color.material_blueGrey_400,
            R.color.material_orange_500,
            R.color.material_teal_300,
            R.color.material_deepPurple_300,
            R.color.material_red_300,
//            R.color.material_indigo_300,
            R.color.material_brown_300,
            R.color.material_pink_400,
            R.color.material_lightBlue_500,
            R.color.material_lightGreen_300,
//            R.color.material_primaryIndigo_300,
//            R.color.material_grey_300,
            R.color.material_Lime_500,
            R.color.material_yellow_500,
    };


    public Syllabus() {
        mSyllabusGrids = new ArrayList<>(new ArrayList());
        for (int i = 0; i < 7; i++) {
            List<SyllabusGrid> daySyllabusGrids = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                daySyllabusGrids.add(new SyllabusGrid());
            }
            mSyllabusGrids.add(daySyllabusGrids);
        }
    }

    public List<List<SyllabusGrid>> getSyllabusGrids() {
        return mSyllabusGrids;
    }

    public void setSyllabusGrids(List<List<SyllabusGrid>> syllabusGrids) {
        mSyllabusGrids = syllabusGrids;
    }

    /**
     * 将0 1 2 3 转化为课程表上的对应时间
     *
     * @param x 课程表字符
     * @return
     */
    public static int chat2time(char x) {
        return timeLists.indexOf(x);
    }

    public static char time2char(int time) {
        if (time < 10) {
            return (char) (time + '0');
        } else if (time > 10) {
            return (char) ('A' + (time - 11));
        } else {
            return '0';
        }
    }

    public void removeSystemLesson(Semester semester) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {
                SyllabusGrid syllabusGrid = getSyllabusGrids().get(i).get(j);
                Iterator<Long> iterator = syllabusGrid.getLessons().iterator();
                while (iterator.hasNext()) {
                    long id = iterator.next();
                    Lesson lesson = LessonModel.getInstance().getLesson(id);
                    if (lesson.getTYPE() == Lesson.TYPE_SYSTEM && lesson.getSemester().equals(semester)) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void convertSyllabus(List<Lesson> lessons, Semester semester) {
        int colorIndex = 0;
        removeSystemLesson(semester);
        LessonModel.getInstance().removeSystemLesson(semester);
        for (Lesson lesson : lessons) {
            //将lesson的时间格式化
            lesson.convertDays();
            lesson.setTYPE(Lesson.TYPE_SYSTEM);
            lesson.setBgColor(Syllabus.bgColors[colorIndex++ % Syllabus.bgColors.length]);
            Semester lessonSemester = new Semester(semester.getStartYear(), semester.getSeason());
            lesson.setSemester(lessonSemester);

            //获取该课程上的节点上的时间列表
            List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();
                        /*if (timeGirds.size() != 0) {
                            Log.d(TAG, "onNext: " + timeGirds.get(0).getTimeList());
                        }*/
            //把该课程添加到课程管理去
            LessonModel.getInstance().addLesson(lesson);

            Log.d("Syllabus", "onNext: " + lesson.getName());
            for (int i = 0; i < timeGirds.size(); i++) {
                Lesson.TimeGird timeGrid = timeGirds.get(i);
                for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                    char x = timeGrid.getTimeList().charAt(j);
                    int time = Syllabus.chat2time(x);

                    SyllabusGrid syllabusGrid = this.getSyllabusGrids()
                            .get(timeGrid.getWeekDate())
                            .get(time);

                    //将该课程添加到时间节点上去
                    syllabusGrid.getLessons().add(lesson.getIntID());
                }
            }
            LessonModel.getInstance().save();
        }
        User.getInstance().setSyllabus(User.getInstance().getCurrentSemester(), this);

    }

    public void addLessonToSyllabus(Lesson lesson, Semester semester, int color) {
        addLessonToSyllabus(lesson, semester, color, false);
    }

    public void addLessonToSyllabus(Lesson lesson, Semester semester, int color, boolean isConverDay) {
        //将lesson的时间格式化
        if (isConverDay) {
            lesson.convertDays();
        }
        lesson.setBgColor(color);
        Semester lessonSemester = new Semester(semester.getStartYear(), semester.getSeason());
        lesson.setSemester(lessonSemester);
        //获取该课程上的节点上的时间列表
        List<Lesson.TimeGird> timeGirds = lesson.getTimeGirds();

        //把该课程添加到课程管理去
        LessonModel.getInstance().addLesson(lesson);

        for (int i = 0; i < timeGirds.size(); i++) {
            Lesson.TimeGird timeGrid = timeGirds.get(i);
            for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                char x = timeGrid.getTimeList().charAt(j);
                int time = Syllabus.chat2time(x);

                SyllabusGrid syllabusGrid = this.getSyllabusGrids()
                        .get(timeGrid.getWeekDate())
                        .get(time);
                //如果该位置没有重复的就添加上去
                if (syllabusGrid.getLessons().indexOf(lesson.getIntID()) == -1) {
                    syllabusGrid.getLessons().add(lesson.getIntID());
                    Logger.t("LessonAdd").e("here");
                }
            }
        }
        LessonModel.getInstance().save();
    }
}
