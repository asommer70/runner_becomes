package com.thehoick.runnerbecomes;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.sql.SQLException;
import java.util.Calendar;

import db.ScheduleDataSource;

/**
 * Created by adam on 8/26/14.
 * Display the time picker and save the selected time to the database.
 */
public class RunDateTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    protected ScheduleDataSource mDataSource;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    int callCount = 0;   //To track number of calls to onTimeSet()

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (callCount == 1) {
            mDataSource = new ScheduleDataSource(view.getContext());
            try {
                mDataSource.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Save Time to SQLite3.
            mDataSource.insertTime(hourOfDay, minute);

            mDataSource.close();
        }

        callCount++; // Increment Call count cause I guess it's called twice for some reason.
    }
}