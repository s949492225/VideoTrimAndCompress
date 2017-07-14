package com.syiyi.digger.util

import android.content.Context
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.syiyi.digger.ex.log
import java.util.*


/**
 * 视频裁剪
 */
fun trimVideo(context: Context, inputFile: String, outputFile: String, startMs: Long, endMs: Long, sumMs: Long, callback: FFmpegCallBck) {

    val fileName = UUID.randomUUID().toString().replace("", "")
    val outputName = "trimmedVideo_$fileName.mp4"

    val start = convertSecondsToTime(startMs / 1000)
    val duration = convertSecondsToTime((endMs - startMs) / 1000)

    /**ffmpeg -ss START -t DURATION -i INPUT -vcodec copy -acodec copy OUTPUT
    -ss 开始时间，如： 00:00:20，表示从20秒开始；
    -t 时长，如： 00:00:10，表示截取10秒长的视频；
    -i 输入，后面是空格，紧跟着就是输入视频文件；
    -vcodec copy 和 -acodec copy 表示所要使用的视频和音频的编码格式，这里指定为copy表示原样拷贝；
    INPUT，输入视频文件；
    OUTPUT，输出视频文件*/
    val cmd = "-ss $start -t $duration -i $inputFile -vcodec copy -acodec copy $outputFile/$outputName"
    val command = cmd.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    try {
        FFmpeg.getInstance(context).execute(command, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {
                callback.onStart()
            }

            override fun onProgress(message: String?) {
                log("FFmpeg-message", message!!)

                val time = TimeUtil.time2ms(message)
                if (time == 0L)
                    return
                val percent: Int = (time / sumMs * 100).toInt()
                log("FFmpeg-trim", "$time:$sumMs:$percent")
                callback.onProgress(percent)
            }

            override fun onSuccess(s: String?) {
                callback.onFinish("$outputFile/$outputName")
            }

        })
    } catch (e: FFmpegCommandAlreadyRunningException) {
        e.printStackTrace()
    }

}


fun compressVideo(context: Context, inputFile: String, width: Int, height: Int, outputFile: String, sumMs: Long, callback: FFmpegCallBck) {

    val fileName = UUID.randomUUID().toString().replace("", "")
    val outputName = "compressVideo_$fileName.mp4"

//    ffmpeg -i input -c:v libx264 -preset ultrafast -crf 0 output.mkv

    val outHeight: Int = (320.0 / width * height).toInt() / 10 * 10
//    val cmd = "-i $inputFile -vcodec libx264 -preset ultrafast -crf 26 -y -vf scale=480:$outHeight -acodec libmp3lame -ab 32k $outputFile/$outputName"
    val cmd = "-threads 8 -i $inputFile -c:v libx264 -preset ultrafast -crf 26 -y -vf scale=320:$outHeight $outputFile/$outputName"
    val command = cmd.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    try {
        FFmpeg.getInstance(context).execute(command, object : ExecuteBinaryResponseHandler() {

            override fun onStart() {
                callback.onStart()
            }

            override fun onProgress(message: String?) {
                log("FFmpeg-message", message!!)

                val time = TimeUtil.time2ms(message)
                if (time == 0L)
                    return
                val percent: Int = (time * 1.0 / sumMs * 100).toInt()
                log("FFmpeg-compress", "$time:$sumMs:$percent")
                callback.onProgress(percent)
            }

            override fun onSuccess(s: String?) {
                callback.onFinish("$outputFile/$outputName")
            }

        })
    } catch (e: FFmpegCommandAlreadyRunningException) {
        e.printStackTrace()
    }

}


private fun convertSecondsToTime(seconds: Long): String {
    val timeStr: String?
    val hour: Int
    var minute: Int
    val second: Int
    if (seconds <= 0)
        return "00:00"
    else {
        minute = seconds.toInt() / 60
        if (minute < 60) {
            second = seconds.toInt() % 60
            timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second)
        } else {
            hour = minute / 60
            if (hour > 99)
                return "99:59:59"
            minute %= 60
            second = (seconds - (hour * 3600).toLong() - (minute * 60).toLong()).toInt()
            timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second)
        }
    }
    return timeStr
}

private fun unitFormat(i: Int): String {
    val retStr: String?
    if (i in 0..9)
        retStr = "0" + Integer.toString(i)
    else
        retStr = "" + i
    return retStr
}