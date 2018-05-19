package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.preference.Preferences;

import java.util.List;

public class PatternActivity extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private Button mRegisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        mPatternLockView = findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setInStealthMode(true);

        mRegisterButton = findViewById(R.id.register_btn);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatternActivity.this, AppOpenActivity.class));
            }
        });
    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d(getClass().getName(), "Pattern progress: " +
                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            Log.d(getClass().getName(), "Pattern complete: " +
                    PatternLockUtils.patternToString(mPatternLockView, pattern));
            String p = PatternLockUtils.patternToString(mPatternLockView, pattern);
//            Toast.makeText(getApplicationContext(), p, Toast.LENGTH_SHORT).show();
            if (new Preferences(PatternActivity.this).getPattern().equals(p)){
//                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PatternActivity.this, LockedAppsActivity.class));
                finish();
            }else {
                Toast.makeText(getApplicationContext(), "Try again!", Toast.LENGTH_SHORT).show();
            }
            mPatternLockView.clearPattern();
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
            mPatternLockView.clearPattern();
        }
    };
}
