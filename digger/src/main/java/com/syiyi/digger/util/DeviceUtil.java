package com.syiyi.digger.util;

import android.content.Context;

/**
 * 工具
 * Created by songlintao on 2017/7/14.
 */

public class DeviceUtil {
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
