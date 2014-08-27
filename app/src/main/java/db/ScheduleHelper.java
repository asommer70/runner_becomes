package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adam on 8/27/14.
 */
public class ScheduleHelper extends SQLiteOpenHelper {

    public static final String TABLE_SCHEDULES = "schedules";

    private static final String DB_NAME = "runner_becomes.db";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE =
            "create table " + TABLE_SCHEDULES +
                    " (id integer primary key autoincrement, time text, date text)";

    // Need to add the type column to signify what type of schedule is the row is.
    // ie Standard, Custom, etc.

    public ScheduleHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
