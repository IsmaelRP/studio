package es.iessaladillo.pedrojoya.pr128;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class SecundaryActivity extends AppCompatActivity {

    private static final String EXTRA_ANIM_RETORNO = "extra_anim_retorno";
    private static final String EXTRA_ANIM_REENTRADA = "extra_anim_reentrada";

    private int mResIdAnimRetorno;
    private int mResIdAnimReentrada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundary);
        // Se obtienen del intent los extras recibidos.
        mResIdAnimRetorno = getIntent().getIntExtra(EXTRA_ANIM_RETORNO, 0);
        mResIdAnimReentrada = getIntent().getIntExtra(EXTRA_ANIM_REENTRADA, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case android.R.id.home:
                super.onOptionsItemSelected(item);
                finish();
                overridePendingTransition(mResIdAnimReentrada, mResIdAnimRetorno);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Se aplica la animación (entrada, salida).
        overridePendingTransition(mResIdAnimReentrada, mResIdAnimRetorno);
    }

    // Método estático para lanzar esta actividad.
    public static void start(Context context, int resIdAnimRetorno, int resIdAnimReentrada) {
        Intent intent = new Intent(context, SecundaryActivity.class);
        intent.putExtra(EXTRA_ANIM_RETORNO, resIdAnimRetorno);
        intent.putExtra(EXTRA_ANIM_REENTRADA, resIdAnimReentrada);
        context.startActivity(intent);
    }

}
