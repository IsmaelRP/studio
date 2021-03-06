package es.iessaladillo.pedrojoya.pr057.utils;

import android.util.SparseBooleanArray;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewUtils {

    private ListViewUtils() {
    }

    // M for Model
    public static <M> List<M> getSelectedItems(ListView listView, boolean uncheck) {
        ArrayList<M> result = new ArrayList<>();
        SparseBooleanArray selec = listView.getCheckedItemPositions();
        for (int i = 0; i < selec.size(); i++) {
            // If selected item.
            if (selec.valueAt(i)) {
                int position = selec.keyAt(i);
                // Uncheck item.
                if (uncheck) {
                    listView.setItemChecked(position, false);
                }
                //noinspection unchecked
                result.add((M) listView.getItemAtPosition(selec.keyAt(i)));
            }
        }
        return result;
    }

}
