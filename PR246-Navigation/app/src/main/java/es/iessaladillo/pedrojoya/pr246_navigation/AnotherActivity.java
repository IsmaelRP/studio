package es.iessaladillo.pedrojoya.pr246_navigation;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AnotherActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_activity);

        setSupportActionBar((Toolbar) ActivityCompat.requireViewById(this, R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        navController.navigate(R.id.anotherFragment, getIntent().getExtras());
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Se le pasa la navegación hacia arriba al navegador.
        onBackPressed();
        return true;
    }

}