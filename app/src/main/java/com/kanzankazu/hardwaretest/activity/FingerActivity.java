package com.kanzankazu.hardwaretest.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerActivity extends LocalBaseActivity implements FingerPrintAuthCallback {
    TextView tvSensorReadingfvbi, tvPassedfvbi;

    SensorManager mySensorManager;
    Sensor mySensor;
    private CountDownTimer timer;
    private TextView tvcountDownfvbi;
    private KeyguardManager keyguardManager;
    private FingerprintManager fingerprintManager;

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

        // Keyguard Manager
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        // Fingerprint Manager
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

        tvSensorReadingfvbi.setText("Letakan jari anda di sensor sidik jari");
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

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        Log.i("Lihat onNoFingerPrintHardwareFound FingerActivity", "Your device does not have finger print scanner. Please type 1234 to authenticate.");
        onBackPressed();
    }

    @Override
    public void onNoFingerPrintRegistered() {
        Log.i("Lihat onNoFingerPrintRegistered FingerActivity", "There are no finger prints registered on this device. Please register your finger from settings.");
        onBackPressed();
    }

    @Override
    public void onBelowMarshmallow() {
        Log.i("Lihat onBelowMarshmallow FingerActivity", "You are running older version of android that does not support finger print authentication. Please type 1234 to authenticate.");
        onBackPressed();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
        setResult(Activity.RESULT_OK);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                Log.i("Lihat onAuthFailed FingerActivity", "Cannot recognize your finger print. Please try again.");
                onBackPressed();
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                Log.i("Lihat onAuthFailed FingerActivity", "Cannot initialize finger print authentication. Please type 1234 to authenticate.");
                onBackPressed();
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                Log.i("Lihat onAuthFailed FingerActivity", errorMessage);
                onBackPressed();
                break;
        }
    }
}
