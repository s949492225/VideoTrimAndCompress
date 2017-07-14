package com.syiyi.video;

import android.app.Application;
import android.os.Environment;

import com.syiyi.digger.init.Digger;

import java.io.File;

import iknow.android.utils.BaseUtils;

/**
 * app
 * Created by songlintao on 2017/7/13.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String path = Environment.getExternalStorageDirectory().getPath() + "/a/";
        Digger.init(this, new File(path));
        BaseUtils.init(this);
    }
}
