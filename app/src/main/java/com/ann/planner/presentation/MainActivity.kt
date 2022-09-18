package com.ann.planner.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.ann.planner.R
import com.ann.planner.databinding.ActivityMainBinding
import com.ann.planner.domain.TaskItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var llTaskList: LinearLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        llTaskList = findViewById(R.id.ll_task_list)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.taskList.observe(this){
            showList(it)
        }
    }

    private fun showList(list: List<TaskItem>){
        llTaskList.removeAllViews()
        for (taskItem in list){
            val layoutId = if(taskItem.enabled){
                R.layout.item_task_enabled
            } else{
                R.layout.item_task_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, llTaskList, false)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            tvTitle.text = taskItem.title
            view.setOnLongClickListener {
                viewModel.changeEnableState(taskItem)
                true
            }
            llTaskList.addView(view)
        }
    }
}