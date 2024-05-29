package com.example.doancoso3

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.AuthStore

open class IsAdminFragment : AuthFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authStore = AuthStore(requireContext())
        if (authStore.isAdmin().not()) {
            AlertDialog.Builder(requireContext())
                .setTitle("F")
                .setMessage("You are not admin!")
                .setPositiveButton("OK") { _, _ ->
                    findNavController().navigate(R.id.homeFragment)
                }
                .show()
        }
    }
}