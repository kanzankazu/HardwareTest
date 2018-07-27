package com.kanzankazu.hardwaretest.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.database.room.AppDatabase;
import com.kanzankazu.hardwaretest.database.room.table.Hardware;
import com.kanzankazu.hardwaretest.service.ScreenReceiver;

public class ButtonActivity extends LocalBaseActivity {

    TextView tvSensorReadingfvbi, tvPassedfvbi, tvcountDownfvbi;

    private CountDownTimer timer;

    private boolean isPower;
    private boolean isVolumeDown;
    private boolean isVolumeUp;
    private AppDatabase appDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        initComponent();
        initContent();
        initListener();
    }

    private void initComponent() {
        tvcountDownfvbi = (TextView) findViewById(R.id.tvCountDown);
        tvSensorReadingfvbi = (TextView) findViewById(R.id.tvSensorReading);
        tvPassedfvbi = (TextView) findViewById(R.id.tvPassed);
    }

    private void initContent() {
        appDatabase = new AppDatabase(ButtonActivity.this);
        //startCounter();
        tvcountDownfvbi.setVisibility(View.INVISIBLE);
        tvSensorReadingfvbi.setText("Tekan Tombol Volume +");
        isVolumeUp = true;

        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        final BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
        Log.d("Lihat initContent ButtonActivity", String.valueOf(filter));
        Log.d("Lihat initContent ButtonActivity", String.valueOf(mReceiver));
        Log.d("Lihat initContent ButtonActivity", String.valueOf(mReceiver.getResultData()));
        Log.d("Lihat initContent ButtonActivity", String.valueOf(mReceiver.getResultCode()));
    }

    private void initListener() {

    }

    private void startCounter() {
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvcountDownfvbi.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                onBackPressed();
            }
        };
        timer.start();
    }

    private void insertDevice(String nameDevice, int statusDevice, int descDevice) {
        String statusDevices = null;
        String descDevices = null;
        if (statusDevice == 0) {
            statusDevices = "tidak ada";
            descDevices = "";
        } else if (statusDevice == 1) {
            statusDevices = "ada";
            if (descDevice == 0) {
                descDevices = "rusak";
            } else if (descDevice == 1) {
                descDevices = "bagus";
            } else if (descDevice == 2) {
                descDevices = "lain-lain";
            }
        }
        appDatabase.insertHardware(new Hardware(nameDevice, statusDevices, descDevices));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            //TODO HERE
            if (isPower) {
                isPower = false;
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
            }
            //event.startTracking(); // Needed to track long presses
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (isVolumeDown) {
                tvSensorReadingfvbi.setText("Tekan Tombol Power");
                insertDevice("tombol vol -", 1, 1);
                isVolumeDown = false;
                isPower = true;
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (isVolumeUp) {
                tvSensorReadingfvbi.setText("Tekan Tombol Volume -");
                insertDevice("tombol vol +", 1, 1);
                isVolumeUp = false;
                isVolumeDown = true;
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPower) {
            if (ScreenReceiver.getScreenStatus()) {
                insertDevice("tombol power", 1, 1);
                isPower = false;
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
                //Toast.makeText(getApplicationContext(), "DONE BUTTON", Toast.LENGTH_SHORT).show();// Set your own toast  message
            }
            /*Intent intent = getIntent();
            boolean screenON = intent.getBooleanExtra("message", false);
            if (screenON) {
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
                //stopService(new Intent(getApplicationContext(), LockService.class));
            } else {
                onBackPressed();
            }*/
        }
    }

    @Override
    public void onBackPressed() {
        /*setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);*/
    }
}
