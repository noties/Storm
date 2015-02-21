package ru.noties.performance_test.ollie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

//import java.util.List;
//
//import ollie.Ollie;
//import ollie.query.Delete;
//import ollie.query.Select;
//import ru.noties.storm.InstanceCreator;
//import ru.noties.performance_test.AbsTest;
//import ru.noties.performance_test.BuildConfig;
//
///**
// * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
// */
//public class OllieTest extends AbsTest<OllieObject> {
//
//    public OllieTest(Context context1, int rounds1) {
//        super(context1, rounds1);
//    }
//
//    @Override
//    protected void setUp() {
//        Ollie.with(getContext())
//                .setName(BuildConfig.OLLIE_DB_NAME)
//                .setVersion(BuildConfig.DB_VERSION)
//                .setLogLevel(Ollie.LogLevel.FULL)
//                .setCacheSize(getRounds())
//                .init();
//    }
//
//    @Override
//    protected void tearDown() {
//        final SQLiteDatabase db = Ollie.getDatabase();
//        if (db != null && db.isOpen()) {
//            db.close();
//        }
//    }
//
//    @Override
//    protected void insert(List<OllieObject> list) {
//        final SQLiteDatabase db = Ollie.getDatabase();
//        db.beginTransaction();
//        try {
//            for (OllieObject object: list) {
//                object.save();
//            }
//            db.setTransactionSuccessful();
//        } catch (SQLiteException e) {
//            e.printStackTrace();
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    @Override
//    protected void queryOne(long id) {
//        Select.from(OllieObject.class).where("id = ?", id);
//    }
//
//    @Override
//    protected void queryAll() {
//        Select.from(OllieObject.class).fetch();
//    }
//
//    @Override
//    protected void deleteAll() {
//        Delete.from(OllieObject.class);
//    }
//
//    @Override
//    protected InstanceCreator<OllieObject> getInstanceCreator() {
//        return new InstanceCreator<OllieObject>() {
//            @Override
//            public OllieObject create(Class<OllieObject> clazz) {
//                return new OllieObject();
//            }
//        };
//    }
//
//    @Override
//    protected String getDBName() {
//        return BuildConfig.OLLIE_DB_NAME;
//    }
//}
