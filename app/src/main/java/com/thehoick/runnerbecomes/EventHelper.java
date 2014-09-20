package com.thehoick.runnerbecomes;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventHelper {

    public static final String TAG = EventHelper.class.getSimpleName();
    public static int DayOfWeek;
    public static List<Calendar> runDays = new ArrayList<Calendar>();

    public static void main(String[] args) {
        // write your code here
        int year = 2014;
        int month = 8; // Months start at 0.
        int dayOfMonth = 22;

        // Figure out the number of days in the week and if there are not three enough days
        // available ask to choose another week.
        String sDay;
        if (dayOfMonth < 10) {
            sDay = "0" + dayOfMonth;
        } else {
            sDay = dayOfMonth + "";
        }

        // Add 1 because month number starts at 0.
        String sMonth;
        if (month < 10) {
            sMonth = "0" + (month + 1);
        } else {
            sMonth = (month + 1) + "";
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dtStart = year + "-" + sMonth + "-" + sDay;
        try {
            Date date = format.parse(dtStart);

            // Week 1
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            DayOfWeek = c.get(Calendar.DAY_OF_WEEK);

            c = getMonday(c);
            buildListStepOne(c);

        }  catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    protected static Calendar getMonday(Calendar c) {


        return c;
    }

    protected static void buildListStepOne(Calendar c) {
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        // Get start of week.
        if (dayOfWeek != 2) {
            int days = (7 - dayOfWeek + 2) % 7;
            c.add(Calendar.DAY_OF_YEAR, days);
        } else if (dayOfWeek == 6) {
            c.add(Calendar.DAY_OF_YEAR, 3);
        }

        // Week 2
        runDays.add((Calendar) c.clone()); // Mon

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add((Calendar) c.clone()); // Wed

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add((Calendar) c.clone()); // Fri

        // Week 3
        c.add(Calendar.DAY_OF_MONTH, 3);
        runDays.add((Calendar) c.clone()); // Mon

        c.add(Calendar.DAY_OF_MONTH, 1);
        runDays.add((Calendar) c.clone()); // Tues

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add((Calendar) c.clone()); // Thurs

        c.add(Calendar.DAY_OF_MONTH, 1);
        runDays.add((Calendar) c.clone()); // Fri

        // Week 4
        c.add(Calendar.DAY_OF_MONTH, 3);
        runDays.add((Calendar) c.clone()); // Mon

        c.add(Calendar.DAY_OF_MONTH, 1);
        runDays.add((Calendar) c.clone()); // Tues

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add((Calendar) c.clone()); // Thurs

        c.add(Calendar.DAY_OF_MONTH, 1); // Fri
        runDays.add((Calendar) c.clone());

    }

}
