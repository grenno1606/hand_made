package com.example.dacs3_ns_22ns082

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface HandmadeServices {
    @GET("/product")
    fun getProducts(@Header("Authorization") authorization: String): Call<List<Product>>

    @GET("/tutorial")
    fun getTutorials(@Header("Authorization") authorization: String): Call<List<Tutorial>>

    @GET("/heart/product")
    fun getHeartP(@Header("Authorization") authorization: String): Call<List<Product>>

    @GET("/heart/tutorial")
    fun getHeartT(@Header("Authorization") authorization: String): Call<List<Tutorial>>

    @GET("/cart")
    fun getCart(@Header("Authorization") authorization: String): Call<List<Product>>

    @POST("/login")
    fun login(@Body loginData: UserLoginRequest): Call<UserLoginResponse>

//    @POST("/product/add")
//    fun addProduct(
//        @Body product: Product,
//        @Header("Authorization") authorization: String
//    ): Call<Product>

    @POST("/product/add")
    @Multipart
    fun addProduct(
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image:
        MultipartBody.Part
    ): Call<Product>

    @POST("/product/delete/{id}")
    fun deleteProduct(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): Call<Product>

//    @POST("/product/update/{id}")
//    fun updateProduct(
//        @Path("id") id: String,
//        @Body product: Product,
//        @Header("Authorization") authorization: String
//    ): Call<Product>
    @POST("/product/update/{id}")
    @Multipart
    fun updateProduct(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image:
        MultipartBody.Part
    ): Call<Product>

    @POST("/tutorial/add")
    @Multipart
    fun addTutorial(
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part video: MultipartBody.Part
    ): Call<Tutorial>

    @POST("/tutorial/delete/{id}")
    fun deleteTutorial(
        @Path("id") id: String,
        @Header("Authorization") authorization: String
    ): Call<Tutorial>

//    @POST("/tutorial/update/{id}")
//    fun updateTutorial(
//        @Path("id") id: String,
//        @Body tutorial: Tutorial,
//        @Header("Authorization") authorization: String
//    ): Call<Tutorial>

    @POST("/tutorial/update/{id}")
    @Multipart
    fun updateTutorial(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part video: MultipartBody.Part
    ): Call<Tutorial>

    @POST("/favorite/add/{productId}")
    fun addProductHeart(
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Call<Product>

    @POST("/cart/add/{productId}")
    fun addProductCart(
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Call<Product>

    @POST("/cart/delete/{productId}")
    fun deleteProductCart(
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Call<Product>

    @POST("/favorite/addtutorial/{tutorialId}")
    fun addTutorialHeart(
        @Path("tutorialId") tutorialId: String,
        @Header("Authorization") authorization: String
    ): Call<Tutorial>

    @POST("/favorite/delete/{productId}")
    fun deleteProductHeart(
        @Path("productId") productId: String,
        @Header("Authorization") authorization: String
    ): Call<Product>

    @POST("/favorite/deletetutorial/{tutorialId}")
    fun deleteTutorialHeart(
        @Path("tutorialId") tutorialId: String,
        @Header("Authorization") authorization: String
    ): Call<Tutorial>

    @GET("/success")
    fun success(@Header("Authorization") authorization: String): Call<List<Product>>

    @POST("/register")
    fun register(@Body registerData: UserRegisterRequest): Call<UserRegisterResponse>

    companion object {
        fun getInstance(): HandmadeServices {
            return ApiServices.retrofit.create(HandmadeServices::class.java)
        }
    }
}