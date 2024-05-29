package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Product
import com.example.dacs3_ns_22ns082.ProductRecyclerAdapter
import com.example.dacs3_ns_22ns082.Tutorial
import com.example.dacs3_ns_22ns082.TutorialRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HeartFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_heart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nestedScrollView = view.findViewById<NestedScrollView>(R.id.nestedScrollView)

        // Sử dụng ViewTreeObserver để đảm bảo NestedScrollView cuộn về đầu sau khi vẽ xong
        nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            nestedScrollView.scrollTo(0, 0)
        }

        authStorage = AuthStore(requireContext())
        val rvFProduct = view.findViewById<RecyclerView>(R.id.rv_fp)
        rvFProduct.layoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        val adapter = ProductRecyclerAdapter(requireContext(), mutableListOf())
        rvFProduct.adapter=adapter
        getHeartP() { products, error ->
            if (error != null || products == null) {
                Toast.makeText(
                    context,
                    "Failed to get products with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getHeartP
            }
            adapter.list = products
            adapter.notifyDataSetChanged()
        }

        adapter.setOnHeartClickedListener{ product, pos ->
            deleteProductHeart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteProductHeart
                }
                adapter.list = adapter.list.filter { i -> i.productId != product.productId }
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Đã xóa sản phẩm khỏi mục yêu thích!", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.setOnCartClickedListener{ product, pos ->
            addProductCart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addProductCart
                }
                Toast.makeText(context, "Đã thêm sản phẩm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
            }
        }

        val rvProduct = view.findViewById<RecyclerView>(R.id.rv_product)
        rvProduct.layoutManager = GridLayoutManager(context, 1, RecyclerView.HORIZONTAL, false)
        val adapterP = ProductRecyclerAdapter(requireContext(), mutableListOf())
        rvProduct.adapter=adapterP
        getProducts() { products, error ->
            if (error != null || products == null) {
                Toast.makeText(
                    context,
                    "Failed to get products with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getProducts
            }
            val viewP= mutableListOf<Product>()
            products.forEachIndexed { index, product ->
                if(index > 3) return@forEachIndexed
                viewP.add(product)
            }
            adapterP.list = viewP
            adapterP.notifyDataSetChanged()
        }

        val rvTutorial = view.findViewById<RecyclerView>(R.id.rv_ft)
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

        adapterT.setOnHeartClickedListener{ tutorial, pos ->
            deleteTutorialHeart(tutorial.tutorialId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete tutorial with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteTutorialHeart
                }
                adapterT.list = adapterT.list.filter { i -> i.tutorialId != tutorial.tutorialId }
                adapterT.notifyDataSetChanged()
                Toast.makeText(context, "Đã xóa hướng dẫn khỏi mục yêu thích!", Toast.LENGTH_SHORT).show()
            }
        }

        val xtc=view.findViewById<TextView>(R.id.tv_xtc)
        xtc.setOnClickListener {
            findNavController().navigate(R.id.productFragment)
        }
    }

    private fun addProductCart(productId: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addProductCart(productId,"Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    p0: Call<Product>,
                    p1: Response<Product>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to add product with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    private fun getHeartP(callback: (List<Product>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getHeartP("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    p0: Call<List<Product>>,
                    p1: Response<List<Product>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to get products with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Product>>, p1: Throwable) {
                    p1.printStackTrace()
                    callback(null, p1)
                }

            })
        return
    }

    private fun getProducts(callback: (List<Product>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getProducts("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    p0: Call<List<Product>>,
                    p1: Response<List<Product>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to get products with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Product>>, p1: Throwable) {
                    p1.printStackTrace()
                    callback(null, p1)
                }

            })
        return
    }

    private fun getTutorials(callback: (List<Tutorial>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getHeartT("Basic ${authStorage.getToken()}")
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

    private fun deleteProductHeart(id: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.deleteProductHeart(id, "Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    p0: Call<Product>,
                    p1: Response<Product>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(null, Throwable("Failed to delete product with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }

    private fun deleteTutorialHeart(id: String, callback: (Tutorial?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.deleteTutorialHeart(id, "Basic ${authStorage.getToken()}")
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

//    override fun onResume() {
//        super.onResume()
//        view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.postDelayed({
//            view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.scrollTo(0, 0)
//        }, 200) // Đợi 200ms trước khi cuộn
//    }
    override fun onResume() {
        super.onResume()
        // Đảm bảo NestedScrollView cuộn về đầu khi Fragment được hiển thị lại
        view?.findViewById<NestedScrollView>(R.id.nestedScrollView)?.let {
            it.scrollTo(0, 0)
        }
    }
}