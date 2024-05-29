package com.example.doancoso3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.Contact
import com.example.dacs3_ns_22ns082.Dashboard3RecyclerAdapter

class Dashboard3Fragment : IsAdminFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contact= mutableListOf<Contact>()
        contact.add(Contact("Thế Dũng","thedung@gmail.com","0374724791","Giao diện ứng dụng đơn giản, sáng sủa, dễ dàng sử dụng. Tốc độ ứng dụng chưa nhanh."))
        contact.add(Contact("Tố Uyên","touyen@gmail.com","0374724791","Giao diện ứng dụng đơn giản, sáng sủa, dễ dàng sử dụng. Tốc độ ứng dụng chưa nhanh."))
        contact.add(Contact("Băng Hạ","bangha@gmail.com","0374724791","Giao diện ứng dụng đơn giản, sáng sủa, dễ dàng sử dụng. Tốc độ ứng dụng chưa nhanh."))
        contact.add(Contact("Tiêu Chiến","tieuchien@gmail.com","0374724791","Giao diện ứng dụng đơn giản, sáng sủa, dễ dàng sử dụng. Tốc độ ứng dụng chưa nhanh."))
        val rvContact=view.findViewById<RecyclerView>(R.id.rv_contact)
        rvContact.layoutManager = GridLayoutManager(context, 1, RecyclerView.VERTICAL, false)
        rvContact.adapter= Dashboard3RecyclerAdapter(requireContext(),contact)
    }

}