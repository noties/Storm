package ru.noties.performance_test.raw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.noties.performance_test.BuildConfig;
import ru.noties.storm.util.MapUtils;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class RawManager {

    private SQLiteDatabase mDB;

    public RawManager(Context context) {
        init(context);
    }

    private void init(Context context) {
        final BuildConfig.ORM orm = BuildConfig.ORM.RAW;
        final RawOpenHelper openHelper = new RawOpenHelper(context, orm.getDbName(), null, orm.getDbVersion());
        try {
            mDB = openHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void reopen(Context context) {
        init(context);
    }

    public void close() {
        if (mDB != null && mDB.isOpen()) {
            mDB.close();
            mDB = null;
        }
    }

    public RawObject parseCursor(Cursor cursor, @Nullable Map<String, Integer> indices) {

        final RawObject object = new RawObject();

        if (indices == null) {
            indices = getIndices(cursor);
        }

        // gosh...
        object.setId(cursor.getLong(indices.get(RawObject.COL_ID)));
        object.setSomeBool(cursor.getInt(indices.get(RawObject.COL_SOME_BOOL)) == 1);
        object.setSomeDouble(cursor.getDouble(indices.get(RawObject.COL_SOME_DOUBLE)));
        object.setSomeFloat(cursor.getFloat(indices.get(RawObject.COL_SOME_FLOAT)));
        object.setSomeLong(cursor.getLong(indices.get(RawObject.COL_SOME_LONG)));
        object.setSomeInt(cursor.getInt(indices.get(RawObject.COL_SOME_INT)));
        object.setSomeString(cursor.getString(indices.get(RawObject.COL_SOME_STRING)));
        object.setSomeShort(cursor.getShort(indices.get(RawObject.COL_SOME_SHORT)));

        return object;
    }

    public List<RawObject> parseCursorList(Cursor cursor) {

        if (cursor == null) {
            return null;
        }

        final List<RawObject> list = new ArrayList<>();

        if (cursor.moveToFirst()) {
            final Map<String, Integer> indices = getIndices(cursor);
            while (!cursor.isAfterLast()) {
                list.add(parseCursor(cursor, indices));
                cursor.moveToNext();
            }
        }

        cursor.close();

        if (list.size() == 0) {
            return null;
        }

        return list;
    }

    private Map<String, Integer> getIndices(Cursor cursor) {
        final Map<String, Integer> indices = MapUtils.create();
        final String[] columns = cursor.getColumnNames();
        for (int i = 0, size = columns.length; i < size; i++) {
            indices.put(columns[i], i);
        }
        return indices;
    }

    public SQLiteDatabase getDatabase() {
        return mDB;
    }

    public ContentValues toCV(RawObject rawObject) {

        final ContentValues cv = new ContentValues();

        cv.put(RawObject.COL_SOME_LONG, rawObject.getSomeLong());
        cv.put(RawObject.COL_SOME_SHORT, rawObject.getSomeShort());
        cv.put(RawObject.COL_SOME_STRING, rawObject.getSomeString());
        cv.put(RawObject.COL_SOME_INT, rawObject.getSomeInt());
        cv.put(RawObject.COL_SOME_BOOL, rawObject.isSomeBool() ? 1 : 0);
        cv.put(RawObject.COL_SOME_DOUBLE, rawObject.getSomeDouble());
        cv.put(RawObject.COL_SOME_FLOAT, rawObject.getSomeFloat());

        return cv;
    }

    public void insert(List<RawObject> list) {
        mDB.beginTransaction();
        try {
            for (RawObject object: list) {
                mDB.insert(RawObject.TABLE_NAME, null, toCV(object));
            }
            mDB.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            mDB.endTransaction();
        }
    }
}
