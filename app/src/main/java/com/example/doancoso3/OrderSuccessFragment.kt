package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.dacs3_ns_22ns082.AuthStore

class OrderSuccessFragment : AuthFragment() {
    private lateinit var authStorage: AuthStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authStorage = AuthStore(requireContext())
        val tvtc=view.findViewById<Button>(R.id.button4)
        tvtc.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }
}