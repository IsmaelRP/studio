package pedrojoya.iessaladillo.es.pr229.ui.main;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import pedrojoya.iessaladillo.es.pr229.R;
import pedrojoya.iessaladillo.es.pr229.base.BaseListAdapter;
import pedrojoya.iessaladillo.es.pr229.base.BaseViewHolder;
import pedrojoya.iessaladillo.es.pr229.data.local.model.Student;
import pedrojoya.iessaladillo.es.pr229.utils.PicassoUtils;

public final class MainActivityAdapter extends BaseListAdapter<Student, MainActivityAdapter
.ViewHolder> {

    MainActivityAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName()) && TextUtils.equals(
                    oldItem.getAddress(), newItem.getAddress()) && TextUtils.equals(oldItem.getPhotoUrl(),
                    newItem.getPhotoUrl());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    class ViewHolder extends BaseViewHolder {

        private final TextView lblName;
        private final TextView lblAddress;
        private final CircleImageView imgAvatar;


        ViewHolder(@NonNull View itemView) {
            super(itemView, onItemClickListener, onItemLongClickListener);
            lblName = ViewCompat.requireViewById(itemView, R.id.lblName);
            lblAddress = ViewCompat.requireViewById(itemView, R.id.lblAddress);
            imgAvatar = ViewCompat.requireViewById(itemView, R.id.imgAvatar);
        }

        void bind(Student student) {
            if (student != null) {
                lblName.setText(student.getName());
                lblAddress.setText(student.getAddress());
                PicassoUtils.loadUrl(imgAvatar, student.getPhotoUrl(), R.drawable.ic_user);
            }
        }

    }

}
