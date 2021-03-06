package es.iessaladillo.pedrojoya.pr102.ui.main;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import es.iessaladillo.pedrojoya.pr102.R;
import es.iessaladillo.pedrojoya.pr102.base.TimePickerDialogFragment;
import es.iessaladillo.pedrojoya.pr102.reminder.ReminderScheduler;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private TextView txtMessage;
    private TextView txtWhen;
    private Calendar when;
    private MainActivityViewModel viewModel;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm",
        Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this,
            new MainActivityViewModelFactory(ReminderScheduler.getInstance(getApplication()))).get(
            MainActivityViewModel.class);
        setupViews();
    }

    private void setupViews() {
        txtMessage = ActivityCompat.requireViewById(this, R.id.txtMessage);
        txtWhen = ActivityCompat.requireViewById(this, R.id.txtWhen);
        Button btnCreateAlarm = ActivityCompat.requireViewById(this, R.id.btnCreateReminder);
        Button btnCreateClockAlarm = ActivityCompat.requireViewById(this, R.id.btnCreateClockAlarm);
        Button btnSendToClockApp = ActivityCompat.requireViewById(this, R.id.btnSendToClockApp);

        txtWhen.setInputType(InputType.TYPE_NULL);
        txtWhen.setKeyListener(null);
        txtWhen.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showTimePickerDialog();
            }
        });
        txtWhen.setOnClickListener(v -> showTimePickerDialog());
        btnCreateAlarm.setOnClickListener(v -> createAlarm());
        btnCreateClockAlarm.setOnClickListener(v -> createClockAlarm());
        btnSendToClockApp.setOnClickListener(v -> sendToClockApp());
    }

    private void showTimePickerDialog() {
        if (when == null) {
            TimePickerDialogFragment.newInstance().show(getSupportFragmentManager(),
                TimePickerDialogFragment.class.getSimpleName());
        } else {
            TimePickerDialogFragment.newInstance(when.get(Calendar.HOUR_OF_DAY),
                when.get(Calendar.MINUTE), true).show(getSupportFragmentManager(),
                TimePickerDialogFragment.class.getSimpleName());
        }
    }

    private void createAlarm() {
        String message = txtMessage.getText().toString();
        if (!TextUtils.isEmpty(message) && when != null) {
            viewModel.createReminder(message, when);
        }
    }

    private void createClockAlarm() {
        String message = txtMessage.getText().toString();
        if (!TextUtils.isEmpty(message) && when != null) {
            viewModel.createClockAlarm(message, when);
        }
    }

    private void sendToClockApp() {
        String message = txtMessage.getText().toString();
        if (!TextUtils.isEmpty(message) && when != null) {
            viewModel.sendToClockApp(message, when);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (when == null) {
            when = Calendar.getInstance();
        }
        when.set(Calendar.HOUR_OF_DAY, hourOfDay);
        when.set(Calendar.MINUTE, minute);
        when.set(Calendar.SECOND, 0);
        txtWhen.setText(simpleDateFormat.format(when.getTime()));
    }

}
