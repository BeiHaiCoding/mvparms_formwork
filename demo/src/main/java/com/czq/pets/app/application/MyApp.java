package com.czq.pets.app.application;

import android.content.Context;

public class MyApp extends BaseApplication{

    //全局Context
    public static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
