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

class ReviewsRecyclerAdapter(
    private val context: Context,
    private var list: List<Reviews> = emptyList()
) : RecyclerView.Adapter<ReviewsRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.reviews_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reviews = list[position]
        holder.bind(reviews)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image=view.findViewById<ImageView>(R.id.img_c)
        val customerName=view.findViewById<TextView>(R.id.tv_cn)
        val rv=view.findViewById<TextView>(R.id.tv_rv)

        fun bind(reviews: Reviews) {
            image.setImageResource(reviews.image)
            customerName.text=reviews.customerName
            rv.text=reviews.reviews
        }
    }
}