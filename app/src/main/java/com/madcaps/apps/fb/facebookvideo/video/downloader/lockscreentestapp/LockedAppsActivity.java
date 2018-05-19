package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.adapter.LockedApplicationsAdapter;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.database.DBHelper;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class LockedAppsActivity extends AppCompatActivity {

    private DBHelper mDbHelper;
    public final String TAG = LockedAppsActivity.class.getSimpleName();
    private AppInfo mAppInfo;
    private List<AppInfo> mLockedAppsList = new ArrayList<>();
    private List<AppInfo> mAppsList = new ArrayList<>();
    private RecyclerView mLockedAppRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private LockedApplicationsAdapter mLockedAppAdapter;
    private FloatingActionButton mFab;
    private Context mContext;
    private List<ApplicationInfo> packages;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_apps);

        mContext = this;
        init();
        onClicks();

        new getLockedAppsListAsync().execute();
        mLockedAppAdapter = new LockedApplicationsAdapter(mContext, (ArrayList<AppInfo>) mLockedAppsList);
        mLockedAppRecycler.setAdapter(mLockedAppAdapter);
    }

    private void init(){
        mLockedAppRecycler = findViewById(R.id.lockedApps_recycler);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLockedAppRecycler.setLayoutManager(mLayoutManager);
        mFab = findViewById(R.id.fab);
    }

    private void onClicks(){
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LockedAppsActivity.this, AllAppsActivity.class));
                finish();
            }
        });
    }

    private class getLockedAppsListAsync extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(LockedAppsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mLockedAppRecycler.setVisibility(View.GONE);
            progressDialog.setMessage("\tloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            mLockedAppRecycler.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String appNames = getLockedAppsName();
            mDbHelper = new DBHelper(mContext);
            pm = getPackageManager();
            //get a list of installed apps.
            packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {

                if (appNames.contains(packageInfo.loadLabel(pm).toString())) {
                    Log.i(TAG, "got one");
                    if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null){
                        mAppInfo = new AppInfo(packageInfo.loadLabel(pm).toString(),
                                packageInfo.loadIcon(pm),
                                packageInfo.packageName,
                                packageInfo.sourceDir,
                                pm.getLaunchIntentForPackage(packageInfo.packageName),
                                AppInfo.APP_STATUS_UNLOCKED);

                        mLockedAppsList.add(mAppInfo);

                        Log.d(TAG, "---------------************-------------------");
                        Log.d(TAG, "App name :" + packageInfo.loadLabel(pm).toString());
                        Log.d(TAG, "App icon : " + packageInfo.loadIcon(pm));
                        Log.d(TAG, "Installed package :" + packageInfo.packageName);
                        Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
                        Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
                        Log.d(TAG, "---------------************-------------------");
                        Log.d(TAG, "---------------************-------------------");
                        Log.d(TAG, "---------------************-------------------");
                    }
                }
            }
            return null;
        }
    }

    private String getLockedAppsName(){
        mDbHelper = new DBHelper(mContext);
        List<AppInfo> appInfoList = mDbHelper.getLockedApps();
        String appNames = "";
        for (int i=0; i<appInfoList.size(); i++){
            appNames += appInfoList.get(i).getmAppName() + "__";
        }
        return appNames;
    }
}
