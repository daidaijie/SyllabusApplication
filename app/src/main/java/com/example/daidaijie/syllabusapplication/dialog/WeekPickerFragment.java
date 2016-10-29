package com.example.daidaijie.syllabusapplication.dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.widget.picker.DatePicker;

import org.joda.time.LocalDate;

import info.hoang8f.widget.FButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeekPickerFragment extends DialogFragment {

    public static final String DIALOG_WEEK_PICKER = "com.example.daidaijie.syllabusapplication" +
            ".activity.WeekPickerFragment";

    NumberPicker numberPicker;

    FButton selectDateButton;

    LocalDate selectTime;

    public interface OnSettingWeekListener {
        void onSettingWeek(LocalDate date, int week);
    }

    OnSettingWeekListener mOnSettingWeekListener;

    public OnSettingWeekListener getOnSettingWeekListener() {
        return mOnSettingWeekListener;
    }

    public void setOnSettingWeekListener(OnSettingWeekListener onSettingWeekListener) {
        mOnSettingWeekListener = onSettingWeekListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        selectTime = LocalDate.now();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_week_picker, null);
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerView);
        selectDateButton = (FButton) view.findViewById(R.id.selectDateButton);
        numberPicker.setMaxValue(16);
        numberPicker.setMinValue(1);

        setFButtonDate(selectTime.getYear() + "", selectTime.getMonthOfYear() + "", selectTime.getDayOfMonth() + "");
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker picker = new DatePicker(getActivity());
                picker.setRange(2000, 2100);
                picker.setSelectedItem(selectTime.getYear(), selectTime.getMonthOfYear(), selectTime.getDayOfMonth());
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        selectTime = new LocalDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        setFButtonDate(selectTime.getYear() + "", selectTime.getMonthOfYear() + "", selectTime.getDayOfMonth() + "");

                    }
                });
                picker.show();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("周数设定")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOnSettingWeekListener.onSettingWeek(selectTime, numberPicker.getValue());
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });
        return dialog;
    }


    private void setFButtonDate(String year, String month, String day) {
        selectDateButton.setText(year + "年" + month + "月" + day + "号");
    }


}
