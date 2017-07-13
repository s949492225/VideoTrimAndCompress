package com.syiyi.digger.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.VideoView

/**
 * 自动
 * Created by songlintao on 2017/7/13.
 */

class AutoSeekBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : android.support.v7.widget.AppCompatSeekBar(context, attrs, defStyleAttr) {

    var video: VideoView? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (video != null && video!!.duration > 0) {
            if (video!!.isPlaying) {
                val duration: Long = video!!.duration.toLong()
                val curr: Long = video!!.currentPosition.toLong()
                val pos = max * (curr / duration).toInt()
                thumbOffset = pos
            }
        }
        postInvalidateDelayed(10)
    }
}
