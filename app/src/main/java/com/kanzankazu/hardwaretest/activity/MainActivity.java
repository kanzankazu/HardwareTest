package com.kanzankazu.hardwaretest.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.database.Check;
import com.kanzankazu.hardwaretest.database.SQLiteHelper;
import com.kanzankazu.hardwaretest.model.ui.CheckModel;
import com.kanzankazu.hardwaretest.util.HardwareCheckUtil;
import com.kanzankazu.hardwaretest.util.PhoneSystemUtil;

import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class MainActivity extends LocalBaseActivity {

    private TextView tvMainInfofvbi;
    private RecyclerView rvMainfvbi;
    private ProgressBar pbMainfvbi;
    private Button bMainTesfvbi;
    private MainCheckAdapter mainCheckAdapter;
    private Dialog dialogCheckSystem;

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

    private void doCheck() {
        doCheckSystem();
    }

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

    private void doCheckConnection() {
        mainCheckAdapter.updateModelAt(1, CheckModel.CHECK_DONE);
        mainCheckAdapter.updateModelAt(2, CheckModel.CHECKING);
        mainCheckAdapter.notifyDataSetChanged();
        pbMainfvbi.setProgress(100 / 7 * 2);

        checkBluetooth();
    }

    private void checkBluetooth() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //code here
                if (HardwareCheckUtil.ishasBluetooth(MainActivity.this)) {
                    if (HardwareCheckUtil.isBluetoothAvailable()) {
                        checkWifi();
                    } else {
                        if (HardwareCheckUtil.isBluetoothOnOff(true)) {
                            checkWifi();
                        } else {
                            checkWifi();
                        }
                    }
                } else {
                    checkWifi();
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
                    } else {
                        if (HardwareCheckUtil.isWifiOnOff(MainActivity.this)) {
                            checkGPS();
                        } else {
                            checkGPS();
                        }
                    }
                } else {
                    checkGPS();
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
                        resetState();
                    } else {
                        if (HardwareCheckUtil.isGPSOnOff(MainActivity.this)) {
                            resetState();
                        } else {
                            resetState();
                        }
                    }
                } else {
                    resetState();
                }
            }
        }, 2000);
    }


}
