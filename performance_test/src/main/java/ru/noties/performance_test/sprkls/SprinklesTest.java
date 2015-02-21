package ru.noties.performance_test.sprkls;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ru.noties.storm.InstanceCreator;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.Time;
import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Query;
import se.emilsjolander.sprinkles.Sprinkles;
import se.emilsjolander.sprinkles.Transaction;
import se.emilsjolander.sprinkles.typeserializers.SqlType;
import se.emilsjolander.sprinkles.typeserializers.TypeSerializer;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 12.02.2015.
 */
public class SprinklesTest extends AbsTest<SprinklesObject> {

    public SprinklesTest(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
        super(context, opTypes, rounds, time, isLast);
    }

    @Override
    protected void setUp() {
        open();
    }

    @Override
    protected void tearDown() {

    }

    @Override
    protected void insert(List<SprinklesObject> list) {
        final Transaction transaction = new Transaction();
        for (SprinklesObject object: list) {
            object.save(transaction);
        }
        transaction.setSuccessful(true);
        transaction.finish();
    }

    @Override
    protected void queryOne(long id) {
        Query.one(SprinklesObject.class, "where id = ?", id).get();
    }

    @Override
    protected void queryAll() {
        Query.all(SprinklesObject.class).get().asList();
    }

    @Override
    protected void deleteAll() {

        // no exceptions are thrown, catch it yourself
        // throws IndexOutOfBoundsException if passed db version is not starts with 0
        // java.lang.IllegalStateException: Couldn't read row 5470, col 5 from CursorWindow.  Make sure the Cursor is initialized correctly before accessing data from it.

        final Transaction dt = new Transaction();
        for (SprinklesObject sprinklesObject : Query.all(SprinklesObject.class).get()) {
            sprinklesObject.delete(dt);
        }

        dt.setSuccessful(true);
        dt.finish();
    }

    Sprinkles open() {

        final BuildConfig.ORM orm = getORM();

        final Sprinkles sprinkles = Sprinkles.init(getContext(), orm.getDbName(), orm.getDbVersion());
        sprinkles.addMigration(new Migration() {
            @Override
            protected void doMigration(SQLiteDatabase sqLiteDatabase) {
                final String sql = "CREATE TABLE IF NOT EXISTS `sprinkles_object` (" +
                        "`someString` TEXT, " +
                        "`someBool` INTEGER, " +
                        "`id`INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "`someDouble` REAL, " +
                        "`someLong` INTEGER, " +
                        "`someFloat` REAL, " +
                        "`someInt` INTEGER, " +
                        "`someShort` INTEGER" +
                        ");";
                sqLiteDatabase.execSQL(sql);
            }
        });

        // no short type
        sprinkles.registerType(Short.TYPE, new TypeSerializer<Short>() {
            @Override
            public Short unpack(Cursor c, String name) {
                return c.getShort(c.getColumnIndex(name));
            }

            @Override
            public void pack(Short object, ContentValues cv, String name) {
                cv.put(name, object);
            }

            @Override
            public String toSql(Short object) {
                return String.valueOf(object);
            }

            @Override
            public SqlType getSqlType() {
                return SqlType.INTEGER;
            }
        });

        return sprinkles;
    }

    @Override
    protected InstanceCreator<SprinklesObject> getInstanceCreator() {
        return new InstanceCreator<SprinklesObject>() {
            @Override
            public SprinklesObject create(Class<SprinklesObject> clazz) {
                return new SprinklesObject();
            }
        };
    }

    @Override
    protected BuildConfig.ORM getORM() {
        return BuildConfig.ORM.SPRINKLES;
    }
}
