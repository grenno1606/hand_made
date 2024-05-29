package com.example.doancoso3

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.HandmadeServices
import com.example.dacs3_ns_22ns082.Tutorial
import com.example.dacs3_ns_22ns082.TutorialRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddTutorialFragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var videoUri: Uri? = null
        authStorage = AuthStore(requireContext())
        val btnSave=view.findViewById<Button>(R.id.btn_save)
        val btnCancel=view.findViewById<Button>(R.id.btn_cancel)
        val edtName=view.findViewById<TextView>(R.id.edt_tn)
        val imgUpload = view.findViewById<ImageView>(R.id.img_upload)
        val videoView = view.findViewById<VideoView>(R.id.videoView)

        val pickVideoLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // Handle the returned Uri
                videoUri = uri!!
                videoView.setVideoURI(uri)
//                videoView.seekTo(100)
            }

        imgUpload.setOnClickListener {
            pickVideoLauncher.launch("video/mp4")
        }


        btnSave.setOnClickListener {
            if (videoUri != null) {
                val tutorialId = ""
                val tutorialName = edtName.text.toString()
                val createdAt = ""
                val updatedAt = ""
                val file = Utils.uriToFile(requireContext(), videoUri!!)
                val videoReq = TutorialRequest(tutorialId,tutorialName,createdAt,updatedAt,file)
                val call = HandmadeServices.getInstance()
                    .addTutorial("Basic ${authStorage.getToken()}", videoReq.toMap(), videoReq.getVideoPart())
                call.enqueue(object : Callback<Tutorial> {
                    override fun onResponse(p0: Call<Tutorial>, p1: Response<Tutorial>) {
                        if (p1.isSuccessful) {
                            val video = p1.body()
                            if (video != null) {
                                Utils.showToast(requireContext(), "Video uploaded successfully")
                                findNavController().navigate(R.id.dashboard2Fragment, null,
                                    navOptions = NavOptions.Builder()
                                        .setPopUpTo(R.id.dashboard2Fragment, true).build())
                            }
                        } else {
                            Utils.showToast(requireContext(), "Failed to upload video")
                        }

                    }

                    override fun onFailure(p0: Call<Tutorial>, p1: Throwable) {
                        p1.printStackTrace()
                        Utils.showToast(requireContext(), "Failed to upload video 2")
                    }
                })

            }
        }
        btnCancel.setOnClickListener {
            findNavController().navigate(R.id.dashboard2Fragment,null,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.dashboard2Fragment, true).build())
        }
    }

}