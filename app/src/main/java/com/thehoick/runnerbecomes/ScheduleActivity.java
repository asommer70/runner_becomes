package com.thehoick.runnerbecomes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import db.ScheduleDataSource;


public class ScheduleActivity extends Activity {

    //protected ScheduleDataSource mDataSource;
    public static int Year;
    public static int Month;
    public static int DayOfMonth;
    public static int WeekDaysLeft;
    public static int DayOfWeek;
    protected String sDay;
    protected String sMonth;
    public static Calendar cal;

    public static final String TAG = RunDateTimePicker.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressBarIndeterminate(false);
        setContentView(R.layout.activity_schedule);

        //mDataSource = new ScheduleDataSource(ScheduleActivity.this);

        final CalendarView startDate = (CalendarView) findViewById(R.id.scheduleStartDate);

        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month,
                                            int dayOfMonth) {

                Year = year;
                Month = month;
                DayOfMonth = dayOfMonth;
                Toast.makeText(getApplicationContext(), "Day Changed...", Toast.LENGTH_LONG).show();

                // Figure out the number of days in the week and if there are not three enough days
                // available ask to choose another week.
                if (ScheduleActivity.DayOfMonth < 10) {
                    sDay = "0" + DayOfMonth;
                } else {
                     sDay = DayOfMonth + "";
                }

                // Add 1 because month number starts at 0.
                if (ScheduleActivity.Month < 10) {
                    sMonth = "0" + (Month + 1);
                } else {
                    sMonth = (Month + 1) + "";
                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dtStart = ScheduleActivity.Year + "-" + sMonth + "-" + sDay;
                try {
                    Date date = format.parse(dtStart);

                    cal = Calendar.getInstance();
                    cal.setTime(date);
                    DayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                    // Week 2 just do Mon, Wed, Fri.
                    // Get start of week 2.
                    if (DayOfWeek != 2) {
                        int days = (7 - DayOfWeek + 2) % 7;
                        cal.add(Calendar.DAY_OF_YEAR, days);
                    } else {
                      cal.add(Calendar.DAY_OF_YEAR, 7);
                    }

                    //Log.i(TAG, "next Monday: " + new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));


                    //Log.i(TAG, dtStart);
                    //Log.i(TAG, "dayOfWeek: " + DayOfWeek);

                    WeekDaysLeft = 7 - DayOfWeek;

                    if (DayOfWeek <= 5) {
                        // Open a pick time dialog.
                        DialogFragment newFragment = new RunDateTimePicker();
                        newFragment.show(getFragmentManager(), "timePicker");
                    } else {
                        // Create a dialog to inform about the need for at least 3 days of practice.
                        AlertDialog.Builder builder = new AlertDialog.Builder(calendarView.getContext());
                        builder.setMessage(R.string.too_late_message)
                                .setTitle(R.string.too_late_title);

                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } catch (Exception e) {
                    Log.d("RunnerBecomes", e.getMessage());
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*try {
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
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();

        //mDataSource.close();
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
