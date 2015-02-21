package ru.noties.storm.sample;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.noties.storm.DatabaseManager;
import ru.noties.storm.Storm;
import ru.noties.storm.StormIterator;
import ru.noties.storm.exc.StormException;
import ru.noties.storm.pool.ListViewObjectPool;

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

    private static class MainAdapter<T> extends BaseAdapter {

        private final LayoutInflater mInflater;
        private final ListViewObjectPool<T> mPool;

        private StormIterator<T> mIterator;

        public MainAdapter(Context context, T[] poolArray) {
            this.mInflater = LayoutInflater.from(context);
            this.mPool = new ListViewObjectPool<>(poolArray);
            registerDataSetObserver(mPool);
        }

        public void setIterator(StormIterator<T> iterator, boolean notify) {
            mIterator = iterator;
            mIterator.setObjectPool(mPool);
            if (notify) {
                notifyDataSetChanged();
            }
        }

        public StormIterator<T> getIterator() {
            return mIterator;
        }

        @Override
        public int getCount() {
            return mIterator != null ? mIterator.getCount() : 0;
        }

        @Override
        public T getItem(int position) {
            return mIterator.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
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
            final View view = mInflater.inflate(android.R.layout.simple_list_item_1, group, false);
            view.setTag(new Holder(view));
            return view;
        }

        private void bindView(int position, View view) {
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
