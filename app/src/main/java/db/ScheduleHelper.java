package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adam on 8/27/14.
 * Setup the database connection and such.
 */
public class ScheduleHelper extends SQLiteOpenHelper {

    public static final String TABLE_SCHEDULES = "schedules";

    private static final String DB_NAME = "runner_becomes.db";
    private static final int DB_VERSION = 2;  // must increment to trigger upgrade.

    private static final String DB_CREATE =
            "create table " + TABLE_SCHEDULES +
                    " (id integer primary key autoincrement, time text, date text, type text)";

    private static final String DB_ALTER =
            "alter table " + TABLE_SCHEDULES + " add column type text";

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
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL(DB_ALTER);
    }
}
