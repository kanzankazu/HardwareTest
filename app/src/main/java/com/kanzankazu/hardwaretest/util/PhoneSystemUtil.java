package com.kanzankazu.hardwaretest.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;

import static java.util.jar.Pack200.Packer.ERROR;

public class PhoneSystemUtil extends Activity{

    public static String getDataPhone(Activity activity) {
        return  "Device Model : "                   + getPhoneModel()
                + "\n Android Version  : "          + getOSAndroid()
                + "\n Current Security Patch : "    + getOSAndroidVersionSecurityPatch()
                + "\n Board : "                     + getCPUManufacture()
                // + " " + getCPUFreq()
                + "\n Serial Number : "             + getSerialnumberBoard()
                + "\n Kernel Version : "            + getKernelVersion()
                // + "\n Builder : " + getBuilderVersion()
                // + "\n Bootloader Version : " + getBootloaderVersion()
                + "\n IMEI Number : "               + getImeiNumber(activity)
                + "\n IMEI SV : "                   + getImeiSV(activity)
                + "\n Operator Name : "             + getOperatorName(activity)
                + "\n Mobile Network : "            + getDataState(activity)
                + "\n Root Permission : "           + isRooted()
                + "\n Screen Resolution : "         + getScreenInfo("resolution",activity)
                + "\n Screen DPI : "                + getScreenInfo("dpi",activity)
                + "\n Screen Size : "               + getScreenInfo("size",activity)
                + "\n RAM Size : "      + getTotalRam()
                + "\n Internal Memory Size : "      + getTotalInternalMemorySize()
                + "\n Front Camera Resolution : "   + getCameraResolutionInMp("front")
                + "\n Rear Camera Resolution : "    + getCameraResolutionInMp("rear")
                //+ "\n External Memory Size : "      + getTotalExternalMemorySize()
                // + "\n Battery Health : " + checkBattery(activity)
                // + "\n Screen Resolution : " + heightPixels + "x" + widthPixels + "pixels"
                // + "\n Screen Size : " + String.format("%.2f", screensize) + "inch"
                ;
    }

    private static String getPhoneModel() {
        Process p = null;
        String phone_model = "";
        if (Build.MANUFACTURER.equals("Sony")) {
            try {
                p = new ProcessBuilder("/system/bin/getprop", "ro.semc.product.name").redirectErrorStream(true).start();
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
                    phone_model = line;
                }
                p.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            phone_model = Build.MODEL;
        }
        return Build.MANUFACTURER + " " + phone_model;
    }



/*    private static String getManufacture() {
        return Build.MANUFACTURER;
    }

    private static String getManufactureModel() {
        return Build.MODEL;
    }*/

    private static String getOSAndroid() {
        String[] AndroidNameList = {
                "", "Petit Four", "Cupcake", "Donut", "Eclair",
                "Eclair", "Eclair", "Froyo", "Gingerbread", "Gingerbread",
                "Honeycomb", "Honeycomb", "Honeycomb", "Ice Cream Sandwich", "Ice Cream Sandwich",
                "Jellybean", "Jellybean", "Jellybean", "Kitkat", "Kitkat",
                "Lolipop", "Lolipop", "Marshmallow", "Nougat", "Nougat",
                "Oreo", "Oreo", "Android P"};
        return "Android " + AndroidNameList[Build.VERSION.SDK_INT - 1] + ", " + Build.VERSION.RELEASE;
    }

/*    private static String getOSAndroidVersion() {
        return Build.VERSION.RELEASE;
    }*/

    private static String getOSAndroidVersionSecurityPatch() {
        return Build.VERSION.SECURITY_PATCH;
    }

    public static String loadJSONFromAsset() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = getAssets().open("cpu_snapdragon.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    private static String getCPUManufacture() {
        String name = null,id=null,devicesoc=null,manufacture=null,board=null;
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (String.valueOf(Build.BOARD).toLowerCase().contains(jsonObject.getString("id"))) {
                    board = jsonObject.getString("name");
                    break;
                } else {
                    board = Build.BOARD;
                }
            }
            devicesoc= Build.HARDWARE + " " + board;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devicesoc;
    }

