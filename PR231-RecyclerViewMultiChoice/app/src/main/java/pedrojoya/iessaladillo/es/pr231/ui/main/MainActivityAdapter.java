package pedrojoya.iessaladillo.es.pr231.ui.main;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.selection.ItemDetailsLookup;
import de.hdodenhof.circleimageview.CircleImageView;
import pedrojoya.iessaladillo.es.pr231.base.BaseAdapter;
import pedrojoya.iessaladillo.es.pr231.base.BaseViewHolder;
import pedrojoya.iessaladillo.es.pr231.base.PositionalDetailsLookup;
import pedrojoya.iessaladillo.es.pr231.base.PositionalItemDetails;
import pedrojoya.iessaladillo.es.pr231.data.local.model.Student;
import pedrojoya.iessaladillo.es.pr231.utils.PicassoUtils;
import pedrojoya.iessaladillo.es.pr331.R;

public class MainActivityAdapter extends BaseAdapter<Student, MainActivityAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<Student> diffUtilItemCallback = new DiffUtil.ItemCallback<Student>() {
        @Override
        public boolean areItemsTheSame(Student oldItem, Student newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
            return false;
        }
    };

    MainActivityAdapter() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Es muy importante que la posición sea convertida a long, o de lo contrario
        // isSelected() devolverá false.
        //noinspection unchecked
        viewHolder.bind(getItem(position),
                selectionTracker != null && selectionTracker.isSelected((long) position));
    }

    class ViewHolder extends BaseViewHolder implements PositionalDetailsLookup.DetailsProvider {

        private final TextView lblName;
        private final TextView lblAddress;
        private final CircleImageView imgAvatar;

        ViewHolder(View itemView) {
            super(itemView, onItemClickListener);
            lblName = ViewCompat.requireViewById(itemView, R.id.lblName);
            lblAddress = ViewCompat.requireViewById(itemView, R.id.lblAddress);
            imgAvatar = ViewCompat.requireViewById(itemView, R.id.imgAvatar);
        }

        void bind(Student student, boolean selected) {
            itemView.setActivated(selected);
            lblName.setText(student.getName());
            lblAddress.setText(student.getAddress());
            PicassoUtils.loadUrl(imgAvatar, student.getPhotoUrl(), R.drawable.ic_person_black_24dp);
        }

        @Override
        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new PositionalItemDetails(getAdapterPosition());
        }

    }

}
