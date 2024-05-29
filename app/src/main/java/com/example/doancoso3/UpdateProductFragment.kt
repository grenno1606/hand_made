package com.example.doancoso3

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.HandmadeServices
import com.example.dacs3_ns_22ns082.Product
import com.example.dacs3_ns_22ns082.ProductRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class UpdateProductFragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val pn = view.findViewById<EditText>(R.id.edt_pn)
        val op = view.findViewById<EditText>(R.id.edt_op)
        val dpe = view.findViewById<EditText>(R.id.edt_dpe)
        val dp = view.findViewById<EditText>(R.id.edt_dp)
        val save = view.findViewById<Button>(R.id.btn_save)
        val cancel = view.findViewById<Button>(R.id.btn_cancel)
        val imgUpload = view.findViewById<ImageView>(R.id.img_upload)
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)
        val scope = CoroutineScope(Dispatchers.IO)

        val selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                imgProduct.setImageURI(uri)
            }

        imgUpload.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        var productImage = arguments?.getString("productImage")
        var productId = arguments?.getString("productId")
        var productName = arguments?.getString("productName")
        var originalPrice = arguments?.getString("originalPrice")
        var discountPercentage = arguments?.getString("discountPercentage")
        var discountedPrice = arguments?.getString("discountedPrice")

        scope.launch {
            try {
                val stream =
                    URL(productImage).openStream()
                val image = BitmapFactory.decodeStream(stream)
                withContext(Dispatchers.Main) {
                    imgProduct.setImageBitmap(image)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        pn.setText(productName)
        op.setText(originalPrice)
        dpe.setText(discountPercentage)
        dp.setText(discountedPrice)

        save.setOnClickListener {
            val image = imgProduct.drawable
            val file = Utils.drawableToFile(image)
            val productId = productId.toString()
            val productName = pn.text.toString()
            val originalPrice = op.text.toString()
            val discountPercentage = dpe.text.toString()
            val discountedPrice = dp.text.toString()
            val createdAt = ""
            val updatedAt = ""
            val productRequest = ProductRequest(
                productId,
                productName,
                originalPrice,
                discountPercentage,
                discountedPrice,
                createdAt,
                updatedAt,
                file
            )
            val productService = HandmadeServices.getInstance()

            val call = productService.updateProduct(
                productId, "Basic ${authStorage.getToken()}", productRequest.toMapPart(),
                productRequest.getImage()
            )

            it.isEnabled = false
            call.enqueue(object : Callback<Product> {
                override fun onResponse(p0: Call<Product>, p1: Response<Product>) {
                    it.isEnabled = true
                    if (p1.isSuccessful) {
                        val task = p1.body()!!
                        Utils.showToast(requireContext(), "Update product successfully")
                        findNavController().navigate(
                            R.id.dashboard1Fragment,
                            null,
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.dashboard1Fragment, true).build()
                        )
                    } else {
                        println(p1.errorBody()?.string())
                        Utils.showToast(requireContext(), "Failed to update product 432")
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    it.isEnabled = true
                    p1.printStackTrace()
                    Utils.showToast(requireContext(), "Failed to update product")
                }
            })
//            updateProduct(productId.toString(), product) { product, error ->
//                if (error != null || product == null) {
//                    Toast.makeText(
//                        context,
//                        "Failed to update product with error ${error?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@updateProduct
//                }
//                findNavController().navigate(R.id.dashboard1Fragment)
//            }
        }
        cancel.setOnClickListener {
            findNavController().navigate(R.id.dashboard1Fragment,
                null,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.dashboard1Fragment, true).build())
        }
    }

//    private fun updateProduct(id: String, product: Product, callback: (Product?, Throwable?) -> Unit) {
//        ApiServices.handmadeServices.updateProduct(id,product, "Basic ${authStorage.getToken()}")
//            .enqueue(object : Callback<Product> {
//                override fun onResponse(
//                    p0: Call<Product>,
//                    p1: Response<Product>
//                ) {
//                    if (p1.isSuccessful) {
//                        val task = p1.body()
//                        callback(task, null)
//                    } else {
//                        callback(null, Throwable("Failed to update product with status ${p1.code()}"))
//                    }
//                }
//
//                override fun onFailure(p0: Call<Product>, p1: Throwable) {
//                    callback(null, p1)
//                }
//
//            })
//        return
//    }

}