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
import com.example.doancoso3.MainFragment
import com.example.doancoso3.R
import com.example.doancoso3.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.util.Locale

class ProductRecyclerAdapter(
    val context: Context,
    var list: List<Product> = emptyList()
) : RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {
    private var handleCartBtnClick: ((Product, Int) -> Unit)? = null
    private  var handleHeartBtnClick: ((Product, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = list[position]
        holder.bind(product)
        holder.btnHeart.setOnClickListener {
            handleHeartBtnClick?.invoke(product, position)
        }
        holder.btnCart.setOnClickListener {
            handleCartBtnClick?.invoke(product, position)
        }
    }

    fun setOnHeartClickedListener(listener: (Product, Int) -> Unit) {
        handleHeartBtnClick = listener
    }

    fun setOnCartClickedListener(listener: (Product, Int) -> Unit) {
        handleCartBtnClick = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgView=view.findViewById<ImageView>(R.id.img_product)
        val scope = CoroutineScope(Dispatchers.IO)
        val productName=view.findViewById<TextView>(R.id.tv_pn)
        val op=view.findViewById<TextView>(R.id.tv_op)
        val dpe=view.findViewById<TextView>(R.id.tv_dpe)
        val dp=view.findViewById<TextView>(R.id.tv_dp)
        val dong=view.findViewById<TextView>(R.id.tv_dong)
        val btnHeart = itemView.findViewById<ImageView>(R.id.img_heart)!!
        val btnCart= itemView.findViewById<ImageView>(R.id.img_cart)!!

        fun bind(product: Product) {
            productName.text=product.productName
            dpe.text="-"+product.discountPercentage.toString()+"%"
            val content1 = SpannableString(dong.text)
            content1.setSpan(StrikethroughSpan(), 0, content1.length, 0)
            dong.text=content1
            val content = SpannableString(String.format(Locale.US,"%.3f",product.originalPrice))
            content.setSpan(StrikethroughSpan(), 0, content.length, 0)
            op.text=content
            dp.text=String.format(Locale.US,"%.3f",product.discountedPrice)
            if (product.image.isNotEmpty()) {
                imgView.visibility = View.VISIBLE
                Utils.loadImageFromUrl(product.image, imgView)
            } else {
                imgView.visibility = View.GONE
            }
        }
    }
}