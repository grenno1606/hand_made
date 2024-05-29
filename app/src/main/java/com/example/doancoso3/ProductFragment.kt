package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Product
import com.example.dacs3_ns_22ns082.ProductRecyclerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val rvProduct = view.findViewById<RecyclerView>(R.id.rv_product)
        rvProduct.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        val adapter = ProductRecyclerAdapter(requireContext(), mutableListOf())
        rvProduct.adapter=adapter
        getProducts() { products, error ->
            if (error != null || products == null) {
                Toast.makeText(
                    context,
                    "Failed to get products with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getProducts
            }
            adapter.list = products
            adapter.notifyDataSetChanged()
        }

        adapter.setOnHeartClickedListener { product, pos ->
            addProductHeart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to add product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@addProductHeart
                }
                Toast.makeText(context, "Đã thêm sản phẩm vào mục yêu thích!", Toast.LENGTH_SHORT).show()
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

    }

    private fun addProductHeart(productId: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.addProductHeart(productId,"Basic ${authStorage.getToken()}")
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

}