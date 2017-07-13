package com.syiyi.video;

import android.app.Application;

import com.syiyi.digger.init.Digger;

/**
 * app
 * Created by songlintao on 2017/7/13.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Digger.init(this);
    }
}
