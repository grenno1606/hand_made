package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.AuthStore

open class AuthFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val authStore = AuthStore(requireContext())
        val token = authStore.getToken()
        println("Token: $token")
        if (token == null) {
            findNavController().navigate(R.id.loginFragment)
        }
        super.onCreate(savedInstanceState)
    }
}