package com.example.dacs3_ns_22ns082

import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class Product(@SerializedName("productid") val productId: String, val image: String,@SerializedName("productname") val productName: String,@SerializedName("original_price") val originalPrice: Double,@SerializedName("discount_percentage") val discountPercentage:Int,@SerializedName("discounted_price") val discountedPrice:Double,@SerializedName("created_at") val createdAt:String,@SerializedName("updated_at") val updatedAt:String)

data class ProductRequest(val productId: String,val productName: String,val originalPrice: String,val discountPercentage:String,val discountedPrice:String,val createdAt:String,val updatedAt:String, val image: File) {
    fun toMapPart(): Map<String, RequestBody> {
        val map = mutableMapOf<String, RequestBody>()
        map["productid"]=productId.toRequestBody()
        map["productname"] = productName.toRequestBody()
        map["original_price"]=originalPrice.toRequestBody()
        map["discount_percentage"]=discountPercentage.toRequestBody()
        map["discounted_price"]=discountedPrice.toRequestBody()
        map["created_at"]=createdAt.toRequestBody()
        map["updated_at"]=updatedAt.toRequestBody()
        return map
    }

    fun getImage(): MultipartBody.Part {
       return MultipartBody.Part.createFormData("image", image.name, image.asRequestBody("image/*".toMediaType()))
    }
}