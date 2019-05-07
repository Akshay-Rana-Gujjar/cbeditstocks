package cb.edit.stocks.buntysingh.com.cbeditstocks;

import android.app.Application;
import android.util.Log;

public class OverrideApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        FontsOverride.setDefaultFont(this, "DEFAULT", "MyFontAsset.ttf");
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "MyFontAsset2.ttf");
//        FontsOverride.setDefaultFont(this, "SERIF", "MyFontAsset3.ttf");
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", "MyFontAsset4.ttf");
//        Log.d("Assets", getAssets().+"");

        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/cabin_regular.ttf");

    }
}
