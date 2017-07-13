package com.syiyi.digger.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.syiyi.digger.R

class VideoTrimActivity : AppCompatActivity() {
    var mDuration: Long = 0
    var mWidth: Int = 0
    var mHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_trim)
        intent.getStringArrayExtra("")
    }
}
