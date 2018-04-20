package es.iessaladillo.pedrojoya.pr028.fragmentos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import es.iessaladillo.pedrojoya.pr028.R;
import es.iessaladillo.pedrojoya.pr028.adaptadores.AlumnosAdapter;
import es.iessaladillo.pedrojoya.pr028.bd.DbContract;
import es.iessaladillo.pedrojoya.pr028.modelos.Alumno;
import es.iessaladillo.pedrojoya.pr028.proveedores.DbAsyncQueryHandler;
import es.iessaladillo.pedrojoya.pr028.proveedores.DbContentProvider;

public class ListaAlumnosFragment extends Fragment implements
        LoaderCallbacks<Cursor>, DbAsyncQueryHandler.Callbacks {

    private static final int TOKEN_DELETE = 0;
    private DbAsyncQueryHandler mAlumnoAsyncQueryHandler;

    // Interfaz de comunicación con la actividad.
    public interface OnListaAlumnosFragmentListener {
        void onAgregarAlumno();

        void onEditarAlumno(long id);

        void onConfirmarEliminarAlumnos();
    }

    // Variables miembro.
    private ListView lstAlumnos;
    private OnListaAlumnosFragmentListener listener;
    private ActionMode modoContextual;
    private AlumnosAdapter adaptador;
    private LoaderManager gestor;

    // Retorna la vista que debe mostrar el fragmento.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Se infla el layout y se retorna la vista que debe mostrar el fragmento.
        return inflater.inflate(R.layout.fragment_lista_alumnos, container, false);
        // Se obtienen e inicializan las vistas.
    }

    // Obtiene e inicializa las vistas.
    private void initVistas(View v) {
        // Se crea el objeto para realizar las operaciones sobre el content provider en segundo
        // plano.
        mAlumnoAsyncQueryHandler = new DbAsyncQueryHandler(requireActivity()
                .getContentResolver(), this);

        lstAlumnos = (ListView) v.findViewById(R.id.lstAlumnos);
        RelativeLayout rlListaVacia = (RelativeLayout) v.findViewById(R.id.rlListaVacia);
        // Si la lista está vacía se muestra un icono y un texto para que al
        // pulsarlo se agregue un alumno.
        rlListaVacia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Se informa al listener (actividad).
                listener.onAgregarAlumno();
            }
        });
        lstAlumnos.setEmptyView(rlListaVacia);
        // Al hacer click sobre un elemento de la lista.
        lstAlumnos.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Se obtiene el alumno sobre el que se ha pulsado.
                Cursor c = (Cursor) lstAlumnos.getItemAtPosition(position);
                Alumno alumno = Alumno.fromCursor(c);
                // Se da la orden de editar.
                listener.onEditarAlumno(alumno.getId());
            }

        });
        // Se establece que se puedan seleccionar varios elementos de la lista.
        lstAlumnos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lstAlumnos.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode arg0) {
            }

            // Al crear el modo de acción contextual.
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Se infla la especificación del menú contextual en el menú.
                mode.getMenuInflater().inflate(R.menu.fragment_lista_alumnos,
                        menu);
                // Se retorna que ya se ha gestionado el evento.
                return true;
            }

            // Al pulsar sobre un ítem del modo de acción contextual.
            @Override
            public boolean onActionItemClicked(ActionMode modo, MenuItem item) {
                // Dependiendo del elemento pulsado.
                switch (item.getItemId()) {
                    case R.id.mnuAlumnoEliminar:
                        // Si hay elementos seleccionados se pide confirmación.
                        if (lstAlumnos.getCheckedItemPositions().size() > 0) {
                            // Se almacena el modo contextual para poder cerrarlo
                            // una vez eliminados.
                            modoContextual = modo;
                            // Se pide confirmación.
                            listener.onConfirmarEliminarAlumnos();
                        }
                        break;
                    default:
                }
                // Se retorna que se ha procesado el evento.
                return true;
            }

            // Al seleccionar un elemento de la lista.
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Se actualiza el título de la action bar contextual.
                mode.setTitle(lstAlumnos.getCheckedItemCount() + "");
            }
        });
        (v.findViewById(R.id.btnAgregar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se informa al listener (actividad) de que se quiere agregar
                // un alumno.
                listener.onAgregarAlumno();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Se obtienen e inicializan las vistas.
        initVistas(getView());
        // Se carga la lista de alumnos.
        cargarAlumnos();
        super.onActivityCreated(savedInstanceState);
    }

    private void cargarAlumnos() {
        // Se inicializa el cargador.
        gestor.initLoader(0, null, this);
        // Se establece el adaptador para la lista, que inicialmente manejará un cursor nulo.
        String[] from = {DbContract.Alumno.NOMBRE, DbContract.Alumno.CURSO,
                         DbContract.Alumno.TELEFONO, DbContract.Alumno.DIRECCION};
        int[] to = {R.id.lblNombre, R.id.lblCurso, R.id.lblTelefono, R.id.lblDireccion};
        adaptador = new AlumnosAdapter(requireActivity(),
                R.layout.fragment_lista_alumnos_item, null, from, to, 0);
        lstAlumnos.setAdapter(adaptador);
    }

    // Cuando el fragmento es cargado en la actividad.
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            // Establece la actividad como objeto listener.
            listener = (OnListaAlumnosFragmentListener) activity;
        } catch (ClassCastException e) {
            // La actividad no implementa la interfaz.
            throw new ClassCastException(activity.toString()
                    + " debe implementar OnElementoSeleccionadoListener");
        }
        // Obtenemos el gestor de cargadores.
        gestor = requireActivity().getSupportLoaderManager();
    }

    // Elimina de la base de datos los alumnos seleccionados, actualiza el
    // adaptador y cierra el modo de acción conextual.
    public void eliminarAlumnos() {
        // Se obtiene el array con las posiciones seleccionadas.
        SparseBooleanArray seleccionados = lstAlumnos.getCheckedItemPositions();
        // Por cada selección.
        for (int i = 0; i < seleccionados.size(); i++) {
            // Se obtiene la posición del elemento en el adaptador.
            int position = seleccionados.keyAt(i);
            // Si ha sido seleccionado
            if (seleccionados.valueAt(i)) {
                // Se obtiene el alumo.
                Cursor cursor = (Cursor) lstAlumnos.getItemAtPosition(position);
                Alumno alu = Alumno.fromCursor(cursor);
                // Se borra de la base de datos a través del content provider.
                Uri uri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI_ALUMNOS, String.valueOf(alu
                        .getId()));
                mAlumnoAsyncQueryHandler.startDelete(TOKEN_DELETE, null, uri, null, null);
            }
        }
        // Se finaliza el modo contextual.
        modoContextual.finish();
    }

    // Cuando se crea el cargador. Retorna el cargador del cursor.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Se retorna el cargador del cursor. Se le pasa el contexto, la uri en
        // la que consultar los datos y las columnas a obtener.
        return new CursorLoader(requireActivity(),
                DbContentProvider.CONTENT_URI_ALUMNOS, DbContract.Alumno.TODOS,
                null, null, null);
    }

    // Cuando terminan de cargarse los datos en el cargador.
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Se cambia el cursor del adaptador por el que tiene datos.
        adaptador.changeCursor(data);
    }

    // Cuando se resetea el cargador.
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Se vacía de datos el adaptador.
        adaptador.changeCursor(null);
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
    }

    @Override
    public void onInsertComplete(int token, Object cookie, Uri uri) {
    }

    @Override
    public void onUpdateComplete(int token, Object cookie, int result) {
    }

    @Override
    public void onDeleteComplete(int token, Object cookie, int result) {
        if (result > 0) {
            Toast.makeText(getContext(), "Eliminación realizada correctamente", Toast.LENGTH_SHORT).show();
        }
    }

}
