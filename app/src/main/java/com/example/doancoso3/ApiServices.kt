package com.example.dacs3_ns_22ns082

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServices {
    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder().baseUrl("http://192.168.43.147:8080")
        .addConverterFactory(GsonConverterFactory.create(gson)).build()

    val handmadeServices = retrofit.create(HandmadeServices::class.java)
}