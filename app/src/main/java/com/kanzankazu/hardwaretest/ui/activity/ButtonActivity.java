package com.kanzankazu.hardwaretest.ui.activity;

import android.annotation.SuppressLint;
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
import com.kanzankazu.hardwaretest.service.ScreenReceiver;

public class ButtonActivity extends LocalBaseActivity {

    TextView tvSensorReadingfvbi, tvPassedfvbi, tvcountDownfvbi;

    private CountDownTimer timer;

    private boolean isPower;
    private boolean isVolumeDown;
    private boolean isVolumeUp;

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

    @SuppressLint("LongLogTag")
    private void initContent() {
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            //TODO HERE
            if (isPower) {
                isPower = false;
                Toast.makeText(getApplicationContext(), "Finish", Toast.LENGTH_SHORT).show();
            }
            //event.startTracking(); // Needed to track long presses
        }
        else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (isVolumeDown) {
                tvSensorReadingfvbi.setText("Tekan Tombol Power");
                isVolumeDown = false;
                isPower = true;
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (isVolumeUp) {
                tvSensorReadingfvbi.setText("Tekan Tombol Volume -");
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
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }
}
