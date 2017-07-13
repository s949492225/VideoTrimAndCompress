package com.syiyi.digger.page

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView
import com.syiyi.digger.R
import com.syiyi.digger.consts.Constrains.*
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


        mVideoView = findViewById(R.id.video)
        mTrimStartText = findViewById(R.id.trim_start)
        mTrimEndText = findViewById(R.id.trim_end)
        mTrimDurationText = findViewById(R.id.trim_duration)
        mPlayAction = findViewById(R.id.play)
        mPlayIcon = findViewById(R.id.play_icon)
        mSeekBar = findViewById(R.id.seek_bar)


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
        } else {
            mVideoView!!.start()
            mPlayIcon!!.visibility = View.GONE
        }
    }

    private fun setUpToolBar() {

    }
}
