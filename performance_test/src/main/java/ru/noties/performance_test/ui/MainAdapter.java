package ru.noties.performance_test.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.noties.performance_test.R;

/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 14.02.2015.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private final Resources mResources;
    private final LayoutInflater mInflater;
    private List<AdapterItem> mList;

    public MainAdapter(Context context) {
        this.mInflater  = LayoutInflater.from(context);
        this.mResources = context.getResources();
    }

    public void setInitialItems(List<AdapterItem> list, boolean notify) {
        this.mList = list;
        if (notify) {
            notifyItemRangeInserted(0, mList.size());
        }
    }

    public List<AdapterItem> getItems() {
        return mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final AdapterItem.Type type = AdapterItem.Type.values()[viewType];

        final int layoutRes;

        switch (type) {

            case HEADER:
                layoutRes = R.layout.adapter_header;
                break;

            case ITEM:
                layoutRes = R.layout.adapter_item;
                break;

            default:
                throw new IllegalStateException("Unknown type: " + type);
        }

        return createViewHolder(layoutRes, parent);
    }

    private ViewHolder createViewHolder(int layoutRes, ViewGroup group) {
        return new ViewHolder(mInflater.inflate(layoutRes, group, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final AdapterItem item = getItem(position);

        switch (item.getType()) {

            case HEADER:
                holder.titleText.setText(
                        getText(
                                R.string.adapter_header_pattern,
                                item.getOpType().name(),
                                item.getRounds()
                        )
                );
                break;

            case ITEM:
                holder.itemView.setBackgroundColor(item.getColor());
                holder.titleText.setText(item.getName());
                holder.valueText.setText(getText(item.getTime().getPatternResId(), item.getValue()));
                break;

        }
    }

    private String getText(int resId, Object... args) {
        return mResources.getString(resId, args);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType().ordinal();
    }

    public AdapterItem getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView valueText;

        public ViewHolder(View itemView) {
            super(itemView);

            titleText = (TextView) itemView.findViewById(R.id.text1);
            valueText = (TextView) itemView.findViewById(R.id.text2);
        }
    }
}
