package es.iessaladillo.pedrojoya.pr006.ui.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import es.iessaladillo.pedrojoya.pr006.R;

public class MainActivity extends AppCompatActivity {

    private static final String STATE_EVENT_LIST = "STATE_EVENT_LIST";

    private TextView lblEvents;

    private String events = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lblEvents = ActivityCompat.requireViewById(this, R.id.lblEvents);
        showEvent(getString(R.string.main_activity_oncreate));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showEvent(getString(R.string.main_activity_ondestroy));
    }

    @Override
    protected void onPause() {
        super.onPause();
        showEvent(getString(R.string.main_activity_onpause));
    }

    @Override
    protected void onResume() {
        super.onResume();
        showEvent(getString(R.string.main_activity_onresume));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        showEvent(getString(R.string.main_activity_onsaveinstancestate));
        outState.putString(STATE_EVENT_LIST, events);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        events = savedInstanceState.getString(STATE_EVENT_LIST);
        showEvent(getString(R.string.main_activity_onrestoreinstancestate));
    }

    @Override
    protected void onStart() {
        super.onStart();
        showEvent(getString(R.string.main_activity_onstart));
    }

    @Override
    protected void onStop() {
        super.onStop();
        showEvent(getString(R.string.main_activity_onstop));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showEvent(getString(R.string.main_activity_onrestart));
    }

    private void showEvent(String event) {
        Log.d(getString(R.string.app_name), event);
        events = events.concat(event);
        lblEvents.setText(events);
    }

}
