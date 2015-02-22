package ru.noties.storm.sample;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.StormIterator;
import ru.noties.storm.adapter.BaseStormIteratorAdapter;
import ru.noties.storm.exc.StormException;

import ru.noties.storm.sample.db.SampleManager;
import ru.noties.storm.sample.model.SampleObject;

public class MainActivity extends ActionBarActivity {

    private DatabaseManager mManager;
    private MainAdapter<SampleObject> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView listView = new ListView(this);
        setContentView(listView);
        mAdapter = new MainAdapter<>(this, new SampleObject[50]);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        mManager = SampleManager.getDatabaseManager();
        mManager.open();

        try {
            Storm.newInsert(mManager).insert(SampleManager.generateNew(10));
        } catch (StormException e) {
            e.printStackTrace();
        }

        final StormIterator<SampleObject> iterator = Storm.newSelect(mManager).queryAllIterator(SampleObject.class);
        mAdapter.setIterator(iterator, true);
    }

    @Override
    public void onStop() {
        super.onStop();

        final StormIterator<?> iterator = mAdapter.getIterator();
        if (iterator != null) {
            iterator.close();
        }

        mManager.close();
    }

    private static class MainAdapter<T> extends BaseStormIteratorAdapter<T> {

        public MainAdapter(Context context, T[] poolArray) {
            super(context, poolArray);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        @Override
        protected View newView(LayoutInflater inflater, int position, ViewGroup group) {
            final View view = inflater.inflate(android.R.layout.simple_list_item_1, group, false);
            view.setTag(new Holder(view));
            return view;
        }

        @Override
        protected void bindView(int position, View view) {
            final Holder holder = (Holder) view.getTag();
            holder.textView.setText(getItem(position).toString());
        }

        private static class Holder {

            private final TextView textView;

            Holder(View view) {
                this.textView = (TextView) view.findViewById(android.R.id.text1);
            }
        }
    }
}
