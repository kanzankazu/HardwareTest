package com.kanzankazu.hardwaretest.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.fingerprint.FingerprintManager;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;
import static android.hardware.Camera.getCameraInfo;

public class HardwareCheckUtil {
    public static boolean ishasBluetooth(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);
    }

    public static boolean isBluetoothAvailable() {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
    }

    public static Boolean isBluetoothOnOff(Context context, @Nullable Boolean enable) {
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (enable) {
            bluetoothAdapter.enable();
            Toast.makeText(context, "Bluetooth On", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            bluetoothAdapter.disable();
            Toast.makeText(context, "Bluetooth Off", Toast.LENGTH_SHORT).show();
            return true;
        }
        /*if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            Toast.makeText(context, "Bluetooth Off", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            bluetoothAdapter.enable();
            Toast.makeText(context, "Bluetooth On", Toast.LENGTH_SHORT).show();
            return true;
        }*/
    }

    public static boolean isHasWIFI(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI);
    }

    public static boolean isWifiAvailable(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi != null && wifi.isWifiEnabled();
    }

    public static boolean isWifiOnOff(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            boolean b = wifi.isWifiEnabled();
            if (b) {
                wifi.setWifiEnabled(false);
                Toast.makeText(context, "Wifi Off", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                wifi.setWifiEnabled(true);
                Toast.makeText(context, "Wifi On", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isHasGPS(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION);
    }

    public static boolean isGPSAvailable(final Activity context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                }
            });
            builder.create().show();
        } else {
            //
        }
        return false;
    }

    public static boolean isGPSOnOff(final Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
            return true;
        } else {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
            return true;
        }
    }

    public static boolean isHasMData(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
        return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);
    }

    public static boolean isMDataSimAvailable(Activity activity) {
        boolean isAvailable = false;
        TelephonyManager telMgr = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT: //SimState = “No Sim Found!”;
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = “Network Locked!”;
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = “PIN Required to access SIM!”;
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = “PUK Required to access SIM!”; // Personal Unblocking Code
                isAvailable = false;
                break;
            case TelephonyManager.SIM_STATE_READY:
                isAvailable = true;
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = “Unknown SIM State!”;
                isAvailable = false;
                break;
        }
        return isAvailable;
    }

    public static boolean isMDataAvailable(final Activity context) {
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);

            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileDataEnabled;
    }

    public static boolean isMDataOnOff(final Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) { //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
            return true;
        } else {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
            return true;
        }
    }

    public static boolean isHasNFC(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    public static boolean isNFCAvailable(Context context) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        boolean enabled = false;

        if (nfcAdapter != null) {
            enabled = nfcAdapter.isEnabled();
        }

        return enabled;
    }

    public static boolean isNFCOnOff(final Context context, Boolean enabled) {
        // Turn NFC on/off
        final boolean desiredState = enabled;
        final NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (mNfcAdapter == null) {
            // NFC is not supported
            //return false;
        }

        new Thread("toggleNFC") {
            public void run() {
                Log.d(TAG, "Setting NFC enabled state to: " + desiredState);
                boolean success = false;
                Class<?> NfcManagerClass;
                Method setNfcEnabled, setNfcDisabled;
                boolean Nfc;
                if (desiredState) {
                    try {
                        NfcManagerClass = Class.forName(mNfcAdapter.getClass().getName());
                        setNfcEnabled = NfcManagerClass.getDeclaredMethod("enable");
                        setNfcEnabled.setAccessible(true);
                        Nfc = (Boolean) setNfcEnabled.invoke(mNfcAdapter);
                        success = Nfc;
                    } catch (ClassNotFoundException e) {
                    } catch (NoSuchMethodException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                } else {
                    try {
                        NfcManagerClass = Class.forName(mNfcAdapter.getClass().getName());
                        setNfcDisabled = NfcManagerClass.getDeclaredMethod("disable");
                        setNfcDisabled.setAccessible(true);
                        Nfc = (Boolean) setNfcDisabled.invoke(mNfcAdapter);
                        success = Nfc;
                    } catch (ClassNotFoundException e) {
                    } catch (NoSuchMethodException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
                if (success) {
                    Log.d(TAG, "Successfully changed NFC enabled state to " + desiredState);
                } else {
                    Log.w(TAG, "Error setting NFC enabled state to " + desiredState);
                }
            }
        }.start();

        if (desiredState) {
            return true;
        } else {
            return true;
        }
    }

    public static boolean checkSensor(Context context, String sensorname, String packManSensorName, int typeSensor) {

        PackageManager packageManager = context.getPackageManager();
        boolean sensorExists = packageManager.hasSystemFeature(packManSensorName);

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(typeSensor);

        if (sensorExists) {
            if (sensor != null) {
                return true;
            } else {
                Toast.makeText(context.getApplicationContext(), "No " + sensorname + " Sensor!", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context.getApplicationContext(), "Sensor " + sensorname + " No Exist", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void checkSensorLight(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            Toast.makeText(context.getApplicationContext(), "No Light Sensor! quit-", Toast.LENGTH_LONG).show();
        } else {
            float max = lightSensor.getMaximumRange();
            //lightMeter.setMax((int) max);
            //textMax.setText("Max Reading: " + String.valueOf(max));
            SensorEventListener sensorListener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
                        float value = sensorEvent.values[0];
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            sensorManager.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public static void checkSensorProximity(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximity == null) {
            Toast.makeText(context.getApplicationContext(), "No Proximity Sensor!", Toast.LENGTH_LONG).show();
        } else {
            SensorEventListener sensorListener = new SensorEventListener() {
                int a = 0, b = 0;

                @Override
                public void onSensorChanged(SensorEvent sensorEvent) {
                    if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                        if (sensorEvent.values[0] == 0) {
                            Log.e("onSensorChanged", "NEAR");
                            a++;
                        } else {
                            Log.e("onSensorChanged", "FAR");
                            b++;
                        }
                        if (a > 2 && b > 2) {

                        }
                    }
                }

                @Override
                public void onAccuracyChanged(Sensor sensor, int i) {

                }
            };
            sensorManager.registerListener(sensorListener, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public static void checkSensorFingerPrint(Context context) {
        // Check if we're running on Android 6.0 (M) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                // Device doesn't support fingerprint authentication
                Toast.makeText(context, "Device doesn't support fingerprint.", Toast.LENGTH_SHORT).show();// Set your own toast  message
            } else {
                //
            }

            /*else if (!fingerprintManager.hasEnrolledFingerprints()) {
                // User hasn't enrolled any fingerprints to authenticate with
            } else {
                // Everything is ready for fingerprint authentication
            }*/
        } else {
            Toast.makeText(context, "Your Os no support fingerprint.", Toast.LENGTH_SHORT).show();// Set your own toast  message
        }
    }

    public static boolean ishasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static boolean ishasCamera1(Context context) {
        int numberOfCameras = Camera.getNumberOfCameras();
        PackageManager pm = context.getPackageManager();
        final boolean deviceHasCameraFlag = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

        return deviceHasCameraFlag && numberOfCameras != 0;
    }

    public static boolean ishasFrontCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public static void openRearCamera(Activity activity, Camera camera, SurfaceHolder surfaceHolder) {
        try {
            camera.stopPreview();
            camera.release();
            camera = Camera.open(0);
            setCameraDisplayOrientation(activity, 0, camera);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openFrontCamera(Activity activity, Camera camera, SurfaceHolder surfaceHolder) {
        try {
            camera.stopPreview();
            camera.release();
            camera = Camera.open(1);
            setCameraDisplayOrientation(activity, 1, camera);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    public static boolean getMicrophoneAvailable(Context context) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(new File(context.getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath());
        boolean available = true;
        try {
            recorder.prepare();
            recorder.start();

        } catch (Exception exception) {
            available = false;
        }
        recorder.release();
        return available;
    }

    public static long getFirstInstallTime(Context context) {
        long installed = 0;
        try {
            installed = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return installed;
    }

    public static long getLastModified(Context context) {
        long installed = 0;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo;
            appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            String appFile = appInfo.sourceDir;
            installed = new File(appFile).lastModified();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return installed;
    }

    public static long getAppFirstInstallTime(Context context) {
        PackageInfo packageInfo;
        try {
            if (Build.VERSION.SDK_INT > 8/*Build.VERSION_CODES.FROYO*/) {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                return packageInfo.firstInstallTime;
            } else {
                //firstinstalltime unsupported return last update time not first install time
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
                String sAppFile = appInfo.sourceDir;
                return new File(sAppFile).lastModified();
            }
        } catch (PackageManager.NameNotFoundException e) {
            //should never happen
            return 0;
        }
    }
}
