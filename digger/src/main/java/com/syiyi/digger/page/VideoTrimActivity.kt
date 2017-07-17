@file:Suppress("DEPRECATION")

package com.syiyi.digger.page

import android.app.Activity
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.syiyi.digger.R
import com.syiyi.digger.consts.Constrains.*
import com.syiyi.digger.init.Digger
import com.syiyi.digger.widget.RangeSeekBar
import com.syiyi.digger.widget.TimeLineView
import com.syiyi.digger.widget.VideoView
import java.io.File
import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaMetadataRetriever
import com.syiyi.digger.consts.Constrains
import com.syiyi.digger.ex.FileEx
import com.syiyi.digger.util.*


@Suppress("DEPRECATION")
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
        mHeight = intent.getIntExtra(VIDEO_HEIGHT, 0)
        mPath = intent.getStringExtra(VIDEO_PATH)

    }

    private fun setUI() {
        setUpToolBar()
        setTimeLine()
        setVideoInfo()

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
        })

        mVideoView!!.setVideoURI(Uri.fromFile(File(mPath)))
    }

    private fun setVideoInfo() {
        Thread(Runnable {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(mPath)
            mDuration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            val rotation = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION).toInt()
            if (rotation / 90 % 2 != 0) {
                mWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
                mHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
            } else {
                mWidth = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH).toInt()
                mHeight = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT).toInt()
            }
            mCurrentEnd = mDuration
            runOnUiThread {
                mSeekBar!!.max = mDuration.toInt()
                setEndTime(mDuration)
                setSelectTimeSum(mDuration)
            }

        }).start()
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
        mSeekBar!!.setPadding(0, 0, 0, 0)
        mSeekBar!!.visibility = View.INVISIBLE
        mSeekBar!!.progress = 0

        mVideoView!!.setOnCompletionListener {
            mSeekBar!!.progress = mCurrentStart.toInt()
            mVideoView!!.seekTo(mCurrentStart.toInt())
        }
        mVideoView!!.setVideoPlayLister(object : VideoView.OnVideoPlayListener {
            override fun onPause() {
                setVisibility(mPlayIcon!!, View.VISIBLE)
                setVisibility(mSeekBar!!, View.INVISIBLE)
            }

            override fun onPlay(currentPosition: Int) {
                if (currentPosition >= mCurrentEnd) {
                    mSeekBar!!.progress = mCurrentStart.toInt()
                    mVideoView!!.seekTo(mCurrentStart.toInt())
                    mVideoView!!.pause()
                } else {
                    mSeekBar!!.progress = currentPosition
                    setVisibility(mPlayIcon!!, View.INVISIBLE)
                    setVisibility(mSeekBar!!, View.VISIBLE)
                }
            }

        })


        mRangeBar!!.setRangeChangeListener { startPercent, endPercent, selectPercent ->
            mCurrentStart = (mDuration * startPercent).toLong()
            mCurrentEnd = (mDuration * endPercent).toLong()
            mVideoView!!.seekTo(mCurrentStart.toInt())
            mVideoView!!.pause()
            mSeekBar!!.progress = mCurrentStart.toInt()
            setStartTime(mCurrentStart)
            setEndTime(mCurrentEnd)
            setSelectTimeSum((selectPercent * mDuration).toLong())
        }
    }

    private fun setStartTime(time: Long) {
        mTrimStartText!!.text = TimeUtil.formatTime(time)
    }

    private fun setEndTime(time: Long) {
        mTrimEndText!!.text = TimeUtil.formatTime(time)
    }

    private fun setSelectTimeSum(time: Long) {
        mTrimDurationText!!.text = TimeUtil.formatSumTime(time)
    }

    private fun videoPlayOrPause() {
        if (mVideoView!!.isPlaying) {
            mVideoView!!.pause()
        } else {
            mVideoView!!.start()
        }
    }

    private fun setVisibility(view: View, vis: Int) {
        if (view.visibility != vis) {
            view.visibility = vis
        }
    }

    private fun setUpToolBar() {
        findViewById<View>(R.id.back).
                setOnClickListener {
                    onBackPressed()
                }

        findViewById<View>(R.id.confirm)
                .setOnClickListener {
                    confirm()
                }
    }

    private fun confirm() {
        val time = (mCurrentEnd - mCurrentStart) / 1000
        if (time < VIDEO_MIN_LENGTH) {
            Toast.makeText(this, "截取的视频不能小于10秒！", Toast.LENGTH_LONG).show()
            return
        }
        mVideoView!!.pause()
        val dialog = ProgressDialog(this)
        dialog.setTitle("提醒")
        dialog.setMessage("正在裁剪,请稍后...")
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.max = 100
        dialog.setCancelable(false)

        trimVideo(this, mPath, Digger.mOutputPath, mCurrentStart, mCurrentEnd, mDuration, object : FFmpegCallBck {

            override fun onFinish(path: String) {
                compressVideo(this@VideoTrimActivity, path, mWidth, mHeight, Digger.mOutputPath, mDuration, object : FFmpegCallBck {
                    override fun onFinish(output: String?) {
                        dialog.dismiss()
                        FileEx.delete(path)
                        val intent = Intent()
                        intent.putExtra(Constrains.VIDEO_PATH, path)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }

                    override fun onProgress(msg: Int) {
                        dialog.progress = msg
                    }

                    override fun onStart() {
                        dialog.setMessage("正在压缩,请稍后...")
                    }
                })
            }

            override fun onStart() {
                dialog.show()
                dialog.progress = 0
            }

            override fun onProgress(msg: Int) {
                dialog.progress = msg
            }
        })

    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
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
