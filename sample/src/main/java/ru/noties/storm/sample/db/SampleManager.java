package ru.noties.storm.sample.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Pragma;
import ru.noties.storm.Storm;
import ru.noties.storm.sample.model.SampleObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class SampleManager {

    private SampleManager() {}

    public static DatabaseManager getDatabaseManager() {

        final Pragma pragma = new Pragma.Builder()
                .setForeignKeys(Pragma.ForeignKeys.ON)
                .build();

        return new DatabaseManager(
                Storm.getApplicationContext(),
                "sample.db",
                1,
                new Class[] {SampleObject.class},
                pragma
        );
    }

    public static List<SampleObject> generateNew(int count) {
        final List<SampleObject> list = new ArrayList<>(count * 2);
        long now;
        SampleObject object;
        SampleObject prev = null;
        for (int i = 0; i < count; i++) {
            now = System.currentTimeMillis();
            object = new SampleObject();
            object.setSomeBoolean((i & 1) == 0);
            object.setSomeDate(new Date(now));
            object.setSomeShort((short) i);
            object.setSomeInt(i);
            object.setSomeLong(now);
            object.setSomeDouble(i);
            object.setSomeFloat(i);
            object.setSomeString(String.valueOf(i));
            object.setSomeWrapper(new SampleObject.Wrapper(prev));
            list.add(object);
            prev = object;
        }
        return list;
    }
}
