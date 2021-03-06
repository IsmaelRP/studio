package es.iessaladillo.pedrojoya.pr082.data.remote.search;

import com.android.volley.RequestQueue;

import java.net.URLEncoder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import es.iessaladillo.pedrojoya.pr082.base.Resource;

public class SearchDataSourceImpl implements SearchDataSource {

    private final RequestQueue requestQueue;

    public SearchDataSourceImpl(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public LiveData<Resource<String>> search(String text, String tag) {
        MutableLiveData<Resource<String>> result = new MutableLiveData<>();
        result.postValue(Resource.loading());
        try {
            requestQueue.add(new SearchRequest(URLEncoder.encode(text, "UTF-8"), tag,
                response -> result.postValue(Resource.success(extractResultFromContent(response))),
                volleyError -> result.postValue(
                    Resource.error(new Exception(volleyError.getMessage())))));
        } catch (Exception e) {
            result.postValue(Resource.error(e));
        }
        return result;
    }

    @Override
    public void cancel(String tag) {
        requestQueue.cancelAll(tag);
    }

    private String extractResultFromContent(String contenido) {
        String resultado = "";
        int ini = contenido.indexOf("Aproximadamente");
        if (ini != -1) {
            // Se busca el siguiente espacio en blanco después de
            // Aproximadamente.
            int fin = contenido.indexOf(" ", ini + 16);
            // El resultado corresponde a lo que sigue a
            // Aproximadamente, hasta el siguiente espacio en blanco.
            resultado = contenido.substring(ini + 16, fin);
        }
        return resultado;
    }

}
