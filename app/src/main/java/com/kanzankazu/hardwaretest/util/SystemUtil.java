package com.kanzankazu.hardwaretest.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Calendar;
import java.util.List;

public class SystemUtil {

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    public static String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    public static void textCaps(EditText editText) {
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        /*
         * If no view is focused, an NPE will be thrown
         *
         * Maxim Dmitriev
         */
        if (focusedView != null) {
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static void showKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static String stringToStars(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            sb.append('*');
        }
        return sb.toString();
    }

    public static Integer stringCount(String s) {
        return s.length();
    }

    public static String stringSpaceToPlus(String s) {
        return s.replace(" ", "+");
    }

    public static void visibile(Context context, View view, int visible, int anim) {
        if (view.getVisibility() != visible) {
            view.setVisibility(visible);
            Animation animation = AnimationUtils.loadAnimation(context, anim);
            view.startAnimation(animation);
        }
    }

    public static void errorET(EditText editText, CharSequence stringerror) {
        editText.setError(stringerror);
        editText.requestFocus();
    }

    public static boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    public static int roundDouble(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    public static void etReqFocus(Activity activity, String s, EditText editText) {
        editText.setError(s);
        editText.requestFocus();
        SystemUtil.showKeyBoard(activity);
    }

    public static void etReqFocus(Activity activity, EditText editText) {
        editText.requestFocus();
        SystemUtil.showKeyBoard(activity);
    }

    /*public static void getEmailAccout(Activity activity, int REQUEST_CODE) {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        activity.startActivityForResult(googlePicker, REQUEST_CODE);
    }

    public static void changeColText(int androidRed, TextView textView) {
        if (DeviceDetailUtil.isKitkatBelow()) {
            textView.setTextColor(App.getContext().getResources().getColor(androidRed));
        } else {
            textView.setTextColor(ContextCompat.getColor(App.getContext(), androidRed));
        }
    }*/
}
