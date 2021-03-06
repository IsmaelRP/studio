package es.iessaladillo.pedrojoya.pr140.ui.main;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import es.iessaladillo.pedrojoya.pr140.base.RequestState;
import es.iessaladillo.pedrojoya.pr140.data.model.City;
import es.iessaladillo.pedrojoya.pr140.data.remote.Api;
import es.iessaladillo.pedrojoya.pr140.data.remote.CountingLiveData;

@SuppressLint("StaticFieldLeak")
class MainFragmentViewModel extends ViewModel {

    private final CountingLiveData countingLiveData;
    private final List<City> cities = new ArrayList<>(
                Arrays.asList(new City("Algeciras", "04"), new City("Los Barrios", "08"),
                        new City("Castellar", "13"), new City("Jimena", "21"),
                        new City("La Línea", "22"), new City("San Roque", "33"),
                        new City("Tarifa", "35")));
    private int selectedCity = -1;

    MainFragmentViewModel(Api api) {
        countingLiveData = new CountingLiveData(api);
    }

    LiveData<RequestState> getCounting() {
        return countingLiveData;
    }

    void loadEscrutinio(String codigoPoblacion) {
        countingLiveData.loadData(codigoPoblacion);
    }

    List<City> getCities() {
        return cities;
    }

    int getSelectedCity() {
        return selectedCity;
    }

    void setSelectedCity(int selectedCity) {
        this.selectedCity = selectedCity;
    }

    @Override
    protected void onCleared() {
        countingLiveData.cancel();
        super.onCleared();
    }

}
