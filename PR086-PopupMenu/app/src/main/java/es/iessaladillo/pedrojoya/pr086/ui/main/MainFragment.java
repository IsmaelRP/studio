package es.iessaladillo.pedrojoya.pr086.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import es.iessaladillo.pedrojoya.pr086.R;
import es.iessaladillo.pedrojoya.pr086.base.EventObserver;
import es.iessaladillo.pedrojoya.pr086.data.RepositoryImpl;
import es.iessaladillo.pedrojoya.pr086.data.local.Database;
import es.iessaladillo.pedrojoya.pr086.data.local.model.Student;

@SuppressWarnings("WeakerAccess")
public class MainFragment extends Fragment {

    private MainFragmentAdapter listAdapter;
    private MainFragmentViewModel viewModel;
    private TextView lblEmptyView;

    static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent,
        @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, parent, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this,
            new MainFragmentViewModelFactory(new RepositoryImpl(Database.getInstance()))).get(
            MainFragmentViewModel.class);
        setupViews(requireView());
        observeViewModel();
    }

    private void setupViews(View view) {
        setupListView(view);
        setupFab(view);
    }

    private void setupListView(View view) {
        RecyclerView lstStudents = ViewCompat.requireViewById(view, R.id.lstStudents);
        lblEmptyView = ViewCompat.requireViewById(view, R.id.lblEmptyView);

        lblEmptyView.setOnClickListener(v -> viewModel.addStudent(Database.newFakeStudent()));
        listAdapter = new MainFragmentAdapter(viewModel);
        lstStudents.setHasFixedSize(true);
        lstStudents.setLayoutManager(new GridLayoutManager(requireContext(),
            getResources().getInteger(R.integer.main_lstStudents_columns)));
        lstStudents.setItemAnimator(new DefaultItemAnimator());
        lstStudents.setAdapter(listAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    viewModel.deleteStudent(listAdapter.getItem(viewHolder.getAdapterPosition()));
                }
            });
        itemTouchHelper.attachToRecyclerView(lstStudents);
    }

    private void setupFab(View view) {
        ViewCompat.requireViewById(view, R.id.fab).setOnClickListener(
            v -> viewModel.addStudent(Database.newFakeStudent()));

    }

    private void observeViewModel() {
        observeStudents();
        observeNavigation();
    }

    private void observeStudents() {
        viewModel.getStudents().observe(getViewLifecycleOwner(), students -> {
            listAdapter.submitList(students);
            lblEmptyView.setVisibility(students.size() > 0 ? View.INVISIBLE : View.VISIBLE);
        });
    }

    private void observeNavigation() {
        viewModel.getNavigateToStudentDetail().observe(getViewLifecycleOwner(),
            new EventObserver<>(this::showStudent));
        viewModel.getNavigateToStudentAssignments().observe(getViewLifecycleOwner(),
            new EventObserver<>(this::showAssignments));
        viewModel.getNavigateToStudentMarks().observe(getViewLifecycleOwner(),
            new EventObserver<>(this::showMarks));
        viewModel.getNavigateToCallStudent().observe(getViewLifecycleOwner(),
            new EventObserver<>(this::call));
        viewModel.getNavigateToSendMessageToStudent().observe(getViewLifecycleOwner(),
            new EventObserver<>(this::sendMessage));
    }

    private void showStudent(Student student) {
        Toast.makeText(requireContext(),
            getString(R.string.main_activity_click_on, student.getName()), Toast.LENGTH_SHORT)
            .show();
    }

    private void showAssignments(Student student) {
        Toast.makeText(requireContext(),
            getString(R.string.main_show_assignments, student.getName()), Toast.LENGTH_SHORT)
            .show();
    }

    private void showMarks(Student student) {
        Toast.makeText(requireContext(), getString(R.string.main_show_marks, student.getName()),
            Toast.LENGTH_SHORT).show();
    }

    private void call(Student student) {
        Toast.makeText(requireContext(),
            getString(R.string.main_activity_call_sb, student.getName()), Toast.LENGTH_SHORT)
            .show();
    }

    private void sendMessage(Student student) {
        Toast.makeText(requireContext(),
            getString(R.string.main_send_message_to, student.getName()), Toast.LENGTH_SHORT).show();
    }

}
