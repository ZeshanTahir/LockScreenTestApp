package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.preference.Preferences;
import com.sdsmdg.tastytoast.TastyToast;

public class PinActivity extends AppCompatActivity {

    private static final String TAG = PinActivity.class.getSimpleName();
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private Button mSavePinButton;
    private android.widget.ImageView mLockImageView;
    private Context mContext;
    private String mPin = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin);

        mContext = this;

        init();
        onClicks();
    }

    private void init(){
        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);
        mSavePinButton = findViewById(R.id.pin_save_btn);
        mLockImageView = findViewById(R.id.pinLock_imageView);
        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);

        mPinLockView.setPinLength(4);
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }

    private void onClicks(){
        mSavePinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Preferences(mContext).saveNewPin(mPin);
                TastyToast.makeText(mContext, "Pin Saved", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
            }
        });
    }

    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {
            Log.d(TAG, "Pin complete: " + pin);
            String savedPin = new Preferences(mContext).getPin();
            mPin = pin;
            if (savedPin.equals(mPin)){
                mLockImageView.setImageResource(R.drawable.correct_lock);
                correctPin();
            }else if (savedPin.equals("")){
                mSavePinButton.setVisibility(View.VISIBLE);
            }else {
                mLockImageView.setImageResource(R.drawable.wrong_lock);
            }
        }

        @Override
        public void onEmpty() {
            Log.d(TAG, "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            Log.d(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };

    private void correctPin(){
        startActivity(new Intent(PinActivity.this, LockedAppsActivity.class));
        finish();
    }
}
