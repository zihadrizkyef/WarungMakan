package com.olsera.warungmakan.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.olsera.repository.model.Warung
import com.olsera.warungmakan.databinding.ItemWarungBinding

class WarungAdapter(private val dataList: ArrayList<Warung>):
    RecyclerView.Adapter<WarungViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarungViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemWarungBinding.inflate(inflater, parent, false)
        return WarungViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarungViewHolder, position: Int) {
        holder.bind(dataList[position], position%2 == 0)
    }

    override fun getItemCount(): Int = dataList.count()
}