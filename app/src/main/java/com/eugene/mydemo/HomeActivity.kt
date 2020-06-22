package com.eugene.mydemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amplifyframework.core.Amplify
import com.eugene.mydemo.adapter.VideoAdapter
import com.eugene.mydemo.app.BaseActivity
import com.eugene.mydemo.utils.SPUtil
import com.eugene.mydemo.utils.start
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    private val videos = listOf<Pair<Int, String>>(
        Pair(
            R.mipmap.frame,
            "https://d3ngi64t7aakaf.cloudfront.net/ecea0248-72c2-4c26-a0d7-fa45fefb4653/hls/8A2E394C-008F-48C4-9EBD-47D16BA54E5C.m3u8"
        ),
        Pair(
            R.mipmap.frame,
            "https://d3ngi64t7aakaf.cloudfront.net/ecea0248-72c2-4c26-a0d7-fa45fefb4653/hls/8A2E394C-008F-48C4-9EBD-47D16BA54E5C.m3u8"
        ),
        Pair(
            R.mipmap.frame,
            "https://d3ngi64t7aakaf.cloudfront.net/ecea0248-72c2-4c26-a0d7-fa45fefb4653/hls/8A2E394C-008F-48C4-9EBD-47D16BA54E5C.m3u8"
        ),
        Pair(
            R.mipmap.frame,
            "https://d3ngi64t7aakaf.cloudfront.net/ecea0248-72c2-4c26-a0d7-fa45fefb4653/hls/8A2E394C-008F-48C4-9EBD-47D16BA54E5C.m3u8"
        ),
        Pair(
            R.mipmap.frame,
            "https://d3ngi64t7aakaf.cloudfront.net/ecea0248-72c2-4c26-a0d7-fa45fefb4653/hls/8A2E394C-008F-48C4-9EBD-47D16BA54E5C.m3u8"
        )

    )

    private val mMinScrollDistance = 20

    private var lastState = RecyclerView.SCROLL_STATE_IDLE

    private var lastVH: VideoAdapter.VH? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar.setTitle("")
        setSupportActionBar(toolbar)


        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })

        initView()

        tvLogOut.setOnClickListener {
            showWaitDialog("Log out ...")
            Amplify.Auth.signOut(
                {
                    closeWaitDialog()
                    SPUtil.clear()
                    start<MainActivity>()
                    finish()
                },
                { error ->
                    closeWaitDialog()
                    showDialogMessage("log out failed", error.message ?: "", null)
                }
            )
        }
    }

    private fun initView() {

        rvVideo.adapter = VideoAdapter(videos).apply {
            listener = {
                this@HomeActivity.start<VideoDetailActivity>(
                    Pair("frame", it.first.toString()),
                    Pair("video", it.second)
                )
            }
        }

        rvVideo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                lastState = newState

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val vh =
                        recyclerView.findViewHolderForAdapterPosition(position) as? VideoAdapter.VH
                    vh?.videoView?.start()
                    vh?.ivFrame?.visibility = View.GONE
                    lastVH = vh
                    Log.i("HomeActivity", "position-->${position}")
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (lastState != RecyclerView.SCROLL_STATE_IDLE) {
                    if (Math.abs(dy) >= mMinScrollDistance) {
                        lastVH?.videoView?.stopPlayback()
                        lastVH?.ivFrame?.visibility = View.VISIBLE
                        Log.i("HomeActivity", "onScrolled")
                    }
                }
            }
        })
    }


}