package com.example.dacs3_ns_22ns082

import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.R
import java.util.Locale

class Dashboard2RecyclerAdapter(
    val context: Context,
    var list: List<Tutorial> = emptyList()
) : RecyclerView.Adapter<Dashboard2RecyclerAdapter.ViewHolder>() {
    private var handleDeleteBtnClick: ((Tutorial, Int) -> Unit)? = null
    private  var handleUpdateBtnClick: ((Tutorial, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dashboard2_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tutorial = list[position]
        holder.bind(tutorial)
        holder.btnDelete.setOnClickListener {
            handleDeleteBtnClick?.invoke(tutorial, position)
        }
        holder.btnUpdate.setOnClickListener {
            handleUpdateBtnClick?.invoke(tutorial, position)
        }
    }

    fun setOnDeleteClickedListener(listener: (Tutorial, Int) -> Unit) {
        handleDeleteBtnClick = listener
    }

    fun setOnUpdateClickedListener(listener: (Tutorial, Int) -> Unit) {
        handleUpdateBtnClick = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val video=view.findViewById<VideoView>(R.id.videoView)
        val tutorialId=view.findViewById<TextView>(R.id.tv_id)
        val tutorialName=view.findViewById<TextView>(R.id.tv_tn)
        val mediaController = MediaController(context)
        val btnDelete = itemView.findViewById<ImageView>(R.id.img_delete)!!
        val btnUpdate= itemView.findViewById<ImageView>(R.id.img_update)!!

        val ca=view.findViewById<TextView>(R.id.tv_ca)
        val ua=view.findViewById<TextView>(R.id.tv_ua)

        fun bind(tutorial: Tutorial) {
            video.setVideoPath(tutorial.video)
            video.seekTo(100)
            tutorialId.text=tutorial.tutorialId
            tutorialName.text=tutorial.tutorialName
            ca.text=tutorial.createdAt
            ua.text=tutorial.updatedAt

            // Set an OnClickListener to show the MediaController when the video is tapped
            video.setOnClickListener {
                val mediaController = MediaController(context)
                mediaController.setAnchorView(video)
                video.setMediaController(mediaController)
                mediaController.show()
            }
        }
    }
}