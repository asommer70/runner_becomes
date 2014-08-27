package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by adam on 8/27/14.
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

    // Insert data to database.
    public void insertTime(int hour, int minute){
        Log.d("RunnerBecomes", "time = " + hour + ":" + minute);

        ContentValues values = new ContentValues();
        String time = hour + ":" + minute;
        values.put("time", time);

        try {
            mDB.insert(ScheduleHelper.TABLE_SCHEDULES, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update data in database.

    // Delete data from database.
}
