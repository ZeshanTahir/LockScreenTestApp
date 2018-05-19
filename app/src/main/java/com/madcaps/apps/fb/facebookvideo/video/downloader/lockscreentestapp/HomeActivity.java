package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.app.KeyguardManager;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sdsmdg.tastytoast.TastyToast;

public class HomeActivity extends AppCompatActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();
    private Button mFingerPrintButton, mPatternButton, mPinButton;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        init();
        onClicks();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {


            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            if (!fingerprintManager.isHardwareDetected()) {
                mFingerPrintButton.setVisibility(View.GONE);
            }
        }
    }

    private void init(){
        mFingerPrintButton = findViewById(R.id.fingerPrint_btn);
        mPatternButton = findViewById(R.id.patternLock_btn);
        mPinButton = findViewById(R.id.pinLock_btn);
    }

    private void onClicks(){
        mFingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, FingerPrintActivity.class));
                finish();
            }
        });

        mPatternButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PatternActivity.class));
                finish();
            }
        });

        mPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PinActivity.class));
                finish();
            }
        });
    }

    /*android:theme="@style/Base.Theme.AppCompat.Light"*/
}
