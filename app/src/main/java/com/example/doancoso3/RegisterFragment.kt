package com.example.doancoso3

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.ApiServices
import com.example.dacs3_ns_22ns082.UserRegisterRequest
import com.example.dacs3_ns_22ns082.UserRegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dkdv=view.findViewById<TextView>(R.id.textView8)
        dkdv.paintFlags = Paint.UNDERLINE_TEXT_FLAG                  //gạch chân
        val dn=view.findViewById<TextView>(R.id.tv_dn)
        dn.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        val dk=view.findViewById<Button>(R.id.button)
        val edtEmail=view.findViewById<TextView>(R.id.editTextText2)
        val edtUsername=view.findViewById<TextView>(R.id.editTextText3)
        val edtPassword=view.findViewById<TextView>(R.id.editTextTextPassword2)
        val edtAP=view.findViewById<TextView>(R.id.editTextTextPassword3)

        dk.setOnClickListener {
            val email=edtEmail.text.toString()
            val username=edtUsername.text.toString()
            val password=edtPassword.text.toString()
            val apassword=edtAP.text.toString()
            ApiServices.handmadeServices.register(UserRegisterRequest(email,username, password,apassword))
                .enqueue(object : Callback<UserRegisterResponse> {
                    override fun onResponse(
                        p0: Call<UserRegisterResponse>,
                        p1: Response<UserRegisterResponse>
                    ) {
                        val message=p1.body()?.message
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (message=="Đăng ký thành công!") {
                        findNavController().navigate(R.id.loginFragment)}
                    }

                    override fun onFailure(p0: Call<UserRegisterResponse>, p1: Throwable) {
                        println(p1)
                    }
                })
        }

        dn.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }



    }

}