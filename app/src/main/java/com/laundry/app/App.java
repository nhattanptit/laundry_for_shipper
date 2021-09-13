package com.laundry.app;

import com.facebook.appevents.AppEventsLogger;
import com.laundry.base.BaseApplication;

public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AppEventsLogger.activateApp(this);
    }
}
