package com.example.daidaijie.syllabusapplication.exam.detail;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Exam;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Period;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class ExamItemModel implements IExamItemModel {

    private Exam mExam;

    private DateTime mExamTime;

    public ExamItemModel(Exam exam) {
        mExam = exam;
        mExamTime = mExam.getExamTime();
    }

    @Override
    public Exam getExam() {
        return mExam;
    }

    @Override
    public SpannableStringBuilder getExamState() {
        DateTime now = DateTime.now();
        LoggerUtil.e("dateTime", "state");
        if (DateTimeComparator.getInstance().compare(mExamTime, now) > 0) {
            LoggerUtil.e("dateTime", "mExam>now");
            Period period = new Period(now, mExamTime);
            StringBuilder sb = new StringBuilder("离开考还有\n");
            int days = period.getWeeks() * 7 + period.getDays();
            if (period.getYears() != 0) {
                sb.append(period.getYears() + "年");
                sb.append(period.getMonths() + "月");
                sb.append(days + "天");
            } else {
                if (period.getMonths() != 0) {
                    sb.append(period.getMonths() + "月");
                    sb.append(days + "天");
                } else {
                    if (days != 0) {
                        sb.append(days + "天");
                    }
                }
            }
            sb.append(String.format("%d小时%d分钟",
                    period.getHours(), period.getMinutes()));

            SpannableStringBuilder style = new SpannableStringBuilder(sb);
            style.setSpan(new ForegroundColorSpan(
                            App.getContext().getResources().getColor(R.color.defaultTextColor)),
                    0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            style.setSpan(new ForegroundColorSpan(
                            ThemeUtil.getInstance().colorPrimary),
                    6, sb.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            return style;
        } else {
            SpannableStringBuilder style = new SpannableStringBuilder("已结束");
            style.setSpan(new ForegroundColorSpan(
                            App.getContext().getResources().getColor(R.color.defaultTextColor)),
                    0, style.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            );
            return style;
        }
    }
}
