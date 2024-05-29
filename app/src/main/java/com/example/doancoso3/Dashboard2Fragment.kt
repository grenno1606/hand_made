package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Dashboard2RecyclerAdapter
import com.example.dacs3_ns_22ns082.Tutorial
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard2Fragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())

        val btnAdd=view.findViewById<Button>(R.id.btn_add)

        val rvTutorial = view.findViewById<RecyclerView>(R.id.rv_tutorial)
        rvTutorial.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        val adapterT = Dashboard2RecyclerAdapter(requireContext(), mutableListOf())
        rvTutorial.adapter=adapterT
        getTutorials() { tutorials, error ->
            if (error != null || tutorials == null) {
                Toast.makeText(
                    context,
                    "Failed to get tutorials with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getTutorials
            }
            adapterT.list = tutorials
            adapterT.notifyDataSetChanged()
        }

        adapterT.setOnDeleteClickedListener() {
                tutorial, pos ->

            deleteTutorial(tutorial.tutorialId) { tutorial, error ->
                if (error != null || tutorial == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete tutorial with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteTutorial
                }

                adapterT.list = adapterT.list.filter { i -> i.tutorialId != tutorial.tutorialId }
                adapterT.notifyDataSetChanged()
            }
        }

        adapterT.setOnUpdateClickedListener {
                tutorial, pos ->
            val bundle = Bundle()
            bundle.putString("tutorialId", tutorial.tutorialId)
            bundle.putString("tutorialName", tutorial.tutorialName)
            bundle.putString("tutorialVideo", tutorial.video)
            findNavController().navigate(R.id.updateTutorialFragment, bundle)
        }

        btnAdd.setOnClickListener {
            findNavController().navigate(R.id.addTutorialFragment)
        }

        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.scrollView)

        nestedScrollView.post {
            nestedScrollView.fullScroll(View.FOCUS_UP)
            view.findViewById<View>(R.id.editTextText7).clearFocus() // Đây là ID của ô tìm kiếm
        }
    }

    private fun getTutorials(callback: (List<Tutorial>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getTutorials("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Tutorial>> {
                override fun onResponse(
                    p0: Call<List<Tutorial>>,
                    p1: Response<List<Tutorial>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to get tutorials with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Tutorial>>, p1: Throwable) {
                    p1.printStackTrace()
                    callback(null, p1)
                }

            })
        return
    }

    private fun deleteTutorial(id: String, callback: (Tutorial?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.deleteTutorial(id, "Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Tutorial> {
                override fun onResponse(
                    p0: Call<Tutorial>,
                    p1: Response<Tutorial>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to delete tutorial with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Tutorial>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }
}