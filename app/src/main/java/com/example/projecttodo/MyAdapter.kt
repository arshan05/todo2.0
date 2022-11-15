package com.example.projecttodo

import android.animation.LayoutTransition
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class MyAdapter(private val onItemLongPressed: OnItemLongPressed, private val onItemChecked: OnItemChecked): ListAdapter<Task, TaskViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_item,parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val viewModel = getItem(position)
        holder.checkBoxView.setOnCheckedChangeListener(null)
        holder.checkBoxView.isChecked = getItem(position).isCompleted!!
        holder.checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isCompleted = isChecked
//            if (isChecked){
//                viewModel.priority = viewModel.priority?.plus(5)
//
//            }
//            else{
//                viewModel.priority = viewModel.priority?.minus(5)
////                notifyItemChanged(position)
//            }
            notifyItemChanged(holder.adapterPosition)
        }
        holder.bind(viewModel, onItemLongPressed, onItemChecked)
//        holder.checkBoxView.setOnCheckedChangeListener { buttonView, isChecked ->
//            viewModel.isCompleted = isChecked
//            holder.bind(viewModel,onItemLongPressed)
//        }
    }
    fun getTaskAt(position: Int) = getItem(position)
}

private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}

class TaskViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    var cardView = itemView.findViewById<CardView>(R.id.card)
    val taskView = itemView.findViewById<TextView>(R.id.inTaskText)
    val tagView = itemView.findViewById<TextView>(R.id.inTagText)
    val checkBoxView = itemView.findViewById<CheckBox>(R.id.inCheckbox)
    val priorityView = itemView.findViewById<View>(R.id.inPriority)


    fun bind(viewModel: Task, clickListener: OnItemLongPressed, checkListener: OnItemChecked) {
        taskView.text = viewModel.task
        tagView.text = "#${viewModel.tag}"


        when (viewModel.priority) {
            1 -> priorityView.setBackgroundResource(R.drawable.high)
            2 -> priorityView.setBackgroundResource(R.drawable.mid)
            3 -> priorityView.setBackgroundResource(R.drawable.low)
            4 -> priorityView.setBackgroundResource(R.drawable.no)
            else -> {
                priorityView.setBackgroundResource(R.drawable.completed)
            }
        }

        if (viewModel.isCompleted==true) {
            taskView.setTextColor(Color.GRAY)
        }
        else{
            taskView.setTextColor(Color.WHITE)
        }
//
        val layoutTransition: LayoutTransition = cardView.layoutTransition
        layoutTransition.disableTransitionType(LayoutTransition.CHANGING)
        layoutTransition.disableTransitionType(LayoutTransition.CHANGE_APPEARING)
        layoutTransition.disableTransitionType(LayoutTransition.APPEARING)
        layoutTransition.disableTransitionType(LayoutTransition.DISAPPEARING)
        layoutTransition.disableTransitionType(LayoutTransition.CHANGE_DISAPPEARING)

        itemView.setOnLongClickListener{
            clickListener.onItemLongPressed(viewModel, position)
            return@setOnLongClickListener true
        }

        val dateDisplay = itemView.findViewById<TextView>(R.id.inDate)
        if(viewModel.date != null){
            dateDisplay.text = viewModel.date
        }

        itemView.setOnClickListener {
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            if (dateDisplay.visibility == View.GONE){
                dateDisplay.visibility = View.VISIBLE
            }
            else{dateDisplay.visibility = View.GONE
            }
//            layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        }
        
        itemView.findViewById<CheckBox>(R.id.inCheckbox).setOnCheckedChangeListener { button, b ->

            itemView.findViewById<CheckBox>(R.id.inCheckbox).isChecked =
                viewModel.isCompleted != true
            checkListener.onItemChecked(viewModel, position)
        }
    }
}

interface OnItemChecked{
    fun onItemChecked(viewModel:Task, position: Int)
}

interface OnItemLongPressed{
    fun onItemLongPressed(viewModel: Task, position: Int)
}
