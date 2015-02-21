package ru.noties.performance_test.ui.configure;

import android.widget.AbsListView;

import ru.noties.performance_test.R;
import ru.noties.performance_test.Time;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureTime extends BaseConfigureChildFragment<Time> {

    public static final String TAG = "tag.ConfigureTime";

    @Override
    public Time getSelectedValues() {
        return Time.values()[getSelectedPosition()];
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    protected int getTitleResId() {
        return R.string.configure_title_time;
    }

    @Override
    protected int getListViewChoiceMode() {
        return AbsListView.CHOICE_MODE_SINGLE;
    }

    @Override
    protected ConfigureAdapterItem[] getAdapterItems() {
        final Time[] times = Time.values();
        final ConfigureAdapterItem[] items = new ConfigureAdapterItem[times.length];
        for (int  i = 0; i < times.length; i++) {
            items[i] = new ConfigureAdapterItem(times[i].name());
        }
        items[0].setChecked(true);
        return items;
    }
}
