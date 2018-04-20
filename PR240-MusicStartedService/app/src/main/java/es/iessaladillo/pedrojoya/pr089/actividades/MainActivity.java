package es.iessaladillo.pedrojoya.pr089.actividades;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.iessaladillo.pedrojoya.pr089.R;
import es.iessaladillo.pedrojoya.pr089.fragmentos.MainFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.Listener {

    private static final String TAG_MAIN_FRAGMENT = "MainFragment";

    private FloatingActionButton btnPlayStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVistas();
        if (getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT) == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.flContenido, MainFragment.newInstance(), TAG_MAIN_FRAGMENT)
                    .commit();
        }
    }

    private void initVistas() {
        setupToolbar();
        setupFAB();
    }

    private void setupToolbar() {
        Toolbar toolbar = ActivityCompat.requireViewById(this, R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    private void setupFAB() {
        btnPlayStop = ActivityCompat.requireViewById(this, R.id.btnPlayStop);
        if (btnPlayStop != null) {
            btnPlayStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playstop();
                }
            });
        }
    }

    private void playstop() {
        Fragment frg = getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
        if (frg instanceof MainFragment) {
            ((MainFragment) frg).playstop();
        }
    }

    @Override
    public void onReproduciendo() {
        if (btnPlayStop != null) {
            btnPlayStop.setImageResource(R.drawable.fab_stop);
        }
    }

    @Override
    public void onParado() {
        if (btnPlayStop != null) {
            btnPlayStop.setImageResource(R.drawable.fab_play);
        }
    }

}
