package es.iessaladillo.pedrojoya.pr246.ui.another;

import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import es.iessaladillo.pedrojoya.pr246.R;

public class SettingsActivity extends AppCompatActivity {

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
