package ru.noties.performance_test.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

//import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;
import ru.noties.debug.Debug;
import ru.noties.performance_test.AbsTest;
import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.ColorEvaluator;
import ru.noties.performance_test.Configuration;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.R;
import ru.noties.performance_test.TestCompleteEvent;
import ru.noties.performance_test.TestEvent;
import ru.noties.performance_test.Time;
import ru.noties.performance_test.aa.AATest;
//import ru.noties.performance_test.dbflow.DBFlowDB;
//import ru.noties.performance_test.dbflow.DBFlowTest;
//import ru.noties.performance_test.ollie.OllieTest;
import ru.noties.performance_test.raw.RawTest;
import ru.noties.performance_test.rush.RushTest;
import ru.noties.performance_test.sprkls.SprinklesTest;
import ru.noties.performance_test.storm.StormTest;
import ru.noties.performance_test.sugar.SugarTest;
import ru.noties.performance_test.ui.configure.ConfigureFragment;

public class MainActivity extends ActionBarActivity
        implements ConfigureFragment.OnConfigurationObtainedListener {

    private final ExecutorService mExecutor;
    {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    private ColorEvaluator mColorEvaluator;
    private MainAdapter mAdapter;
    private boolean mIsTestsRunning;

    private Configuration mConfiguration;
    private boolean mIsConfigurationShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initOrientation();
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MainAdapter(this);

        recyclerView.setAdapter(mAdapter);

        final Resources r = getResources();
        mColorEvaluator = new ColorEvaluator(
                r.getColor(R.color.good_color),
                r.getColor(R.color.bad_color)
        );
    }

    // todo cancel

    private void initOrientation() {
        if (Build.VERSION.SDK_INT >= 18) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            return;
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed() {
        if (mIsConfigurationShowing) {
            hideConfiguration();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        processMenu(menu);
        return true;
    }

    private void processMenu(Menu menu) {
        final MenuItem runItem = menu.findItem(R.id.menu_run);
        runItem.setVisible(!mIsConfigurationShowing && !mIsTestsRunning);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_run:
                configurationRequested();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configurationRequested() {
        clearAdapter();
        showConfiguration();
    }

    private void showConfiguration() {

        mIsConfigurationShowing = true;
        supportInvalidateOptionsMenu();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.child_content_frame, new ConfigureFragment(), ConfigureFragment.TAG)
                .commit();
    }

    private void hideConfiguration() {

        mIsConfigurationShowing = false;
        supportInvalidateOptionsMenu();

        final Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConfigureFragment.TAG);
        if (fragment == null) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .remove(fragment)
                .commit();
    }

    @Override
    public void onConfigurationObtained(Configuration configuration) {
        prepareAndRunTests(configuration);
        hideConfiguration();
    }

    private void prepareAndRunTests(Configuration configuration) {

        mConfiguration = configuration;

        prepareAdapter  (mConfiguration);
        runTests        (mConfiguration);
    }

    private void clearAdapter() {
        final int count = mAdapter.getItemCount();
        if (count != 0) {
            mAdapter.setInitialItems(null, false);
            mAdapter.notifyItemRangeRemoved(0, count);
        }
    }

    private void prepareAdapter(Configuration configuration) {

        updateOptionsMenu(true);

        clearAdapter();

        mAdapter.setInitialItems(getInitialItems(configuration), true);
    }

    private List<AdapterItem> getInitialItems(Configuration configuration) {
        final List<AdapterItem> headers = new ArrayList<>();
        AdapterItem item;
        for (int round: configuration.getRounds()) {
            for (OpType opType: configuration.getOpTypes()) {
                item = AdapterItem.newHeader(opType, round);
                headers.add(item);
            }
        }
        return headers;
    }

    private void runTests(Configuration configuration) {

        final BuildConfig.ORM[] orms = configuration.getOrms();
        final BuildConfig.ORM   last = orms[orms.length - 1];

        for (BuildConfig.ORM orm: orms) {

            final boolean isLast = orm == last;

            switch (orm) {

                case RAW:
                    testRaw(configuration, isLast);
                    break;

                case STORM:
                    testStorm(configuration, isLast);
                    break;

                case SPRINKLES:
                    testSprinkles(configuration, isLast);
                    break;

                case ACTIVE_ANDROID:
                    testActiveAndroid(configuration, isLast);
                    break;

                case SUGAR_ORM:
                    testSugarORM(configuration, isLast);
                    break;

                case RUSH_ORM:
                    testRushORM(configuration, isLast);
                    break;
            }
        }
    }

    private void updateOptionsMenu(boolean running) {
        mIsTestsRunning = running;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void testRaw(Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new RawTest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    private void testStorm(Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new StormTest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    private void testSprinkles(Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new SprinklesTest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    // does not work
//    private void testDBFlow() {
//        FlowManager.init(this.getApplicationContext());
//        test(new TestProvider() {
//            @Override
//            public AbsTest create(Context context, int rounds) {
//                return new DBFlowTest(context, rounds);
//            }
//        });
//        FlowManager.destroy();
//    }

    private void testActiveAndroid(Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new AATest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    private void testSugarORM(Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new SugarTest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    private void testRushORM(final Configuration configuration, boolean checkLast) {
        test(configuration, checkLast, new TestProvider() {
            @Override
            public AbsTest create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast) {
                return new RushTest(context, opTypes, rounds, time, isLast);
            }
        });
    }

    // doesn't work either
//    private void testOllie() {
//        test(new TestProvider() {
//            @Override
//            public AbsTest create(Context context, int rounds) {
//                return new OllieTest(context, rounds);
//            }
//        });
//    }

//    private void testORMLite() {
//        test(null);
//    }

    private void test(Configuration configuration, boolean checkLast, TestProvider factory) {
        final int[] configurationRounds = configuration.getRounds();
        for (int round: configuration.getRounds()) {
            final boolean isLast = checkLast && configurationRounds[configurationRounds.length - 1] == round;
            mExecutor.execute(factory.create(this, configuration.getOpTypes(), round, configuration.getTime(), isLast));
        }
    }

    private static interface TestProvider<T extends AbsTest & Runnable> {
        T create(Context context, OpType[] opTypes, int rounds, Time time, boolean isLast);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(TestCompleteEvent event) {
        onTestComplete();
    }

    private void onTestComplete() {

        final List<AdapterItem> items = mAdapter.getItems();
        final AdapterItem dummy = AdapterItem.newDummy();

        List<AdapterItem> list;

        for (int round: mConfiguration.getRounds()) {
            for (OpType opType: mConfiguration.getOpTypes()) {

                dummy.setRounds(round);
                dummy.setOpType(opType);
                list = getSpecificItems(items, dummy);
                processItemsOfOneType(list);
            }
        }

        mAdapter.notifyDataSetChanged();

        updateOptionsMenu(false);
    }

    private List<AdapterItem> getSpecificItems(List<AdapterItem> items, AdapterItem toFind) {
        final List<AdapterItem> list = new ArrayList<>();
        for (AdapterItem item: items) {
            if (item.getType() == AdapterItem.Type.HEADER) {
                continue;
            }
            if (item.equals(toFind)) {
                list.add(item);
            }
        }
        return list;
    }

    private void processItemsOfOneType(List<AdapterItem> items) {

        // find min
        // find max
        // apply color

        long min = Long.MAX_VALUE;
        long max = 0L;

        long value;

        for (AdapterItem item: items) {

            value = item.getValue();

            if (value == AbsTest.NO_VALUE) {
                continue;
            }

            if (value < min) {
                min = value;
            }

            if (value > max) {
                max = value;
            }
        }

        // if min is still max, set to 0L
        if (min == Long.MAX_VALUE) {
            min = 0L;
        }

        for (AdapterItem item: items) {
            item.setColor(getColor(min, max, item.getValue()));
        }
    }

    private int getColor(long min, long max, long value) {
        if (value == AbsTest.NO_VALUE) {
            return getResources().getColor(R.color.no_color);
        }
        return mColorEvaluator.getColor(min, max, value);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(TestEvent event) {
        addItems(event.getItems());
    }

    private void addItems(List<AdapterItem> items) {

        if (items == null
                || items.size() == 0) {
            return;
        }

        final List<AdapterItem> current = mAdapter.getItems();

        int index;
        int position;

        for (AdapterItem item: items) {

            // find last index for type
            // insert
            // notify item inserted
            index = current.lastIndexOf(item);
            if (index == -1) {
                // should not happen
                continue;
            }

            position = index + 1;

            current.add(position, item);
            mAdapter.notifyItemInserted(position);
        }
    }
}
