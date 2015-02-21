package ru.noties.performance_test.ui.configure;

import android.util.SparseBooleanArray;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import ru.noties.performance_test.BuildConfig;
import ru.noties.performance_test.R;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureRounds extends BaseConfigureChildFragment<int[]> {

    public static final String TAG = "tag.ConfigureRounds";

    @Override
    public int[] getSelectedValues() {
        final int[] rounds = BuildConfig.ROUNDS;
        final SparseBooleanArray array = getSelectedPositions();
        final List<Integer> list = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            if (array.valueAt(i)) {
                list.add(rounds[array.keyAt(i)]);
            }
        }

        final int[] selected = new int[list.size()];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = list.get(i);
        }

        return selected;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    protected int getTitleResId() {
        return R.string.configure_title_rounds;
    }

    @Override
    protected int getListViewChoiceMode() {
        return AbsListView.CHOICE_MODE_MULTIPLE;
    }

    @Override
    protected ConfigureAdapterItem[] getAdapterItems() {
        final int[] rounds = BuildConfig.ROUNDS;
        final ConfigureAdapterItem[] items = new ConfigureAdapterItem[rounds.length];

        final int start = BuildConfig.ROUNDS_SELECTED_FROM;
        final int end   = BuildConfig.ROUNDS_SELECTED_TO;

        int round;
        boolean selected;

        for (int i = 0; i < rounds.length; i++) {
            round = rounds[i];
            selected = round > start && round < end;
            items[i] = new ConfigureAdapterItem(String.valueOf(rounds[i]), selected);
        }
        return items;
    }
}
