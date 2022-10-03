package com.ann.planner.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.ann.planner.R
import com.ann.planner.databinding.ItemTaskDisabledBinding
import com.ann.planner.databinding.ItemTaskEnabledBinding
import com.ann.planner.domain.TaskItem

class TaskListAdapter: ListAdapter<TaskItem, TaskItemViewHolder>(TaskItemDiffCallback()) {

    var onTaskItemLongClickListener: ((TaskItem) -> Unit)? = null
    var onTaskItemClickListener: ((TaskItem)-> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskItemViewHolder {
         val layout = when (viewType){
            VIEW_TYPE_DISABLED -> R.layout.item_task_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_task_enabled
            else -> throw RuntimeException("Unknown view type: $viewType")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), layout, parent, false )
        return TaskItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val taskItem = getItem(position)
        val binding = holder.binding
        binding.root.setOnClickListener {
            onTaskItemLongClickListener?.invoke(taskItem)
            true
        }
        binding.root.setOnClickListener {
            onTaskItemClickListener?.invoke(taskItem)
        }
        when (binding){
            is ItemTaskDisabledBinding -> {
                binding.tvTitle.text = taskItem.title
            }
            is ItemTaskEnabledBinding -> {
                binding.tvTitle.text = taskItem.title
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled){
            VIEW_TYPE_ENABLED
        }else{
            VIEW_TYPE_DISABLED
        }
    }

    companion object{
        const val VIEW_TYPE_ENABLED = 111
        const val VIEW_TYPE_DISABLED = 121

        const val MAX_POOL_SIZE = 30
    }

}