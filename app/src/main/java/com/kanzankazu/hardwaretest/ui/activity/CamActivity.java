package com.kanzankazu.hardwaretest.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.util.HardwareCheckUtil;

public class CamActivity extends LocalBaseActivity {
    private static final int RP_ACCESS = 1;
    private SurfaceView svCamerafvbi;
    private ImageButton ibCameraShotfvbi, ibCameraflashfvbi, ibCameraSwitchfvbi;
    private SurfaceHolder surfaceHolder;
    static Camera camera;
    public boolean isRear, isFront;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);

        if (HardwareCheckUtil.ishasCamera1(CamActivity.this)) {
            getpermission();
        } else {
            onBackPressed();
        }

    }

    private void initAll() {
        initComponent();
        initContent();
        initListener();

    }

    private void getpermission() {
        // cek apakah sudah memiliki permission untuk access fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // cek apakah perlu menampilkan info kenapa membutuhkan access fine location
            if (ActivityCompat.shouldShowRequestPermissionRationale(CamActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(CamActivity.this, "Access dibutuhkan untuk menentukan lokasi anda", Toast.LENGTH_LONG).show();
                String[] PERMISSIONS = {
                        Manifest.permission.CAMERA
                };
                ActivityCompat.requestPermissions(CamActivity.this, PERMISSIONS, RP_ACCESS);
            } else {
                // request permission untuk access fine location
                String[] PERMISSIONS = {
                        Manifest.permission.CAMERA
                };
                ActivityCompat.requestPermissions(CamActivity.this, PERMISSIONS, RP_ACCESS);
            }
        } else {
            initAll();
            // permission access fine location didapat
            // Toast.makeText(CamActivity.this, "Yay, has permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RP_ACCESS: //private final int = 1
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do location thing
                    // access location didapatkan
                    //Toast.makeText(CamActivity.this, "Akses di berikan", Toast.LENGTH_SHORT).show();
                    initAll();
                } else {
                    // access location ditolak user
                    //Toast.makeText(CamActivity.this, "Akses di tolak", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                return;
        }
    }

    private void initComponent() {
        svCamerafvbi = (SurfaceView) findViewById(R.id.svCamera);
        ibCameraShotfvbi = (ImageButton) findViewById(R.id.ibCameraShot);
        ibCameraflashfvbi = (ImageButton) findViewById(R.id.ibCameraflash);
        ibCameraSwitchfvbi = (ImageButton) findViewById(R.id.ibCameraSwitch);
    }

    private void initContent() {
        ibCameraSwitchfvbi.setVisibility(View.GONE);
        ibCameraflashfvbi.setVisibility(View.GONE);

        getWindow().setFormat(PixelFormat.UNKNOWN);

        surfaceHolder = svCamerafvbi.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //HardwareCheckUtil.openRearCamera(CamActivity.this, camera, surfaceHolder);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                openCamera(0);
            }
        }, 1);
        isRear = true;
    }

    private void openCamera(int camId) {
        try {
            releaseCameraAndPreview();
            if (camId == 0) {
                camera = Camera.open(0);
                camera.setDisplayOrientation(HardwareCheckUtil.setCameraDisplayOrientationInt(CamActivity.this, camId, camera));
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }
            if (camId == 1) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                camera.setDisplayOrientation(HardwareCheckUtil.setCameraDisplayOrientationInt(CamActivity.this, camId, camera));
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            }
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void initListener() {
        ibCameraShotfvbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRear) {
                    if (HardwareCheckUtil.ishasFrontCamera(CamActivity.this)) {
                        openCamera(1);
                        isRear = false;
                        isFront = true;
                    } else {
                        releaseCameraAndPreview();
                        onBackPressed();
                    }
                } else if (isFront) {
                    camera.stopPreview();
                    camera.release();
                    setResult(Activity.RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);

    }
}
