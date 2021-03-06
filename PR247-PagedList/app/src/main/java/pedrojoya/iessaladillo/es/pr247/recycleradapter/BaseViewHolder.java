package pedrojoya.iessaladillo.es.pr247.recycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

// M is Model.
public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {

    protected BaseViewHolder(View itemView, BasePagedListAdapter<M, ? extends BaseViewHolder<M>> adapter) {
        super(itemView);
        if (adapter.getOnItemClickListener() != null) {
            itemView.setOnClickListener(v -> {
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    adapter.getOnItemClickListener().onItemClick(v,
                            adapter.getItem(getAdapterPosition()),
                            getAdapterPosition(),
                            getItemId());
                }
            });
        }
        if (adapter.getOnItemLongClickListener() != null) {
            itemView.setOnLongClickListener(v -> {
                //noinspection SimplifiableIfStatement
                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    return adapter.getOnItemLongClickListener().onItemLongClick(v,
                            adapter.getItem(getAdapterPosition()), getAdapterPosition(),
                            getItemId());
                }
                return false;
            });
        }
    }

    public abstract void bind(M item);

}
