package com.eugene.mydemo.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.eugene.mydemo.R

class VideoAdapter(val data: List<Pair<Int, String>>) : RecyclerView.Adapter<VideoAdapter.VH>() {

    var listener: ((Pair<Int, String>) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VH(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.ivFrame.setImageResource(data[position].first)
        val uri = Uri.parse(data[position].second)
        holder.videoView.setVideoURI(uri)
        holder.videoView.setOnPreparedListener {
            it?.isLooping = true
        }

        holder.itemView.setOnClickListener {
            listener?.invoke(data[position])
        }
    }


    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val videoView = view.findViewById<VideoView>(R.id.videoView)
        val ivFrame = view.findViewById<ImageView>(R.id.ivFrame)
    }


}