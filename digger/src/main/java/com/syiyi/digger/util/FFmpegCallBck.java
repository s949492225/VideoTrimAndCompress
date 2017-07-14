package com.syiyi.digger.util;

/**
 * 压缩回调
 * Created by songlintao on 2017/7/13.
 */

public interface FFmpegCallBck {

    void onStart();

    void onProgress(int msg);

    void onFinish(String path);
}
