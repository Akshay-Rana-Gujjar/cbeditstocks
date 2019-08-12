package cb.edits.stocks.myapps.com.cbeditstocks;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class OverrideApp extends Application {
    public final static String SERVER_IP = "http://155.138.247.166";
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/cabin_regular.ttf");
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);

    }


}
