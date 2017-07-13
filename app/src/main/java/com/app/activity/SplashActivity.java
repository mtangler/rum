package com.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

import com.app.core.Globals;
import com.app.core.LogEx;
import com.app.core.Storage;
import com.app.rum.R;

import trikita.log.Log;

public class SplashActivity extends Activity {

    int SPLASH_TIME = 2700;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.ac_splash);

        initHandlers();

        new CountDownTimer(SPLASH_TIME, 200) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Intent t = new Intent(SplashActivity.this, TestActivity.class);
                startActivity(t);
                finish();
            }

        }.start();

    }

    private void initHandlers() {

        Storage.setContext(getApplicationContext());
        Globals.app = Storage.get();

        if (Globals.app.log_enabled) {
            LogEx.PRINT_STACK = true;
            Log.usePrinter(Log.ANDROID, true);
        } else {
            LogEx.PRINT_STACK = false;
            Log.usePrinter(Log.ANDROID, false);
        }
    }
}
