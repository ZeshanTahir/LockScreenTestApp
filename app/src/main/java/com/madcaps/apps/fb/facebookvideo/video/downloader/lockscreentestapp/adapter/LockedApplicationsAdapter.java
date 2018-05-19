package com.madcaps.apps.fb.facebookvideo.video.downloader.lockscreentestapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

public class LockedApplicationsAdapter extends RecyclerView.Adapter<LockedApplicationsAdapter.ViewHolder>{

private DBHelper mDBHelper;
private Context mContext;
private ArrayList<AppInfo> mAppList = new ArrayList<>();
private boolean locked = false;

public LockedApplicationsAdapter(Context context, ArrayList<AppInfo> appList) {
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
    public LockedApplicationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lock_app_item, parent, false);
        return new LockedApplicationsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final LockedApplicationsAdapter.ViewHolder holder, final int position) {
        final AppInfo appInfo = mAppList.get(position);

        mDBHelper = new DBHelper(mContext);
        Glide.with(mContext).load(appInfo.getmAppIcon()).into(holder.mAppIconImageView);
        holder.mAppNameTextView.setText(appInfo.getmAppName());
        holder.mAppLockImageView.setImageResource(R.drawable.lock);
        holder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showToast(appInfo.getmAppName() + " unlocked", TastyToast.WARNING);
                appInfo.setmAppStatus(AppInfo.APP_STATUS_LOCKED);
                mDBHelper.unlockApp(appInfo.getmAppName(), mContext);
                mAppList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mAppList.size());
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