package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Tutorial
import com.example.dacs3_ns_22ns082.TutorialRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TutorialFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

        // Sử dụng ViewTreeObserver để đảm bảo NestedScrollView cuộn về đầu sau khi vẽ xong
        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            nestedScrollView.scrollTo(0, 0)
        }

        authStorage = AuthStore(requireContext())
        val rvTutorial = view.findViewById<RecyclerView>(R.id.rv_tutorial)
        rvTutorial.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        val adapterT = TutorialRecyclerAdapter(requireContext(), mutableListOf())
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

        adapterT.setOnHeartClickedListener { tutorial, pos ->
            addTutorialHeart(tutorial.tutorialId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add tutorial with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addTutorialHeart
                }
                Toast.makeText(context, "Đã thêm hướng dẫn vào mục yêu thích!", Toast.LENGTH_SHORT).show()
            }
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

    private fun addTutorialHeart(tutorialId: String, callback: (Tutorial?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addTutorialHeart(tutorialId,"Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Tutorial> {
                override fun onResponse(
                    p0: Call<Tutorial>,
                    p1: Response<Tutorial>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to add tutorial with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Tutorial>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    override fun onResume() {
        super.onResume()
        // Đảm bảo NestedScrollView cuộn về đầu khi Fragment được hiển thị lại
        view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.let {
            it.scrollTo(0, 0)
        }
    }
}