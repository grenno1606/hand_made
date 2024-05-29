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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Dashboard1RecyclerAdapter
import com.example.dacs3_ns_22ns082.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Dashboard1Fragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())

        val btnAdd = view.findViewById<Button>(R.id.btn_add)

        val rvProduct = view.findViewById<RecyclerView>(R.id.rv_product)
        rvProduct.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        val adapter = Dashboard1RecyclerAdapter(requireContext(), mutableListOf())
        rvProduct.adapter = adapter
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

        adapter.setOnDeleteClickedListener() { product, pos ->

            deleteProduct(product.productId) { product, error ->
                if (error != null || product == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteProduct
                }

                adapter.list = adapter.list.filter { i -> i.productId != product.productId }
                adapter.notifyDataSetChanged()
            }
        }

        adapter.setOnUpdateClickedListener { product, pos ->
            val bundle = Bundle()
            bundle.putString("productImage", product.image)
            bundle.putString("productId", product.productId)
            bundle.putString("productName", product.productName)
            bundle.putString("originalPrice", product.originalPrice.toString())
            bundle.putString("discountPercentage", product.discountPercentage.toString())
            bundle.putString("discountedPrice", product.discountedPrice.toString())
            findNavController().navigate(
                R.id.updateProductFragment, bundle
            )
        }

        btnAdd.setOnClickListener {
            findNavController().navigate(R.id.addProductFragment)

        }

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

    private fun deleteProduct(id: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.deleteProduct(id, "Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<Product> {
                override fun onResponse(
                    p0: Call<Product>,
                    p1: Response<Product>
                ) {
                    if (p1.isSuccessful) {
                        val task = p1.body()
                        callback(task, null)
                    } else {
                        callback(
                            null,
                            Throwable("Failed to delete product with status ${p1.code()}")
                        )
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    callback(null, p1)
                }

            })
        return
    }
}