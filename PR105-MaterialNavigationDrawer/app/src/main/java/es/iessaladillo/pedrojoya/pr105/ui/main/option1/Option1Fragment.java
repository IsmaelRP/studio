package es.iessaladillo.pedrojoya.pr105.ui.main.option1;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import es.iessaladillo.pedrojoya.pr105.R;
import es.iessaladillo.pedrojoya.pr105.base.OnToolbarAvailableListener;
import es.iessaladillo.pedrojoya.pr105.ui.main.MainActivityViewModel;
import es.iessaladillo.pedrojoya.pr105.ui.main.MainActivityViewModelFactory;


public class Option1Fragment extends Fragment {

    private OnToolbarAvailableListener onToolbarAvailableListener;
    private View fab;

    public static Option1Fragment newInstance() {
        return new Option1Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_option1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews(requireView());
        MainActivityViewModel activityViewModel = ViewModelProviders.of(requireActivity(),
            new MainActivityViewModelFactory()).get(MainActivityViewModel.class);
        // In order to update the checked menuItem when coming from backstack.
        activityViewModel.setCurrentOption(R.id.mnuOption1);
    }

    private void setupViews(View view) {
        setupToolbar(view);
        setupFab(view);
    }

    private void setupFab(View view) {
        fab = ViewCompat.requireViewById(view, R.id.fab);

        fab.setOnClickListener(v -> showMessage());
    }

    private void showMessage() {
        Snackbar.make(fab, R.string.option1_fragment_fab_clicked, Snackbar.LENGTH_SHORT).show();
    }

    private void setupToolbar(View view) {
        Toolbar toolbar = ViewCompat.requireViewById(view, R.id.toolbar);

        toolbar.setTitle(getString(R.string.activity_main_option1));
        onToolbarAvailableListener.onToolbarAvailable(toolbar);
    }

    @Override
    public void onAttach(@NonNull Context activity) {
        super.onAttach(activity);
        try {
            onToolbarAvailableListener = (OnToolbarAvailableListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                activity.toString() + " must implement OnToolbarAvailableListener interface");
        }
    }

}
