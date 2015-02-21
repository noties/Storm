package ru.noties.performance_test.aa;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 13.02.2015.
 */
public class AATest extends AbsTest<AAObject> {

    public AATest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {
        open();
    }

    @Override
    protected void tearDown() {
        ActiveAndroid.dispose();
    }

    @Override
    protected void insert(List<AAObject> list) {
        ActiveAndroid.beginTransaction();
        try {
            for (AAObject aaObject: list) {
                aaObject.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    @Override
    protected void queryOne(long id) {
        new Select().from(AAObject.class).where("id = ?", id).execute();
    }

    @Override
    protected void queryAll() {
        new Select().from(AAObject.class).execute();
    }

    @Override
    protected void deleteAll() {
        new Delete().from(AAObject.class).execute();
    }

    @Override
    protected InstanceCreator<AAObject> getInstanceCreator() {
        return new InstanceCreator<AAObject>() {
            @Override
            public AAObject create(Class<AAObject> clazz) {
                return new AAObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.ACTIVE_ANDROID;
    }

    void open() {
        ActiveAndroid.initialize(getContext(), true);
    }
}
