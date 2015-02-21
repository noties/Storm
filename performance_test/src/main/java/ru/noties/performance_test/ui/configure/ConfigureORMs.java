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
public class ConfigureORMs extends BaseConfigureChildFragment<BuildConfig.ORM[]> {

    public static final String TAG = "tag.ConfigureOrms";

    @Override
    public BuildConfig.ORM[] getSelectedValues() {

        final BuildConfig.ORM[] orms = BuildConfig.ORM.values();
        final SparseBooleanArray array = getSelectedPositions();
        final List<BuildConfig.ORM> list = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            if (array.valueAt(i)) {
                list.add(orms[array.keyAt(i)]);
            }
        }

        final BuildConfig.ORM[] selected = new BuildConfig.ORM[list.size()];
        list.toArray(selected);

        return selected;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    protected int getTitleResId() {
        return R.string.configure_title_orms;
    }

    @Override
    protected int getListViewChoiceMode() {
        return AbsListView.CHOICE_MODE_MULTIPLE;
    }

    @Override
    protected ConfigureAdapterItem[] getAdapterItems() {
        final BuildConfig.ORM[] orms = BuildConfig.ORM.values();
        final ConfigureAdapterItem[] items = new ConfigureAdapterItem[orms.length];
        for (int i = 0; i < orms.length; i++) {
            items[i] = new ConfigureAdapterItem(orms[i].getOrmName(), true);
        }
        return items;
    }
}
