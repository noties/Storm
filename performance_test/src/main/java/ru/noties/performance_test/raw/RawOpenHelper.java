package ru.noties.performance_test.raw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class RawOpenHelper extends SQLiteOpenHelper {

    private static final String CREATE = "CREATE TABLE `" + RawObject.TABLE_NAME + "` (" +
            "`" + RawObject.COL_SOME_STRING + "` TEXT, " +
            "`" + RawObject.COL_SOME_BOOL + "` INTEGER, " +
            "`" + RawObject.COL_ID + "`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`" + RawObject.COL_SOME_DOUBLE +"` REAL, " +
            "`" + RawObject.COL_SOME_LONG + "` INTEGER, " +
            "`" + RawObject.COL_SOME_FLOAT + "` REAL, " +
            "`" + RawObject.COL_SOME_INT + "` INTEGER, " +
            "`" + RawObject.COL_SOME_SHORT + "` INTEGER" +
            ");";

    public RawOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
