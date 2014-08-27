package com.thehoick.runnerbecomes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Logger;

import db.ScheduleDataSource;


public class ScheduleActivity extends Activity {

    protected ScheduleDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mDataSource = new ScheduleDataSource(ScheduleActivity.this);

        // Setup variables and assing them.
        final CalendarView startDate = (CalendarView) findViewById(R.id.scheduleStartDate);

        startDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i2, int i3) {
                /*Context context = getApplicationContext();
                CharSequence text = "Calendar Clicked...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();*/

                //Toast.makeText(getApplicationContext(),
                //dayOfMonth +"/"+month+"/"+ year,Toast.LENGTH_LONG).show();}});

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
