package es.iessaladillo.pedrojoya.pr137;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// Adaptador para la lista de alumnos.
public class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.ViewHolder> {

    // Interfaz que debe implementar el listener para cuando se haga click
    // sobre un elemento.
    public interface OnItemClickListener {
        void onItemClick(View view, Alumno alumno, int position);
    }

    // Interfaz que debe implementar el listener para cuando se haga click
    // largo sobre un elemento.
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, Alumno alumno, int position);
    }

    // Interfaz que debe implementar el listener para cuando la lista pase a
    // o deje de estar vacía.
    public interface OnEmptyStateChangedListener {
        void onEmptyStateChanged(boolean isEmpty);
    }

    private final ArrayList<Alumno> mDatos;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemClickListener mOnItemClickListener;
    private OnEmptyStateChangedListener mOnEmptyStateChangedListener;
    private final SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private boolean mIsEmpty = true;


    // Constructor.
    public AlumnosAdapter(ArrayList<Alumno> datos) {
        mDatos = datos;
    }

    // Cuando se debe crear una nueva vista para el elemento.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Se infla la especificación XML para obtener la vista-fila.
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_main_item, parent, false);
        // Se crea el contenedor de vistas para la fila.
        final ViewHolder viewHolder = new ViewHolder(itemView);

        // Se establecen los listener de la vista correspondiente al ítem
        // y de las subvistas.

        // Cuando se hace click sobre el elemento.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    // Se informa al listener.
                    mOnItemClickListener.onItemClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                }
            }
        });
        // Cuando se hace click largo sobre el elemento.
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    // Se informa al listener.
                    mOnItemLongClickListener.onItemLongClick(v,
                            mDatos.get(viewHolder.getAdapterPosition()),
                            viewHolder.getAdapterPosition());
                    return true;
                } else {
                    return false;
                }
            }
        });
        // Se retorna el contenedor.
        return viewHolder;
    }

    // Cuando se deben escribir los datos en las subvistas de la
    // vista correspondiente al ítem.
    @Override
    public void onBindViewHolder(AlumnosAdapter.ViewHolder holder, int position) {
        // Se obtiene el alumno correspondiente.
        Alumno alumno = mDatos.get(position);
        // Se escriben los mDatos en la vista.
        holder.lblNombre.setText(alumno.getNombre());
        holder.lblDireccion.setText(alumno.getDireccion());
        Picasso.with(holder.imgAvatar.getContext())
                .load(alumno.getUrlFoto())
                .into(holder.imgAvatar);
        // Se establece el estado de selección del elemento.
        holder.itemView.setActivated(mSelectedItems.get(position, false));
    }

    // Retorna el número de ítems gestionados.
    @Override
    public int getItemCount() {
        return mDatos.size();
    }

    // Retorna un objeto de la colección.
    public Alumno getItem(int position) {
        return mDatos.get(position);
    }


    // Elimina un elemento de la lista.
    public void removeItem(int position) {
        mDatos.remove(position);
        notifyItemRemoved(position);
        // Se comprueba si pasa a estar vacía.
        checkEmptyStateChanged();
    }

    // Elimina los elementos seleccionados.
    public void removeSelectedItems() {
        // Se eliminan en orden inverso para que no haya problemas. Al
        // eliminar se cambia el
        // estado de selección del elemento.
        List<Integer> seleccionados = getSelectedItemsPositions();
        Collections.sort(seleccionados, Collections.reverseOrder());
        for (int i = 0; i < seleccionados.size(); i++) {
            int pos = seleccionados.get(i);
            toggleSelection(pos);
            removeItem(pos);
        }
        // Se comprueba si pasa a estar vacía.
        checkEmptyStateChanged();
    }

    // Añade un elemento a la lista.
    public void addItem(Alumno alumno, int position) {
        // Se añade el elemento.
        mDatos.add(position, alumno);
        // Se notifica que se ha insertado un elemento en la última posición.
        notifyItemInserted(position);
        // Si comprueba si deja de estar vacía.
        checkEmptyStateChanged();
    }

    // Intercambia dos elementos de la lista.
    void swapItems(int from, int to) {
        // Se realiza el intercambio.
        Collections.swap(mDatos, from, to);
        // Se notifica el movimiento.
        notifyItemChanged(from);
        notifyItemChanged(to);
    }

    // Comprueba si ha pasa a estar vacía o deja de estar vacía.
    private void checkEmptyStateChanged() {
        // Deja de estar vacía
        if (mIsEmpty && mDatos.size() > 0) {
            mIsEmpty = false;
            if (mOnEmptyStateChangedListener != null) {
                mOnEmptyStateChangedListener.onEmptyStateChanged(false);
            }
        }
        else if (!mIsEmpty && mDatos.size()==0) {
            mIsEmpty = true;
            if (mOnEmptyStateChangedListener != null) {
                mOnEmptyStateChangedListener.onEmptyStateChanged(true);
            }
        }
    }

    // Establece el listener a informar cuando se hace click sobre un
    // elemento de la lista.
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    // Establece el listener a informar cuando se hace click largo sobre un
    // elemento de la lista.
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    // Establece el listener a informar cuando la lista pasa a o deja de
    // estar vacía.
    public void setOnEmptyStateChangedListener(
            OnEmptyStateChangedListener listener) {
        mOnEmptyStateChangedListener = listener;
        checkEmptyStateChanged();
    }

    // Cambia el estado de selección de un elemento.
    private void toggleSelection(int position) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
        } else {
            mSelectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    // Selecciona un elemento.
    public void setSelected(int position) {
        mSelectedItems.put(position, true);
        notifyItemChanged(position);

    }

    // Quita la selección de un elemento.
    public void clearSelection(int position) {
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
        }
        notifyItemChanged(position);
    }

    // Quita la selección de todos los elementos seleccionados.
    public void clearSelections() {
        if (mSelectedItems.size() > 0) {
            mSelectedItems.clear();
            notifyDataSetChanged();
        }
    }

    // Retorna el número de elementos seleccionados.
    public int getSelectedItemCount() {
        return mSelectedItems.size();
    }

    // Retorna un array con las posiciones de los elementos seleccionados.
    private List<Integer> getSelectedItemsPositions() {
        List<Integer> items = new ArrayList<>(mSelectedItems.size());
        for (int i = 0; i < mSelectedItems.size(); i++) {
            items.add(mSelectedItems.keyAt(i));
        }
        return items;
    }

    // Intercambia los elementos indicados.
    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < mDatos.size()) {
            Alumno item = mDatos.remove(min);
            mDatos.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    // Contenedor de vistas para la vista-fila.
    static class ViewHolder extends RecyclerView.ViewHolder {
        // El contenedor de vistas para un elemento de la lista debe contener...
        private final TextView lblNombre;
        private final TextView lblDireccion;


        private final CircleImageView imgAvatar;

        // El constructor recibe la vista-fila.
        public ViewHolder(View itemView) {
            super(itemView);
            // Se obtienen las vistas de la vista-fila.
            lblNombre = (TextView) ViewCompat.requireViewById(itemView, R.id.lblNombre);
            lblDireccion = (TextView) ViewCompat.requireViewById(itemView, R.id.lblDireccion);
            imgAvatar = (CircleImageView) ViewCompat.requireViewById(itemView, R.id.imgAvatar);
        }

    }

}
