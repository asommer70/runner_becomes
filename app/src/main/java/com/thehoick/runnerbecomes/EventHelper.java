package com.thehoick.runnerbecomes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EventHelper {

    public static int DayOfWeek;
    public static List<Integer> runDays = new ArrayList<Integer>();

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

            c = getMonday(DayOfWeek, c);
            buildList(3, c);

        }  catch (Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.print("\n");
        System.out.print("\n");
        for (int i = 0; i < runDays.size(); i++) {
            System.out.println(runDays.get(i));
        }
    }

    protected static Calendar getMonday(int dayOfWeek, Calendar c) {
        // Get start of week.
        if (dayOfWeek != 2) {
            int days = (7 - dayOfWeek + 2) % 7;
            c.add(Calendar.DAY_OF_YEAR, days);
        } else {
            c.add(Calendar.DAY_OF_YEAR, 7);
        }

        return c;
    }

    protected static void buildList(int weeks, Calendar c) {
        System.out.println("buldList weeks: " + weeks);

        System.out.println("Week 2 Monday: " + new SimpleDateFormat("yyyy-MM-dd")
                .format(c.getTime()));
        System.out.println("Week 2 Monday int: " + c.get(Calendar.DAY_OF_MONTH));
        runDays.add(c.get(Calendar.DAY_OF_MONTH));

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add(c.get(Calendar.DAY_OF_MONTH));
        System.out.println("Week 2 Wednesday int: " + c.get(Calendar.DAY_OF_MONTH));

        c.add(Calendar.DAY_OF_MONTH, 2);
        runDays.add(c.get(Calendar.DAY_OF_MONTH));
        System.out.println("Week 2 Friday int: " + c.get(Calendar.DAY_OF_MONTH));
        System.out.print("\n");

        for (int i = 1; i < weeks; i++) {
            // Add day numbers to the list.
            c.add(Calendar.DAY_OF_MONTH, 3);
            runDays.add(c.get(Calendar.DAY_OF_MONTH));
            System.out.println("Week " + i +  " Monday: " + new SimpleDateFormat("yyyy-MM-dd")
                    .format(c.getTime()));
            System.out.println("Week " + i + " Monday int: " + c.get(Calendar.DAY_OF_MONTH));

            c.add(Calendar.DAY_OF_MONTH, 2);
            runDays.add(c.get(Calendar.DAY_OF_MONTH));
            System.out.println("Week " + i + " Wednesday int: " + c.get(Calendar.DAY_OF_MONTH));

            c.add(Calendar.DAY_OF_MONTH, 2);
            runDays.add(c.get(Calendar.DAY_OF_MONTH));
            System.out.println("Week " + i + " Friday int: " + c.get(Calendar.DAY_OF_MONTH));
            System.out.print("\n");

        }

    }

}
