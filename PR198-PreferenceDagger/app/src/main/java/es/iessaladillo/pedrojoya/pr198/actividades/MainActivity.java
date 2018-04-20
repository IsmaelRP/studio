package es.iessaladillo.pedrojoya.pr198.actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Set;

import javax.inject.Inject;

import es.iessaladillo.pedrojoya.pr198.App;
import es.iessaladillo.pedrojoya.pr198.R;

@SuppressWarnings({"CanBeFinal", "unused"})
public class MainActivity extends AppCompatActivity {

    private TextView lblPreferencias;

    // Los campos injectados no pueden ser private.
    @SuppressWarnings("WeakerAccess")
    @Inject
    SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Se realiza la inyección de dependencias.
        ((App) getApplication()).getAppComponent().inject(this);
        initVistas();
    }

    private void initVistas() {
        Toolbar toolbar = ActivityCompat.requireViewById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        overflowEnDispositivoConTeclaMenu();
        lblPreferencias = (TextView) this.findViewById(R.id.lblPreferencias);
    }

    @Override
    protected void onResume() {
        if (preferencias != null) {
            // Escribo las preferencias en el TextView.
            StringBuilder sb = new StringBuilder("");
            sb.append(getString(R.string.sincronizar)).append(": ").append(
                    preferencias.getBoolean("prefSincronizar", false)).append("\n");
            sb.append(getString(R.string.tipo_conexion)).append(": ").append(
                    preferencias.getString("prefTipoConexion",
                            getString(R.string.tipo_conexion_default))).append("\n");
            sb.append(getString(R.string.letras_grandes)).append(": ").append(
                    preferencias.getBoolean("prefLetrasGrandes", false)).append("\n");
            sb.append(getString(R.string.turnos)).append(":\n");
            Set<String> turnosSeleccionados = preferencias.getStringSet("prefTurnos", null);
            if (turnosSeleccionados != null) {
                String[] turnos = new String[turnosSeleccionados.size()];
                turnosSeleccionados.toArray(turnos);
                for (String turno : turnos) {
                    sb.append(turno).append("\n");
                }
            }
            sb.append(getString(R.string.lema)).append(": ").append(
                    preferencias.getString("prefLema", "")).append("\n");
            sb.append(getString(R.string.tono_notificacion)).append(": ");
            String pathTono = preferencias.getString("prefTonoNotificacion", "");
            Uri uriTono = Uri.parse(pathTono);
            Ringtone tono = RingtoneManager.getRingtone(this, uriTono);
            String nombreTono = tono.getTitle(this);
            sb.append(nombreTono).append("\n");
            sb.append(getString(R.string.red)).append(": ").append(
                    preferencias.getBoolean("prefRed", false)).append("\n");
            lblPreferencias.setText(sb.toString());
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnuPreferencias:
                // Lanzamos la actividad de preferencias.
                Intent i = new Intent(this,
                        es.iessaladillo.pedrojoya.pr198.actividades.PreferenciasActivity.class);
                this.startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Activa el ítem de overflow en dispositivos con botón físico de menú.
    private void overflowEnDispositivoConTeclaMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignorar
        }
    }
}
