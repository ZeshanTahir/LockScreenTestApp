package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.service.MyService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class AppOpenActivity extends AppCompatActivity {

    private static final String TAG = AppOpenActivity.class.getSimpleName();
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_open);

//        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
//        List<RunningAppProcessInfo> procs = am.getRunningAppProcesses();
//        for (RunningAppProcessInfo info : procs) {
//            for (String pkg : info.pkgList) {
//                Log.d(TAG, "---------------************-------------------");
//                Log.d(TAG, "Installed package :" + pkg);
//                Log.d(TAG, "---------------************-------------------");
//                Log.d(TAG, "---------------************-------------------");
//                Log.d(TAG, "---------------************-------------------");
//            }
//        }

        context = this;

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


//        while (i.hasNext()) {
//            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
//            try {
//                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
//                        info.processName, PackageManager.GET_META_DATA));
//                Log.w("LABEL", c.toString());
//            } catch (Exception e) {
//                // Name Not FOund Exception
//            }
//        }
//        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        final List<ActivityManager.RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
//
//        for (int i = 0; i < recentTasks.size(); i++)
//        {
//            Log.i("Executed app", "Application executed : " +recentTasks.get(i).baseActivity.toShortString()+ "\t\t ID: "+recentTasks.get(i).id+"");
//        }
//
//        ActivityManager actvityManager = (ActivityManager)
//                this.getSystemService( ACTIVITY_SERVICE );
//        List<RunningAppProcessInfo> procInfos = actvityManager.getRunningAppProcesses();
//        for (int i = 0; i < procInfos.size(); i++)
//        {
//            Log.i("Executed app", "Application executed : " +procInfos.get(i).toString()+ "\t\t ID: "+recentTasks.get(i).id+"");
//        }
//        Log.d(TAG, getActiveApps(this));

//        startService(new Intent(AppOpenActivity.this, MyService.class));
        loadProcessInfo();

        PackageManager pm = context.getPackageManager();
// Get the output of running "ps" in a shell.
// This uses libsuperuser: https://github.com/Chainfire/libsuperuser
// To add this to your project: compile 'eu.chainfire:libsuperuser:1.0.0.+'
        List<String> stdout = Shell.SH.run("ps");
        List<String> packages = new ArrayList<>();
        for (String line : stdout) {
            // Get the process-name. It is the last column.
            String[] arr = line.split("\\s+");
            String processName = arr[arr.length - 1].split(":")[0];
            packages.add(processName);
        }

// Get a list of all installed apps on the device.
        List<ApplicationInfo> apps = pm.getInstalledApplications(0);

// Remove apps which are not running.
        for (Iterator<ApplicationInfo> it = apps.iterator(); it.hasNext(); ) {
            if (!packages.contains(it.next().packageName)) {
                it.remove();
            }
        }

        for (ApplicationInfo app : apps) {
            String appName = app.loadLabel(pm).toString();
            int uid = app.uid;
            long ulBytes = TrafficStats.getUidTxBytes(uid);
            long dlBytes = TrafficStats.getUidRxBytes(uid);
            /* do your stuff */


        }
    }

    private void loadProcessInfo() {

        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningProcesses = manager.getRunningAppProcesses();

        for (RunningAppProcessInfo info : runningProcesses){
            Log.i("Background apps", "Application executed : " + info.processName);
        }
    }


    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static String getActiveApps(Context context) {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        String value = "";//u.dateStamp(); // basic date stamp
        value += "---------------------------------\n";
        value += "Active Apps\n";
        value += "=================================\n";

        for (ApplicationInfo packageInfo : packages) {

            //system apps! get out
            if (!isSTOPPED(packageInfo) && !isSYSTEM(packageInfo)) {

                value += getApplicationLabel(context, packageInfo.packageName) + "\n" + packageInfo.packageName  + "\n-----------------------\n";

            }
        }

        return value;

        //result on my emulator

    /* 2 Ekim 2017 Pazartesi 14:35:17
    ---------------------------------
    Active Apps
    =================================
    SystemSetting
    com.xyz.systemsetting
    -----------------------
    myMail
    com.my.mail
    -----------------------
    X-plore
    com.lonelycatgames.Xplore
    -----------------------
    Renotify
    com.liamlang.renotify
    -----------------------
    Mail Box
    com.mailbox.email
    -----------------------   */


    }

    private static boolean isSTOPPED(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0);
    }

    private static boolean isSYSTEM(ApplicationInfo pkgInfo) {

        return ((pkgInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static String getApplicationLabel(Context context, String packageName) {

        PackageManager        packageManager = context.getPackageManager();
        List<ApplicationInfo> packages       = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        String                label          = null;

        for (int i = 0; i < packages.size(); i++) {

            ApplicationInfo temp = packages.get(i);

            if (temp.packageName.equals(packageName))
                label = packageManager.getApplicationLabel(temp).toString();
        }

        return label;
    }
}
