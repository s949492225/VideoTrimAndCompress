package com.syiyi.digger.init;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.syiyi.digger.ex.FileEx;
import com.syiyi.digger.ex.LogExKt;

import java.io.File;

/**
 * 启动类
 * Created by songlintao on 2017/7/13.
 */

public class Digger {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static String mOutputPath;

    public static void init(Application app, File outputDir) {
        mOutputPath = outputDir.getPath();
        FileEx.createDir(outputDir);
        mContext = app.getApplicationContext();
        initFFmpegBinary();
    }

    public static Context getContext() {
        return mContext;
    }

    private static void initFFmpegBinary() {
        try {
            FFmpeg.getInstance(getContext()).loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onSuccess() {
                    LogExKt.log("FFmpeg", "loadBinary:success");
                }

                @Override
                public void onFailure() {
                    LogExKt.log("FFmpeg", "loadBinary:fail");

                }
            });
        } catch (FFmpegNotSupportedException e) {
            LogExKt.log("FFmpeg", "loadBinary:exception->" + e.getMessage());
        }
    }


}
