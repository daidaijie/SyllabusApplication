package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.util.ColorUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;

/**
 * Created by daidaijie on 2016/7/19.
 * 课表类，储存一个课表
 */
public class Syllabus extends RealmObject {

    private RealmList<SyllabusGrid> mSyllabusGrids;

    @Ignore
    private Map<LessonID, Lesson> mLessonMap;

    private Semester mSemester;

    private static final String timeLists = "1234567890ABC";

    public Syllabus() {
        mLessonMap = new HashMap<>();
        mSyllabusGrids = new RealmList<>();
        for (int i = 0; i < 7 * 13; i++) {
            mSyllabusGrids.add(new SyllabusGrid());
        }
    }

    public void setSyllabusGrids(int i, int j, SyllabusGrid syllabusGrid) {
        int index = i * 13 + j;
        mSyllabusGrids.set(index, syllabusGrid);
    }

    public SyllabusGrid getSyllabusGrids(int i, int j) {
        int index = i * 13 + j;
        return mSyllabusGrids.get(index);
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

    public void removeSystemLesson(Realm realm, final Semester semester) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Syllabus> results = realm.where(Syllabus.class)
                        .equalTo("mSemester.season", semester.getSeason())
                        .equalTo("mSemester.startYear", semester.getStartYear())
                        .findAll();

                if (results.size() != 0) {
                    Syllabus syllabus = results.first();
                    for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 13; j++) {
                            SyllabusGrid syllabusGrid = syllabus.getSyllabusGrids(i, j);
                            Iterator<LessonID> iterator = syllabusGrid.getLessons().iterator();
                            while (iterator.hasNext()) {
                                LessonID lessonID = iterator.next();
                                Lesson lesson = realm.where(Lesson.class)
                                        .equalTo("id", lessonID.getId() + "").findFirst();
                                if (lesson != null && lesson.getTYPE() == Lesson.TYPE_SYSTEM
                                        && lesson.getSemester().isSame(semester)) {
                                    LoggerUtil.e("lessonID", lesson.getId());
                                    lesson.deleteFromRealm();
                                    iterator.remove();
                                    mLessonMap.remove(lessonID);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 转化课程
     * @param realm
     * @param lessons
     * @param semester
     */
    public void convertSyllabus(Realm realm, final List<Lesson> lessons, final Semester semester) {
        int colorIndex = 0;
        this.setSemester(new Semester(semester.getStartYear(), semester.getSeason()));
        removeSystemLesson(realm, semester);
        // 测试代码
//        Lesson lesson1 = new Lesson();
//        Lesson.Days days1 = new Lesson.Days();
//        days1.setW1("34");
//        lesson1.setName("030");
//        lesson1.setId("12345");
//        lesson1.setRoom("030");
//        lesson1.setDays(days1);
//        lesson1.setDuration("1 -16");
//        lessons.add(lesson1);
//
//        Lesson lesson2 = new Lesson();
//        Lesson.Days days2 = new Lesson.Days();
//        days2.setW1("12");
//        lesson2.setName("123");
//        lesson2.setId("12346");
//        lesson2.setRoom("123");
//        lesson2.setDuration("1 -16");
//        lesson2.setDays(days2);
//        lessons.add(lesson2);

        for (final Lesson lesson : lessons) {
            //将lesson的时间格式化
            lesson.convertDays();
            lesson.setTYPE(Lesson.TYPE_SYSTEM);
            lesson.setBgColor(ColorUtil.bgColors[colorIndex++ % ColorUtil.bgColors.length]);
            Semester lessonSemester = new Semester(semester.getStartYear(), semester.getSeason());
            lesson.setSemester(lessonSemester);

            //获取该课程上的节点上的时间列表
            final List<TimeGrid> timeGrids = lesson.getTimeGrids();

            /**
             * 将课程添加到时间节点上
             */
            for (int i = 0; i < timeGrids.size(); i++) {
                TimeGrid timeGrid = timeGrids.get(i);
                for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                    char x = timeGrid.getTimeList().charAt(j);
                    int time = Syllabus.chat2time(x);
                    SyllabusGrid syllabusGrid = this.getSyllabusGrids(timeGrid.getWeekDate(), time);
                    //将该课程添加到时间节点上去
                    syllabusGrid.getLessons().add(new LessonID(lesson.getLongID()));
                }
            }

            /**
             * 要记住有的课程是没时间的，会出错
             */

            if (timeGrids.size() > 0) {
                mLessonMap.put(new LessonID(lesson.getLongID()), lesson);
            }
        }
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Syllabus.class)
                        .equalTo("mSemester.season", semester.getSeason())
                        .equalTo("mSemester.startYear", semester.getStartYear())
                        .findAll().deleteAllFromRealm();
                realm.copyToRealm(Syllabus.this);
                for (Lesson lesson : lessons) {
                    if (lesson.getTimeGrids().size() > 0) {
                        realm.copyToRealm(lesson);
                    }
                }
            }
        });

    }

    public void loadLessonFromDisk(Realm realm) {
        mLessonMap.clear();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Lesson> results = realm.where(Lesson.class)
                        .equalTo("mSemester.season", mSemester.getSeason())
                        .equalTo("mSemester.startYear", mSemester.getStartYear()).findAll();
                for (Lesson lesson : results) {
                    mLessonMap.put(new LessonID(lesson.getLongID()), lesson);
                }
            }
        });
    }

    public void addLessonToSyllabus(Lesson lesson, Semester semester, int color) {
        addLessonToSyllabus(lesson, semester, color, false);
    }

    public void addLessonToSyllabus(Lesson lesson, Semester semester, int color, boolean isConverDay) {
        //将lesson的时间格式化
        /*if (isConverDay) {
            lesson.convertDays();
        }
        lesson.setBgColor(color);
        Semester lessonSemester = new Semester(semester.getStartYear(), semester.getSeason());
        lesson.setSemester(lessonSemester);
        //获取该课程上的节点上的时间列表
        List<Lesson.TimeGrid> timeGrids = lesson.getTimeGrids();

        //把该课程添加到课程管理去
        LessonModel.getInstance().addLesson(lesson);

        for (int i = 0; i < timeGrids.size(); i++) {
            Lesson.TimeGrid timeGrid = timeGrids.get(i);
            for (int j = 0; j < timeGrid.getTimeList().length(); j++) {
                char x = timeGrid.getTimeList().charAt(j);
                int time = Syllabus.chat2time(x);

                SyllabusGrid syllabusGrid = this.getSyllabusGrids()
                        .get(timeGrid.getWeekDate())
                        .get(time);
                //如果该位置没有重复的就添加上去
                if (syllabusGrid.getLessons().indexOf(lesson.getLongID()) == -1) {
                    syllabusGrid.getLessons().add(lesson.getLongID());
                    Logger.t("LessonAdd").e("here");
                }
            }
        }*/
    }

    public Semester getSemester() {
        return mSemester;
    }

    public void setSemester(Semester semester) {
        mSemester = semester;
    }

    public Lesson getLessonByID(LessonID lessonID) {
        return mLessonMap.get(lessonID);
    }

    public Lesson getLessonByID(long id) {
        return mLessonMap.get(new LessonID(id));
    }
}
