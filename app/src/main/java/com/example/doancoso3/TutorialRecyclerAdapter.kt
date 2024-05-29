package com.example.dacs3_ns_22ns082

import android.content.Context
import android.net.Uri
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.R
import com.example.doancoso3.TutorialDiffCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class TutorialRecyclerAdapter(
    val context: Context,
    var list: List<Tutorial> = emptyList()
) : RecyclerView.Adapter<TutorialRecyclerAdapter.ViewHolder>() {
    private var handleHeartBtnClick: ((Tutorial, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tutorial_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tutorial = list[position]
        holder.bind(tutorial)
        holder.btnHeart.setOnClickListener {
            handleHeartBtnClick?.invoke(tutorial, position)
        }
    }

    fun setOnHeartClickedListener(listener: (Tutorial, Int) -> Unit) {
        handleHeartBtnClick = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val video = view.findViewById<VideoView>(R.id.videoView)
        private val tutorialName = view.findViewById<TextView>(R.id.tv_tn)
        private val mediaController = MediaController(context)
        val btnHeart = itemView.findViewById<ImageView>(R.id.img_heart)!!

        init {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            video.setOnClickListener {
                coroutineScope.launch {
                    mediaController.setAnchorView(video)
                    video.setMediaController(mediaController)
                    mediaController.show()
                }
            }
        }

        fun bind(tutorial: Tutorial) {
            tutorialName.text = tutorial.tutorialName
            video.setVideoPath(tutorial.video)
//        video.requestFocus()
            video.seekTo(100)
        }
    }

    fun updateList(newList: List<Tutorial>) {
        val diffResult = DiffUtil.calculateDiff(TutorialDiffCallback(list, newList))
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }
}