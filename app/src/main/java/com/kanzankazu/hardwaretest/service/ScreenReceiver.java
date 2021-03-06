package com.kanzankazu.hardwaretest.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
    public static boolean wasScreenOn;

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = true;
        } else if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_ON)) {
            //wasScreenOn = true;
        } else if (intent.getAction().equalsIgnoreCase(Intent.ACTION_USER_PRESENT)) {
            //wasScreenOn = true;
        }
    }

    public static boolean getScreenStatus() {
        return wasScreenOn;
    }
}
