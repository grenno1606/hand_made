package com.example.dacs3_ns_22ns082

import com.google.gson.annotations.SerializedName

data class UserData(val userId: String, val userName: String, val password: String, val email:String,val role:String)

data class UserLoginResponse(val token: String,val role: String, val user: UserData)

data class UserLoginRequest(val userName: String, val password: String)

data class UserRegisterRequest(val email:String,val userName: String,val password: String,@SerializedName("confirm-password") val confirmPassword:String)

data class UserRegisterResponse(val message: String)
