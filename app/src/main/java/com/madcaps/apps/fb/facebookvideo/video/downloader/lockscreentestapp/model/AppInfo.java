package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.model;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class AppInfo {

    public static final int APP_STATUS_UNLOCKED = 0;
    public static final int APP_STATUS_LOCKED = 1;
    private int mAppStatus;
    private String mAppName, mAppPackage, mAppSourceDir;
    private Drawable mAppIcon;
    private Intent mAppLaunchActivity;

    public AppInfo() {
    }

    public AppInfo(int mAppStatus, String mAppName, String mAppPackage, Drawable mAppIcon) {
        this.mAppStatus = mAppStatus;
        this.mAppName = mAppName;
        this.mAppPackage = mAppPackage;
        this.mAppIcon = mAppIcon;
    }

    public AppInfo(String mAppName, Drawable mAppIcon, String mAppPackage, String mAppSourceDir, Intent mAppLaunchActivity, int mAppStatus) {
        this.mAppName = mAppName;
        this.mAppIcon = mAppIcon;
        this.mAppPackage = mAppPackage;
        this.mAppSourceDir = mAppSourceDir;
        this.mAppLaunchActivity = mAppLaunchActivity;
        this.mAppStatus = mAppStatus;
    }

    public AppInfo(String mAppName, String mAppPackage, int mAppStatus) {
        this.mAppName = mAppName;
        this.mAppPackage = mAppPackage;
        this.mAppStatus = mAppStatus;
    }

    public int getmAppStatus() {
        return mAppStatus;
    }

    public void setmAppStatus(int mAppStatus) {
        this.mAppStatus = mAppStatus;
    }

    public String getmAppName() {
        return mAppName;
    }

    public void setmAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public Drawable getmAppIcon() {
        return mAppIcon;
    }

    public void setmAppIcon(Drawable mAppIcon) {
        this.mAppIcon = mAppIcon;
    }

    public String getmAppPackage() {
        return mAppPackage;
    }

    public void setmAppPackage(String mAppPackage) {
        this.mAppPackage = mAppPackage;
    }

    public String getmAppSourceDir() {
        return mAppSourceDir;
    }

    public void setmAppSourceDir(String mAppSourceDir) {
        this.mAppSourceDir = mAppSourceDir;
    }

    public Intent getmAppLaunchActivity() {
        return mAppLaunchActivity;
    }

    public void setmAppLaunchActivity(Intent mAppLaunchActivity) {
        this.mAppLaunchActivity = mAppLaunchActivity;
    }
}
