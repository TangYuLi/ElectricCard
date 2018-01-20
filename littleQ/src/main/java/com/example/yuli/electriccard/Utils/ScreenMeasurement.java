package com.example.yuli.electriccard.Utils;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.example.yuli.electriccard.Activity.LoginActivity;

/**
 * Created by YULI on 2017/9/7.
 */

public class ScreenMeasurement{

    public static DisplayMetrics dm;

    public static final ScreenMeasurement instance = new ScreenMeasurement(LoginActivity.instance);
    public static final float width = dm.widthPixels;
    public static final float height = dm.heightPixels;

    private ScreenMeasurement(Activity ac){
        dm = new DisplayMetrics();
        ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
    }
}
