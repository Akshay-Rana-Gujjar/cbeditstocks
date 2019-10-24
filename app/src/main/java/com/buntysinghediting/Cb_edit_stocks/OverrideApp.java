package com.buntysinghediting.Cb_edit_stocks;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class OverrideApp extends Application {
    public final static String SERVER_IP = "http://144.202.12.33/";
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/cabin_regular.ttf");
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

    }


}
