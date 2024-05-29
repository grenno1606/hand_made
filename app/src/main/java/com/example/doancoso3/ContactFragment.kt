package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class ContactFragment : AuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ph=view.findViewById<Button>(R.id.button2)
        ph.setOnClickListener {
            Toast.makeText(context, "Phản hồi đã được gửi\nXin cảm ơn quý khách!", Toast.LENGTH_SHORT).show()
        }
    }
}