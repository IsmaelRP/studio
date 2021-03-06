package pedrojoya.iessaladillo.es.pr231.old;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import pedrojoya.iessaladillo.es.pr231.data.local.model.Student;
import pedrojoya.iessaladillo.es.pr231.old.multichoice.MultiChoiceModeListAdapter;
import pedrojoya.iessaladillo.es.pr231.old.multichoice.MultiChoiceModeViewHolder;
import pedrojoya.iessaladillo.es.pr331.R;

public class MainActivityAdapter2 extends MultiChoiceModeListAdapter<Student, MainActivityAdapter2.ViewHolder> {

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

    @SuppressWarnings("unused")
    public MainActivityAdapter2() {
        super(diffUtilItemCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false), this);
    }

    @SuppressWarnings("WeakerAccess")
    static class ViewHolder extends MultiChoiceModeViewHolder<Student> {

        private final TextView lblName;
        private final TextView lblAddress;
        private final CircleImageView imgAvatar;

        public ViewHolder(View itemView, MainActivityAdapter2 adapter) {
            super(itemView, adapter);
            lblName = ViewCompat.requireViewById(itemView, R.id.lblName);
            lblAddress = ViewCompat.requireViewById(itemView, R.id.lblAddress);
            imgAvatar = ViewCompat.requireViewById(itemView, R.id.imgAvatar);
        }

        @Override
        protected void setChecked(boolean isChecked) {
            itemView.setActivated(isChecked);
        }

        @Override
        public void bind(Student student) {
            lblName.setText(student.getName());
            lblAddress.setText(student.getAddress());
            Picasso.with(imgAvatar.getContext()).load(student.getPhotoUrl()).placeholder(
                    R.drawable.ic_person_black_24dp).error(R.drawable.ic_person_black_24dp).into(
                    imgAvatar);
        }

    }

}
