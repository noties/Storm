package ru.noties.performance_test.ui.configure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import ru.noties.performance_test.R;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 15.02.2015.
 */
public class ConfigureAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final ConfigureAdapterItem[] mItems;

    public ConfigureAdapter(Context context, ConfigureAdapterItem[] items) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public ConfigureAdapterItem getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ConfigureAdapterItem[] getItems() {
        return mItems;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view;

        if (convertView == null) {
            view = newView(position, parent);
        } else {
            view = convertView;
        }

        bindView(position, view);

        return view;
    }

    private View newView(int position, ViewGroup group) {
        final View view = mInflater.inflate(R.layout.adapter_configure_item, group, false);
        view.setTag(new Holder(view));
        return view;
    }

    private void bindView(int position, View view) {

        final Holder holder = (Holder) view.getTag();
        final ConfigureAdapterItem item = getItem(position);

        holder.text.setText(item.getName());
        holder.checkBox.setChecked(item.isChecked());
    }

    private static class Holder {

        private final CheckBox checkBox;
        private final TextView text;

        Holder(View view) {
            this.checkBox   = (CheckBox) view.findViewById(R.id.checkbox);
            this.text       = (TextView) view.findViewById(R.id.text1);
        }
    }
}
