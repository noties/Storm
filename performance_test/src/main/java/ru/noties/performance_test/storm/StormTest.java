package ru.noties.performance_test.storm;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;

import java.util.List;

import ru.noties.debug.Debug;
import ru.noties.storm.DatabaseManager;
import ru.noties.storm.InstanceCreator;
import ru.noties.storm.Storm;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.query.Selection;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.02.2015.
 */
public class StormTest extends AbsTest<StormObject> {

    private DatabaseManager mManager;

    public StormTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {

        // open
        mManager = open();

        if (mManager == null) {
            throw new IllegalStateException("Could not acquire SQLite database instance");
        }
    }

    @Override
    protected void tearDown() {
        mManager.close();
        delDB(mManager.getDataBaseName());
    }

    @Override
    protected void insert(List<StormObject> list) {
        try {
            Storm.newInsert(mManager).insert(list, false, false);
        } catch (StormException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void queryOne(long id) {
        Storm.newSelect(mManager)
                .query(StormObject.class, Selection.eq(StormObject.COL_ID, id));
    }

    @Override
    protected void queryAll() {
        Storm.newSelect(mManager).queryAll(StormObject.class);
    }

    @Override
    protected void deleteAll() {
        try {
            Storm.newDelete(mManager).deleteAll(StormObject.class);
        } catch (StormException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected InstanceCreator<StormObject> getInstanceCreator() {
        return new InstanceCreator<StormObject>() {
            @Override
            public StormObject create(Class<StormObject> clazz) {
                return new StormObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.STORM;
    }

    @Nullable DatabaseManager open() {

        final BuildConfig.ORM orm = getORM();

        final DatabaseManager manager = new DatabaseManager(
                getContext(),
                orm.getDbName(),
                orm.getDbVersion(),
                new Class[]{
                        StormObject.class
                }
        );

        try {
            manager.open();
        } catch (SQLiteException e) {
            Debug.e(e);
            return null;
        }

        return manager;
    }
}
