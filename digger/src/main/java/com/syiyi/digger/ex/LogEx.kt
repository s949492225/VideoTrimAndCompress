package com.syiyi.digger.ex

import android.util.Log
import com.syiyi.digger.BuildConfig

/**
 * log
 * Created by songlintao on 2017/7/13.
 */

fun log(tag:String,msg:String){
     if (BuildConfig.DEBUG){
        Log.d(tag,msg)
     }
}
