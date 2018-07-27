package com.kanzankazu.hardwaretest.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kanzankazu.hardwaretest.R;
import com.kanzankazu.hardwaretest.database.room.AppDatabase;
import com.kanzankazu.hardwaretest.database.room.table.Hardware;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.Date;


public class ScreenActivity extends AppCompatActivity {

    private static final int RP_ACCESS = 1;
    private View[][] circles;
    private int counter;
    private int squareXY;
    private boolean toastShownFlag;
    private int total;
    private boolean[][] flags;

    private GridLayout gridLayout;
    private TextView tvcountDown;
    private CountDownTimer timer;
    private long timeRemaining = 0;
    private boolean isPauseTimer = false;
    private AppDatabase appDatabase;

    public static class CircleView extends View {
        public int i, j;

        public CircleView(Context context) {
            super(context);
        }

        public CircleView(Context context, int i, int j) {
            super(context);
            i = i;
            j = j;
        }

        public CircleView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @SuppressLint("NewApi")
        public CircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_screen);

        getpermission();
        initComponent();
        initContent();
        initListener();

    }

    private void getpermission() {
        // cek apakah sudah memiliki permission untuk access fine location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // cek apakah perlu menampilkan info kenapa membutuhkan access fine location
            if (ActivityCompat.shouldShowRequestPermissionRationale(ScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(ScreenActivity.this, "Access dibutuhkan untuk menentukan lokasi anda", Toast.LENGTH_LONG).show();
                String[] perm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(ScreenActivity.this, perm, RP_ACCESS);
            } else {
                // request permission untuk access fine location
                String[] perm = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(ScreenActivity.this, perm, RP_ACCESS);
            }
        } else {
            // permission access fine location didapat
            Toast.makeText(ScreenActivity.this, "Yay, has permission", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ScreenActivity.this, "Akses di berikan", Toast.LENGTH_SHORT).show();
                    /*readSMS();*///jalankan alpkasi yang mau di jalankan
                } else {
                    // access location ditolak user
                    Toast.makeText(ScreenActivity.this, "Akses di tolak", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void initComponent() {
        gridLayout = (GridLayout) findViewById(R.id.screen_test_grid_layout);
        tvcountDown = (TextView) findViewById(R.id.tvcountDown);

    }

    private void initContent() {

        appDatabase = new AppDatabase(ScreenActivity.this);

        startCounter();

        dialogStart();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        } else {
            display.getSize(size);
        }

        int square = calculateSquare(size.y, size.x, size.x / 15);
        if (square < size.x / 15) {
            square = size.x / 15;
        }
        squareXY = square;
        int rows = size.y / squareXY;
        int cells = size.x / squareXY;
        flags = (boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{rows, cells});
        total = rows * cells;
        counter = 0;
        gridLayout.setRowCount(rows);
        gridLayout.setColumnCount(cells);
        circles = (View[][]) Array.newInstance(View.class, new int[]{rows, cells});

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cells; j++) {
                circles[i][j] = new CircleView(getApplicationContext(), i, j);
                View view = circles[i][j];
                view.setLayoutParams(new LayoutParams(squareXY, squareXY));
                view.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        CircleView view = (CircleView) v;
                        view.setBackground(ContextCompat.getDrawable(ScreenActivity.this, R.drawable.background_edittext_search));
                        checkOnSuccess(view.i, view.j);
                    }
                });
                gridLayout.addView(view);
            }
        }
    }

    private void dialogStart() {
        // Munculkan alert dialog apabila user ingin keluar aplikasi
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Silahkan penuhi layar dengan lingkarang dengan mengusap-usap layar kurang dari 1 menit, Siap?");
        alertDialogBuilder.setPositiveButton("Siap",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        timer.start();
                        getWindow().getDecorView().setSystemUiVisibility(
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                    }
                });
        // Pilihan jika NO
        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
        // Tampilkan alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void startCounter() {
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isPauseTimer) {
                    cancel();
                } else {
                    tvcountDown.setText(String.valueOf(millisUntilFinished / 1000));
                    timeRemaining = millisUntilFinished;
                }
            }

            @Override
            public void onFinish() {
                finish();
                insertDevice("touchscreen", 1, 0);
            }
        };
    }

    private void initListener() {

    }

    public boolean dispatchTouchEvent(MotionEvent e) {
        if (2 == e.getAction()) {
            checkOnSuccess(((int) e.getY()) / squareXY, ((int) e.getX()) / squareXY);
        }
        return super.dispatchTouchEvent(e);
    }

    public boolean checkOnSuccess(int i, int j) {
        try {
            //circles[i][j].setBackground(green);
            circles[i][j].setBackground(ContextCompat.getDrawable(ScreenActivity.this, R.drawable.background_edittext_search));
            if (!flags[i][j]) {
                flags[i][j] = true;
                counter++;
            }
            if (counter == total) {
                if (!toastShownFlag) {
                    Toast.makeText(getApplicationContext(), "TEST PASSED", Toast.LENGTH_SHORT).show();
                    insertDevice("touchscreen", 1, 1);
                    //finish();
                    toastShownFlag = true;
                    //takeScreenshot();
                    setResult(Activity.RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
                }
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return false;
    }

    @NonNull
    private ShapeDrawable createBackground(int green) {
        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(green);
        return drawable;
    }

    private void insertDevice(String nameDevice, int statusDevice, int descDevice) {
        String statusDevices = null;
        String descDevices = null;
        if (statusDevice == 0) {
            statusDevices = "tidak ada";
            descDevices = "";
        } else if (statusDevice == 1) {
            statusDevices = "ada";
            if (descDevice == 0) {
                descDevices = "rusak";
            } else if (descDevice == 1) {
                descDevices = "bagus";
            } else if (descDevice == 2) {
                descDevices = "lain-lain";
            }
        }
        appDatabase.insertHardware(new Hardware(nameDevice, statusDevices, descDevices));
    }

    private int calculateSquare(int screenHeight, int screenWidth, int basedOn) {
        int i = basedOn;
        while (i != 0) {
            if (screenHeight % i == 0 && screenWidth % i == 0) {
                return i;
            }
            i--;
        }
        throw new RuntimeException("Cannot Calculate!!");
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/Download/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            //make image
            File imageFile = new File(mPath);

            //save image
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //openScreenshot(imageFile);
            Log.d("Lihat takeScreenshot ScreenActivity", String.valueOf(bitmap));
            Log.d("Lihat takeScreenshot ScreenActivity", String.valueOf(imageFile));
            Log.d("Lihat takeScreenshot ScreenActivity", String.valueOf(imageFile.exists()));
            Log.d("Lihat takeScreenshot ScreenActivity", String.valueOf(imageFile.isFile()));
            Log.d("Lihat takeScreenshot ScreenActivity", String.valueOf(outputStream));
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
        overridePendingTransition(R.anim.masuk_dari_kiri_ke_kanan, R.anim.keluar_ke_kanan);
    }
}
