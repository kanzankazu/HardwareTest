package com.kanzankazu.hardwaretest.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;

public class ProxActivity extends LocalBaseActivity {
    TextView tvSensorReadingfvbi, tvPassedfvbi;

    SensorManager mySensorManager;
    Sensor mySensor;
    private CountDownTimer timer;
    private TextView tvcountDownfvbi;

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
        startCounter();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (mySensor != null) {
            mySensorManager.registerListener(sensorEventListener, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            onBackPressed();
        }

        tvSensorReadingfvbi.setText("Buka Tutup hp anda berkali kali");
        timer.start();
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
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        int a = 0, b = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (String.valueOf(event.values[0]).equalsIgnoreCase("0.0")) {
                    tvSensorReadingfvbi.setText("Proximity sensor covered");
                    a++;
                } else {
                    tvSensorReadingfvbi.setText("Proximity sensor uncovered");
                    b++;
                }
                if (a > 2 && b > 2) {
                    setResult(Activity.RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }
}