package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.CheckoutRecyclerAdapter
import com.example.dacs3_ns_22ns082.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class CheckoutFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val dh=view.findViewById<Button>(R.id.btn_dh)
        dh.setOnClickListener {
            deleteProductCart() { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteProductCart
                }
            }
            findNavController().navigate(R.id.orderSuccessFragment)
        }

        val sum=view.findViewById<TextView>(R.id.tv_tt)
        val pvc=view.findViewById<TextView>(R.id.tv_pvc)
        val ttt=view.findViewById<TextView>(R.id.tv_sum)
        var s=0.0
        val rvProduct=view.findViewById<RecyclerView>(R.id.rv_product)
        rvProduct.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        val adapter= CheckoutRecyclerAdapter(requireContext(), mutableListOf())
        rvProduct.adapter=adapter
        getCart() { products, error ->
            if (error != null || products == null) {
                Toast.makeText(
                    context,
                    "Failed to get products with error ${error?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                return@getCart
            }
            adapter.list = products
            adapter.notifyDataSetChanged()
            for (i in 0..products.size-1)
            {
                s=products[i].discountedPrice.toDouble()+s
            }
            sum.text= String.format(Locale.US,"%.3f",s)
            pvc.text="30.000"
            ttt.text= String.format(Locale.US,"%.3f",s+30)
        }

    }

    private fun deleteProductCart(callback: (List<Product>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.success("Basic ${authStorage.getToken()}")
            .enqueue(object : Callback<List<Product>> {
                override fun onResponse(
                    p0: Call<List<Product>>,
                    p1: Response<List<Product>>
                ) {
                    if (p1.isSuccessful) {
                        val tasks = p1.body()
                        callback(tasks, null)
                    } else {
                        callback(null, Throwable("Failed to add product with status ${p1.code()}"))
                    }
                }

                override fun onFailure(p0: Call<List<Product>>, p1: Throwable) {
                    callback(null, p1)
                }
            })
        return
    }

    private fun getCart(callback: (List<Product>?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.getCart("Basic ${authStorage.getToken()}")
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