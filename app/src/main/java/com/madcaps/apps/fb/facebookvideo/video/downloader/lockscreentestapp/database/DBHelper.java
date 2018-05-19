package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.model.AppInfo;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "lockApp.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "AppLockList";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_APP_NAME = "appName";
    public static final String COLUMN_PACKAGE = "appPackage";
    public static final String COLUMN_STATUS = "lockStatus";

    private Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private static final String CREATE_TABLE_QUERY =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_APP_NAME + " TEXT, " +
                    COLUMN_PACKAGE + " TEXT, " +
                    COLUMN_STATUS + " TEXT" + ")";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void LockApp(AppInfo appInfo){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APP_NAME, appInfo.getmAppName());
        values.put(COLUMN_PACKAGE, appInfo.getmAppPackage());
        values.put(COLUMN_STATUS, appInfo.getmAppStatus());
        database.insert(TABLE_NAME, null, values);
        database.close();
//        TastyToast.makeText(mContext, appInfo.getmAppName() + " LOCKED in DATABASE!", Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
        Log.i(TAG, appInfo.getmAppName() + " saved in DB");
        Log.i(TAG, appInfo.getmAppName() + " --" +  appInfo.getmAppPackage() +  "--" +  appInfo.getmAppStatus());
    }

    public void unlockApp(String appName, Context context){
        SQLiteDatabase database = this.getWritableDatabase();

        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_APP_NAME + " = '" + appName + "'" );
//        TastyToast.makeText(context, appName + " UNLOCKED in DATABASE!", Toast.LENGTH_LONG, TastyToast.WARNING).show();
        Log.i(TAG, appName + " deleted from DB");
    }

    public List<AppInfo> getLockedApps(){
        String query = "SELECT DISTINCT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        AppInfo appInfo;
        List<AppInfo> lockedAppsList = new ArrayList<>();
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()){

            do {
                appInfo = new AppInfo();
                appInfo.setmAppName(cursor.getString(cursor.getColumnIndex(COLUMN_APP_NAME)));
                appInfo.setmAppPackage(cursor.getString(cursor.getColumnIndex(COLUMN_PACKAGE)));
                lockedAppsList.add(appInfo);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return lockedAppsList;
    }
}
