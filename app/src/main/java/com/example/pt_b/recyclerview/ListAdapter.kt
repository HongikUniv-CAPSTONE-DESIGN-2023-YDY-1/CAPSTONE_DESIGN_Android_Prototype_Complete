package com.example.pt_b.recyclerview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pt_b.R

class ListAdapter(val itemList: ArrayList<ListLayout>): RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_itmes_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val item = itemList[position]
        val brand = item.brand
        val brandColor = when(brand) {
            "CU" -> Color.parseColor("#800080")
            "GS25" -> Color.parseColor("#50bcdf")
            "SEVENELEVEN" -> Color.parseColor("#008000")
            "EMART24" -> Color.parseColor("#FFFF00")
            else -> Color.BLACK
        }
        holder.brand.text = brand
        holder.brand.setTextColor(brandColor)

        holder.name.text = itemList[position].name
        holder.price.text = itemList[position].price.toString()

        val promotion = itemList[position].promotion
        val formattedPromtion = when(promotion) {
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> promotion
        }
        holder.promotion.text = formattedPromtion



        val imgData = itemList[position].imgUrl
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/$imgData"
        if (imgData != null) {
            Glide.with(holder.itemView)
                .load(fullImgUrl)
                .into(holder.imgUrl)
        } else {
            Glide.with(holder.itemView)
                .load(R.drawable.ic_launcher_foreground)
                .into(holder.imgUrl)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUrl: ImageView = itemView.findViewById(R.id.items_img)
        val brand: TextView = itemView.findViewById(R.id.items_brand)
        val name: TextView = itemView.findViewById(R.id.items_name)
        val price: TextView = itemView.findViewById(R.id.items_price)
        val promotion: TextView = itemView.findViewById(R.id.items_promotion)
    }
}
