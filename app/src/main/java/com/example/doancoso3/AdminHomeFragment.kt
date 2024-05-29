package com.example.doancoso3

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dacs3_ns_22ns082.AuthStore
import com.example.dacs3_ns_22ns082.Dashboard
import com.example.dacs3_ns_22ns082.DashboardRecyclerAdapter

class AdminHomeFragment : IsAdminFragment() {
    private lateinit var authStorage: AuthStore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imgLogout = view.findViewById<ImageView>(R.id.img_logout)
        authStorage = AuthStore(requireContext())
        imgLogout.setOnClickListener {
            authStorage.clearToken()
            authStorage.clearIsAdmin()
            findNavController().navigate(R.id.loginFragment)
        }

        val dashboard= mutableListOf<Dashboard>()
        dashboard.add(Dashboard(R.drawable.sanpham,"Sản phẩm", "SP"))
        dashboard.add(Dashboard(R.drawable.huongdan,"Hướng dẫn", "HD"))
        dashboard.add(Dashboard(R.drawable.lienhe1,"Liên hệ", "LH"))
        dashboard.add(Dashboard(R.drawable.danhgia2,"Đánh giá", "DG"))
        val rvDashboard=view.findViewById<RecyclerView>(R.id.rv_db)
        rvDashboard.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        val adapter = DashboardRecyclerAdapter(requireContext(),dashboard)

        adapter.setOnDashboardItemClickListener { item, pos ->
            var intent: Intent? = null
            when(item.code) {
                "SP" ->
                    findNavController().navigate(R.id.dashboard1Fragment)
                "HD" ->
                    findNavController().navigate(R.id.dashboard2Fragment)
                "LH" ->
                    findNavController().navigate(R.id.dashboard3Fragment)
                "DG" ->
                    findNavController().navigate(R.id.dashboard4Fragment)
            }
        }
        rvDashboard.adapter = adapter
    }

}