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
import com.example.dacs3_ns_22ns082.CartRecyclerAdapter
import com.example.dacs3_ns_22ns082.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class CartFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val sum=view.findViewById<TextView>(R.id.tv_sum)
        var s=0.0
        val rvCart = view.findViewById<RecyclerView>(R.id.rv_cart)
        rvCart.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        val adapter = CartRecyclerAdapter(requireContext(), mutableListOf())
        rvCart.adapter=adapter
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
        }

        adapter.setOnDeleteClickedListener{ product, pos ->
            deleteProductCart(product.productId) { p, error ->
                if (error != null || p == null) {
                    Toast.makeText(
                        context,
                        "Failed to delete product with error ${error?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@deleteProductCart
                }
                s=s-product.discountedPrice.toDouble()
                sum.text= String.format(Locale.US,"%.3f",s)
                adapter.list = adapter.list.filter { i -> i.productId != product.productId }
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng!", Toast.LENGTH_SHORT).show()
            }
        }

        val mh=view.findViewById<Button>(R.id.btn_mh)
        mh.setOnClickListener {
            findNavController().navigate(R.id.checkoutFragment)
        }

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

    private fun deleteProductCart(id: String, callback: (Product?, Throwable?) -> Unit) {
        ApiServices.handmadeServices.deleteProductCart(id, "Basic ${authStorage.getToken()}")
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
}