package com.olsera.warungmakan.home.adapter

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.olsera.repository.model.Warung
import com.zr.warungmakan.databinding.ItemWarungBinding

class WarungViewHolder(private val binding: ItemWarungBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: Warung, isOdd: Boolean, onClickListener: (warung: Warung) -> Unit) {
        Glide.with(binding.root)
            .load(data.imageUrl)
            .into(binding.imageView)

        binding.textName.text = data.name
        binding.textDesc.text = data.desc
        binding.textIsInactive.isVisible = !data.isActive

        if (isOdd) {
            binding.root.setBackgroundColor(Color.parseColor("#EFEFEF"))
        } else {
            binding.root.setBackgroundColor(Color.WHITE)
        }

        binding.root.setOnClickListener {
            onClickListener(data)
        }
    }
}