package com.ann.planner.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ann.planner.R
import com.ann.planner.databinding.ActivityMainBinding
import com.ann.planner.domain.TaskItem
import javax.inject.Inject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), TaskItemFragment.OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var taskListAdapter: TaskListAdapter
    private var taskItemContainer: FragmentContainerView? = null
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var  viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as TaskApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        taskItemContainer = findViewById(R.id.task_item_container)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.taskList.observe(this) {
            taskListAdapter.submitList(it)
        }
        binding.butttonAddTaskItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = TaskItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(TaskItemFragment.newInstanceAddItem())
            }
        }

        thread {
            val cursor = contentResolver.query(
            Uri.parse("content://com.ann.planner/task_lists"),
            null, null, null, null, null
        )
            while (cursor?.moveToNext() == true){
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                val enabled = cursor.getInt(cursor.getColumnIndexOrThrow("enabled")) > 0
                val taskItem = TaskItem(
                    id = id,
                    title = title,
                    enabled = enabled
                )
                Log.d("MainActivity", taskItem.toString())
            }
            cursor?.close()
        }
    }

    override fun onEditingFinished(){
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun isOnePaneMode(): Boolean {
        return binding.taskItemContainer == null
    }

    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.task_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        with(binding.rvTaskList) {
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
        setupSwipeListener(binding.rvTaskList)
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
//                viewModel.deleteTaskItem(item)
                thread {
                    contentResolver.delete(
                        Uri.parse("content://com.ann.planner/task_items"),
                        null,
                        arrayOf(item.id.toString())
                    )
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTaskList)
    }

    private fun setupClickListener() {
        taskListAdapter.onTaskItemClickListener = {
            if (isOnePaneMode()) {
                val intent = TaskItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(TaskItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        taskListAdapter.onTaskItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }


}