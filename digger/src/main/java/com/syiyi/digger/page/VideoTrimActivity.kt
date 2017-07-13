package com.syiyi.digger.page

import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import com.syiyi.digger.R
import com.syiyi.digger.consts.Constrants
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_trim)

        getVideoInfo()
        setUI()

    }

    private fun getVideoInfo() {
        mDuration = intent.getLongExtra(Constrants.VIDEO_DURATION, 0)
        mWidth = intent.getIntExtra(Constrants.VIDEO_WIDTH, 0)
        mHeight = intent.getIntExtra(Constrants.VIDEO_DURATION, 0)
        mPath = intent.getStringExtra(Constrants.VIDEO_DURATION)
    }

    private fun setUI() {
        mVideoView = findViewById(R.id.video);
        mTrimStartText = findViewById(R.id.trim_start)
        mTrimEndText = findViewById(R.id.trim_end)
        mTrimDurationText = findViewById(R.id.trim_duration)

        setUpToolBar()

        mVideoView!!.setOnPreparedListener({

        })

        mVideoView!!.setOnCompletionListener { MediaPlayer.OnCompletionListener{

        } }
        mVideoView!!.setVideoURI(Uri.fromFile(File(mPath)))
    }

    private fun setUpToolBar() {

    }
}
