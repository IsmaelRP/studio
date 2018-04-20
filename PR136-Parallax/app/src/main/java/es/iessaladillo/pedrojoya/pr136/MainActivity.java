package es.iessaladillo.pedrojoya.pr136;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private ImageView imgFoto;
    private ScrollView svScroll;
    private Toolbar toolbar;
    private TextView lblTitulo;
    private View vOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = ActivityCompat.requireViewById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initVistas();
    }

    // Obtiene e inicializa las vistas.
    private void initVistas() {
        imgFoto = ActivityCompat.requireViewById(this, R.id.imgFoto);
        svScroll = ActivityCompat.requireViewById(this, R.id.svScroll);
        lblTitulo = ActivityCompat.requireViewById(this, R.id.lblTitulo);
        vOverlay = ActivityCompat.requireViewById(this, R.id.vOverlay);
        // Cuando se hace scroll se realiza el efecto parallax.
        parallax(svScroll, imgFoto);
        svScroll.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver
                        .OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        parallax(svScroll, imgFoto);
                    }
                });
    }

    // Realiza el efecto parallax sobre la imagen a partir del scroll realizado.
    private void parallax(ScrollView scrollView, ImageView image) {

        // Datos b�sicos.
        int scrollY = scrollView.getScrollY();
        int imageHeight = getResources().getDimensionPixelSize(R.dimen.image_height);
        // En el onCreate la action bar a�n no tiene altura.
        int actionBarHeight = getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material);
        if (getSupportActionBar() != null) {
            actionBarHeight = Math.max(getSupportActionBar().getHeight(),
                    getResources().getDimensionPixelSize(R.dimen
                            .abc_action_bar_default_height_material));
        }
        // En el onCreate lblTitulo a�n no tiene altura.
        int lblTituloHeight = Math.max(lblTitulo.getHeight(), actionBarHeight);
        int baseColor = getResources().getColor(R.color.colorPrimary);

        // La imagen se traslada hacia arriba la mitad de la cantidad de scroll
        // que hayamos realizado. El overlay se traslada con la imagen.
        image.setTranslationY(-scrollY / 2);
        vOverlay.setTranslationY(-scrollY / 2);
        Log.d(getString(R.string.app_name), "Traslaci�n imagen: " +
                -scrollY);

        /* Si la imagen no tuviera t�tulo, simplemente har�amos que la toolbar
           se volviera m�s opaca conforme se hiciera scroll (m�ximo 1).
        float alpha = Math.min(1, (float) scrollY / imageHeight);
        int alphaColor = getColorWithAlpha(alpha, baseColor);
        toolbar.setBackgroundColor(alphaColor);
        */

        // Escalado del t�tulo. Cuanto m�s scroll m�s peque�o ser� la
        // escala. Como m�nimo 1. Como m�ximo 1,3.
        float flexibleRange = imageHeight - actionBarHeight;
        float scale = 1 + ((flexibleRange - scrollY) / flexibleRange);
        scale = Math.max(1, scale);
        scale = Math.min(1.3f, scale);
        lblTitulo.setPivotX(0);
        lblTitulo.setPivotY(0);
        lblTitulo.setScaleX(scale);
        lblTitulo.setScaleY(scale);
        Log.d(getString(R.string.app_name), "Escala: " + scale);

        // El overlay se vuelve m�s opaca conforme se hace scroll.
        float maxOverlayDistance = imageHeight - actionBarHeight +
                (lblTituloHeight * scale);
        float overlayAlpha = Math.min(maxOverlayDistance, scrollY)
                / maxOverlayDistance;
        vOverlay.setAlpha(overlayAlpha);
        Log.d(getString(R.string.app_name), "Alpha overlay: " + overlayAlpha);

        // Translaci�n hacia abajo del t�tulo. Caunto m�s scroll menos
        // traslaci�n. Como m�ximo la altura de la imagen menos
        // la altura del TextView (en la escala actual).
        // Como minimo 0 (situ�ndose debajo de la toolbar).
        int maxTitleTranslationY = (int) (imageHeight - lblTituloHeight *
                scale);
        int titleTranslationY = maxTitleTranslationY - scrollY;
        titleTranslationY = Math.max(0, titleTranslationY);
        lblTitulo.setTranslationY(titleTranslationY);

        // El t�tulo de la toolbar s�lo debe mostrarse si el t�tulo se ha
        // trasladado por completo.
        if (titleTranslationY == 0) {
            getSupportActionBar().setTitle(getString(R.string.app_name));
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            getSupportActionBar().setTitle("");
            toolbar.setBackgroundColor(getResources().getColor(android.R
                    .color.transparent));
        }
        Log.d(getString(R.string.app_name), "Traslaci�n t�tulo: " + titleTranslationY);
    }

    // Retorna el color base apl�candole una determinada transaparencia alpha.
    private int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }

}
