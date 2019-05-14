package cb.edits.stocks.myapps.com.cbeditstocks;

import android.app.Application;

public class OverrideApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/cabin_regular.ttf");


    }


}
