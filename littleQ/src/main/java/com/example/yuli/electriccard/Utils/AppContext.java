package com.example.yuli.electriccard.Utils;

import android.app.Application;

/**
 * Created by YULI on 2017/9/4.
 */

public class AppContext extends Application {
    private static AppContext instance;

    public static AppContext getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = AppContext.this;
    }
}
