package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by adam on 8/27/14.
 * Methods for manipulating the SQLite database.
 */
public class ScheduleDataSource {
    private SQLiteDatabase mDB;
    private ScheduleHelper mScheduleHelper;
    private Context mContext;

    public ScheduleDataSource(Context context) {
        mContext = context;
        mScheduleHelper = new ScheduleHelper(mContext);
    }

    // Open Database
    public void open() throws SQLException{
        mDB = mScheduleHelper.getWritableDatabase();
    }


    // Close Database
    public void close() {
        mDB.close();
    }


    // Read data from database.
    public Cursor selectAllSchedules() {
        return mDB.query(
                ScheduleHelper.TABLE_SCHEDULES, //table
                new String[] {"time", "date", "event_id"},  //columns
                null,                           //where clause
                null,                           //where parameters
                null,                           //group by
                null,                           //having
                null                            //order by
        );
    }

    public boolean checkScheduled() {
        Cursor cursor = mDB.query(
                ScheduleHelper.TABLE_SETTINGS,
                new String[] {"name", "value"},
                "name = 'scheduled'",
                null,
                null,
                null,
                null
        );
        String scheduled = "false";
        cursor.moveToFirst();
        while( !cursor.isAfterLast() ) {
            // do stuff
            int i = cursor.getColumnIndex("value");
            //Log.i("RunnerBecomes", "value index: " + i);
            scheduled = cursor.getString(i);
            cursor.moveToNext();
        }

        if (scheduled.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    // Insert data to database.
    public void insertEvent(int hour, int minute, String date, long eventId){
        Log.d("RunnerBecomes", "time = " + hour + ":" + minute);

        ContentValues values = new ContentValues();
        String time = hour + ":" + minute;
        values.put("time", time);
        values.put("date", date);
        values.put("type", "main");
        values.put("event_id", eventId);

        mDB.insert(ScheduleHelper.TABLE_SCHEDULES, null, values);

    }

    public void insertScheduleSetting(String value){
        ContentValues values = new ContentValues();
        values.put("name", "scheduled");
        values.put("value", value);

        mDB.insert(ScheduleHelper.TABLE_SETTINGS, null, values);
    }

    // Update data in database.
    public int updateTime(String newTime) {
        ContentValues values = new ContentValues();
        values.put("time", newTime);
        return mDB.update(
                ScheduleHelper.TABLE_SCHEDULES, //table
                values,                         //values
                null,                           // where clause
                null                            // where parameters
         );
    }

    // Delete data from database.
    public void deleteAllSchedules() {
        mDB.delete(
                ScheduleHelper.TABLE_SCHEDULES, // table name
                null,                           // where clause
                null                            // where params
        );

        mDB.delete(
                ScheduleHelper.TABLE_SETTINGS,
                "name = 'scheduled'",
                null
        );
    }
}
