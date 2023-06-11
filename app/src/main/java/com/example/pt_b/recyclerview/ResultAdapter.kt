package com.example.pt_b.recyclerview


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pt_b.R
import com.example.pt_b.ui.MapActivity


class ResultAdapter(val itemList: ArrayList<ResultLayouot>): RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_camera_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ResultAdapter.ViewHolder, position: Int) {
        val item = itemList[position]

        val brand = item.brand
        if (brand == "CU") {
            holder.brand.setImageResource(R.drawable.culog)
        } else if (brand == "GS25") {
            holder.brand.setImageResource(R.drawable.gs25)
        } else if (brand == "SEVENELEVEN") {
            holder.brand.setImageResource(R.drawable.seven)
        } else if (brand == "EMART24") {
            holder.brand.setImageResource(R.drawable.emart24)
        } else {
            holder.brand.setImageResource(R.drawable.ic_launcher_foreground)
        }
        holder.brand.setOnClickListener{

            val intent = Intent(holder.itemView.context, MapActivity::class.java)
            intent.putExtra("placeName", brand)
            startActivity(holder.itemView.context, intent, null)
        }
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

        val name = itemList[position].name
        holder.name.text = "이름: \n ${itemList[position].name}"

        val promotion = itemList[position].promotion
        val formattedPromtion = when(promotion) {
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> promotion
        }
        holder.promotion.text = "행사 종류: \n $formattedPromtion"
        holder.pricePerUnit.text = "개당 가격: \n ${itemList[position].pricePerUnit}"
        holder.pricePerGroup.text = "총 가격: \n ${itemList[position].pricePerGroup}"
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUrl: ImageView = itemView.findViewById(R.id.search_img)
        val name: TextView = itemView.findViewById(R.id.search_name)
        val promotion: TextView = itemView.findViewById(R.id.search_promotion)
        val pricePerUnit: TextView = itemView.findViewById(R.id.search_price_per)
        val pricePerGroup: TextView = itemView.findViewById(R.id.search_price_per_group)
        val brand: ImageButton = itemView.findViewById(R.id.search_brand)

    }
}