package com.syiyi.digger.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.VideoView
import com.syiyi.digger.ex.log

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
                val duration: Double = video!!.duration.toDouble()
                val curr: Double = video!!.currentPosition.toDouble()
                progress = (max * (curr / duration)).toInt()
                log("xxxxx", "$thumbOffset")
            }
        }
        postInvalidate()

    }

}
