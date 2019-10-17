package com.example.seetaface2demo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompareAdapter(private val mContext: Context, private val data:List<FaceBean>) : RecyclerView.Adapter<CompareAdapter.CompareHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompareHolder {
        return CompareHolder(LayoutInflater.from(mContext).inflate(R.layout.item_compare_rv, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CompareHolder, position: Int) {
        val img = holder.itemView.findViewById<ImageView>(R.id.item_img)
        val txt = holder.itemView.findViewById<TextView>(R.id.item_txt)
        img.setImageResource(data[position].resId)
        txt.text = "相似度：${data[position].value}"
    }

    inner class CompareHolder(itemView:View):RecyclerView.ViewHolder(itemView)
}