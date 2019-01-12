package com.kieronquinn.app.springboardexample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.kieronquinn.library.amazfitcommunication.Transporter;

import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;

public class SpringboardPage extends AbstractPlugin {


    private static final String TAG = "SergioMatadodeBug";

    private Context mContext;
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;
    private Transporter transporter;

    @Override
    public View getView(Context paramContext) {
        Log.d(TAG, "getView()" + paramContext.getPackageName());
        this.mContext = paramContext;
        Log.d(TAG, "context" + this.mContext.getClass().getName());

        this.mView = LayoutInflater.from(paramContext).inflate(R.layout.widget_blank, null);
        View container = this.mView.findViewById(R.id.container);

        container.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Toast.makeText(mContext, "Clicked!", Toast.LENGTH_LONG).show();
                sendActionToTransporter();
            }
        });
        return this.mView;
    }

    //Return the icon for this page, used when the page is disabled in the app list. In this case, the launcher icon is used
    @Override
    public Bitmap getWidgetIcon(Context paramContext) {
        return ((BitmapDrawable) this.mContext.getResources().getDrawable(R.mipmap.ic_launcher)).getBitmap();
    }

    //Return the launcher intent for this page. This might be used for the launcher as well when the page is disabled?
    @Override
    public Intent getWidgetIntent() {
        Intent localIntent = new Intent();
        return localIntent;
    }

    //Return the title for this page, used when the page is disabled in the app list. In this case, the app name is used
    @Override
    public String getWidgetTitle(Context paramContext) {
        return this.mContext.getResources().getString(R.string.app_name);
    }

    public void sendActionToTransporter() {
        if (mContext == null) {
            transporter = Transporter.get(this.mContext, "com.android.camera");
        } else {
            transporter = Transporter.get(mContext, "com.android.camera");
        }

        Log.d(TAG, "gerou transporter");
        transporter.addChannelListener(new Transporter.ChannelListener() {
            @Override
            public void onChannelChanged(boolean ready) {
                if (ready) {
                    transporter.send("vol_up");
                    Toast.makeText(mContext, "Gabriel!", Toast.LENGTH_LONG).show();
                }
            }
        });
        transporter.connectTransportService();
    }


    //Called when the page is shown
    @Override
    public void onActive(Bundle paramBundle) {
        super.onActive(paramBundle);
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    private void refreshView() {

        //Called when the page reloads, check for updates here if you need to
    }

    public ISpringBoardHostStub getHost() {
        return this.mHost;
    }

    //Called when the page is loading and being bound to the host
    @Override
    public void onBindHost(ISpringBoardHostStub paramISpringBoardHostStub) {
        Log.d(TAG, "onBindHost");
        //Store host
        this.mHost = paramISpringBoardHostStub;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Called when the page becomes inactive (the user has scrolled away)
    @Override
    public void onInactive(Bundle paramBundle) {
        super.onInactive(paramBundle);
        //Store active state
        this.mHasActive = false;
    }

    //Called when the page is paused (in app mode)
    @Override
    public void onPause() {
        super.onPause();
        this.mHasActive = false;
    }

    //Not sure what this does, can't find it being used anywhere. Best leave it alone
    @Override
    public void onReceiveDataFromProvider(int paramInt, Bundle paramBundle) {
        super.onReceiveDataFromProvider(paramInt, paramBundle);
    }

    //Called when the page is shown again (in app mode)
    @Override
    public void onResume() {
        super.onResume();
        //Check if view already loaded
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            this.mHasActive = true;
            refreshView();
        }
        //Store active state
        this.mHasActive = true;
    }

    //Called when the page is stopped (in app mode)
    @Override
    public void onStop() {
        super.onStop();
        this.mHasActive = false;
    }
}
