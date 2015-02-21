package ru.noties.performance_test;


import com.orm.SugarApp;

import co.uk.rushorm.android.RushAndroid;
import ru.noties.debug.Debug;
import ru.noties.storm.InstanceCreator;
import ru.noties.storm.Storm;
import ru.noties.performance_test.storm.StormObject;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 09.02.2015.
 */
// hell ye!
public class MainApplication extends SugarApp {

    // I don't think that we should do it in production
    public static MainApplication sInstance;

    private boolean mIsFakeCreate;

    @Override
    public void onCreate() {
        super.onCreate();

        Debug.init(true);

        if (sInstance == null) {
            sInstance = this;
        }

        if (!mIsFakeCreate) {
            Storm.getInstance().init(getApplicationContext(), true);
//            Storm.getInstance().registerInstanceCreator(StormObject.class, new InstanceCreator<StormObject>() {
//                @Override
//                public StormObject create(Class<StormObject> clazz) {
//                    return new StormObject();
//                }
//            });
//            RushAndroid.initialize(getApplicationContext());
        }
    }

    public void callOnCreate() {
        mIsFakeCreate = true;
        onCreate();
    }
}
