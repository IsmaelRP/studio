package es.iessaladillo.pedrojoya.pr194.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import es.iessaladillo.pedrojoya.pr194.R;
import es.iessaladillo.pedrojoya.pr194.data.model.Student;

public class MainActivity extends AppCompatActivity {

    private ListView lstStudents;
    private SwipeRefreshLayout swlPanel;
    private TextView lblEmptyView;

    private MainActivityAdapter adapter;
    private MainActivityViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        initViews();
        viewModel.getStudents().observe(this, studentsRequest -> {
            if (studentsRequest != null) {
                if (studentsRequest.getError() != null) {
                    showErrorLoadingStudents();
                    // In order not to be displayed again on orientation change.
                    studentsRequest.removeError();
                } else {
                    showStudents(studentsRequest.getStudents());
                }
            }
        });
    }

    private void findViews() {
        lstStudents = findViewById(R.id.lstStudents);
        swlPanel = findViewById(R.id.swlPanel);
        lblEmptyView = findViewById(R.id.lblEmptyView);
    }

    private void initViews() {
        setupListView();
        setupPanel();
    }

    private void setupListView() {
        lstStudents.setEmptyView(lblEmptyView);
        // In order to work well with SwipeRefreshLayout.
        lstStudents.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                    int totalItemCount) {
                int topRowVerticalPosition = (lstStudents == null
                        || lstStudents.getChildCount() == 0) ? 0 : lstStudents.getChildAt(0)
                                                     .getTop();
                swlPanel.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });
        // ---
        adapter = new MainActivityAdapter(this, null);
        lstStudents.setAdapter(adapter);
    }

    private void setupPanel() {
        swlPanel.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swlPanel.setOnRefreshListener(() -> viewModel.forceLoadStudents());
    }

    private void showStudents(List<Student> students) {
        adapter.setData(students);
        swlPanel.post(() -> swlPanel.setRefreshing(false));
    }

    private void showErrorLoadingStudents() {
        Toast.makeText(this, getString(R.string.main_activity_error_loading_students),
                Toast.LENGTH_LONG).show();
        swlPanel.post(() -> swlPanel.setRefreshing(false));
    }

}
