package com.kanzankazu.hardwaretest.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.model.ui.CheckHardware;
import com.kanzankazu.hardwaretest.ui.adapter.MainCheckAdapter;
import com.kanzankazu.hardwaretest.util.HardwareCheckUtil;
import com.kanzankazu.hardwaretest.util.PhoneSystemUtil;

import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends LocalBaseActivity {

    private static final int KEY_INTENT_FINGERPRINT = 1;
    private static final int KEY_INTENT_PROXIMITY = 2;
    private static final int KEY_INTENT_ACCELEROMETER = 3;
    private static final int KEY_INTENT_BUTTON = 4;
    private static final int KEY_INTENT_CAM = 5;
    private static final int KEY_INTENT_SCREEN = 6;
    private static final int KEY_INTENT_MIC = 7;

    private TextView tvMainInfofvbi;
    private RecyclerView rvMainfvbi;
    private ProgressBar pbMainfvbi;
    private Button bMainTes2fvbi, bMainTesfvbi;
    private MainCheckAdapter mainCheckAdapter;
    private Dialog dialogCheckSystem, dialogCheckAccelerometers, dialogCheckProximitys;
    private SensorManager sensorManager;
    private boolean isSensorAccelerometer;
    private boolean isSensorProximity;
    private List<Integer> connListStatus = new ArrayList<>();
    private List<Integer> sensListStatus = new ArrayList<>();
    private int checkamount;
    String[] stringsCheck = new String[]{
            "Hardware (CPU,RAM,ROM,Battery,ETC)",
            "Koneksi (Wifi,Bluetooth,GPS,Mobile data)",
            "Sensor (Gyroscope, Proximity, Compass)",
            "Tombol Volume + Daya",
            "Kamera Depan & Belakang",
            "Layar Sentuh",
            "Mikrofon"};
    private int progressState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //startActivity(new Intent(MainActivity.this, PremainActivity.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (PhoneSystemUtil.isRooted()) {
            finish();
            Toast.makeText(getApplicationContext(), "Perangkat anda sudah di root", Toast.LENGTH_SHORT).show();// Set your own toast  message
        } else {
            initPermissions();
            initComponent();
            initContent();
            initListener();
        }
    }

    private void initComponent() {
        tvMainInfofvbi = (TextView) findViewById(R.id.tvMainInfo);
        rvMainfvbi = (RecyclerView) findViewById(R.id.rvMain);
        pbMainfvbi = (ProgressBar) findViewById(R.id.pbMain);
        bMainTesfvbi = (Button) findViewById(R.id.bMainTes);
        bMainTes2fvbi = (Button) findViewById(R.id.bMainTes2);
    }

    private void initContent() {

        dialogStartInformation();

        //initSensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //init adapter
        List<CheckHardware> checkHardwares = new ArrayList<>();
        rvMainfvbi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainCheckAdapter = new MainCheckAdapter(this, this, checkHardwares);
        rvMainfvbi.setAdapter(mainCheckAdapter);

        pbMainfvbi.setProgress(0);

        checkamount = stringsCheck.length + 1;

        for (int i = 0; i < stringsCheck.length; i++) {
            mainCheckAdapter.addModel(new CheckHardware(i + 1, stringsCheck[i]));
        }
        mainCheckAdapter.notifyDataSetChanged();
        /*mainCheckAdapter.addModel(new CheckHardware(1, "Hardware (CPU,RAM,ROM,Battery,ETC)"));
        mainCheckAdapter.addModel(new CheckHardware(2, "Koneksi (Wifi,Bluetooth,GPS,Mobile data)"));
        mainCheckAdapter.addModel(new CheckHardware(3, "Sensor (Gyroscope, Proximity, Compass)"));
        mainCheckAdapter.addModel(new CheckHardware(4, "Tombol Volume + Daya"));
        mainCheckAdapter.addModel(new CheckHardware(5, "Kamera Depan & Belakang"));
        mainCheckAdapter.addModel(new CheckHardware(6, "Layar Sentuh"));
        mainCheckAdapter.addModel(new CheckHardware(7, "Mikrofon"));
        mainCheckAdapter.notifyDataSetChanged();*/

    }

    private void dialogStartInformation() {

    }

    private void initListener() {
        bMainTesfvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCheck();
                bMainTesfvbi.setEnabled(false);
            }
        });
        bMainTes2fvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhoneSystemUtil.scanPhone(MainActivity.this);
            }
        });
    }

    private void initPermissions() {
        Nammu.init(getApplicationContext());

        if (!Nammu.checkPermission(Manifest.permission.BLUETOOTH)) {
            Nammu.askForPermission(this, Manifest.permission.BLUETOOTH, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {

                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.BLUETOOTH_ADMIN)) {
            Nammu.askForPermission(this, Manifest.permission.BLUETOOTH_ADMIN, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.ACCESS_WIFI_STATE)) {
            Nammu.askForPermission(this, Manifest.permission.ACCESS_WIFI_STATE, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.CHANGE_WIFI_STATE)) {
            Nammu.askForPermission(this, Manifest.permission.CHANGE_WIFI_STATE, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.NFC)) {
            Nammu.askForPermission(this, Manifest.permission.NFC, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.READ_PHONE_STATE)) {
            Nammu.askForPermission(this, Manifest.permission.READ_PHONE_STATE, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.USE_FINGERPRINT)) {
            Nammu.askForPermission(this, Manifest.permission.USE_FINGERPRINT, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {
                    finish();
                }
            });
        }

        if (!Nammu.checkPermission(Manifest.permission.WRITE_SECURE_SETTINGS)) {
            Nammu.askForPermission(this, Manifest.permission.WRITE_SECURE_SETTINGS, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {

                }
            });
        }
        if (!Nammu.checkPermission(Manifest.permission.CAMERA)) {
            Nammu.askForPermission(this, Manifest.permission.CAMERA, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {

                }
            });
        }
        if (!Nammu.checkPermission(Manifest.permission.VIBRATE)) {
            Nammu.askForPermission(this, Manifest.permission.VIBRATE, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {

                }
            });
        }
        if (!Nammu.checkPermission(Manifest.permission.RECORD_AUDIO)) {
            Nammu.askForPermission(this, Manifest.permission.RECORD_AUDIO, new PermissionCallback() {
                @Override
                public void permissionGranted() {

                }

                @Override
                public void permissionRefused() {

                }
            });
        }
    }

    private void resetState() {
        for (int i = 0; i < stringsCheck.length; i++) {
            mainCheckAdapter.updateModelAt(i + 1, CheckHardware.UNCHECKING);
        }
        mainCheckAdapter.notifyDataSetChanged();

        pbMainfvbi.setProgress(0);
        bMainTesfvbi.setEnabled(true);
    }

    private void progresCheck() {
        progressState++;
        if (progressState == 1) {
            mainCheckAdapter.updateModelAt(progressState, CheckHardware.CHECKING);
            mainCheckAdapter.notifyDataSetChanged();
            pbMainfvbi.setProgress(100 / checkamount * progressState);
            dialogCheckSystems();
        } else if (progressState > 1 && progressState <= checkamount) {
            mainCheckAdapter.updateModelAt(progressState - 1, CheckHardware.CHECK_DONE);
            mainCheckAdapter.updateModelAt(progressState, CheckHardware.CHECKING);
            mainCheckAdapter.notifyDataSetChanged();
            pbMainfvbi.setProgress(100 / checkamount * progressState);
        } /*else if (i > checkamount) {
            Toast.makeText(getApplicationContext(), "Hardware Done", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void doCheck() {
        doCheckSystem();
    }

    //check system hardware
    private void doCheckSystem() {
        /*mainCheckAdapter.updateModelAt(1, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * 1);*/
        progresCheck();
        //dialogCheckSystems();
    }

    private void dialogCheckSystems() {
        dialogCheckSystem = new Dialog(MainActivity.this);
        dialogCheckSystem.setContentView(R.layout.popchecksystem);
        dialogCheckSystem.setCanceledOnTouchOutside(false);
        dialogCheckSystem.setTitle("System");

        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
        layoutparams.copyFrom(dialogCheckSystem.getWindow().getAttributes());
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;//ukuran lebar layout
        layoutparams.height = WindowManager.LayoutParams.WRAP_CONTENT;//ukuran tinggi layout

        // set the custom dialogCheckSystem components - text, image and button
        //CheckSystem = () dialogCheckSystem.findViewById(R.idHardware.);
        TextView tvPopCheckSystem = (TextView) dialogCheckSystem.findViewById(R.id.tvPopCheckSystem);
        Button bPopCheckSystem = (Button) dialogCheckSystem.findViewById(R.id.bPopCheckSystem);

        tvPopCheckSystem.setText(PhoneSystemUtil.getDataPhone(MainActivity.this));
        bPopCheckSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCheckSystem.dismiss();
                doCheckConnection();
            }
        });

        dialogCheckSystem.show();
        dialogCheckSystem.getWindow().setAttributes(layoutparams);
    }

    //check Connection
    private void doCheckConnection() {
        /*mainCheckAdapter.updateModelAt(1, CheckHardware.CHECK_DONE);
        mainCheckAdapter.updateModelAt(2, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * 2);*/

        progresCheck();
        checkBluetooth();
    }

    private void checkBluetooth() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.ishasBluetooth(MainActivity.this)) {
                    if (HardwareCheckUtil.isBluetoothAvailable()) {
                        checkWifi();
                        connListStatus.add(CheckHardware.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isBluetoothOnOff(MainActivity.this, true)) {
                            checkWifi();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        } else {
                            checkWifi();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        }
                    }
                } else {
                    checkWifi();
                    connListStatus.add(CheckHardware.CHECK_ERROR);
                }
            }
        }, 1000);
    }

    private void checkWifi() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasWIFI(MainActivity.this)) {
                    if (HardwareCheckUtil.isWifiAvailable(MainActivity.this)) {
                        checkGPS();
                        connListStatus.add(CheckHardware.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isWifiOnOff(MainActivity.this)) {
                            checkGPS();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        } else {
                            checkGPS();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        }
                    }
                } else {
                    checkGPS();
                    connListStatus.add(CheckHardware.CHECK_ERROR);
                }
            }
        }, 1000);
    }

    private void checkGPS() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasGPS(MainActivity.this)) {
                    if (HardwareCheckUtil.isGPSAvailable(MainActivity.this)) {
                        checkData();
                        connListStatus.add(CheckHardware.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isGPSOnOff(MainActivity.this)) {
                            checkData();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        } else {
                            checkData();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        }
                    }
                } else {
                    checkData();
                    connListStatus.add(CheckHardware.CHECK_ERROR);
                }
            }
        }, 1000);
    }

    private void checkData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasMData(MainActivity.this)) {
                    if (HardwareCheckUtil.isMDataSimAvailable(MainActivity.this)) {
                        if (HardwareCheckUtil.isMDataAvailable(MainActivity.this)) {
                            checkNFC();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        } else {
                            checkNFC();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        }
                    } else {
                        checkNFC();
                        connListStatus.add(CheckHardware.CHECK_ERROR);
                    }
                } else {
                    checkNFC();
                    connListStatus.add(CheckHardware.CHECK_ERROR);
                }
            }
        }, 1000);
    }

    private void checkNFC() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasNFC(MainActivity.this)) {
                    if (HardwareCheckUtil.isNFCAvailable(MainActivity.this)) {
                        if (HardwareCheckUtil.isNFCOnOff(MainActivity.this, true)) {
                            doCheckSensor();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        } else {
                            doCheckSensor();
                            connListStatus.add(CheckHardware.CHECK_DONE);
                        }
                    } else {
                        doCheckSensor();
                        connListStatus.add(CheckHardware.CHECK_ERROR);
                    }
                } else {
                    doCheckSensor();
                    connListStatus.add(CheckHardware.CHECK_ERROR);
                }
            }
        }, 1000);
    }

    //check sensor
    private void doCheckSensor() {
        /*int[] ints = ListArrayUtil.convertListIntegertToIntArray(connListStatus);
        if (ListArrayUtil.isIntArrayContainInt(ints, CheckHardware.CHECK_ERROR)) {
            mainCheckAdapter.updateModelAt(2, CheckHardware.CHECK_ERROR);
        } else {
            mainCheckAdapter.updateModelAt(2, CheckHardware.CHECK_DONE);
        }
        //mainCheckAdapter.updateModelAt(2, CheckHardware.CHECK_DONE);
        mainCheckAdapter.updateModelAt(3, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * 3);*/

        progresCheck();
        checkFingerprint();
    }

    private void checkFingerprint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            if (fingerprintManager.isHardwareDetected()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                    if (fingerprintManager.hasEnrolledFingerprints()) {
                        sensListStatus.add(CheckHardware.CHECK_DONE);
                        checkProximity();
                    } else {
                        sensListStatus.add(CheckHardware.CHECK_DONE);
                        checkProximity();
                    }
                } else {
                    sensListStatus.add(CheckHardware.CHECK_ERROR);
                    checkProximity();
                }
            } else {
                sensListStatus.add(CheckHardware.CHECK_ERROR);
                checkProximity();
            }
        } else {
            sensListStatus.add(CheckHardware.CHECK_ERROR);
            checkProximity();
        }
        // Hardware whether the device has a Fingerprint sensor.

        /*if (HardwareCheckUtil.checkSensorFingerPrint(MainActivity.this)) {
            Intent intent = new Intent(MainActivity.this, FingerActivity.class);
            startActivityForResult(intent, KEY_INTENT_FINGERPRINT);
            overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
        } else {
            checkProximity();
            sensListStatus.add(CheckHardware.CHECK_ERROR);
        }*/
    }

    private void checkProximity() {
        if (HardwareCheckUtil.checkSensor(MainActivity.this, "Proximity", PackageManager.FEATURE_SENSOR_PROXIMITY, Sensor.TYPE_PROXIMITY)) {
            Intent intent = new Intent(MainActivity.this, ProxActivity.class);
            startActivityForResult(intent, KEY_INTENT_PROXIMITY);
            overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
        } else {
            checkAccelerometer();
            sensListStatus.add(CheckHardware.CHECK_ERROR);
        }
    }

    private void checkAccelerometer() {
        if (HardwareCheckUtil.checkSensor(MainActivity.this, "Accelerometer", PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ACCELEROMETER)) {
            Intent intent = new Intent(MainActivity.this, AccelActivity.class);
            startActivityForResult(intent, KEY_INTENT_ACCELEROMETER);
            overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
        } else {
            //TODO
            doCheckButton();
            sensListStatus.add(CheckHardware.CHECK_ERROR);
        }
    }

    //check Tombol
    private void doCheckButton() {
        /*int[] ints = ListArrayUtil.convertListIntegertToIntArray(sensListStatus);
        if (ListArrayUtil.isIntArrayContainInt(ints, CheckHardware.CHECK_ERROR)) {
            mainCheckAdapter.updateModelAt(3, CheckHardware.CHECK_ERROR);
        } else {
            mainCheckAdapter.updateModelAt(3, CheckHardware.CHECK_DONE);
        }
        //mainCheckAdapter.updateModelAt(3, CheckHardware.CHECK_DONE);
        mainCheckAdapter.updateModelAt(4, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * 4);*/

        progresCheck();
        checkButton();
    }

    private void checkButton() {
        Intent intent = new Intent(MainActivity.this, ButtonActivity.class);
        startActivityForResult(intent, KEY_INTENT_BUTTON);
        overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
    }

    //check Kamera
    private void doCheckCamera() {
        /*mainCheckAdapter.updateModelAt(6, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * 6);*/

        progresCheck();
        checkCamera();
    }

    private void checkCamera() {
        Intent intent = new Intent(MainActivity.this, CamActivity.class);
        startActivityForResult(intent, KEY_INTENT_CAM);
        overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
    }

    //check TouchScreen
    private void doCheckScreen() {
        /*mainCheckAdapter.updateModelAt(7, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * KEY_INTENT_SCREEN);*/

        progresCheck();
        checkScreen();
    }

    private void checkScreen() {
        Intent intent = new Intent(MainActivity.this, ScreenActivity.class);
        startActivityForResult(intent, KEY_INTENT_SCREEN);
        overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
    }

    //Do check Microphone
    private void doCheckMic() {
        /*mainCheckAdapter.updateModelAt(8, CheckHardware.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / checkamount * KEY_INTENT_MIC);*/

        progresCheck();
        checkMic();
    }

    private void checkMic() {
        Intent intent = new Intent(MainActivity.this, WaveformActivity.class);
        startActivityForResult(intent, KEY_INTENT_MIC);
        overridePendingTransition(R.anim.masuk_dari_kanan_ke_kiri, R.anim.keluar_ke_kiri);
    }

    //check Finish
    private void doFinish() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_INTENT_FINGERPRINT) {
            if (resultCode == RESULT_OK) {
                checkProximity();
                sensListStatus.add(CheckHardware.CHECK_DONE);
            } else {
                checkProximity();
                sensListStatus.add(CheckHardware.CHECK_ERROR);
            }
        } else if (requestCode == KEY_INTENT_PROXIMITY) {
            if (resultCode == RESULT_OK) {
                checkAccelerometer();
                sensListStatus.add(CheckHardware.CHECK_DONE);
            } else {
                checkAccelerometer();
                sensListStatus.add(CheckHardware.CHECK_ERROR);
            }
        } else if (requestCode == KEY_INTENT_ACCELEROMETER) {
            if (resultCode == RESULT_OK) {
                doCheckButton();
                sensListStatus.add(CheckHardware.CHECK_DONE);
            } else {
                doCheckButton();
                sensListStatus.add(CheckHardware.CHECK_ERROR);
            }
        } else if (requestCode == KEY_INTENT_BUTTON) {
            if (resultCode == RESULT_OK) {
                doCheckCamera();
                mainCheckAdapter.updateModelAt(4, CheckHardware.CHECK_DONE);
                mainCheckAdapter.updateModelAt(5, CheckHardware.CHECK_DONE);
                mainCheckAdapter.notifyDataSetChanged();
            } else {
                doCheckCamera();
                mainCheckAdapter.updateModelAt(4, CheckHardware.CHECK_ERROR);
                mainCheckAdapter.updateModelAt(5, CheckHardware.CHECK_ERROR);
                mainCheckAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == KEY_INTENT_CAM) {
            if (resultCode == RESULT_OK) {
                doCheckScreen();
                mainCheckAdapter.updateModelAt(6, CheckHardware.CHECK_DONE);
                mainCheckAdapter.notifyDataSetChanged();
            } else {
                doCheckScreen();
                mainCheckAdapter.updateModelAt(6, CheckHardware.CHECK_ERROR);
                mainCheckAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == KEY_INTENT_SCREEN) {
            if (resultCode == RESULT_OK) {
                doCheckMic();
                mainCheckAdapter.updateModelAt(7, CheckHardware.CHECK_DONE);
                mainCheckAdapter.notifyDataSetChanged();
            } else {
                doCheckMic();
                mainCheckAdapter.updateModelAt(7, CheckHardware.CHECK_ERROR);
                mainCheckAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == KEY_INTENT_MIC) {
            if (resultCode == RESULT_OK) {
                resetState();
            } else {
                resetState();
            }
        }
    }
}
