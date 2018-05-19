package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    SharedPreferences sharedPreferences;
    Context context;
    public static final String PREFERENCE = "PatternAppPreference";
    public static final String NEW_PATTERN = "new_pattern";
    public static final String NEW_PIN = "new_pin";

    public Preferences(Context context) {
        this.context = context;
    }

    public void saveNewPattern(String pattern){
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NEW_PATTERN, pattern);
        editor.apply();
    };

    public String getPattern(){
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NEW_PATTERN, "");
    }

    public void saveNewPin(String pin){
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NEW_PIN, pin);
        editor.apply();
    };

    public String getPin(){
        sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(NEW_PIN, "");
    }
}
