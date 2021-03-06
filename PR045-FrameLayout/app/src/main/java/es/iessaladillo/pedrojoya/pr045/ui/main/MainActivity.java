package es.iessaladillo.pedrojoya.pr045.ui.main;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import es.iessaladillo.pedrojoya.pr045.R;

public class MainActivity extends AppCompatActivity {

    private ImageView imgDetail;
    private TextView lblDetail;

    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setupViews();
    }

    private void setupViews() {
        imgDetail = ActivityCompat.requireViewById(this, R.id.imgDetail);
        lblDetail = ActivityCompat.requireViewById(this, R.id.lblDetail);
        FrameLayout flRoot = ActivityCompat.requireViewById(this, R.id.flRoot);
        // Enable transition when childen size change.
        flRoot.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        imgDetail.setOnClickListener(v -> toggleDetailVisibility());
        setupPanelState(viewModel.isDetailOpen());
    }

    private void setupPanelState(boolean isVisible) {
        if (isVisible) {
            lblDetail.setVisibility(View.VISIBLE);
            imgDetail.setImageResource(R.drawable.expand_anim);
        } else {
            lblDetail.setVisibility(View.GONE);
            imgDetail.setImageResource(R.drawable.collapse_anim);
        }
    }

    private void toggleDetailVisibility() {
        viewModel.toggleDetailOpen();
        setupPanelState(viewModel.isDetailOpen());
    }

}