/*    @SuppressLint("LongLogTag")
    private static String getCPUFreq() {
        String z =  null;
        try {
            Process x[] = {
                    Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"),
                    Runtime.getRuntime().exec("cat /sys/devices/system/cpu/cpu1/cpufreq/cpuinfo_max_freq"),
            };
            InputStream is[] = {null, null};
            String a[] = {};
            for (int i = 0; i < 2; i++) {
                if (x[i].waitFor() == 0) {
                    is[i] = x[i].getInputStream();
                } else {
                    is[i] = x[i].getErrorStream();
                }
                a[i] = new BufferedReader(new InputStreamReader(is[i])).readLine();
                new BufferedReader(new InputStreamReader(is[i])).close();
            }
            z=a[0];
            Log.d("CPU info",z);
        } catch (Exception ex) {
            Log.d("Lihat onCreate PhoneSystemUtil", ex.getMessage());
        }
        return String.format("%.2f", Float.valueOf(z) / 1000000) + " GHz";
    }*/


    private static String getSerialnumberBoard() {
        return Build.SERIAL;
    }

    @SuppressLint("LongLogTag")
    private static String getKernelVersion() {
        String line = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/version");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();
            Log.i("Kernel Version", line);
            br.close();
        } catch (Exception ex) {
            Log.d("Lihat getKernelVersion PhoneSystemUtil", ex.getMessage());
        }
        return line;
    }

