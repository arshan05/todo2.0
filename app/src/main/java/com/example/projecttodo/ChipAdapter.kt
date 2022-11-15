package com.example.projecttodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip

class ChipAdapter():ListAdapter<Tags, ChipViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chip_item, parent,false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.bind(viewModel)
    }
}

class ChipViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val chip = itemView.findViewById<Chip>(R.id.chipItem)
    fun bind(viewModel: Tags) {
        chip.text = viewModel.tag
    }

}

private val DiffCallback = object : DiffUtil.ItemCallback<Tags>() {
    override fun areItemsTheSame(oldItem: Tags, newItem: Tags): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tags, newItem: Tags): Boolean {
        return oldItem == newItem
    }
}
