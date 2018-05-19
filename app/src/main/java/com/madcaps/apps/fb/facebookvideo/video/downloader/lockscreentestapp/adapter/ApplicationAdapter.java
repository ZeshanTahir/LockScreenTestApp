package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.R;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.database.DBHelper;
import com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.model.AppInfo;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder>{

    private DBHelper mDBHelper;
    private Context mContext;
    private ArrayList<AppInfo> mAppList;
    private boolean locked = false;

    public ApplicationAdapter(Context context, ArrayList<AppInfo> appList) {
        mContext = context;
        mAppList = appList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout mParentLayout;
        ImageView mAppIconImageView, mAppLockImageView;
        TextView mAppNameTextView;

        ViewHolder(View itemView) {
            super(itemView);

            mParentLayout = itemView.findViewById(R.id.parent_layout);
            mAppIconImageView = itemView.findViewById(R.id.item_app_icon_img);
            mAppLockImageView = itemView.findViewById(R.id.item_lock_img);
            mAppNameTextView = itemView.findViewById(R.id.item_app_name_txt);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_app_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final AppInfo appInfo = mAppList.get(position);
        Glide.with(mContext).load(appInfo.getmAppIcon()).into(holder.mAppIconImageView);
        holder.mAppNameTextView.setText(appInfo.getmAppName());

        mDBHelper = new DBHelper(mContext);

        holder.mAppLockImageView.setImageResource(R.drawable.unlock);

        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast(appInfo.getmAppName() + " locked", TastyToast.SUCCESS);
                Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.lock)).into(holder.mAppLockImageView);
                appInfo.setmAppStatus(AppInfo.APP_STATUS_LOCKED);
                mDBHelper.LockApp(appInfo);
                mAppList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mAppList.size());
//                if (locked ){
//                    locked = false;
//
//                }else {
//                    locked = true;
//                    showToast(appInfo.getmAppName() + " unlocked", TastyToast.WARNING);
//                    Glide.with(mContext).load(mContext.getResources().getDrawable(R.drawable.unlock)).into(holder.mAppLockImageView);
//                    appInfo.setmAppStatus(AppInfo.APP_STATUS_UNLOCKED);
//                    mDBHelper.unlockApp(appInfo.getmAppName(), mContext);
//                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mAppList.size();
    }

    private void showToast(String message, int mode){
        TastyToast.makeText(mContext, message, Toast.LENGTH_SHORT, mode).show();
    }
}


