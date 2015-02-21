package ru.noties.performance_test;

import android.content.Context;
import android.os.SystemClock;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import ru.noties.debug.Debug;
import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.ui.AdapterItem;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 12.02.2015.
 */
public abstract class AbsTest<T extends IObject> implements Runnable {

    protected abstract void setUp();
    protected abstract void tearDown();

    protected abstract void insert(List<T> list);
    protected abstract void queryOne(long id);
    protected abstract void queryAll();
    protected abstract void deleteAll();

    protected abstract InstanceCreator<T> getInstanceCreator();
    protected abstract BuildConfig.ORM getORM();

    public static final long NO_VALUE = -1;

    private final Context context;
    private final OpType[] opTypes;
    private final int rounds;
    private final Time time;
    private final boolean isLast;

    private final List<AdapterItem> list;
    {
        list = new ArrayList<>();
    }

    private long mStart;
    private long mEnd;

    public AbsTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        this.context = context.getApplicationContext();
        this.opTypes = opTypes;
        this.rounds = rounds;
        this.time = time;
        this.isLast = isLast;
    }

    @Override
    public void run() {
        try {
            setUp();

            for (OpType opType: opTypes) {

                switch (opType) {

                    case INSERT:
                        testInsert();
                        break;

                    case QUERY_ONE:
                        testQueryOne();
                        break;

                    case QUERY_ALL:
                        testQueryAll();
                        break;

                    case DELETE_ALL:
                        testDelete();
                        break;
                }
            }

        } catch (Throwable t) {
            Debug.e(t, getClass().getSimpleName());
        } finally {
            tearDown();
        }

        deleteDB();
        post();

        System.gc();

        if (isLast) {
            EventBus.getDefault().post(new TestCompleteEvent());
        }
    }

    private void deleteDB() {
        final String dbName = getORM().getDbName();
        if (dbName == null) {
            return;
        }
        delDB(dbName);
    }

    private void post() {
        ensureThatEveryOpTypePresent();
        final TestEvent event = new TestEvent(list);
        EventBus.getDefault().post(event);
    }

    private void ensureThatEveryOpTypePresent() {

        // check if all items are present, if not, add dummy

        final AdapterItem dummy = AdapterItem.newDummy();
        dummy.setRounds(rounds);

        AdapterItem item;
        int index;

        for (OpType type: opTypes) {
            dummy.setOpType(type);
            index = list.indexOf(dummy);

            if (index == -1) {
                item = AdapterItem.newItem(type, rounds, time);
                item.setName(getORM().getOrmName());
                item.setValue(NO_VALUE);
                list.add(item);
            }
        }
    }

    private void testInsert() {
        final List<T> list = createList(rounds, getInstanceCreator());
        start();
        insert(list);
        end();
        summary(OpType.INSERT);
    }

    private void testQueryAll() {
        start();
        queryAll();
        end();
        summary(OpType.QUERY_ALL);
    }

    private void testQueryOne() {

        final long id = (int) (rounds * .75F + .5F);

        start();
        queryOne(id);
        end();
        summary(OpType.QUERY_ONE);
    }

    private void testDelete() {
        start();
        deleteAll();
        end();
        summary(OpType.DELETE_ALL);
    }

    public Context getContext() {
        return context;
    }

    public int getRounds() {
        return rounds;
    }

    protected void delDB(String name) {
        final File file = context.getDatabasePath(name);
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public List<T> createList(int rounds, InstanceCreator<T> creator) {
        final TestRandomizer randomizer = new TestRandomizer();
        return randomizer.create(rounds, creator);
    }

    protected void start() {
        switch (time) {
            case MILLIS:
                mStart = SystemClock.elapsedRealtime();
                break;

            case NANOS:
                mStart = System.nanoTime();
                break;
        }

    }

    protected void end() {
        switch (time) {
            case MILLIS:
                mEnd = SystemClock.elapsedRealtime();
                break;

            case NANOS:
                mEnd = System.nanoTime();
                break;
        }
    }

    private void summary(OpType type) {
        final long took = mEnd - mStart;
        final AdapterItem item = AdapterItem.newItem(type, rounds, time);
        item.setValue(took);
        item.setName(getORM().getOrmName());
        list.add(item);
    }
}
