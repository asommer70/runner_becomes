package com.thehoick.runnerbecomes;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.nio.channels.SelectableChannel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import db.ScheduleDataSource;
import db.ScheduleHelper;

/**
 * Created by adam on 8/26/14.
 * Display the time picker and save the selected time to the database.
 */
public class RunDateTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    //protected ScheduleDataSource mDataSource;
    //protected String mMonth;
    //protected String mDayOfMonth;

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
/*            mDataSource = new ScheduleDataSource(view.getContext());
            try {
                mDataSource.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Save Time to SQLite3.
            mDataSource.insertTime(hourOfDay, minute);

            mDataSource.close();*/

            int stepNumber = 1;

            // Choose run duration.

            // Week 1 = 3 20-25 minute walks.
            // Week 2 = 3 25-30 minute walks.
            // Optional Week 3 = 4 30-35 minute walks.
            // Optional Week 4 = 4 35-40 minute walks.

            // Create Calendar events.
            // Might move this into it's own method.
            /*Calendar beginTime = Calendar.getInstance();
            beginTime.set(ScheduleActivity.Year, ScheduleActivity.Month, ScheduleActivity.DayOfMonth, hourOfDay, minute, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.set(ScheduleActivity.Year, ScheduleActivity.Month, ScheduleActivity.DayOfMonth, hourOfDay, minute + 25, 0);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, "[RunnerBecomes] Run Time")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "[Step " + stepNumber + "]\nTime to Run!")
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, "")
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            startActivity(intent);*/

            //

            // Populate the whole program.


        }

        callCount++; // Increment Call count cause I guess it's called twice for some reason.
    }
}