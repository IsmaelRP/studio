package es.iessaladillo.pedrojoya.pr059.deprecated;

import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

// M for Model, VH for ViewHolder, K for selection tracker Key.
@SuppressWarnings("ALL")
abstract public class BaseListAdapter<M, VH extends RecyclerView.ViewHolder> extends ListAdapter<M, VH>
        implements Filterable {

    private List<M> original;
    private FilterPredicate<M> filterPredicate;

    protected BaseListAdapter(DiffUtil.ItemCallback<M> diffUtilItemCallback) {
        super(diffUtilItemCallback);
    }

    @Override
    public void submitList(List<M> list) {
        original = list;
        super.submitList(list);
    }

    private void submitFilteredList(List<M> list) {
        super.submitList(list);
    }

    @Override
    public M getItem(int position) {
        return super.getItem(position);
    }

    protected interface FilterPredicate<T> {
        boolean test(T item, CharSequence constraint);
    }

    protected void setFilterPredicate(FilterPredicate<M> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && filterPredicate != null) {
                    List<M> filtered = new ArrayList<>();
                    for (M element: original) {
                        if (filterPredicate.test(element, constraint)) {
                            filtered.add(element);
                        }
                    }
                    filterResults.values = filtered;
                    filterResults.count = filtered.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null) {
                    //noinspection unchecked
                    submitFilteredList((List<M>) results.values);
                }
            }
        };
    }

}