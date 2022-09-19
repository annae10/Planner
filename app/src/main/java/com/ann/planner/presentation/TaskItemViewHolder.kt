package com.ann.planner.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ann.planner.R

class TaskItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
}