package com.example.dacs3_ns_22ns082

import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.R
import java.util.Locale

class DashboardRecyclerAdapter(
    private val context: Context,
    private var list: List<Dashboard> = emptyList()
) : RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder>() {
    private var onDashBoardItemClick: OnDashboardItemClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dashboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dashboard = list[position]
        holder.bind(dashboard)
        holder.itemView.setOnClickListener {
            onDashBoardItemClick?.onClick(dashboard, position)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image=view.findViewById<ImageView>(R.id.img_db)
        val title=view.findViewById<TextView>(R.id.tv_db)

        fun bind(dashboard: Dashboard) {
            image.setImageResource(dashboard.image)
            title.text=dashboard.title
        }
    }

    fun setOnDashboardItemClickListener(listener: OnDashboardItemClick) {
        onDashBoardItemClick = listener
    }

    fun interface OnDashboardItemClick {
        fun onClick(item: Dashboard, pos: Int)
    }
}