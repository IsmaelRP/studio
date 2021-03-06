package es.iessaladillo.pedrojoya.pr216;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import es.iessaladillo.pedrojoya.pr216.utils.FragmentUtils;


public class MainActivity extends AppCompatActivity {

    private static final String TAG_MAIN_FRAGMENT = "TAG_MAIN_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT) == null) {
            FragmentUtils.replaceFragment(getSupportFragmentManager(), R.id.flContent,
                    MainFragment.newInstance(), TAG_MAIN_FRAGMENT);
        }
    }

}
