package ru.noties.performance_test.ui.configure;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import ru.noties.storm.Storm;
import ru.noties.performance_test.R;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public abstract class BaseConfigureChildFragment<T> extends BaseFragment {

    public      abstract T getSelectedValues();
    public      abstract String getFragmentTag();
    protected   abstract @StringRes int getTitleResId();
    protected   abstract int getListViewChoiceMode();
    protected   abstract ConfigureAdapterItem[] getAdapterItems();

    public CharSequence getTitle() {
        return Storm.getApplicationContext().getString(getTitleResId());
    }

    private ListView            mListView;
    private ConfigureAdapter    mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle sis) {

        final View view = inflater.inflate(R.layout.fragment_base_configure_child, parent, false);

        final ConfigureAdapterItem[] items = getAdapterItems();

        mAdapter = new ConfigureAdapter(getActivity(), items);

        mListView = findView(view, R.id.list_view);
        mListView.setChoiceMode(getListViewChoiceMode());

        for (int i = 0; i < items.length; i++) {
            mListView.setItemChecked(i, items[i].isChecked());
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ConfigureAdapterItem item = (ConfigureAdapterItem) parent.getAdapter().getItem(position);
                mListView.setItemChecked(position, !item.isChecked());
                processSelectedStates(mListView, items);
                mAdapter.notifyDataSetChanged();
            }
        });

        mListView.setAdapter(mAdapter);
        return view;
    }

    protected void processSelectedStates(ListView listView, ConfigureAdapterItem[] items) {
        final int choiceMode = listView.getChoiceMode();
        if (choiceMode == AbsListView.CHOICE_MODE_MULTIPLE) {
            final SparseBooleanArray array = listView.getCheckedItemPositions();
            int key;
            for (int i = 0; i < array.size(); i++) {
                key = array.keyAt(i);
                items[key].setChecked(array.valueAt(i));
            }
        } else if (choiceMode == AbsListView.CHOICE_MODE_SINGLE) {
            final int position = listView.getCheckedItemPosition();
            for (int i = 0; i < items.length; i++) {
                items[i].setChecked(i == position);
            }
        }
    }

    protected SparseBooleanArray getSelectedPositions() {
        final SparseBooleanArray array = new SparseBooleanArray();
        final ConfigureAdapterItem[] items = mAdapter.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].isChecked()) {
                array.put(i, true);
            }
        }
        return array;
    }

    protected int getSelectedPosition() {
        final ConfigureAdapterItem[] items = mAdapter.getItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].isChecked()) {
                return i;
            }
        }
        return 0;
    }
}
