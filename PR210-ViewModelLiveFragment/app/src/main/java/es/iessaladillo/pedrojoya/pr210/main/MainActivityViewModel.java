package es.iessaladillo.pedrojoya.pr210.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;

import java.util.List;

import es.iessaladillo.pedrojoya.pr210.data.Database;
import es.iessaladillo.pedrojoya.pr210.detail.DetailFragmentBaseViewModel;

@SuppressWarnings("WeakerAccess")
public class MainActivityViewModel extends DetailFragmentBaseViewModel {

    private static final String STATE_ITEM = "STATE_ITEM";

    private final Database database;

    private List<String> items;
    private final MutableLiveData<String> currentItem = new MutableLiveData<>();

    public MainActivityViewModel() {
        database = Database.getInstance();
    }


    public List<String> getItems() {
        if (items == null) {
            items = database.getItems();
        }
        return items;
    }

    @Override
    public LiveData<String> getCurrentItem() {
        return currentItem;
    }

    @Override
    public void setCurrentItem(String item) {
        currentItem.setValue(item);
    }

    public void onSaveInstanceState(Bundle outState)  {
        outState.putString(STATE_ITEM, currentItem.getValue());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (currentItem.getValue() == null) {
            currentItem.setValue(savedInstanceState.getString(STATE_ITEM));
        }
    }

}
