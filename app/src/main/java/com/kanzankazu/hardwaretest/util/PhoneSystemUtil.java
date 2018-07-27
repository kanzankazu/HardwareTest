package com.kanzankazu.hardwaretest.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kanzankazu.hardwaretest.database.room.AppDatabase;
import com.kanzankazu.hardwaretest.database.room.table.Hardware;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class PhoneSystemUtil extends Activity {

    private static final String TAG = "Lihat";

    public static String getDataPhone(Activity activity) {
        return ""
                + "\n Device Model : " + getPhoneModel()
                + "\n Android Version  : " + getOSAndroid()
                + "\n Current Security Patch : " + getOSAndroidVersionSecurityPatch()
                + "\n Board : " + getCPUManufacture(activity)
                + "\n Serial Number : " + getSerialnumberBoard()
                + "\n Kernel Version : " + getKernelVersion()
                + "\n Builder : " + getBuilderVersion()
                + "\n Bootloader Version : " + getBootloaderVersion()
                + "\n IMEI Number : " + getImeiNumber(activity)
                + "\n IMEI SV : " + getImeiSV(activity)
                + "\n Operator Name : " + getOperatorName(activity)
                + "\n Mobile Network : " + getDataState(activity)
                + "\n Root Permission : " + isRooted()
                + "\n Screen Resolution : " + getScreenInfo("resolution", activity)
                + "\n Screen DPI : " + getScreenInfo("dpi", activity)
                + "\n Screen Size : " + getScreenInfo("size", activity)
                + "\n RAM Size : " + getTotalRam()
                + "\n Internal Memory Size : " + getTotalInternalMemorySize()
                + "\n Front Camera Resolution : " + getFrontCameraResolutionInMp()
                + "\n Rear Camera Resolution : " + getBackCameraResolutionInMp()
                + "\n Total Battery : " + getBatteryCapacity(activity)
                + "\n Screen Resolution : " + getScreenInfo("resolution", activity)
                + "\n Screen Size : " + getScreenInfo("size", activity)
                ;
    }

    public static void scanPhone(Activity activity) {
        AppDatabase appDatabase = new AppDatabase(activity);

        appDatabase.insertHardware(new Hardware("Device Model", getPhoneModel(), ""));
        appDatabase.insertHardware(new Hardware("Device Serial Number Board", getSerialnumberBoard(), ""));
        appDatabase.insertHardware(new Hardware("Device Kernel Version", getKernelVersion(), ""));
        appDatabase.insertHardware(new Hardware("Device Builder Version", getBuilderVersion(), ""));
        appDatabase.insertHardware(new Hardware("Device Bootloader Version", getBootloaderVersion(), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getPhoneModel());
        Log.d("Lihat scanPhone PhoneSystemUtil", getSerialnumberBoard());
        Log.d("Lihat scanPhone PhoneSystemUtil", getKernelVersion());
        Log.d("Lihat scanPhone PhoneSystemUtil", getBuilderVersion());
        Log.d("Lihat scanPhone PhoneSystemUtil", getBootloaderVersion());

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("Display size", getPhoneModel(), ""));
        appDatabase.insertHardware(new Hardware("Resolution", getSerialnumberBoard(), ""));
        appDatabase.insertHardware(new Hardware("Pixel density", getKernelVersion(), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getScreenInfo("size", activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getScreenInfo("resolution", activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getScreenInfo("dpi", activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getScreenSize(activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getScreenDensity(activity));

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("Rear camera", getBackCameraResolutionInMp(), ""));
        appDatabase.insertHardware(new Hardware("Front camera", String.valueOf(getFrontCameraResolutionInMp()), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getBackCameraResolutionInMp());
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(getFrontCameraResolutionInMp()));

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("System chip", getCPUManufacture(activity), ""));
        appDatabase.insertHardware(new Hardware("Processor", String.valueOf(getNumberOfCores()), ""));
        appDatabase.insertHardware(new Hardware("RAM", getTotalRam(), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getCPUManufacture(activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(getNumberOfCores()));
        //Log.d("Lihat scanPhone PhoneSystemUtil", getCPUFreq());
        Log.d("Lihat scanPhone PhoneSystemUtil", getTotalRam());
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(externalMemoryAvailable()));
        Log.d("Lihat scanPhone PhoneSystemUtil", getAvailableInternalMemorySize());
        Log.d("Lihat scanPhone PhoneSystemUtil", getTotalInternalMemorySize());

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("OS", getOSAndroid(), ""));
        appDatabase.insertHardware(new Hardware("OS Security", getOSAndroidVersionSecurityPatch(), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getOSAndroid());
        if (DeviceDetailUtil.isMarmellowAbove()) {
            Log.d("Lihat scanPhone PhoneSystemUtil", getOSAndroidVersionSecurityPatch());
        }

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("Battery Capacity", String.valueOf(getBatteryCapacity(activity)), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(getBatteryCapacity(activity)));
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(getExactBattery(activity)));
        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(getMobileDataState(activity)));

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        appDatabase.insertHardware(new Hardware("Imei Number", getImeiNumber(activity), ""));
        Log.d("Lihat scanPhone PhoneSystemUtil", getImeiNumber(activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getImeiSV(activity));
        Log.d("Lihat scanPhone PhoneSystemUtil", getOperatorName(activity));

        Log.d("Lihat scanPhone PhoneSystemUtil", "");

        Log.d("Lihat scanPhone PhoneSystemUtil", String.valueOf(isRooted()));

    }

    /**/
    private static String getPhoneModel() {
        Process p = null;
        String phone_model = "";
        if (Build.MANUFACTURER.equalsIgnoreCase("Sony")) {
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

    private static String getSerialnumberBoard() {
        return Build.SERIAL;
    }

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
            Log.d("Lihat getKernelVersion", ex.getMessage());
        }
        return line;
    }

    private static String getBuilderVersion() {
        return Build.USER + "@" + Build.HOST;
    }

    private static String getBootloaderVersion() {
        return Build.BOOTLOADER;
    }

    /**/
    public static String getScreenInfo(String a, Activity activity) {
        String screenresolution, screensize, screendpi = null;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;

        float xdpi = widthPixels / metrics.xdpi;
        float ydpi = heightPixels / metrics.ydpi;

        float x = (float) Math.pow(xdpi, 2);
        float y = (float) Math.pow(ydpi, 2);

        float screensizenofullscreen = (float) Math.sqrt(x + y);

        screenresolution = String.valueOf(widthPixels) + "x" + String.valueOf(heightPixels) + " pixels";
        screensize = String.format("%.1f", Float.valueOf(screensizenofullscreen)) + " inch";
        screendpi = String.valueOf(metrics.densityDpi) + " DPI";

        if (a.equalsIgnoreCase("resolution")) {
            a = screenresolution;
        } else if (a.equalsIgnoreCase("dpi")) {
            a = screendpi;
        } else if (a.equalsIgnoreCase("size")) {
            a = screensize;
        }

        return a;
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
                size = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                size = "XXXHDPI";
                break;

        }
        return size;
    }

    /**/
    public static String getBackCameraResolutionInMp() {
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

        return new DecimalFormat("#.#").format(maxResolution);
    }

    public static int getFrontCameraResolutionInMp() {
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

        return (((Collections.max(arrayListForWidth)) * (Collections.max(arrayListForHeight))) / 1024000);
    }

    /**/
    private static String getCPUManufacture(Context context) {
        String a = firstTwo(Build.BOARD);

        String devicesoc = null;
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset(context, "cpu.json"));

            String board = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (String.valueOf(Build.BOARD).toLowerCase().contains(jsonObject.getString("id"))) {
                    board = jsonObject.getString("name");
                    break;
                } else {
                    board = Build.BOARD;
                }
            }
            if (a.equalsIgnoreCase("ms") || a.equalsIgnoreCase("ap") || a.equalsIgnoreCase("sd")) {
                devicesoc = "Qualcomm Snapdragon " + board;
            } else if (a.equalsIgnoreCase("mt")) {
                devicesoc = "Mediatek " + board;
            } else if (a.equalsIgnoreCase("hi") || a.equalsIgnoreCase("ki")) {
                devicesoc = "Hi-Silicon Kirin " + board;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return devicesoc;
    }

    private static String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    public static String loadJSONFromAsset(Context context, String jsonFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(jsonFile);
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

    public static int getNumberOfCores() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            // Use saurabh64's answer
            return getNumCoresOldPhones();
        }
    }

    public static int getNumCoresOldPhones() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
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
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }

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

    /**/

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

    private static String getOSAndroidVersionSecurityPatch() {
        if (DeviceDetailUtil.isMarmellowAbove()) {
            return Build.VERSION.SECURITY_PATCH;
        } else {
            return "tidak ada, karena di bawah OS marshmello";
        }
    }

    /**/

    public static double getBatteryCapacity(Context context) {
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

    private static int getBatteryPercent(Activity activity) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = activity.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        return level;
    }

    private static double getExactBattery(Activity activity) {
        Log.d("Debug", "In getExactBattery()");
        float batteryPercentage = getBatteryPercent(activity);
        Log.d("Debug", "batteryPercentage = " + batteryPercentage);
        double totalCapacity = getBatteryCapacity(activity);
        Log.d("Debug", "totalCapacity = " + totalCapacity);
        double currLevel = (totalCapacity * (double) batteryPercentage) / 100.0d;
        Log.d("Debug", "currLevel = " + currLevel);
        return currLevel;

    }

    /**/

    @SuppressLint("MissingPermission")
    private static String getImeiNumber(Activity activity) {
        TelephonyManager mTelephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint({"NewApi", "LocalSuppress"})
        String imei = mTelephonyManager.getImei(0) + ", "
                + mTelephonyManager.getImei(1) + ", "
                + mTelephonyManager.getImei(2) + ", "
                + mTelephonyManager.getImei(3) + ", "
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

    /**/

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

    public static boolean getMobileDataState(Activity activity) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod) {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error getting mobile data state", ex);
        }

        return false;
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


    /*private static String getPhoneModel2(Context context) {
        String model = null;
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset2(context));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (String.valueOf(Build.MODEL).equalsIgnoreCase(jsonObject.getString("model"))) {
                    model = jsonObject.getString("marketing-name");
                    break;
                } else {
                    model = Build.MODEL;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static String loadJSONFromAsset2(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open("phone.json");
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
                    if (params.length == 1 && params[0].getName().equalsIgnoreCase("int")) {
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

    public static float getCameraResolutionInMp(int a) {
        int cameraid = 1;
        int cameraman = 1;

        try {
            if (a == 0) {
                cameraid = 0;
                cameraman = Camera.CameraInfo.CAMERA_FACING_BACK;
            } else if (a == 1) {
                cameraid = 1;
                cameraman = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            float maxResolution = -1;
            long pixelCount = -1;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraid, cameraInfo);
            if (cameraInfo.facing == cameraman) {
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

    public static String checkBattery(Activity activity) {
        final String[] health = {null};
        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
                if (status == BatteryManager.BATTERY_HEALTH_COLD) {
                    health[0] = "COLD";
                    //textview.setText("Battery health = Cold");
                }
                if (status == BatteryManager.BATTERY_HEALTH_DEAD) {
                    health[0] = "DEAD";

                    //textview.setText("Battery health = Dead");
                }
                if (status == BatteryManager.BATTERY_HEALTH_GOOD) {
                    health[0] = "GOOD";
                    //textview.setText("Battery health = Good");
                }
                if (status == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                    health[0] = "OVERHEAT";
                    //textview.setText("Battery health = Over Heat");
                }
                if (status == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                    health[0] = "OVER VOLTAGE";
                    //textview.setText("Battery health = Over Voltage");
                }
                if (status == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
                    health[0] = "UNKNOWN";
                    //textview.setText("Battery health = Unknown");
                }
                if (status == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                    health[0] = "UNSPECIFIED FAILURE";
                    //textview.setText("Battery health = Unspecified failure");
                }

            }
        };
        activity.registerReceiver(broadcastreceiver, intentfilter);
        return health[0];
    }

    private static float getFullScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        int width = size.x;
        int height = size.y;

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

        return screensize;
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

    public static String getTotalRAM(Activity activity) {
        String totalMemory;

        ActivityManager actManager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        totalMemory = String.valueOf(memInfo.totalMem);

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try

        {
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


        } catch (
                IOException ex)

        {
            ex.printStackTrace();
        } finally

        {
            // Streams.close(reader);
        }

        totalMemory = lastValue;

        //scanBtn.setText("" +"/"+lastValue );
        return totalMemory;
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

    public static long TotalMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Total = ((long) statFs.getBlockCount() * (long) statFs.getBlockSize());
        return Total;
    }

    public static long FreeMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long Free = (statFs.getAvailableBlocks() * (long) statFs.getBlockSize());
        return Free;
    }

    public static long BusyMemory() {
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

    private static double getBatteryCapacity2(Activity activity) {
        double battCapacity = 0.0d;
        Object mPowerProfile_ = null;

        Log.d("Debug", "in getBatteryCapacity()");

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(activity);
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

    public static void getGPUInfo() {
        GL10 gl = null;
        Log.d("GL", "GL_RENDERER = " + gl.glGetString(GL10.GL_RENDERER));
        Log.d("GL", "GL_VENDOR = " + gl.glGetString(GL10.GL_VENDOR));
        Log.d("GL", "GL_VERSION = " + gl.glGetString(GL10.GL_VERSION));
        Log.i("GL", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));

    }

    public static void setMobileDataState(boolean mobileDataEnabled, Activity activity) {
        try {
            TelephonyManager telephonyService = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }*/

}
