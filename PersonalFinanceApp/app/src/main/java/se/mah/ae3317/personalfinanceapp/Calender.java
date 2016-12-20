package se.mah.ae3317.personalfinanceapp;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by xisth on 2016-09-16.
 */
public class Calender extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {
    private String date;
    private Calendar c;
    private int year;
    private int month;
    private int day;

    @TargetApi(Build.VERSION_CODES.N)
    public Calender() {
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        setCurrentDate(year, month, day);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        setCurrentDate(year, month, day);
        AddFragment.setDate(date);
    }

    public void setCurrentDate(int year, int month, int day) {
        month++; // For some reason month is indexed from 0-11. Now it will be 1-12
        if ((month) > 9) {
            date = year + "-" + month + "-" + day;
        }
        else {
            date = year + "-" + 0 + month + "-" + day;
        }
    }

    public String getCurrentDate() {
        return date;
    }
}
