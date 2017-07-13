package com.syiyi.digger.util

import android.content.Context
import com.syiyi.digger.models.VideoInfo
import android.provider.MediaStore
import android.text.TextUtils


/**
 * 媒体信息获取
 * Created by songlintao on 2017/7/13.
 */

fun getAllVideoFile(context: Context): ArrayList<VideoInfo> {
    var video: VideoInfo
    val videos = ArrayList<VideoInfo>()
    val contentResolver = context.contentResolver
    try {
        val cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DATE_MODIFIED + " desc")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                video = VideoInfo()
                val temp: Long = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                if (temp > 0) {
                    video.duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                    video.videoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                    video.createTime = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                    video.videoName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                    videos.add(video)
                }
            }
            cursor.close()
        }
    } catch (e: Exception) {
        //ignore
    }

    return videos
}

fun getVideoFilePath(url: String): String {
    var ret = url
    if (TextUtils.isEmpty(url) || url.length < 5)
        return ""

    if (url.substring(0, 4).equals("http", ignoreCase = true)) {
    } else
        ret = "file://" + url

    return ret
}