/*    private static String getBuilderVersion() {
        return Build.USER + "@" + Build.HOST;

    }

    private static String getBootloaderVersion() {
        return Build.BOOTLOADER;
    }
    */

    @SuppressLint("MissingPermission")
    private static String getImeiNumber(Activity activity) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"NewApi", "LocalSuppress"})
        String imei = mTelephonyManager.getImei(0)+", "
                    + mTelephonyManager.getImei(1)+", "
                    + mTelephonyManager.getImei(2)+", "
                    + mTelephonyManager.getImei(3)+", "
                    + mTelephonyManager.getImei(4);
        return imei.replace(", null", "");
    }

    @SuppressLint("MissingPermission")
    private static String getImeiSV(Activity activity) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getDeviceSoftwareVersion();
    }

    private static String getOperatorName(Activity activity) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getNetworkOperatorName();
    }

    private static String getOutput(Context context, String methodName, int slotId) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        String reflectionMethod = null;
        String output = null;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            for (Method method : telephonyClass.getMethods()) {
                String name = method.getName();
                if (name.contains(methodName)) {
                    Class<?>[] params = method.getParameterTypes();
                    if (params.length == 1 && params[0].getName().equals("int")) {
                        reflectionMethod = name;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (reflectionMethod != null) {
            try {
                output = getOpByReflection(telephony, reflectionMethod, slotId, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return output;
    }

    private static String getOpByReflection(TelephonyManager telephony, String predictedMethodName, int slotID, boolean isPrivate) {

        //Log.i("Reflection", "Method: " + predictedMethodName+" "+slotID);
        String result = null;

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID;
            if (slotID != -1) {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName, parameter);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
                }
            } else {
                if (isPrivate) {
                    getSimID = telephonyClass.getDeclaredMethod(predictedMethodName);
                } else {
                    getSimID = telephonyClass.getMethod(predictedMethodName);
                }
            }

            Object ob_phone;
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            if (getSimID != null) {
                if (slotID != -1) {
                    ob_phone = getSimID.invoke(telephony, obParameter);
                } else {
                    ob_phone = getSimID.invoke(telephony);
                }

                if (ob_phone != null) {
                    result = ob_phone.toString();

                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
        //Log.i("Reflection", "Result: " + result);
        return result;
    }

    private static String getDataState(Activity activity) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        int datastate_int = mTelephonyManager.getDataState();
        String datastate = null;
        if (datastate_int == TelephonyManager.DATA_DISCONNECTED) {
            datastate = "Not Connected";
        } else if (datastate_int == TelephonyManager.DATA_CONNECTING) {
            datastate = "Connecting";
        } else if (datastate_int == TelephonyManager.DATA_CONNECTED) {
            datastate = "Connected";
        } else if (datastate_int == TelephonyManager.DATA_SUSPENDED) {
            datastate = "Suspend";
        }
        return datastate;
    }

    public static float getCameraResolutionInMp(String a) {
        int cameraid=1;
        int cameraman=1;

        try {
            if(a.equals("rear")){
                cameraid=0;
                cameraman = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
            else if(a.equals("front")){
                cameraid=1;
                cameraman = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            float maxResolution = -1;
            long pixelCount = -1;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraid, cameraInfo);
            if (cameraInfo.facing == cameraman ) {
                try {
                    Camera.Parameters cameraParams = Camera.open(cameraid).getParameters();
                    for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                        long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                        if (pixelCountTemp > pixelCount) {
                            pixelCount = pixelCountTemp;
                            maxResolution = Math.round(((float) pixelCountTemp) / 1000000);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return maxResolution;
        } catch (Exception e) {
            return 0;
        }

    }

    public static boolean isRooted() {
        boolean rootstate = false;
        String[] paths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) {
                rootstate = true;
            }
        }
        return rootstate;
    }

    public static String getScreenInfo(String a,Activity activity){
        String screenresolution,screensize,screendpi=null;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;

        float xdpi = widthPixels / metrics.xdpi;
        float ydpi = heightPixels / metrics.ydpi;

        float x = (float) Math.pow(xdpi, 2);
        float y = (float) Math.pow(ydpi, 2);

        float screensizenofullscreen = (float) Math.sqrt(x + y);

        screenresolution = String.valueOf(heightPixels) + "x" + String.valueOf(widthPixels) + " pixels";
        screensize = String.format("%.1f",Float.valueOf(screensizenofullscreen)) + " inch";
        screendpi = String.valueOf(metrics.densityDpi)+ " DPI";

        if (a.equals("resolution")){
            a = screenresolution;
        }
        else if(a.equals("dpi")){
            a = screendpi;
        }
        else if(a.equals("size")){
            a = screensize;
        }

        return  a;
    }

    public static String checkBattery(Activity activity) {
        final String[] health = {null};
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                if (status == BatteryManager.BATTERY_HEALTH_COLD) {
                    health[0] ="COLD";
                    //textview.setText("Battery health = Cold");
                }
                if (status == BatteryManager.BATTERY_HEALTH_DEAD) {
                    health[0] ="DEAD";

                    //textview.setText("Battery health = Dead");
                }
                if (status == BatteryManager.BATTERY_HEALTH_GOOD) {
                    health[0] ="GOOD";
                    //textview.setText("Battery health = Good");
                }
                if (status == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                    health[0] ="OVERHEAT";
                    //textview.setText("Battery health = Over Heat");
                }
                if (status == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                    health[0] ="OVER VOLTAGE";
                    //textview.setText("Battery health = Over Voltage");
                }
                if (status == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
                    health[0] ="UNKNOWN";
                    //textview.setText("Battery health = Unknown");
                }
                if (status == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                    health[0] ="UNSPECIFIED FAILURE";
                    //textview.setText("Battery health = Unspecified failure");
                }

            }
        };
        activity.registerReceiver(broadcastreceiver, intentfilter);
        return health[0];
    }

    public static String getTotalRam() {
        // Read Size of RAM
        String totalram = null;
        String totalramkb = null;
        String totalramgb = null;
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/meminfo");
            InputStream is = null;
            if (p.waitFor() == 0) {
                is = p.getInputStream();
            } else {
                is = p.getErrorStream();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            totalramkb = br.readLine().replace("MemTotal:      ", "");
            totalramgb = String.valueOf(Float.valueOf(totalramkb.replace(" kB", "")) / 1048576.0);
            totalram = String.format("%.1f", Float.valueOf(totalramgb)) + " GB";
            br.close();
        } catch (Exception ex) {
        }
        return totalram;
    }
/*    @SuppressLint({"NewApi", "LongLogTag"})
    private void getFullScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        int height = size.y;
        Log.d("Lihat getFullScreenSize PhoneSystemUtil", String.valueOf(width));
        Log.d("Lihat getFullScreenSize PhoneSystemUtil", String.valueOf(height));


        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        float x = (float) Math.pow(xdpi, 2);
        float y = (float) Math.pow(ydpi, 2);
        float screensize = (float) Math.sqrt(x + y) / 100;
    }

    private void getFullScreenResolution() {

    }

    private static String getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        return "{" + width + "," + height + "}";
    }

    private static String getScreenSize(Activity activity) {
        String size = "";
        int screenSize = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                size = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                size = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                size = "Small screen";
                break;
            default:
                size = "Screen size is neither large, normal or small";
        }
        return size;
    }

    private static String getScreenDensity(Activity activity) {
        String size = "";
        int density = activity.getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_LOW:
                size = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                size = "MDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                size = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                size = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                size = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                size = "XHDPI";
                break;

        }
        return size;
    }

    private static String getScreenInch(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen inches : " + screenInches);
        return String.valueOf(screenInches);
    }

    private static String getScreenInch2(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        double screenInches = Math.sqrt(x + y);
        //log.info("inches: {}", screenInches);
        return String.valueOf(screenInches);
    }

    public float getBackCameraResolutionInMp() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        for (int i = 0; i < noOfCameras; i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);

            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Camera camera = Camera.open(i);
                ;
                Camera.Parameters cameraParams = camera.getParameters();
                for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                    long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                    if (pixelCountTemp > pixelCount) {
                        pixelCount = pixelCountTemp;
                        maxResolution = ((float) pixelCountTemp) / (1024000.0f);
                    }
                }

                camera.release();
            }
        }

        return maxResolution;
    }

/*    public void getBackRearCameraResolutionInMp() {
        Camera camera = Camera.open(0);    // For Back Camera
        android.hardware.Camera.Parameters params = camera.getParameters();
        List sizes = params.getSupportedPictureSizes();
        Camera.Size result = null;

        ArrayList<Integer> arrayListForWidth = new ArrayList<Integer>();
        ArrayList<Integer> arrayListForHeight = new ArrayList<Integer>();

        for (int i = 0; i < sizes.size(); i++) {
            result = (Camera.Size) sizes.get(i);
            arrayListForWidth.add(result.width);
            arrayListForHeight.add(result.height);
            //Log.debug("PictureSize", "Supported Size: " + result.width + "height : " + result.height);
            System.out.println("BACK PictureSize Supported Size: " + result.width + "height : " + result.height);
        }
        if (arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0) {
            System.out.println("Back max W :" + Collections.max(arrayListForWidth));              // Gives Maximum Width
            System.out.println("Back max H :" + Collections.max(arrayListForHeight));                 // Gives Maximum Height
            System.out.println("Back Megapixel :" + (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000));
        }
        camera.release();

        arrayListForWidth.clear();
        arrayListForHeight.clear();

        camera = Camera.open(1);        //  For Front Camera
        android.hardware.Camera.Parameters params1 = camera.getParameters();
        List sizes1 = params1.getSupportedPictureSizes();
        Camera.Size result1 = null;
        for (int i = 0; i < sizes1.size(); i++) {
            result1 = (Camera.Size) sizes1.get(i);
            arrayListForWidth.add(result1.width);
            arrayListForHeight.add(result1.height);
            //Log.debug("PictureSize", "Supported Size: " + result1.width + "height : " + result1.height);
            System.out.println("FRONT PictureSize Supported Size: " + result1.width + "height : " + result1.height);
        }
        if (arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0) {
            System.out.println("FRONT max W :" + Collections.max(arrayListForWidth));
            System.out.println("FRONT max H :" + Collections.max(arrayListForHeight));
            System.out.println("FRONT Megapixel :" + (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000));
        }

        camera.release();
    }

    public void getBackFrontCameraResolutionInMp() {
        Camera camera = Camera.open(1);    // For Back Camera
        android.hardware.Camera.Parameters params = camera.getParameters();
        List sizes = params.getSupportedPictureSizes();
        Camera.Size result = null;

        ArrayList<Integer> arrayListForWidth = new ArrayList<Integer>();
        ArrayList<Integer> arrayListForHeight = new ArrayList<Integer>();

        for (int i = 0; i < sizes.size(); i++) {
            result = (Camera.Size) sizes.get(i);
            arrayListForWidth.add(result.width);
            arrayListForHeight.add(result.height);
            //Log.debug("PictureSize", "Supported Size: " + result.width + "height : " + result.height);
            System.out.println("BACK PictureSize Supported Size: " + result.width + "height : " + result.height);
        }
        if (arrayListForWidth.size() != 0 && arrayListForHeight.size() != 0) {
            System.out.println("Back max W :" + Collections.max(arrayListForWidth));              // Gives Maximum Width
            System.out.println("Back max H :" + Collections.max(arrayListForHeight));                 // Gives Maximum Height
            System.out.println("Back Megapixel :" + (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000));
        }
        camera.release();
    }*/

    public String getTotalRAM(Activity activity) {
        String totalMemory;

        /*ActivityManager actManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        totalMemory = String.valueOf(memInfo.totalMem);*/

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        totalMemory = "" + "/" + lastValue;

        //scanBtn.setText("" +"/"+lastValue );
        return totalMemory;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
        }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return formatSize(totalBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public long TotalMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        return Total;
    }

    public long FreeMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Free = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        return Free;
    }

    public long BusyMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        long Free = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        long Busy = Total - Free;
        return Busy;
    }

    public static String floatForm(double d) {
        return new DecimalFormat("#.##").format(d);
    }

    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + " byte";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + " Kb";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + " Mb";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + " Gb";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + " Tb";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + " Pb";
        if (size >= Eb) return floatForm((double) size / Eb) + " Eb";

        return "???";
    }

    // Method for getting the current battery percentage
    public double getBatteryCapacity(Context context) {
        Object mPowerProfile;
        double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class)
                    .newInstance(context);

            batteryCapacity = (double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getBatteryCapacity")
                    .invoke(mPowerProfile);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return batteryCapacity;

    }

    int getBatteryPercent(Activity activity) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = activity.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return level;
    }
    // Method for getting the total battery capacity

    double getBatteryCapacity() {
        double battCapacity = 0.0d;
        Object mPowerProfile_ = null;

        Log.d("Debug", "in getBatteryCapacity()");

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Debug", "Try 1 - Exception e: " + e.toString());
        }

        try {
            battCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            Log.d("Debug", "battCapacity is now: " + battCapacity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Debug", "Try 2 - Exception e: " + e.toString());
        }

        Log.d("Debug", "Leaving getBatteryCapacity()");
        return battCapacity;
    }
    // Method for getting the exact current battery level

    double getExactBattery(Activity activity) {
        /*Log.d("Debug", "In getExactBattery()");
        float batteryPercentage = getBatteryPercent();
        Log.d("Debug", "batteryPercentage = " + batteryPercentage);
        double totalCapacity = getBatteryCapacity();
        Log.d("Debug", "totalCapacity = " + totalCapacity);
        double currLevel = (totalCapacity * (double) batteryPercentage) / 100.0d;
        Log.d("Debug", "currLevel = " + currLevel);
        return currLevel;*/

        int batteryPercentage = getBatteryPercent(activity); //Your function
        double batteryCapacity = getBatteryCapacity(); //code from link from user87049. Change it to return value
        double currentCapacity = (batteryPercentage * batteryCapacity) / 100;
        return currentCapacity;
    }

    public void getGPUInfo() {
        GL10 gl = null;
        Log.d("GL", "GL_RENDERER = " + gl.glGetString(GL10.GL_RENDERER));
        Log.d("GL", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
        Log.d("GL", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
        Log.i("GL", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));

    }

    public int getNumberOfCores() {
        if(Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }
        else {
            // Use saurabh64's answer
            return getNumCoresOldPhones();
        }
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * @return The number of cores, or 1 if failed to get result
     */

    public int getNumCoresOldPhones() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }

    }
/*    public void setMobileDataState(boolean mobileDataEnabled)
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }

    public boolean getMobileDataState()
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error getting mobile data state", ex);
        }

        return false;
    }*/

}
