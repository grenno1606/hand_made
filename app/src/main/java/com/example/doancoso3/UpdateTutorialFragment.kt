package com.example.doancoso3

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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

class UpdateTutorialFragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var videoUri: Uri? = null
        authStorage = AuthStore(requireContext())
        val tn=view.findViewById<EditText>(R.id.edt_tn)
        val save=view.findViewById<Button>(R.id.btn_save)
        val cancel=view.findViewById<Button>(R.id.btn_cancel)
        var tutorialId=arguments?.getString("tutorialId")
        var tutorialName=arguments?.getString("tutorialName")
        var tutorialVideo=arguments?.getString("tutorialVideo")
        val imgUpload = view.findViewById<ImageView>(R.id.img_upload)
        val videoView = view.findViewById<VideoView>(R.id.videoView)

        tn.setText(tutorialName)
        videoView.setVideoPath(tutorialVideo)
        videoView.seekTo(100)

        val pickVideoLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                // Handle the returned Uri
                videoUri = uri!!
                videoView.setVideoURI(uri)
                videoView.seekTo(100)
            }

        imgUpload.setOnClickListener {
            pickVideoLauncher.launch("video/mp4")
        }

        save.setOnClickListener {
            if (videoUri != null) {
                val tutorialId = tutorialId.toString()
                val tutorialName = tn.text.toString()
                val createdAt = ""
                val updatedAt = ""
                val file = Utils.uriToFile(requireContext(), videoUri!!)
                val videoReq = TutorialRequest(tutorialId,tutorialName,createdAt,updatedAt,file)
                val call = HandmadeServices.getInstance()
                    .updateTutorial(tutorialId,"Basic ${authStorage.getToken()}", videoReq.toMap(), videoReq.getVideoPart())
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


//            updateTutorial(tutorialId.toString(), tutorial) { tutorial, error ->
//                if (error != null || tutorial == null) {
//                    Toast.makeText(
//                        context,
//                        "Failed to update tutorial with error ${error?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@updateTutorial
//                }
//                findNavController().navigate(R.id.dashboard2Fragment)
            }
        }
        cancel.setOnClickListener {
            findNavController().navigate(R.id.dashboard2Fragment, null,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.dashboard2Fragment, true).build())
        }

    }

//    private fun updateTutorial(id: String, tutorial: Tutorial, callback: (Tutorial?, Throwable?) -> Unit) {
//        ApiServices.handmadeServices.updateTutorial(id,tutorial, "Basic ${authStorage.getToken()}")
//            .enqueue(object : Callback<Tutorial> {
//                override fun onResponse(
//                    p0: Call<Tutorial>,
//                    p1: Response<Tutorial>
//                ) {
//                    if (p1.isSuccessful) {
//                        val task = p1.body()
//                        callback(task, null)
//                    } else {
//                        callback(null, Throwable("Failed to update tutorial with status ${p1.code()}"))
//                    }
//                }
//
//                override fun onFailure(p0: Call<Tutorial>, p1: Throwable) {
//                    callback(null, p1)
//                }
//
//            })
//        return
//    }

}