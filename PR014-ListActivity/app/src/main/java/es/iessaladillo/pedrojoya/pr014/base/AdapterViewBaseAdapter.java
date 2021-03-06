package es.iessaladillo.pedrojoya.pr014.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

// M for Model, VH for ViewHolder
public abstract class AdapterViewBaseAdapter<M, VH> extends BaseAdapter {

    @NonNull
    private List<M> data;
    @LayoutRes
    private final int layoutResId;

    @SuppressWarnings("SameParameterValue")
    protected AdapterViewBaseAdapter(@NonNull List<M> data, @LayoutRes int layoutResId) {
        this.data = data;
        this.layoutResId = layoutResId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        VH viewHolder;
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent,
                    false);
            viewHolder = onCreateViewHolder(itemView);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (VH) itemView.getTag();
        }
        onBindViewHolder(viewHolder, position);
        return itemView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public M getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void submitList(@NonNull List<M> newList) {
        data = newList;
        notifyDataSetChanged();
    }

    protected abstract VH onCreateViewHolder(@NonNull View itemView);

    protected abstract void onBindViewHolder(@NonNull VH holder, int position);

}
