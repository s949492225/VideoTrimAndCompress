package com.syiyi.digger.page

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.VideoView
import com.syiyi.digger.R
import com.syiyi.digger.consts.Constrains.*
import com.syiyi.digger.view.AutoSeekBar
import com.syiyi.digger.view.RangeSeekBar
import com.syiyi.digger.view.RangeSeekBar2
import com.syiyi.digger.widget.TimeLineView
import java.io.File

class VideoTrimActivity : AppCompatActivity() {
    var mDuration: Long = 0
    var mWidth: Int = 0
    var mHeight: Int = 0
    var mPath: String = ""

    var mVideoView: VideoView? = null
    var mTrimStartText: TextView? = null
    var mTrimEndText: TextView? = null
    var mTrimDurationText: TextView? = null
    var mPlayAction: View? = null
    var mPlayIcon: ImageView? = null
    var mSeekBar: AutoSeekBar? = null
    var mRangeBar: RangeSeekBar2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_trim)

        getVideoInfo()
        setUI()

    }

    private fun getVideoInfo() {
        mDuration = intent.getLongExtra(VIDEO_DURATION, 0)
        mWidth = intent.getIntExtra(VIDEO_WIDTH, 0)
        mHeight = intent.getIntExtra(VIDEO_HEIGHT, 0)
        mPath = intent.getStringExtra(VIDEO_PATH)
    }

    private fun setUI() {
        setUpToolBar()
        setTimeLine()

        mVideoView = findViewById(R.id.video)
        mTrimStartText = findViewById(R.id.trim_start)
        mTrimEndText = findViewById(R.id.trim_end)
        mTrimDurationText = findViewById(R.id.trim_duration)
        mPlayAction = findViewById(R.id.play)
        mPlayIcon = findViewById(R.id.play_icon)
        mSeekBar = findViewById(R.id.seek_bar)
        mRangeBar = findViewById(R.id.rangeSeekBar)

        mSeekBar!!.video = mVideoView

        mSeekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

            override fun onProgressChanged(p0: SeekBar?, pos: Int, p2: Boolean) {
                if (mVideoView!!.isPlaying)
                    return
                val time = mDuration * (pos.toDouble() / mSeekBar!!.max)
                mVideoView!!.seekTo(time.toInt())
            }

        })

        mPlayAction!!.setOnClickListener({
            videoPlayOrPause()
        })

        mVideoView!!.setOnPreparedListener({
            mVideoView!!.seekTo(0)
        })


        mVideoView!!.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {

            }
        }
        mVideoView!!.setVideoURI(Uri.fromFile(File(mPath)))
    }

    private fun videoPlayOrPause() {
        if (mVideoView!!.isPlaying) {
            mVideoView!!.pause()
            mPlayIcon!!.visibility = View.VISIBLE
            mSeekBar!!.isEnabled = true
        } else {
            mVideoView!!.start()
            mPlayIcon!!.visibility = View.GONE
            mSeekBar!!.isEnabled = false
        }
    }

    private fun setUpToolBar() {

    }

    private fun setTimeLine() {
        val timeLineView = findViewById<TimeLineView>(R.id.time_line)
        timeLineView.setVideo(Uri.fromFile(File(mPath)))
    }
}
