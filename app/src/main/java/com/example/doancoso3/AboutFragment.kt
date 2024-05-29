package com.example.doancoso3

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView

class AboutFragment : AuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoView = view.findViewById<VideoView>(R.id.videoView1)
        // Lấy packageName từ context
        val packageName = requireContext().packageName
        // Tạo URI từ tài nguyên nội bộ
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.vd}")
        // Thiết lập URI cho VideoView
        videoView.setVideoURI(videoUri)
        // Thiết lập lặp lại video
        videoView.setOnPreparedListener { mp ->
            mp.isLooping = true
        }
        // Bắt đầu phát video
        videoView.start()
    }
}