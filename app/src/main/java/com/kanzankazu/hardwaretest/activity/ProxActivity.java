package com.kanzankazu.hardwaretest.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;

public class ProxActivity extends AppCompatActivity {
    private static final String TAG = "ProxActivity";
    TextView tvProximityReadingfvbi, tvPassedfvbi;

    SensorManager mySensorManager;
    Sensor myProximitySensor;
    private CountDownTimer timer;
    private TextView tvcountDownfvbi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        initComponent();
        initContent();
        initListener();


    }

    private void initComponent() {
        tvProximityReadingfvbi = (TextView) findViewById(R.id.tvProximityReading);
        tvcountDownfvbi = (TextView) findViewById(R.id.tvCountDown);
        tvPassedfvbi = (TextView) findViewById(R.id.tvPassed);

    }

    private void initContent() {
        startCounter();

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myProximitySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (myProximitySensor != null) {
            mySensorManager.registerListener(proximitySensorEventListener,
                    myProximitySensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Intent intent = new Intent();
            setResult(Activity.RESULT_CANCELED, intent);
            finish();
            //overridePendingTransition(R.anim., R.anim.);
        }
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
                finish();
            }
        };
    }

    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        int a = 0, b = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (String.valueOf(event.values[0]).equals("0.0")) {
                    tvProximityReadingfvbi.setText("Proximity sensor covered");
                    a++;
                } else {
                    tvProximityReadingfvbi.setText("Proximity sensor uncovered");
                    b++;
                }
                if (a > 2 && b > 2) {

                } else {

                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        finish();
    }
}