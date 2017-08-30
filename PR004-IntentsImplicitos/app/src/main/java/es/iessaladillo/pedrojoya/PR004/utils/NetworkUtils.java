package es.iessaladillo.pedrojoya.PR004.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    private NetworkUtils() {
    }

    // Is Internet connection available?.
    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager gestorConectividad = (ConnectivityManager) context
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infoRed = gestorConectividad.getActiveNetworkInfo();
        return infoRed != null && infoRed.isConnected();
    }

}
