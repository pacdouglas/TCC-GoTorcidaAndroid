package br.com.gotorcida.gotorcida.utils;

import android.support.multidex.MultiDexApplication;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class GoTorcidaApp extends MultiDexApplication {

    @Override
    public void onCreate(){
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
