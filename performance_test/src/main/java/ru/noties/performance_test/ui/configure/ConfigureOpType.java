package ru.noties.performance_test.ui.configure;

import android.util.SparseBooleanArray;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import ru.noties.performance_test.OpType;
import ru.noties.performance_test.R;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureOpType extends BaseConfigureChildFragment<OpType[]> {

    public static final String TAG = "tag.ConfigureOpTypes";

    @Override
    public OpType[] getSelectedValues() {
        final OpType[] opTypes = OpType.values();
        final SparseBooleanArray array = getSelectedPositions();
        final List<OpType> list = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            if (array.valueAt(i)) {
                list.add(opTypes[array.keyAt(i)]);
            }
        }

        final OpType[] selected = new OpType[list.size()];
        list.toArray(selected);

        return selected;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    protected int getTitleResId() {
        return R.string.configure_title_op_types;
    }

    @Override
    protected int getListViewChoiceMode() {
        return AbsListView.CHOICE_MODE_MULTIPLE;
    }

    @Override
    protected ConfigureAdapterItem[] getAdapterItems() {
        final OpType[] opTypes = OpType.values();
        final ConfigureAdapterItem[] items = new ConfigureAdapterItem[opTypes.length];
        for (int i = 0; i < opTypes.length; i++) {
            items[i] = new ConfigureAdapterItem(opTypes[i].name(), true);
        }
        return items;
    }
}
