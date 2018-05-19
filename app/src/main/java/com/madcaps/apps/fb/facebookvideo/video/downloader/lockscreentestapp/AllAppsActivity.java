package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.adapter.ApplicationAdapter;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.database.DBHelper;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AllAppsActivity extends AppCompatActivity {

    public final String TAG = AllAppsActivity.class.getSimpleName();
    private AppInfo mAppInfo;
    private List<AppInfo> mAppInfoList = new ArrayList<>();
    private RecyclerView mAppRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private ApplicationAdapter mAppAdapter;
    private Context mContext;
    private List<ApplicationInfo> packages;
    private PackageManager pm;
    private DBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_apps);

        mContext = this;
        init();

        new getAllAppsListAsync().execute();
        mAppAdapter = new ApplicationAdapter(mContext, (ArrayList<AppInfo>) mAppInfoList);
        mAppRecycler.setAdapter(mAppAdapter);
    }

    private void init(){
        mAppRecycler = findViewById(R.id.app_list_recycler);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAppRecycler.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        new getAllAppsListAsync().execute();
//        mAppAdapter = new ApplicationAdapter(mContext, (ArrayList<AppInfo>) mAppInfoList);
//        mAppRecycler.setAdapter(mAppAdapter);
//    }

    private class getAllAppsListAsync extends AsyncTask<Void, Void, Void>{
        ProgressDialog progressDialog = new ProgressDialog(AllAppsActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mAppRecycler.setVisibility(View.GONE);
            progressDialog.setMessage("\tloading...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            mAppRecycler.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String appNames = getLockedAppsName();
            mDBHelper = new DBHelper(mContext);
            pm = getPackageManager();
            //get a list of installed apps.
            packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo packageInfo : packages) {

                if (appNames.contains(packageInfo.loadLabel(pm).toString())) {
                    Log.i(TAG, "got one");
                }else if (pm.getLaunchIntentForPackage(packageInfo.packageName) != null){
                    mAppInfo = new AppInfo(packageInfo.loadLabel(pm).toString(),
                            packageInfo.loadIcon(pm),
                            packageInfo.packageName,
                            packageInfo.sourceDir,
                            pm.getLaunchIntentForPackage(packageInfo.packageName),
                            AppInfo.APP_STATUS_UNLOCKED);

                    mAppInfoList.add(mAppInfo);

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
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AllAppsActivity.this, LockedAppsActivity.class));
        finish();
    }

    private String getLockedAppsName(){
        mDBHelper = new DBHelper(mContext);
        List<AppInfo> appInfoList = mDBHelper.getLockedApps();
        String appNames = "";
        for (int i=0; i<appInfoList.size(); i++){
            appNames += appInfoList.get(i).getmAppName() + "__";
        }
        return appNames;
    }
}
