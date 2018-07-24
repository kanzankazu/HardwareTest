package com.kanzankazu.hardwaretest.ui.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import app.beelabs.com.codebase.base.BaseActivity;

/**
 * Created by glenn on 1/26/18.
 */

public class LocalBaseActivity extends BaseActivity {

    public void showSnekbar(View root, String content) {
        Snackbar snekbar = Snackbar.make(root, content, Snackbar.LENGTH_SHORT);
        snekbar.show();
    }

    public void showSnekbar(View root, String content, View.OnClickListener listener) {
        Snackbar snekbar = Snackbar.make(root, content, Snackbar.LENGTH_SHORT);
        snekbar.setAction(" Konek Kembali", listener);
        snekbar.show();
    }

    public void showSnekbar(View root, String content, int textColorid, int colorid) {
        Snackbar snekbar = Snackbar.make(root, content, Snackbar.LENGTH_SHORT);
        TextView snackbarTextView = (TextView) snekbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarTextView.setTextColor(ContextCompat.getColor(this, textColorid));
        snekbar.getView().setBackgroundColor(ContextCompat.getColor(this, colorid));
        snekbar.show();
    }
}
