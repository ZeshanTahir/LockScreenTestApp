package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

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

public class RegisterActivity extends AppCompatActivity {

    private PatternLockView mPatternLockView;
    private Button mSaveButton;
    private String patternStr = "", patternStrConfirm = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPatternLockView = (PatternLockView) findViewById(R.id.save_pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);

        mSaveButton = findViewById(R.id.save_btn);
//        mSaveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (! patternStr.equals("") || patternStr.length() < 3) {
//                    if (new Preferences(RegisterActivity.this).getPattern().equals(patternStr)) {
//                        Toast.makeText(getApplicationContext(), "Already Saved.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        new Preferences(RegisterActivity.this).saveNewPattern(patternStr);
//                        Toast.makeText(getApplicationContext(), "Pattern Saved.", Toast.LENGTH_SHORT).show();
//                        onBackPressed();
//                        finish();
//                    }
//                }else {
//                    Toast.makeText(getApplicationContext(), "Please connect 3 or more dots to provide some pattern.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
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

            if (patternStr.equals("") && pattern.size()>=3){
                patternStr = PatternLockUtils.patternToString(mPatternLockView, pattern);
                mSaveButton.setText("Confirm Pattern");
                mSaveButton.setEnabled(false);
                mPatternLockView.clearPattern();
            }else if (!patternStr.equals("") && patternStrConfirm.equals("") && pattern.size()>=3){
                patternStrConfirm = PatternLockUtils.patternToString(mPatternLockView, pattern);
                mSaveButton.setText("Save");
                mSaveButton.setEnabled(false);
                mPatternLockView.clearPattern();
                if (!patternStr.equals("") && !patternStrConfirm.equals("")) {
                    if (patternStr.equals(patternStrConfirm)) {
                        mSaveButton.setEnabled(true);
                        mSaveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Preferences(RegisterActivity.this).saveNewPattern(patternStr);
                                Toast.makeText(getApplicationContext(), "Pattern Saved.", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                                finish();
                            }
                        });
                    } else {
                        mSaveButton.setText("Pattern was not confirmed. Try again!");
                        mSaveButton.setEnabled(false);patternStr = "";
                        patternStrConfirm = "";
                        mPatternLockView.clearPattern();
                    }
                }
            }else if (pattern.size() < 3){
                mSaveButton.setText("Please connect 3 or more dots to provide some pattern.");
                mSaveButton.setEnabled(false);
                patternStr = "";
                patternStrConfirm = "";
                mPatternLockView.clearPattern();
            }else {
                mSaveButton.setText("Try again!");
                mPatternLockView.clearPattern();
                mSaveButton.setEnabled(false);
                patternStr = "";
                patternStrConfirm = "";
            }
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
            mPatternLockView.clearPattern();
        }
    };
}
