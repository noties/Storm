package ru.noties.performance_test.ui.configure;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.Configuration;
import ru.noties.performance_test.OpType;
import ru.noties.performance_test.R;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureFragment extends BaseFragment {

    public static interface OnConfigurationObtainedListener {
        void onConfigurationObtained(Configuration configuration);
    }

    public static final String TAG = "tag.ConfigurationFragment";

    private OnConfigurationObtainedListener mListener;

    private TextView mTitle;
    private TextView mPages;

    private ConfigurePagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle sis) {
        super.onCreate(sis);

        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof OnConfigurationObtainedListener)) {
            throw new IllegalStateException("Holding activity must implement OnConfigurationObtainedListener");
        }

        mListener = (OnConfigurationObtainedListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {

        final View view = inflater.inflate(R.layout.fragment_configure, parent, false);

        mTitle = findView(view, R.id.configure_title);
        mPages = findView(view, R.id.configure_pages);

        final BaseConfigureChildFragment<?> [] fragments = getFragments();

        mAdapter = new ConfigurePagerAdapter(getChildFragmentManager(), fragments);

        final ViewPager viewPager = findView(view, R.id.view_pager);
        viewPager.setOffscreenPageLimit(fragments.length - 1);
        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPagerOnPageChangeListenerAdapter() {
            @Override
            public void onPageSelected(int position) {
                mTitle.setText(mAdapter.getPageTitle(position));
                mPages.setText(getString(R.string.configure_pages_pattern, position + 1, mAdapter.getCount()));
            }
        };
        viewPager.setOnPageChangeListener(onPageChangeListener);
        viewPager.setAdapter(mAdapter);
        onPageChangeListener.onPageSelected(0);

        return view;
    }

    private BaseConfigureChildFragment<?>[] getFragments() {
        return new BaseConfigureChildFragment[] {
                new ConfigureOpType(),
                new ConfigureORMs(),
                new ConfigureRounds(),
                new ConfigureTime()
        };
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_configure, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_configure_done) {
            done();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void done() {

//        Debug.i("opTypes; %s", Arrays.toString(getSelectedOpTypes()));
//        Debug.i("orms: %s", Arrays.toString(getSelectedORMs()));
//        Debug.i("rounds: %s", Arrays.toString(getSelectedRounds()));
//        Debug.i("time: %s", getSelectedTime());

        final BuildConfig.ORM[] orms    = getSelectedORMs();
        final OpType[] opTypes          = getSelectedOpTypes();
        final int[] rounds              = getSelectedRounds();
        final Time time                 = getSelectedTime();

        if (orms == null
                || orms.length == 0) {
            toastError(R.string.configure_error_orms);
            return;
        }

        if (opTypes == null
                || opTypes.length == 0) {
            toastError(R.string.configure_error_op_types);
            return;
        }

        if (rounds == null
                || rounds.length == 0) {
            toastError(R.string.configure_error_rounds);
            return;
        }

        final Configuration configuration = new Configuration(orms, opTypes, rounds, time);
        mListener.onConfigurationObtained(configuration);
    }

    private OpType[] getSelectedOpTypes() {
        final ConfigureOpType opType = (ConfigureOpType) getChildFragmentManager()
                .findFragmentByTag(ConfigureOpType.TAG);
        if (opType == null) {
            // hm...
            return null;
        }
        return opType.getSelectedValues();
    }

    private BuildConfig.ORM[] getSelectedORMs() {
        final ConfigureORMs orms = (ConfigureORMs) getChildFragmentManager()
                .findFragmentByTag(ConfigureORMs.TAG);
        if (orms == null) {
            return null;
        }
        return orms.getSelectedValues();
    }

    private int[] getSelectedRounds() {
        final ConfigureRounds configureRounds = (ConfigureRounds) getChildFragmentManager()
                .findFragmentByTag(ConfigureRounds.TAG);
        if (configureRounds == null) {
            return null;
        }
        return configureRounds.getSelectedValues();
    }

    private Time getSelectedTime() {
        final ConfigureTime configureTime = (ConfigureTime) getChildFragmentManager()
                .findFragmentByTag(ConfigureTime.TAG);
        if (configureTime == null) {
            return null;
        }
        return configureTime.getSelectedValues();
    }

    private void toastError(@StringRes int errorId) {
        Toast.makeText(getActivity(), errorId, Toast.LENGTH_LONG).show();
    }

    private static class ConfigurePagerAdapter extends TaggedFragmentPagerAdapter {

        private final BaseConfigureChildFragment<?>[] mFragments;

        public ConfigurePagerAdapter(FragmentManager fm, BaseConfigureChildFragment<?>[] fragments) {
            super(fm);
            this.mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public String createFragmentTag(int position) {
            return mFragments[position].getFragmentTag();
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments[position].getTitle();
        }
    }
}
