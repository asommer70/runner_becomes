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
                new String[] {"time", "date"},  //columns
                null,                           //where clause
                null,                           //where parameters
                null,                           //group by
                null,                           //having
                null                            //order by
        );
    }

    // Insert data to database.
    public void insertTime(int hour, int minute){
        Log.d("RunnerBecomes", "time = " + hour + ":" + minute);

        ContentValues values = new ContentValues();
        String time = hour + ":" + minute;
        values.put("time", time);
        values.put("type", "main");

        mDB.insert(ScheduleHelper.TABLE_SCHEDULES, null, values);

    }

    // Update data in database.
    public int udpateTime(String newTime) {
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
    public void deleteAll() {
        mDB.delete(
                ScheduleHelper.TABLE_SCHEDULES, // table name
                null,                           // where clause
                null                            // where params
        );
    }
}
