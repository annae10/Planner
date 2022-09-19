package com.ann.planner.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ann.planner.R
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
        val view = LayoutInflater.from(parent.context).inflate(
            layout, parent, false
        )
        return TaskItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val taskItem = getItem(position)
        holder.view.setOnLongClickListener {
            onTaskItemLongClickListener?.invoke(taskItem)
            true
        }
        holder.view.setOnClickListener {
            onTaskItemClickListener?.invoke(taskItem)
        }

            holder.tvTitle.text = taskItem.title

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