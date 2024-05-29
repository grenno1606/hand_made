package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.AuthStore

open class IsLogged : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val authStore = AuthStore(requireContext())
        if (authStore.isLogged()) {
            if (authStore.isAdmin()) {
                findNavController().navigate(R.id.adminHomeFragment)
            } else {
                findNavController().navigate(R.id.mainFragment)
            }
        }
        super.onCreate(savedInstanceState)
    }
}