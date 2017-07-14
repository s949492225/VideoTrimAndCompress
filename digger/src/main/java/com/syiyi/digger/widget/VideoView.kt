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
        postDelayed({
            if (!stop) {
                if (duration>0) {

                    if (isPlaying) {
                        mVideoPlayListener!!.onPlay(currentPosition)
                    } else {
                        mVideoPlayListener!!.onPause()
                    }
                }
                runListener()
            }
        },10)
    }


    fun setVideoPlayLister(listener: OnVideoPlayListener) {
        mVideoPlayListener = listener
    }


    interface OnVideoPlayListener {
        fun onPlay(currentPosition: Int)

        fun onPause()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stop=true
    }
}