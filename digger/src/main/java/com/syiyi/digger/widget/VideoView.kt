package com.syiyi.digger.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/**
 *
 * Created by songlintao on 2017/7/14.
 */
class VideoView(context: Context?, attrs: AttributeSet?) : VideoView(context, attrs) {

    var mVideoPlayListener: OnVideoPlayListener? = null
    var stop: Boolean = false

    init {
        runListener()
    }

    fun runListener() {
        post({
            if (!stop) {
                if (duration > 0 && isPlaying) {
                    mVideoPlayListener!!.onPlay(currentPosition)
                }
                runListener()
            }
        })
    }


    fun setVideoPlayLister(listener: OnVideoPlayListener) {
        mVideoPlayListener = listener
    }


    interface OnVideoPlayListener {
        fun onPlay(currentPosition: Int)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop=true
    }
}