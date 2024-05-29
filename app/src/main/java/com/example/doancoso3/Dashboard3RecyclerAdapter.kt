package com.example.dacs3_ns_22ns082

import android.content.Context
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.R
import java.util.Locale

class Dashboard3RecyclerAdapter(
    private val context: Context,
    private var list: List<Contact> = emptyList()
) : RecyclerView.Adapter<Dashboard3RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dashboard3_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = list[position]
        holder.bind(contact)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name=view.findViewById<TextView>(R.id.tv_name)
        val email=view.findViewById<TextView>(R.id.tv_email)
        val phonenumber=view.findViewById<TextView>(R.id.tv_phonenumber)
        val ct=view.findViewById<TextView>(R.id.tv_contact)

        fun bind(contact: Contact) {
            name.text=contact.name
            email.text=contact.email
            phonenumber.text=contact.phoneNumber
            ct.text=contact.contact
        }
    }
}