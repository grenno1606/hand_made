package com.example.doancoso3

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.UserLoginRequest
import com.example.dacs3_ns_22ns082.UserLoginResponse
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : IsLogged() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dn = view.findViewById<Button>(R.id.btn_dn)
        val edtUsername = view.findViewById<EditText>(R.id.edtUsername)
        val edtPassword = view.findViewById<EditText>(R.id.edtPassword)
        val authStore = AuthStore(requireContext())
        dn.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()
            ApiServices.handmadeServices.login(UserLoginRequest(username, password))
                .enqueue(object : Callback<UserLoginResponse> {
                    override fun onResponse(
                        p0: Call<UserLoginResponse>,
                        p1: Response<UserLoginResponse>
                    ) {
                        if (p1.isSuccessful) {
                            val userLoginRes = p1.body()
                            if (userLoginRes != null) {
                                edtUsername.setText("")
                                edtPassword.setText("")
                                authStore.saveToken(userLoginRes.token)
                                if (userLoginRes.role == "admin") {
                                    authStore.setIsAdmin(true)
                                } else authStore.setIsAdmin(false)
                                if (authStore.isAdmin()) {
                                    findNavController().navigate(R.id.adminHomeFragment)
                                } else findNavController().navigate(R.id.mainFragment)
                            }else {
                                Toast.makeText(context, "error", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {
                            val jsonObject = Gson().fromJson(p1.errorBody()?.string(), JsonObject::class.java)
                            val error = jsonObject.get("error").asString
                            Toast.makeText(context, error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(p0: Call<UserLoginResponse>, p1: Throwable) {
                        println(p1)
                    }
                })
        }

        val r = view.findViewById<TextView>(R.id.tv_dk)
        r.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        val qmk = view.findViewById<TextView>(R.id.textView4)
        qmk.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val dk = view.findViewById<TextView>(R.id.tv_dk)
        dk.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }


}