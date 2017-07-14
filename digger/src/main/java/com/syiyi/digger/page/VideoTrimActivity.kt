package com.syiyi.digger.page

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.syiyi.digger.R
import com.syiyi.digger.consts.Constrains.*
import com.syiyi.digger.util.DeviceUtil
import com.syiyi.digger.widget.RangeSeekBar
import com.syiyi.digger.widget.TimeLineView
import com.syiyi.digger.widget.VideoView
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
    var mSeekBar: SeekBar? = null
    var mRangeBar: RangeSeekBar? = null
    var mCurrentStart: Long = 0
    var mCurrentEnd: Long = 0


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
        setSeekBar()

        mPlayAction!!.setOnClickListener({
            videoPlayOrPause()
        })

        mVideoView!!.setOnPreparedListener({
            mVideoView!!.seekTo(0)
            mSeekBar!!.max = mVideoView!!.duration
            mCurrentEnd = mVideoView!!.duration.toLong()
            mDuration = mVideoView!!.duration.toLong()
        })


        mVideoView!!.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {
                mVideoView!!.seekTo(mCurrentStart.toInt())
            }
        }

        mVideoView!!.setVideoURI(Uri.fromFile(File(mPath)))
    }

    private fun setSeekBar() {
        mSeekBar = findViewById(R.id.seek_bar)
        mRangeBar = findViewById(R.id.rangeSeekBar)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.video_trim_handle)
        val scaleHeight = DeviceUtil.dip2px(this, 60f).toFloat() / bitmap.height
        val width = (bitmap.width * scaleHeight).toInt()
        val params: ViewGroup.MarginLayoutParams = mSeekBar!!.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = width
        params.rightMargin = width
        mSeekBar!!.layoutParams = params

        mSeekBar!!.isEnabled = false
        mVideoView!!.setVideoPlayLister(object : VideoView.OnVideoPlayListener {
            override fun onPlay(currentPosition: Int) {
                if (currentPosition >= mCurrentEnd) {
                    setVisibility(mPlayIcon!!, View.VISIBLE)
                    setVisibility(mSeekBar!!, View.INVISIBLE)
                    mSeekBar!!.progress = mCurrentStart.toInt()
                    mVideoView!!.pause()
                    mVideoView!!.seekTo(mCurrentStart.toInt())
                } else {
                    mSeekBar!!.progress = currentPosition
                }
            }

        })


        mRangeBar!!.setRangeChangeListener { startPercent, endPercent, selectPercent ->
            mCurrentStart = (mDuration * startPercent).toLong()
            mCurrentEnd = (mDuration * endPercent).toLong()
            mVideoView!!.pause()
            setVisibility(mPlayIcon!!, View.VISIBLE)
            setVisibility(mSeekBar!!, View.INVISIBLE)
            mVideoView!!.seekTo(mCurrentStart.toInt())
        }
    }

    private fun videoPlayOrPause() {
        if (mVideoView!!.isPlaying) {
            mVideoView!!.pause()
            setVisibility(mPlayIcon!!, View.VISIBLE)
            setVisibility(mSeekBar!!, View.INVISIBLE)
        } else {
            mVideoView!!.start()
            setVisibility(mPlayIcon!!, View.INVISIBLE)
            setVisibility(mSeekBar!!, View.VISIBLE)
        }
    }

    private fun setVisibility(view: View, vis: Int) {
        if (view.visibility != vis) {
            view.visibility = vis
        }
    }

    private fun setUpToolBar() {

    }

    private fun setTimeLine() {
        val timeLineView = findViewById<TimeLineView>(R.id.time_line)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.video_trim_handle)
        val scaleHeight = DeviceUtil.dip2px(this, 60f).toFloat() / bitmap.height
        val width = (bitmap.width * scaleHeight).toInt()
        val params: ViewGroup.MarginLayoutParams = timeLineView.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = width
        params.rightMargin = width
        timeLineView.layoutParams = params
        timeLineView.setVideo(Uri.fromFile(File(mPath)))

    }

}
