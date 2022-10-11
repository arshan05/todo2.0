package com.example.projecttodo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TagsAdapter(val onTagLongPressed: OnTagLongPressed):ListAdapter<Tags, TagsViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tags_item, parent,false)
        return TagsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.bind(viewModel,onTagLongPressed)
    }
}

class TagsViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val tagText = itemView.findViewById<TextView>(R.id.tagsName)
    fun bind(viewModel: Tags?, clickListener: OnTagLongPressed) {
        tagText.text = viewModel?.tag

        itemView.setOnLongClickListener{
            if (viewModel != null) {
                clickListener.onTagLongPressed(viewModel, position)
            }
            return@setOnLongClickListener true
        }
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

interface OnTagLongPressed{
    fun onTagLongPressed(viewModel: Tags, position: Int)
}