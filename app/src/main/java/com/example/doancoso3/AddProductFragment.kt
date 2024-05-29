package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductFragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val btnSave = view.findViewById<Button>(R.id.btn_save)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val edtName = view.findViewById<TextView>(R.id.edt_name)
        val edtOp = view.findViewById<TextView>(R.id.edt_op)
        val edtDpe = view.findViewById<TextView>(R.id.edt_dpe)
        val edtDp = view.findViewById<TextView>(R.id.edt_dp)
        val imgUpload = view.findViewById<ImageView>(R.id.img_upload)
        val imgProduct = view.findViewById<ImageView>(R.id.img_product)

        val selectImageLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                imgProduct.setImageURI(uri)
            }

        imgUpload.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            val image = imgProduct.drawable
            val file = Utils.drawableToFile(image)
            val productId = ""
            val productName = edtName.text.toString()
            val originalPrice = edtOp.text.toString()
            val discountPercentage = edtDpe.text.toString()
            val discountedPrice = edtDp.text.toString()
            val createdAt = ""
            val updatedAt = ""
            val productRequest = ProductRequest(productId,productName,originalPrice,discountPercentage,discountedPrice,createdAt,updatedAt,file)
            val productService = HandmadeServices.getInstance()

            val call = productService.addProduct(
                "Basic ${authStorage.getToken()}", productRequest.toMapPart(),
                productRequest.getImage()
            )

            it.isEnabled = false
            call.enqueue(object : Callback<Product> {
                override fun onResponse(p0: Call<Product>, p1: Response<Product>) {
                    it.isEnabled = true
                    if (p1.isSuccessful) {
                        val task = p1.body()!!
                        Utils.showToast(requireContext(), "Add product successfully")
                        findNavController().navigate(R.id.dashboard1Fragment,
                            null,
                            navOptions = NavOptions.Builder()
                                .setPopUpTo(R.id.dashboard1Fragment, true).build())
                    } else {
                        println(p1.errorBody()?.string())
                        Utils.showToast(requireContext(), "Failed to add product 432")
                    }
                }

                override fun onFailure(p0: Call<Product>, p1: Throwable) {
                    it.isEnabled = true
                    p1.printStackTrace()
                    Utils.showToast(requireContext(), "Failed to add product")
                }
            })
//            addProduct(product) { p, error ->
//                if (error != null || p == null) {
//                    Toast.makeText(
//                        context,
//                        "Failed to add product with error ${error?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@addProduct
//                }
//                findNavController().navigate(R.id.dashboard1Fragment)
//            }

        }
        btnCancel.setOnClickListener {
            findNavController().navigate(R.id.dashboard1Fragment,
                null,
                navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.dashboard1Fragment, true).build())
        }
    }

//    private fun addProduct(product: Product, callback: (Product?, Throwable?) -> Unit) {
//        ApiServices.handmadeServices.addProduct(product, "Basic ${authStorage.getToken()}")
//            .enqueue(object : Callback<Product> {
//                override fun onResponse(
//                    p0: Call<Product>,
//                    p1: Response<Product>
//                ) {
//                    if (p1.isSuccessful) {
//                        val task = p1.body()
//                        callback(task, null)
//                    } else {
//                        callback(null, Throwable("Failed to add product with status ${p1.code()}"))
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