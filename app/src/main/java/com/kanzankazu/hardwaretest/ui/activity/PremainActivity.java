package com.kanzankazu.hardwaretest.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kanzankazu.hardwaretest.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

public class PremainActivity extends AppCompatActivity {
    private SlidrInterface slidr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premain);
        slidr = Slidr.attach(this);
        slidr.unlock();
    }
}
