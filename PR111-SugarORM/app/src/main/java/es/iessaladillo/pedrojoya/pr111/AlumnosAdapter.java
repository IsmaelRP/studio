package es.iessaladillo.pedrojoya.pr111;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// Adaptador para la lista.
class AlumnosAdapter extends ArrayAdapter<Alumno> {

    private final ArrayList<Alumno> mAlumnos;
    private final LayoutInflater mInflador;

    // Constructor.
    public AlumnosAdapter(Context contexto, ArrayList<Alumno> alumnos) {
        super(contexto, R.layout.activity_main_item, alumnos);
        mAlumnos = alumnos;
        mInflador = LayoutInflater.from(contexto);
    }

    // Retorna la vista que se debe "dibujar" para un determinado elemento.
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            // Se obtiene la vista-fila inflando el layout.
            convertView = mInflador.inflate(R.layout.activity_main_item, parent, false);
            // Se crea el contenedor de vistas para la vista-fila.
            holder = new ViewHolder(convertView);
            // Se almacena el contenedor en la vista.
            convertView.setTag(holder);
        } else {
            // Se obtiene el contenedor de vistas desde la vista reciclada.
            holder = (ViewHolder) convertView.getTag();
        }
        // Se escriben los datos en las vistas del contenedor de vistas.
        onBindViewHolder(holder, position);
        // Se retorna la vista que representa el elemento.
        return convertView;
    }

    // Cuando se deben escribir los datos en la vista del elemento.
    private void onBindViewHolder(ViewHolder holder, int position) {
        // Se obtiene el alumno que debe mostrar el elemento.
        Alumno alumno = mAlumnos.get(position);
        // Se escriben los datos del alumno en las vistas.
        holder.lblNombre.setText(alumno.getNombre());
        holder.lblCurso.setText(alumno.getCurso());
        holder.lblDireccion.setText(alumno.getDireccion());
        Glide.with(getContext()).load(alumno.getAvatar()).into(holder.imgAvatar);
    }

    // Contenedor de vistas para la vista-fila.
    static class ViewHolder {

        // El contenedor de vistas para un elemento de la lista debe contener...
        private final CircleImageView imgAvatar;
        private final TextView lblNombre;
        private final TextView lblCurso;
        private final TextView lblDireccion;

        // El constructor recibe la vista-fila.
        public ViewHolder(View itemView) {
            // Se obtienen las vistas de la vista-fila.
            imgAvatar = (CircleImageView) ViewCompat.requireViewById(itemView, R.id.imgAvatar);
            lblNombre = (TextView) ViewCompat.requireViewById(itemView, R.id.lblNombre);
            lblCurso = (TextView) ViewCompat.requireViewById(itemView, R.id.lblCurso);
            lblDireccion = (TextView) ViewCompat.requireViewById(itemView, R.id.lblDireccion);
        }

    }
}
