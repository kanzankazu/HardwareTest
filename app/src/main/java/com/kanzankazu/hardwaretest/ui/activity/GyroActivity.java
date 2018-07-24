package com.kanzankazu.hardwaretest.ui.activity;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GyroActivity extends AppCompatActivity implements SensorEventListener {
//    private TextView tv;
    private SensorManager sManager;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gyro);
 //       tv = (TextView) findViewById(R.idHardware.txtGyroReport);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop()
    {
        finish();
        sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1){ }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        //Here
/*        tv.setText(Html.fromHtml(
                "X: " + String.format("%.2f", (event.values[2])) + "<br>" +
                        "Y: " + String.format("%.2f", (event.values[1])) + "<br>" +
                        "Z: " + String.format("%.2f", (event.values[0]))));*/
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}