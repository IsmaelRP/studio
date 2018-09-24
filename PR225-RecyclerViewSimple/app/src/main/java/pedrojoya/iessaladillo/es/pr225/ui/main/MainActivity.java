package pedrojoya.iessaladillo.es.pr225.ui.main;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import pedrojoya.iessaladillo.es.pr225.R;
import pedrojoya.iessaladillo.es.pr225.data.RepositoryImpl;
import pedrojoya.iessaladillo.es.pr225.data.local.Database;
import pedrojoya.iessaladillo.es.pr225.utils.SnackbarUtils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView lstStudents;
    private MainActivityViewModel mViewModel;
    @SuppressWarnings("FieldCanBeLocal")
    private MainActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this,
                new MainActivityViewModelFactory(new RepositoryImpl(Database.getInstance()))).get(
                MainActivityViewModel.class);
        initViews();
    }

    private void initViews() {
        lstStudents = ActivityCompat.requireViewById(this, R.id.lstStudents);
        TextView lblEmpty = ActivityCompat.requireViewById(this, R.id.lblEmpty);
        lblEmpty.setVisibility(View.INVISIBLE);
        setupToolbar();
        setupRecyclerView();
        setupFab();
    }

    private void setupToolbar() {
        Toolbar toolbar = ActivityCompat.requireViewById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupFab() {
        FloatingActionButton fabAccion = ActivityCompat.requireViewById(this, R.id.fab);
        fabAccion.setOnClickListener(view -> addStudent());
    }

    private void setupRecyclerView() {
        mAdapter = new MainActivityAdapter(mViewModel.getStudents());
        lstStudents.setHasFixedSize(true);
        lstStudents.setAdapter(mAdapter);
        lstStudents.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        lstStudents.setItemAnimator(new DefaultItemAnimator());
    }

    private void addStudent() {
        SnackbarUtils.snackbar(lstStudents, getString(R.string.activity_main_fabClicked));
    }

}
