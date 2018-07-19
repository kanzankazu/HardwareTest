package com.kanzankazu.hardwaretest.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.model.ui.CheckModel;
import com.kanzankazu.hardwaretest.util.HardwareCheckUtil;
import com.kanzankazu.hardwaretest.util.PhoneSystemUtil;

import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends LocalBaseActivity implements SensorEventListener {

    private static final int KEY_INTENT_PROXIMITY = 123123;
    private TextView tvMainInfofvbi;
    private RecyclerView rvMainfvbi;
    private ProgressBar pbMainfvbi;
    private Button bMainTesfvbi;
    private MainCheckAdapter mainCheckAdapter;
    private Dialog dialogCheckSystem, dialogCheckAccelerometers, dialogCheckProximitys;
    private SensorManager sensorManager;
    private boolean isSensorAccelerometer;
    private boolean isSensorProximity;
    private List<Integer> connListStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(PhoneSystemUtil.isRooted().equals("Allowed")){
            Intent myIntent = new Intent(this, ClosingActivity.class);
            startActivity(myIntent);
            finish();
        }
        else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            initComponent();
            initContent();
            initListener();
            initPermissions();
        }
    }

    private void initComponent() {
        tvMainInfofvbi = (TextView) findViewById(R.id.tvMainInfo);
        rvMainfvbi = (RecyclerView) findViewById(R.id.rvMain);
        pbMainfvbi = (ProgressBar) findViewById(R.id.pbMain);
        bMainTesfvbi = (Button) findViewById(R.id.bMainTes);
    }

    private void initContent() {
        List<CheckModel> checkModels = new ArrayList<>();
        rvMainfvbi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mainCheckAdapter = new MainCheckAdapter(this, this, checkModels);
        rvMainfvbi.setAdapter(mainCheckAdapter);

        pbMainfvbi.setProgress(0);

        mainCheckAdapter.addModel(new CheckModel(1, "Hardware (CPU,RAM,ROM,Battery,ETC)"));
        mainCheckAdapter.addModel(new CheckModel(2, "Koneksi (Wifi,Bluetooth,GPS,Mobile data)"));
        mainCheckAdapter.addModel(new CheckModel(3, "Sensor (Gyroscope, Proximity, Compass)"));
        mainCheckAdapter.addModel(new CheckModel(4, "Tombol Volume"));
        mainCheckAdapter.addModel(new CheckModel(5, "Tombol Daya"));
        mainCheckAdapter.addModel(new CheckModel(6, "Kamera Depan & Belakang"));
        mainCheckAdapter.addModel(new CheckModel(7, "Layar Sentuh"));
        mainCheckAdapter.notifyDataSetChanged();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    private void initListener() {
        bMainTesfvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCheck();
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
    }

    private void resetState() {
        mainCheckAdapter.updateModelAt(1, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(2, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(3, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(4, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(5, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(6, CheckModel.UNCHECKING);
        mainCheckAdapter.updateModelAt(7, CheckModel.UNCHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(0);
    }

    /*private void setProgresCheck(int i) {
        if (i == 1) {
            mainCheckAdapter.updateModelAt(i, CheckModel.CHECKING);
            mainCheckAdapter.notifyDataSetChanged();
            pbMainfvbi.setProgress(100 / 7 * i);
            dialogCheckSystems();
        } else if (i > 1 && i <= 7) {
            mainCheckAdapter.updateModelAt(i - 1, CheckModel.CHECK_DONE);
            mainCheckAdapter.updateModelAt(i, CheckModel.CHECKING);
            mainCheckAdapter.notifyDataSetChanged();
            pbMainfvbi.setProgress(100 / 7 * i);
        } else if (i > 7) {
            Toast.makeText(getApplicationContext(), "Check Done", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void doCheck() {
        doCheckSystem();
    }

    //check system hardware
    private void doCheckSystem() {
        mainCheckAdapter.updateModelAt(1, CheckModel.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / 7 * 1);
        dialogCheckSystems();

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
        //CheckSystem = () dialogCheckSystem.findViewById(R.id.);
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
        mainCheckAdapter.updateModelAt(1, CheckModel.CHECK_DONE);
        mainCheckAdapter.updateModelAt(2, CheckModel.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / 7 * 2);

        checkBluetooth();
        connListStatus = new ArrayList<Integer>();
    }

    private void checkBluetooth() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.ishasBluetooth(MainActivity.this)) {
                    if (HardwareCheckUtil.isBluetoothAvailable()) {
                        checkWifi();
                        connListStatus.add(CheckModel.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isBluetoothOnOff(MainActivity.this, true)) {
                            checkWifi();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        } else {
                            checkWifi();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        }
                    }
                } else {
                    checkWifi();
                    connListStatus.add(CheckModel.CHECK_ERROR);
                }
            }
        }, 2000);
    }

    private void checkWifi() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasWIFI(MainActivity.this)) {
                    if (HardwareCheckUtil.isWifiAvailable(MainActivity.this)) {
                        checkGPS();
                        connListStatus.add(CheckModel.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isWifiOnOff(MainActivity.this)) {
                            checkGPS();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        } else {
                            checkGPS();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        }
                    }
                } else {
                    checkGPS();
                    connListStatus.add(CheckModel.CHECK_ERROR);
                }
            }
        }, 2000);
    }

    private void checkGPS() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasGPS(MainActivity.this)) {
                    if (HardwareCheckUtil.isGPSAvailable(MainActivity.this)) {
                        checkData();
                        connListStatus.add(CheckModel.CHECK_DONE);
                    } else {
                        if (HardwareCheckUtil.isGPSOnOff(MainActivity.this)) {
                            checkData();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        } else {
                            checkData();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        }
                    }
                } else {
                    checkData();
                    connListStatus.add(CheckModel.CHECK_ERROR);
                }
            }
        }, 2000);
    }

    private void checkData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasMData(MainActivity.this)) {
                    if (HardwareCheckUtil.isMDataSimAvailable(MainActivity.this)) {
                        if (HardwareCheckUtil.isMDataAvailable(MainActivity.this)) {
                            checkNFC();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        } else {
                            checkNFC();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        }
                    } else {
                        checkNFC();
                        connListStatus.add(CheckModel.CHECK_ERROR);
                    }
                } else {
                    checkNFC();
                    connListStatus.add(CheckModel.CHECK_ERROR);
                }
            }
        }, 2000);
    }

    private void checkNFC() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.isHasNFC(MainActivity.this)) {
                    if (HardwareCheckUtil.isNFCAvailable(MainActivity.this)) {
                        if (HardwareCheckUtil.isNFCOnOff(MainActivity.this, true)) {
                            doCheckSensor();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        } else {
                            doCheckSensor();
                            connListStatus.add(CheckModel.CHECK_DONE);
                        }
                    } else {
                        doCheckSensor();
                        connListStatus.add(CheckModel.CHECK_ERROR);
                    }
                } else {
                    doCheckSensor();
                    connListStatus.add(CheckModel.CHECK_ERROR);
                }
            }
        }, 2000);
    }

    //check sensor
    private void doCheckSensor() {
        /*int[] ints = ListArrayUtil.convertListIntegertToIntArray(connListStatus);
        if (ListArrayUtil.isIntArrayContainInt(ints, CheckModel.CHECK_ERROR)) {
            mainCheckAdapter.updateModelAt(2, CheckModel.CHECK_ERROR);
        } else {
            mainCheckAdapter.updateModelAt(2, CheckModel.CHECK_DONE);
        }*/
        mainCheckAdapter.updateModelAt(2, CheckModel.CHECK_DONE);
        mainCheckAdapter.updateModelAt(3, CheckModel.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / 7 * 3);

        checkAccelerometer();
    }

    private void checkAccelerometer() {
        if (HardwareCheckUtil.checkSensor(MainActivity.this, "Accelerometer", PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ACCELEROMETER)) {
            Intent intent = new Intent(MainActivity.this, ProxActivity.class);
            startActivityForResult(intent, KEY_INTENT_PROXIMITY);
            //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            checkProximity();
        }
    }

    private void dialogCheckAccelerometer() {
        dialogCheckAccelerometers = new Dialog(MainActivity.this);
        dialogCheckAccelerometers.setContentView(R.layout.popchecksystem);
        dialogCheckAccelerometers.setTitle("");

        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
        layoutparams.copyFrom(dialogCheckAccelerometers.getWindow().getAttributes());
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;//ukuran lebar layout
        layoutparams.height = WindowManager.LayoutParams.WRAP_CONTENT;//ukuran tinggi layout

        // set the custom dialogCheckAccelerometers components - text, image and button
        //CheckAccelerometers = () dialogCheckAccelerometers.findViewById(R.id.);
        TextView tvPopCheckAccelerometers = (TextView) dialogCheckSystem.findViewById(R.id.tvPopCheckSystem);
        Button bPopCheckAccelerometers = (Button) dialogCheckSystem.findViewById(R.id.bPopCheckSystem);

        tvPopCheckAccelerometers.setText("Gerakan HP anda!");
        bPopCheckAccelerometers.setVisibility(View.GONE);

        isSensorAccelerometer = true;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        dialogCheckAccelerometers.show();
        dialogCheckAccelerometers.getWindow().setAttributes(layoutparams);
    }

    private void checkProximity() {
        if (HardwareCheckUtil.checkSensor(MainActivity.this, "Proximity", PackageManager.FEATURE_SENSOR_PROXIMITY, Sensor.TYPE_PROXIMITY)) {
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
            isSensorProximity = true;

            dialogCheckProximity();
        } else {
            resetState();
        }
    }

    private void dialogCheckProximity() {
        dialogCheckProximitys = new Dialog(MainActivity.this);
        dialogCheckProximitys.setContentView(R.layout.popchecksystem);
        dialogCheckProximitys.setTitle("");

        WindowManager.LayoutParams layoutparams = new WindowManager.LayoutParams();
        layoutparams.copyFrom(dialogCheckProximitys.getWindow().getAttributes());
        layoutparams.width = WindowManager.LayoutParams.MATCH_PARENT;//ukuran lebar layout
        layoutparams.height = WindowManager.LayoutParams.WRAP_CONTENT;//ukuran tinggi layout

        // set the custom dialogCheckProximitys components - text, image and button
        //CheckAccelerometers = () dialogCheckProximitys.findViewById(R.id.);
        TextView tvPopCheckAccelerometers = (TextView) dialogCheckSystem.findViewById(R.id.tvPopCheckSystem);
        Button bPopCheckAccelerometers = (Button) dialogCheckSystem.findViewById(R.id.bPopCheckSystem);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        isSensorAccelerometer = true;

        tvPopCheckAccelerometers.setText("Gerakan Tangan anda di atas HP anda!");
        bPopCheckAccelerometers.setVisibility(View.GONE);

        dialogCheckProximitys.show();
        dialogCheckProximitys.getWindow().setAttributes(layoutparams);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isSensorAccelerometer) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                float diff = (float) Math.sqrt(x * x + y * y + z * z);
                if (diff > 0.5) {//TODO 0.5 is a threshold, you can test it and change it
                    isSensorAccelerometer = false;
                    dialogCheckAccelerometers.dismiss();
                    checkProximity();
                }
            }
        } else if (isSensorProximity) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                    int a = 0, b = 0;
                    if (sensorEvent.values[0] == 0) {
                        a++;
                    } else {
                        b++;
                    }
                    if (a > 2 && b > 2) {
                        isSensorProximity = false;

                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
