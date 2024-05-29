package com.example.dacs3_ns_22ns082

import android.content.Context
import android.graphics.BitmapFactory
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doancoso3.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale

class CheckoutRecyclerAdapter(
    val context: Context,
    var list: List<Product> = emptyList()
) : RecyclerView.Adapter<CheckoutRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.checkout_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]
        holder.bind(product)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView=view.findViewById<ImageView>(R.id.img_product)
        val scope = CoroutineScope(Dispatchers.IO)
        val productName=view.findViewById<TextView>(R.id.tv_pn)
        val dp=view.findViewById<TextView>(R.id.tv_dp)
        val sum=view.findViewById<TextView>(R.id.tv_sum)

        fun bind(product: Product) {
//            image.setImageResource(product.image)
            scope.launch {
                try {
                    val stream =
                        URL(product.image).openStream()
                    val image = BitmapFactory.decodeStream(stream)
                    withContext(Dispatchers.Main) {
                        imgView.setImageBitmap(image)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            productName.text=product.productName
            dp.text=String.format(Locale.US,"%.3f",product.discountedPrice)
            sum.text=String.format(Locale.US,"%.3f",product.discountedPrice)
        }
    }
}