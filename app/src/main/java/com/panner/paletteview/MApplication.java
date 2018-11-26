package com.panner.paletteview;

import android.app.Application;

import com.panner.paletteview.utils.AppUtils;

/**
 * @author panzhijie
 * @version 2018-11-26-09:19
 */

public class MApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
    }
}
