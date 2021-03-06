package es.iessaladillo.pedrojoya.pr028.proveedores;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import es.iessaladillo.pedrojoya.pr028.BuildConfig;
import es.iessaladillo.pedrojoya.pr028.bd.DbContract;
import es.iessaladillo.pedrojoya.pr028.bd.DbHelper;

@SuppressWarnings("ConstantConditions")
public class DbContentProvider extends ContentProvider {

    // Constantes generales.
    // Autoridad. Debe ser similar al valor de android:authority de la etiqueta
    // <provider> en el manifiesto.
    private static final String AUTHORITY = String.format("%s.provider", BuildConfig.APPLICATION_ID);
    private static final Uri CONTENT_URI_BASE = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build(); // Uri base de acceso al proveedor.

    // Constantes para la entidad Alumnos.
    private static final String BASE_PATH_ALUMNOS = "alumnos"; // Segmento path.
    public static final Uri CONTENT_URI_ALUMNOS = Uri.withAppendedPath(
            CONTENT_URI_BASE, BASE_PATH_ALUMNOS); // Uri pública de acceso a alumnos.
    private static final String MIME_TYPE_ALUMNOS = String.format("%s/vnd.%s.%s",
            ContentResolver.CURSOR_DIR_BASE_TYPE,
            AUTHORITY,
            BASE_PATH_ALUMNOS); // Tipo MIME alumnos.
    private static final String MIME_ITEM_TYPE_ALUMNOS = String.format("%s/vnd.%s.%s",
            ContentResolver.CURSOR_ITEM_BASE_TYPE,
            AUTHORITY,
            BASE_PATH_ALUMNOS); // Tipo MIME alumno.
    // Constantes para tipos de Uris (deben tener todos un valor diferente).
    private static final int URI_TYPE_ALUMNOS_LIST = 10; // Tipo para alumnos.
    private static final int URI_TYPE_ALUMNOS_ID = 20; // Tipo para alumno.

