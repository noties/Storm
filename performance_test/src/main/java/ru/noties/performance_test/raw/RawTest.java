package ru.noties.performance_test.raw;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class RawTest extends AbsTest<RawObject> {

    private RawManager mManager;

    public RawTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {
        mManager = new RawManager(getContext());
    }

    @Override
    protected void tearDown() {
        mManager.close();
    }

    @Override
    protected void insert(List<RawObject> list) {
        mManager.insert(list);
    }

    @Override
    protected void queryOne(long id) {
        final String select = RawObject.COL_ID + " = ?";
        final SQLiteDatabase db = mManager.getDatabase();
        final Cursor cursor = db.query(RawObject.TABLE_NAME, null, select, new String[] { String.valueOf(id) }, null, null, null);

        if (cursor == null){
            return;
        }

        if (cursor.moveToFirst()) {
            mManager.parseCursor(cursor, null);
        }

        cursor.close();
    }

    @Override
    protected void queryAll() {
        final SQLiteDatabase db = mManager.getDatabase();
        final Cursor cursor = db.query(RawObject.TABLE_NAME, null, null, null, null, null, null);
        mManager.parseCursorList(cursor);
    }

    @Override
    protected void deleteAll() {
        mManager.getDatabase().delete(RawObject.TABLE_NAME, null, null);
    }

    @Override
    protected InstanceCreator<RawObject> getInstanceCreator() {
        return new InstanceCreator<RawObject>() {
            @Override
            public RawObject create(Class<RawObject> clazz) {
                return new RawObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.RAW;
    }
}
