package es.iessaladillo.pedrojoya.pr030;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity implements PickOrCaptureDialogFragment
        .Listener {

    private static final int RC_CAPTURAR_FOTO = 0;
    private static final int RC_SELECCIONAR_FOTO = 1;

    private static final String PREF_PATH_FOTO = "prefPathFoto";

    private static final int OPTION_PICK = 0;

    private String sPathFotoOriginal; // path en el que se guarda la foto capturada.
    private String sNombreArchivo; // Nombre para guardar en privado la foto escalada.

    private ImageView imgFoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgFoto = findViewById(R.id.imgFoto);
        // Se lee de las preferencias el path del archivo con la foto escalada y privada
        // (si fuera una base de datos, leeríamos del registro correspondiente).
        SharedPreferences preferencias = getSharedPreferences(getString(R.string.app_name),
                MODE_PRIVATE);
        String pathFoto = preferencias.getString(PREF_PATH_FOTO, "");
        if (!TextUtils.isEmpty(pathFoto)) {
            // Se muestra en el ImageView.
            imgFoto.setImageURI(Uri.fromFile(new File(pathFoto)));
        }
    }

    // Guarda en preferencias el path de archivo mostrado en el ImageView.
    private void guardarEnPreferencias(String path) {
        // Se almacena en las preferencias el path del archivo con la foto escalada y privada
        SharedPreferences preferencias = getSharedPreferences(getString(R.string.app_name),
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString(PREF_PATH_FOTO, path);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuFoto) {
            PickOrCaptureDialogFragment frgDialogo = new PickOrCaptureDialogFragment();
            frgDialogo.show(this.getSupportFragmentManager(), "PickOrCaptureDialogFragment");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    // Envía un intent implícito para seleccionar una foto de la galería.
    // Recibe el nombre que debe tomar el archivo con la foto escalada y guardada en privado.
    @SuppressWarnings("SameParameterValue")
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void seleccionarFoto(String nombreArchivoPrivado) {
        // Se guarda el nombre para uso posterior.
        sNombreArchivo = nombreArchivoPrivado;
        // Se seleccionará un imagen de la galería.
        // (el segundo parámetro es el Data, que corresponde a la Uri de la galería.)
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        // Se deshabilita temporalmente el sensor de orientación.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        startActivityForResult(i, RC_SELECCIONAR_FOTO);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForReadExternalStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this).setMessage(R.string.permission_readexternalstorage_rationale)
                .setPositiveButton(R.string.permitir, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.rechazar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForReadExternalStorage() {
        Toast.makeText(this, R.string.no_se_pudo, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForReadExternalStorage() {
        Toast.makeText(this, R.string.permission_readexternalstorage_rationale, Toast.LENGTH_SHORT)
                .show();
    }

    // Envía un intent implícito para la captura de una foto.
    // Recibe el nombre que debe tomar el archivo con la foto escalada y guardada en privado.
    @SuppressWarnings("SameParameterValue")
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void capturarFoto(String nombreArchivoPrivado) {
        // Se guarda el nombre para uso posterior.
        sNombreArchivo = nombreArchivoPrivado;
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Si hay alguna actividad que sepa realizar la acción.
        if (i.resolveActivity(getPackageManager()) != null) {
            // Se crea el archivo para la foto en el directorio público (true).
            // Se obtiene la fecha y hora actual.
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
                    new Date());
            String nombre = "IMG_" + timestamp + "_.jpg";
            // OJO, USANDO ALMACENAMIENTO EXTERNO PÚBLICO NO FUNCIONA. LA ACTIVIDAD DE CAPTURA
            // NO ES CAPAZ DE ESCRIBIR EN EL ARCHIVO.
            File fotoFile = crearArchivoFoto(nombre, true, false);
            if (fotoFile != null) {
                // Se guarda el path del archivo para cuando se haya hecho la captura.
                sPathFotoOriginal = fotoFile.getAbsolutePath();
                // Se obtiene la Uri correspondiente al archivo creado a través del FileProvider,
                // cuyo autorithies debe coincidir con lo especificado para el FileProvider en el
                // manifiesto (necesario para API >= 25).
                Uri fotoURI = FileProvider.getUriForFile(this.getApplicationContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        fotoFile);
                // Se añade como extra del intent la uri donde debe guardarse.
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoURI);
                //i.setData(fotoURI);
                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Se deshabilita temporalmente el sensor de orientación.
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                startActivityForResult(i, RC_CAPTURAR_FOTO);
            }
        }
    }

    // Crea un archivo de foto con el nombre indicado en almacenamiento externo si es posible, o si
    // no en almacenamiento interno, y lo retorna. Retorna null si falló.
    // Si publico es true -> en la carpeta pública de imágenes.
    // Si publico es false, en la carpeta propia de imágenes.
    private File crearArchivoFoto(String nombre, boolean external, boolean publico) {
        // Se obtiene el directorio en el que almacenarlo.
        File directorio;
        if (external && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (publico) {
                // En el directorio público para imágenes del almacenamiento externo.
                directorio = Environment.getExternalStorageDirectory();
            } else {
                directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            }
        } else {
            // En almacenamiento interno.
            directorio = getFilesDir();
        }
        // Su no existe el directorio, se crea.
        if (directorio != null && !directorio.exists()) {
            if (!directorio.mkdirs()) {
                Log.d(getString(R.string.app_name), "error al crear el directorio");
                return null;
            }
        }
        // Se crea un archivo con ese nombre y la extensión jpg en ese
        // directorio.
        File archivo = null;
        if (directorio != null) {
            archivo = new File(directorio.getPath(), nombre);
            Log.d(getString(R.string.app_name), archivo.getAbsolutePath());
        }
        // Se retorna el archivo creado.
        return archivo;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RC_CAPTURAR_FOTO:
                    // Se agrega la foto a la Galería
                    agregarFotoAGaleria(sPathFotoOriginal);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    cargarImagenEscalada(sPathFotoOriginal);
                    break;
                case RC_SELECCIONAR_FOTO:
                    // Se obtiene el path real a partir de la uri retornada por la galería.
                    Uri uriGaleria = intent.getData();
                    sPathFotoOriginal = getRealPath(uriGaleria);
                    // Se escala la foto, se almacena en archivo propio y se muestra en ImageView.
                    if (!TextUtils.isEmpty(sPathFotoOriginal)) {
                        cargarImagenEscalada(sPathFotoOriginal);
                    }
                    break;
            }
        }
        // Se vuelve a habilitar el sensor de orientación.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    // Obtiene el path real de una imagen a partir de la URI de Galería obtenido con ACTION_PICK.
    private String getRealPath(Uri uriGaleria) {
        String path = "";
        // Se consulta en el content provider de la galería el path real del archivo de la foto.
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(uriGaleria, filePath, null, null, null);
        if (c != null && c.moveToFirst()) {
            int columnIndex = c.getColumnIndex(filePath[0]);
            path = c.getString(columnIndex);
            c.close();
        }
        return path;
    }

    // Agrega a la Galería la foto indicada.
    private void agregarFotoAGaleria(String pathFoto) {
        // Se crea un intent implícito con la acción de
        // escaneo de un fichero multimedia.
        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // Se obtiene la uri del archivo a partir de su path.
        File archivo = new File(pathFoto);
        Uri uri = Uri.fromFile(archivo);
        // Se establece la uri con datos del intent.
        i.setData(uri);
        // Se envía un broadcast con el intent.
        this.sendBroadcast(i);
    }

    // Escala y muestra la imagen en el visor.
    private void cargarImagenEscalada(String pathFoto) {
        // Se utiliza una tarea asíncrona, para escalar, guardar en archivo propio y mostrar
        // la foto en el ImageView.
        MostrarFotoAsyncTask tarea = new MostrarFotoAsyncTask();
        tarea.execute(pathFoto);
    }

    // Cuando se selecciona una opción del diálogo PickOrCaptureDialogFragment.
    @Override
    public void onItemClick(DialogFragment dialog, int which) {
        if (which == OPTION_PICK) {
            MainActivityPermissionsDispatcher.seleccionarFotoWithPermissionCheck(this, "mifoto.jgp");
            //seleccionarFoto(); // Si BD, por ejemplo ID_alumno.jpg.
        } else {
            MainActivityPermissionsDispatcher.capturarFotoWithPermissionCheck(this, "mifoto.jgp");
            //capturarFoto("mifoto.jpg"); // Si BD, por ejemplo ID_alumno.jpg.
        }
    }

    // Tarea asíncrona que obtiene una foto a partir de su path y la muestra en
    // un visor.
    private class MostrarFotoAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // Se escala la foto, cuyo path corresponde al primer parámetro,
            // retornado el Bitmap correspondiente.
            return escalarFoto(params[0], getResources().getDimensionPixelSize(R.dimen.ancho_visor),
                    getResources().getDimensionPixelSize(R.dimen.alto_visor));
        }

        // Una vez finalizado el hilo de trabajo. Se ejecuta en el hilo
        // principal. Recibe el Bitmap de la foto escalada (o null si error).
        @Override
        protected void onPostExecute(Bitmap bitmapFoto) {
            if (bitmapFoto != null) {
                // Se guarda la copia propia de la imagen.
                File archivo = crearArchivoFoto(sNombreArchivo, true, false);
                if (archivo != null) {
                    if (guardarBitmapEnArchivo(bitmapFoto, archivo)) {
                        // Se almacena el path de la foto a mostrar en el ImageView.
                        guardarEnPreferencias(archivo.getAbsolutePath());
                        // Se muestra la foto en el ImageView.
                        imgFoto.setImageBitmap(bitmapFoto);
                    }
                }
            }
        }


        // Escala la foto indicada, para ser mostarda en un visor determinado.
        // Retorna el bitmap correspondiente a la imagen escalada o null si
        // se ha producido un error.
        private Bitmap escalarFoto(String pathFoto, int anchoVisor, int altoVisor) {
            try {
                // Se obtiene el tamaño de la imagen.
                BitmapFactory.Options opciones = new BitmapFactory.Options();
                opciones.inJustDecodeBounds = true; // Solo para cálculo.
                BitmapFactory.decodeFile(pathFoto, opciones);
                int anchoFoto = opciones.outWidth;
                int altoFoto = opciones.outHeight;
                // Se obtiene el factor de escalado para la imagen.
                int factorEscalado = Math.min(anchoFoto / anchoVisor, altoFoto / altoVisor);
                // Se escala la imagen con dicho factor de escalado.
                opciones.inJustDecodeBounds = false; // Se escalará.
                opciones.inSampleSize = factorEscalado;
                return BitmapFactory.decodeFile(pathFoto, opciones);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // Guarda el bitamp de la foto en un archivo. Retorna si ha ido bien.
        private boolean guardarBitmapEnArchivo(Bitmap bitmapFoto, File archivo) {
            try {
                FileOutputStream flujoSalida = new FileOutputStream(archivo);
                bitmapFoto.compress(Bitmap.CompressFormat.JPEG, 100, flujoSalida);
                flujoSalida.flush();
                flujoSalida.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
