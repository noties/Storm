package ru.noties.performance_test.sugar;

import android.content.Context;

import com.orm.SugarApp;

import java.util.List;

import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.MainApplication;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (dima.ivanov@cleverpumpkin.ru) / macbook on 13/02/15.
 */
public class SugarTest extends AbsTest<SugarObject> {

    public SugarTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {
        // requires Application + manifest config...
        MainApplication.sInstance.callOnCreate();
    }

    @Override
    protected void tearDown() {
        SugarApp.getSugarContext().onTerminate();
    }

    @Override
    protected void insert(List<SugarObject> list) {
        // turn off logs?
        // java.lang.IllegalStateException: Cannot perform this operation because there is no current transaction.
        SugarObject.saveInTx(list);
    }

    @Override
    protected void queryOne(long id) {
        SugarObject.findById(SugarObject.class, id); // cool pass me my class
    }

    @Override
    protected void queryAll() {
        SugarObject.listAll(SugarObject.class);
    }

    @Override
    protected void deleteAll() {
        SugarObject.deleteAll(SugarObject.class);
    }

    @Override
    protected InstanceCreator<SugarObject> getInstanceCreator() {
        return new InstanceCreator<SugarObject>() {
            @Override
            public SugarObject create(Class<SugarObject> clazz) {
                return new SugarObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.SUGAR_ORM;
    }
}
