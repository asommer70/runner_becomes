package com.thehoick.runnerbecomes;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.nio.channels.SelectableChannel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import db.ScheduleDataSource;
import db.ScheduleHelper;

/**
 * Created by adam on 8/26/14.
 * Display the time picker and save the selected time to the database.
 */
public class RunDateTimePicker extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = RunDateTimePicker.class.getSimpleName();
    protected ScheduleDataSource mDataSource;
    protected static Calendar beginTime = Calendar.getInstance();
    protected static List<Integer> daysOfMonth = new ArrayList<Integer>();

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

            int stepNumber = 1;

            // Choose run duration.

            // Week 1 = 3 20-25 minute walks.
            // Week 2 = 3 25-30 minute walks.
            // Optional Week 3 = 4 30-35 minute walks.
            // Optional Week 4 = 4 35-40 minute walks.

            // Setup spacing of run days.
            // Sunday = 1;
            // Thursday = 5;

            if (ScheduleActivity.DayOfWeek == 2) {
                daysOfMonth.add(ScheduleActivity.DayOfMonth);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 2);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 4);
            } else if (ScheduleActivity.DayOfWeek == 3) {
                daysOfMonth.add(ScheduleActivity.DayOfMonth);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 2);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 3);
            } else if (ScheduleActivity.DayOfWeek == 4) {
                daysOfMonth.add(ScheduleActivity.DayOfMonth);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 1);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 2);
            } else if (ScheduleActivity.DayOfWeek == 5) {
                daysOfMonth.add(ScheduleActivity.DayOfMonth);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 1);
                daysOfMonth.add(ScheduleActivity.DayOfMonth + 2);
            }
            // Convert the day of week spacing to actual dates based on the
            // ScheduleActivity.DayOfMonth attribute.

            EventHelper.getMonday(ScheduleActivity.DayOfWeek, beginTime);
            EventHelper.buildList(3, beginTime);
            daysOfMonth.addAll(EventHelper.runDays);

            // Create Calendar events.
            // Might move this into it's own method.


            for (int i = 0; i < daysOfMonth.size(); i++) {
                long calID = 1;
                long startMillis = 0;
                long endMillis = 0;
                beginTime.set(ScheduleActivity.Year, ScheduleActivity.Month, daysOfMonth.get(i),
                        hourOfDay - 1, minute, 0);
                startMillis = beginTime.getTimeInMillis();
                Calendar endTime = Calendar.getInstance();
                endTime.set(ScheduleActivity.Year, ScheduleActivity.Month, daysOfMonth.get(i),
                        hourOfDay - 1, minute + 25, 0);
                endMillis = endTime.getTimeInMillis();


                ContentResolver cr = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.TITLE, "[RunnerBecomes] Run Time");
                values.put(CalendarContract.Events.DESCRIPTION, "[Step " + stepNumber + "]\n\nTime to Run!");
                values.put(CalendarContract.Events.CALENDAR_ID, calID);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().toString());
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                long eventID = Long.parseLong(uri.getLastPathSegment());

                // Save the eventIDs into the SQLite db.
                Log.i(TAG, "event id: " + eventID);

                // Update the scheduled preference.
                //PreferenceManager.setDefaultValues(this.getActivity(), R.xml.preferences, true);
                //SharedPreferences preferences = this.getActivity().getSharedPreferences("preferences", 0);
                //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

                /*SharedPreferences preferences = this.getActivity().getSharedPreferences("pref_general", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("schedule", true);
                editor.apply();

                Log.i(TAG, "schedule preference: " + preferences.getBoolean("scheduled", false));*/

                if (! mDataSource.checkScheduled()) {
                    mDataSource.insertScheduleSetting("true");
                }

                mDataSource.insertEvent(
                        hourOfDay - 1,
                        minute,
                        new SimpleDateFormat("yyyy-MM-dd").format(beginTime.getTime()),
                        eventID
                );
            }

            // Set alarm as well as calendar notification.

            // Populate the whole program.

            mDataSource.close();
            getActivity().finish();
        }

        callCount++; // Increment Call count cause I guess it's called twice for some reason.
    }
}