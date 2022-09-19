package com.ann.planner.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ann.planner.R
import com.ann.planner.databinding.ActivityMainBinding
import com.ann.planner.domain.TaskItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var taskListAdapter: TaskListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        //llTaskList = findViewById(R.id.ll_task_list)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.taskList.observe(this){
            taskListAdapter.submitList(it)
        }
    }

    private fun setupRecyclerView(){
        val rvTaskList = findViewById<RecyclerView>(R.id.rv_task_list)
        with(rvTaskList) {
            taskListAdapter = TaskListAdapter()
            adapter = taskListAdapter
            recycledViewPool.setMaxRecycledViews(
                TaskListAdapter.VIEW_TYPE_ENABLED,
                TaskListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                TaskListAdapter.VIEW_TYPE_DISABLED,
                TaskListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvTaskList)
    }

    private fun setupSwipeListener(rvTaskList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = taskListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteTaskItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTaskList)
    }

    private fun setupClickListener() {
        taskListAdapter.onTaskItemClickListener = {
            Log.d("Main Activity", it.toString())
        }
    }

    private fun setupLongClickListener() {
        taskListAdapter.onTaskItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}