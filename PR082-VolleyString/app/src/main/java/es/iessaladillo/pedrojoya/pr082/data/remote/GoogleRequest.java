package es.iessaladillo.pedrojoya.pr082.data.remote;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GoogleRequest extends StringRequest {

    public GoogleRequest(String nombre, Listener<String> listener,
            ErrorListener errorListener) {
        super(Method.GET, "https://www.google.es/search?hl=es&q=\""
                + nombre + "\"", listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
        return params;
    }

}
