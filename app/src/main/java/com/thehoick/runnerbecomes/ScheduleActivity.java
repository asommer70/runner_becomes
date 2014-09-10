package com.thehoick.runnerbecomes;

import android.app.Activity;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import java.sql.SQLException;


import db.ScheduleDataSource;


public class ScheduleActivity extends Activity {

    protected ScheduleDataSource mDataSource;
    public static int Year;
    public static int Month;
    public static int DayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mDataSource = new ScheduleDataSource(ScheduleActivity.this);

        // Setup variables and assing them.
        final CalendarView startDate = (CalendarView) findViewById(R.id.scheduleStartDate);

        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {

                Year = year;
                Month = month;
                DayOfMonth = dayOfMonth;
                Toast.makeText(getApplicationContext(), "Day Changed...", Toast.LENGTH_LONG).show();

                // Open a pick time dialog.
                DialogFragment newFragment = new RunDateTimePicker();
                newFragment.show(getFragmentManager(), "timePicker");

                // Save the date to SQLite3.

                // Change the start date display style.
                //startDate.setSelectedWeekBackgroundColor(0x68965B);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = mDataSource.selectAllSchedules();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int i = cursor.getColumnIndex("time");
            Toast.makeText(getApplicationContext(), cursor.getString(i), Toast.LENGTH_LONG).show();
            cursor.moveToNext();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
