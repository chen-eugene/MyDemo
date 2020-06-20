package com.eugene.mydemo

import android.os.Bundle
import android.view.View
import com.eugene.mydemo.app.BaseActivity
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.android.synthetic.main.activity_video_detail.toolbar

class VideoDetailActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        toolbar.setTitle("")
        setSupportActionBar(toolbar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })

        val frame = intent.getStringExtra("frame")?.toInt() ?: 0
        val video = intent.getStringExtra("video")


        videoView.setBackgroundResource(frame)
        videoView.setVideoPath(video)
        videoView.setOnPreparedListener {
            videoView.background = null
            videoView.start()
        }
    }


}