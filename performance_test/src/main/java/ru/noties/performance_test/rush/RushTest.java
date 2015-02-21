package ru.noties.performance_test.rush;

import android.content.Context;
import android.os.Looper;

import java.util.List;

import co.uk.rushorm.android.RushAndroid;
import co.uk.rushorm.core.RushCore;
import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 13.02.2015.
 */
public class RushTest extends AbsTest<RushObject> {

    private static boolean sIsPrepared;

    public RushTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {

        //java.lang.NoClassDefFoundError: android.support.v4.content.AsyncTaskLoader$LoadTask RLY? add v4

        // kinda strange. why it's needed?
        if (!sIsPrepared) {
            Looper.prepare();
            sIsPrepared = true;
        }

        RushAndroid.initialize(getContext());
    }

    @Override
    protected void tearDown() {
        // nothing?
    }

    @Override
    protected void insert(List<RushObject> list) {
        RushCore.getInstance().save(list);
    }

    @Override
    protected void queryOne(long id) {
        // java.lang.IndexOutOfBoundsException: Invalid index 7, size is 7
//        new RushSearch().whereEqual("id", id).find(RushObject.class);
        if (true) {
            throw new IllegalStateException("no need to go further");
        }
    }

    @Override
    protected void queryAll() {
        // java.lang.IndexOutOfBoundsException: Invalid index 7, size is 7
//        new RushSearch().find(RushObject.class);
        if (true) {
            throw new IllegalStateException("no need to go further");
        }
    }

    @Override
    protected void deleteAll() {
        // java.lang.IndexOutOfBoundsException: Invalid index 7, size is 7
//        final List<RushObject> list = new RushSearch().find(RushObject.class);
//        RushCore.getInstance().delete(list);
        if (true) {
            throw new IllegalStateException("no need to go further");
        }
    }

    @Override
    protected InstanceCreator<RushObject> getInstanceCreator() {
        return new InstanceCreator<RushObject>() {
            @Override
            public RushObject create(Class<RushObject> clazz) {
                return new RushObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.RUSH_ORM;
    }
}
