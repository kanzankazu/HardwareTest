package com.kanzankazu.hardwaretest.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;

public class AccelActivity extends LocalBaseActivity {
    TextView tvSensorReadingfvbi, tvPassedfvbi, tvcountDownfvbi;

    SensorManager mySensorManager;
    Sensor mySensor;
    private CountDownTimer timer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private float vibrateThreshold;
    private Vibrator v;


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
        mySensor = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        vibrateThreshold = mySensor.getMaximumRange() / 3;

        if (mySensor != null) {
            mySensorManager.registerListener(sensorEventListener, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            onBackPressed();
        }

        tvSensorReadingfvbi.setText("Guncangkan Hp Anda");
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
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // get the change of the x,y,z values of the accelerometer
                float lastX = 0;
                float lastY = 0;
                float lastZ = 0;

                float deltaX = Math.abs(lastX - event.values[0]);
                float deltaY = Math.abs(lastY - event.values[1]);
                float deltaZ = Math.abs(lastZ - event.values[2]);

                // if the change is below 2, it is just plain noise
                if (deltaX < 2)
                    deltaX = 0;
                if (deltaY < 2)
                    deltaY = 0;
                if (deltaZ < 2)
                    deltaZ = 0;

                // set the last know values of x,y,z
                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];

                if ((deltaX > vibrateThreshold) || (deltaY > vibrateThreshold) || (deltaZ > vibrateThreshold)) {
                    setResult(Activity.RESULT_OK);
                    finish();
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
