package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.PinActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {

    private static Timer timer = new Timer();
    public Boolean userAuth = false;
    private Context ctx;
    public String pActivity="";

    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public void onCreate()
    {
        super.onCreate();
        ctx = this;
        startService();
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0, 500);
    }

    private class mainTask extends TimerTask
    {
        public void run()
        {
            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped ...", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("HandlerLeak")
    private final Handler toastHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            String activityOnTop;
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            activityOnTop=ar.topActivity.getClassName();

            if(activityOnTop.equals("com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.LockedAppsActivity"))
            {
                pActivity = activityOnTop;
            }
            else
            {
                if(activityOnTop.equals(pActivity) || activityOnTop.equals("com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.PatternActivity"))
                {
                    Toast.makeText(getApplicationContext(), activityOnTop, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "about to launch", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(MyService.this, PinActivity.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
//                    Toast.makeText(MyService.this, pActivity, Toast.LENGTH_SHORT).show();
//                    pActivity = activityOnTop;

                }
            }


         /*if(!activityOnTop.equals(pActivity))
         {
          if(!activityOnTop.equals("com.javacodegeeks.android.androidserviceexample.LockScreen"))
          {
           pActivity =activityOnTop;
          }
          else
          {
           Intent i = new Intent(MyService.this, LockScreen.class);
              i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
              Toast.makeText(MyService.this, pActivity, 1).show();

          }
         }
         else
         {
          Toast.makeText(MyService.this, "Hi", 1).show();
         }
         */


        }
    };
}
