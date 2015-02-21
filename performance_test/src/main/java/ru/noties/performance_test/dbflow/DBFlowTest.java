package ru.noties.performance_test.dbflow;

import android.content.Context;
//import android.database.DatabaseUtils;
//
//import com.raizlabs.android.dbflow.config.FlowManager;
//import com.raizlabs.android.dbflow.runtime.DBTransactionInfo;
//import com.raizlabs.android.dbflow.runtime.TransactionManager;
//import com.raizlabs.android.dbflow.runtime.transaction.BaseTransaction;
//import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
//import com.raizlabs.android.dbflow.sql.language.Delete;
//import com.raizlabs.android.dbflow.sql.language.Select;
//
//import java.util.List;
//
//import ru.noties.storm.InstanceCreator;
//import java.util.List;
//
//import ru.noties.storm.InstanceCreator;
//import ru.noties.performance_test.AbsTest;
//
///**
// * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 12.02.2015.
// */
//public class DBFlowTest extends AbsTest {
//
//    public DBFlowTest(Context context1, int rounds1) {
//        super(context1, rounds1);
//    }
//
////    @Override
////    protected void execute() {
////
////        // java.lang.IllegalStateException: Context cannot be null for FlowManager
//////        open();
////
//////        final List<DBFlowObject> list = createList(getRounds(), new InstanceCreator<DBFlowObject>() {
//////            @Override
//////            public DBFlowObject create(Class<DBFlowObject> clazz) {
//////                return new DBFlowObject();
//////            }
//////        });
//////
//////        insert(list);
//////        query();
//////
//////        deleteAll();
////
//////        FlowManager.destroy();
////
//////        getContext().deleteDatabase("db_flow.db");
////    }
//
//    @Override
//    protected void setUp() {
//
//    }
//
//    @Override
//    protected void tearDown() {
//
//    }
//
//    @Override
//    protected void insert(List list) {
//
//    }
//
//    @Override
//    protected void queryOne(long id) {
//
//    }
//
//    @Override
//    protected void queryAll() {
//
//    }
//
//    @Override
//    protected void deleteAll() {
//
//    }
//
//    @Override
//    protected InstanceCreator getInstanceCreator() {
//        return null;
//    }
//
//    @Override
//    protected String getDBName() {
//        return null;
//    }
//
////    void open() {
////        start();
//////        FlowManager.init(getContext().getApplicationContext());
////        end();
////        summary("open", false);
////    }
////
////    void deleteAll() {
////        start();
//////        final DBTransactionInfo info = DBTransactionInfo.create(BaseTransaction.PRIORITY_UI);
//////        TransactionManager.getInstance().delete(info, DBFlowObject.class);
////        Delete.table(DBFlowObject.class);
////        end();
////        summary("deleteAll", true);
////    }
////
////    void insert(List<DBFlowObject> list) {
////        start();
////        TransactionManager.getInstance().insert(ProcessModelInfo.withModels(list));
////        end();
////        summary("insert", true);
////    }
////
////    void query() {
////        start();
////        new Select().from(DBFlowObject.class).queryList();
////        end();
////        summary("query", true);
////    }
//}
