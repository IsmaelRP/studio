package es.iessaladillo.pedrojoya.pr016.ui.main;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import es.iessaladillo.pedrojoya.pr016.data.Repository;


@SuppressWarnings("WeakerAccess")
public class MainActivityViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Repository repository;

    public MainActivityViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainActivityViewModel(repository);
    }
}