    // Se crea el validador de Uris, al que se le añaden todas los tipos de uris
    // considerados válidos.
    private static final UriMatcher validadorURIs = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        validadorURIs.addURI(AUTHORITY, BASE_PATH_ALUMNOS,
                URI_TYPE_ALUMNOS_LIST);
        validadorURIs.addURI(AUTHORITY, BASE_PATH_ALUMNOS + "/#",
                URI_TYPE_ALUMNOS_ID);
    }

    // Retorna el tipo de uri recibida.
    @Override
    public String getType(@NonNull Uri uri) {
        // Dependiendo del tipo de uri solicitada.
        int tipoURI = validadorURIs.match(uri);
        switch (tipoURI) {
            case URI_TYPE_ALUMNOS_LIST:
                return MIME_TYPE_ALUMNOS;
            case URI_TYPE_ALUMNOS_ID:
                return MIME_ITEM_TYPE_ALUMNOS;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
    }

    private DbHelper mHelper;

    // Retorna si ha ido bien.
    @Override
    public boolean onCreate() {
        // Se crea el helper para el acceso a la bd.
        mHelper = DbHelper.getInstance(getContext());
        return true;
    }

    // Retorna el cursor con el resultado de la consulta. Recibe los parámetros
    // indicados en el método query del ContentResolver.
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Se abre la base de datos.
        SQLiteDatabase bd = mHelper.getReadableDatabase();
        // Se crea un constructor de consultas.
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        // Dependiendo de la uri solicitada.
        int tipoURI = validadorURIs.match(uri);
        switch (tipoURI) {
            case URI_TYPE_ALUMNOS_LIST:
                // Se compueba si el llamador ha solicitado una columna que no
                // existe.
                checkColumns(DbContract.Alumno.TODOS, projection);
                // Se establece la tabla para la consulta.
                builder.setTables(DbContract.Alumno.TABLA);
                break;
            case URI_TYPE_ALUMNOS_ID:
                // Se compueba si el llamador ha solicitado una columna que no
                // existe.
                checkColumns(DbContract.Alumno.TODOS, projection);
                // Se establece la tabla para la consulta.
                builder.setTables(DbContract.Alumno.TABLA);
                // Se agrega al where la selección de ese alumno.
                builder.appendWhere(DbContract.Alumno._ID + " = "
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        // Se realiza la consulta.
        Cursor cursor = builder.query(bd, projection, selection, selectionArgs,
                null, null, sortOrder);
        // Se notifica a los escuchadores del content provider.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    // Retorna el número de registros eliminados. Recibe los parámetros recibido
    // por el método delete del ContentResolver.
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int filasBorradas;
        // Se inicializa la selección.
        String where = selection;
        // Se obtiene la base de datos.
        SQLiteDatabase bd = mHelper.getWritableDatabase();
        // Dependiendo de la uri solicitada.
        int tipoURI = validadorURIs.match(uri);
        switch (tipoURI) {
            case URI_TYPE_ALUMNOS_LIST:
                // Se realiza el borrado.
                filasBorradas = bd.delete(DbContract.Alumno.TABLA, where, selectionArgs);
                break;
            case URI_TYPE_ALUMNOS_ID:
                // Se agrega al where la selección de ese alumno.
                where = DbContract.Alumno._ID + "=" + uri.getLastPathSegment()
                        + (TextUtils.isEmpty(selection) ? "" : " and " + where);
                // Se realiza el borrado.
                filasBorradas = bd.delete(DbContract.Alumno.TABLA, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        // Se notifica de los cambios a todos los listener.
        if (filasBorradas > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return filasBorradas;
    }

    // Retorna la uri del nuevo registro. Recibe los parámetros recibido por el
    // método insert del ContentResolver.
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long id;
        // Se obtiene la base de datos.
        SQLiteDatabase bd = mHelper.getWritableDatabase();
        // Dependiendo de la uri solicitada.
        int tipoURI = validadorURIs.match(uri);
        switch (tipoURI) {
            case URI_TYPE_ALUMNOS_LIST:
                id = bd.insert(DbContract.Alumno.TABLA, null, values);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        // Se notifica a los escuchadores del content provider.
        getContext().getContentResolver().notifyChange(uri, null);
        // Se retorna la URI del registro insertado.
        return ContentUris.withAppendedId(uri, id);
    }

    // Retorna el número de registros actualizados. Recibe los parámetros
    // recibidos por el método update del ContentResolver.
    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int filasActualizadas;
        // Se inicializa la parte del where.
        String where = selection;
        // Se obtiene la base de datos.
        SQLiteDatabase bd = mHelper.getWritableDatabase();
        // Depndiendo del tipo de uri solicitada.
        int tipoURI = validadorURIs.match(uri);
        switch (tipoURI) {
            case URI_TYPE_ALUMNOS_LIST:
                filasActualizadas = bd.update(DbContract.Alumno.TABLA, values, where,
                        selectionArgs);
                break;
            case URI_TYPE_ALUMNOS_ID:
                // Se agrega al where la selección de ese alumno.
                where = DbContract.Alumno._ID + "=" + uri.getLastPathSegment()
                        + (TextUtils.isEmpty(selection) ? "" : " and " + where);
                filasActualizadas = bd.update(DbContract.Alumno.TABLA, values, where,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        // Se notifica a los listeners.
        if (filasActualizadas > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Se retorna el número de registros actualizados.
        return filasActualizadas;
    }

    // Comprueba si todas las columnas están entre las disponibles.
    @SuppressWarnings("SameParameterValue")
    private void checkColumns(String[] disponibles, String[] columnas) {
        if (columnas != null) {
            HashSet<String> columnasSolicitadas = new HashSet<>(
                    Arrays.asList(columnas));
            HashSet<String> columnasDisponibles = new HashSet<>(
                    Arrays.asList(disponibles));
            // Si hay alguna solicitada no disponible se lanza excepción.
            if (!columnasDisponibles.containsAll(columnasSolicitadas)) {
                throw new IllegalArgumentException(
                        "Se ha solicitado un campo desconocido");
            }
        }
    }
}
